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
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.dao.TransientDataAccessException;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProdutoService {

    @Autowired
    ProdutoRepository produtoRepository;

    @Autowired
    ProdutoProducer produtoProducer;

    @Autowired
    CacheManager cacheManager;

    @CacheEvict(value = "produtos", allEntries = true)
    public CreateProdutoDTO criarProduto(CreateProdutoDTO novo_produto) {
        Produto produto = new Produto();
        produto.setNomeProduto(novo_produto.nomeProduto());
        produto.setDescricaoProduto(novo_produto.descricaoProduto());
        produto.setQuantidadeEmEstoque(novo_produto.quantidadeEmEstoque());
        produto.setQuantidadeMinima(novo_produto.quantidadeMinima());
        produto.setPrecoProduto(novo_produto.precoProduto());
        produto.setImagem(novo_produto.imagem());
        produtoRepository.save(produto);
        return novo_produto;
    }

    @Cacheable(value = "produto", key = "#id")
    public RecoveryProdutoDTO buscarProduto(long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ErroProdutoException("Produto não encontrado"));
        return toDto(produto);
    }

    @Cacheable(value = "produtos")
    public List<RecoveryProdutoDTO> listarProdutos() {
        return produtoRepository.findAll().stream().map(this::toDto).toList();
    }

    @Caching(evict = {
        @CacheEvict(value = "produto", key = "#id"),
        @CacheEvict(value = "produtos", allEntries = true)
    })
    public RecoveryProdutoDTO editarProduto(long id, CreateProdutoDTO dto) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ErroProdutoException("Produto não encontrado"));
        produto.setNomeProduto(dto.nomeProduto());
        produto.setDescricaoProduto(dto.descricaoProduto());
        produto.setQuantidadeEmEstoque(dto.quantidadeEmEstoque());
        produto.setQuantidadeMinima(dto.quantidadeMinima());
        produto.setPrecoProduto(dto.precoProduto());
        if (dto.imagem() != null) {
            produto.setImagem(dto.imagem());
        }
        produtoRepository.save(produto);
        return toDto(produto);
    }

    private RecoveryProdutoDTO toDto(Produto produto) {
        int estoque = produto.getQuantidadeEmEstoque();
        int minimo = produto.getQuantidadeMinima() != null ? produto.getQuantidadeMinima() : 0;
        return new RecoveryProdutoDTO(
                produto.getId(),
                produto.getNomeProduto(),
                produto.getDescricaoProduto(),
                estoque,
                minimo,
                produto.getPrecoProduto(),
                produto.getImagem(),
                estoque > 0,
                estoque > 0 && estoque <= minimo);
    }

    @Caching(evict = {
        @CacheEvict(value = "produto", key = "#id"),
        @CacheEvict(value = "produtos", allEntries = true)
    })
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
            RecoveryProdutoDTO produtoAtualizado = new RecoveryProdutoDTO(
                    produto.getId(),
                    produto.getNomeProduto(),
                    produto.getDescricaoProduto(),
                    produto.getQuantidadeEmEstoque(),
                    produto.getPrecoProduto()
            );
            var cacheProduto = cacheManager.getCache("produto");
            if (cacheProduto != null) {
                cacheProduto.put(id, produtoAtualizado);
            }

            var cacheLista = cacheManager.getCache("produtos");
            if (cacheLista != null) {
                cacheLista.clear();
            }

            RecoveryProdutoDTO produtoAtualizado = toDto(produto);
            var cacheProduto = cacheManager.getCache("produto");
            if (cacheProduto != null) {
                cacheProduto.put(id, produtoAtualizado);
            }

            var cacheLista = cacheManager.getCache("produtos");
            if (cacheLista != null) {
                cacheLista.clear();
            }

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
        System.out.println("çprod");
        produtoProducer.dlqSender(dlqSupportDTO);
        System.out.println(dlqSupportDTO);
    }

}
