package com.produto_service.service;

import com.produto_service.dto.CreateProdutoDTO;
import com.produto_service.dto.RecoveryProdutoDTO;
import com.produto_service.entities.Produto;
import com.produto_service.exceptions.ErroProdutoException;
import com.produto_service.exceptions.InfraException;
import com.produto_service.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    @Autowired
    ProdutoRepository produtoRepository;

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

    public void removerDoEstoque(long id, int quantidade_vendida) {
        int quantidade_produto = produtoRepository.getById(id).getQuantidadeEmEstoque();
        int nova_quantidade = quantidade_produto - quantidade_vendida;

        if(nova_quantidade < 0) {
            throw new ErroProdutoException("Não há estoque para esse produto para atender essa quantidade pedida");
        }

        produtoRepository.getById(id).setQuantidadeEmEstoque(nova_quantidade);
    }



}
