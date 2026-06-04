package com.produto_service.service;

import com.produto_service.dto.CreateProdutoDTO;
import com.produto_service.dto.DLQSupportDTO;
import com.produto_service.dto.RecoveryProdutoDTO;
import com.produto_service.entities.Produto;
import com.produto_service.exceptions.ErroProdutoException;
import com.produto_service.exceptions.InfraException;
import com.produto_service.producer.ProdutoProducer;
import com.produto_service.repository.ProdutoRepository;
import org.hibernate.QueryTimeoutException;
import org.springframework.amqp.AmqpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.TransientDataAccessException;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    @Autowired
    ProdutoRepository produtoRepository;

    @Autowired
    ProdutoProducer produtoProducer;

    public CreateProdutoDTO criarProduto(CreateProdutoDTO novo_produto) {
        Produto produto = new Produto();
        produto.setNomeProduto(novo_produto.nomeProduto());
        produto.setDescricaoProduto(novo_produto.descricaoProduto());
        produto.setQuantidadeEmEstoque(novo_produto.quantidadeEmEstoque());
        produto.setPrecoProduto(novo_produto.precoProduto());
        produtoRepository.save(produto);
        return novo_produto;
    }

    public RecoveryProdutoDTO buscarProduto(long id) {
        Produto produto = produtoRepository.findById(id).orElseThrow();
        return new RecoveryProdutoDTO(produto.getNomeProduto(),
                produto.getDescricaoProduto(),
                produto.getQuantidadeEmEstoque(),
                produto.getPrecoProduto());
    }

    public List<RecoveryProdutoDTO> listarProdutos() {
        List<Produto> produtos = produtoRepository.findAll();
        return produtos
                .stream()
                .map(p -> new RecoveryProdutoDTO(p.getNomeProduto(),
                        p.getDescricaoProduto(), p.getQuantidadeEmEstoque(),
                        p.getPrecoProduto()))
                .toList();
    }

    public void deletarProduto(long id) {
        try {
            produtoRepository.deleteById(id);
        } catch (InfraException e) {
            System.out.println("/nErro ao tentar excluir o produto. Erro: " + e.getMessage());
        }
    }

    @Transactional
    public void removerDoEstoque(long id, int quantidadeVendida) {
        try {
            Produto produto = produtoRepository.findById(id).orElseThrow(() -> new ErroProdutoException("Produto não encontrado"));
            int quantidadeProduto = produto.getQuantidadeEmEstoque();
            int novaQuantidade = quantidadeProduto - quantidadeVendida;
            if (novaQuantidade < 0) {
                throw new ErroProdutoException("Não há estoque para esse produto para atender essa quantidade pedida");
            }

            produto.setQuantidadeEmEstoque(novaQuantidade);
            produtoRepository.save(produto);

        } catch (CannotCreateTransactionException | QueryTimeoutException | TransientDataAccessException |
                 AmqpException e) {
            throw new InfraException("Erro na conexão");
        }
    }

    public void processarErro(Exception e, String JSON) {
        String tipo = "DATA_ERROR";
        if (e instanceof InfraException) {
            tipo = "INFRA_ERROR";
        }
        DLQSupportDTO dlqSupportDTO = new DLQSupportDTO(
                "PRODUTO_UPDATE",
                "produto-queue",
                tipo,
                e.getMessage(),
                JSON,
                LocalDateTime.now()
        );
        produtoProducer.dlqSender(dlqSupportDTO);
        System.out.println(dlqSupportDTO);
    }



}
