package com.produto_service.service;

import com.produto_service.dto.CreateProdutoDTO;
import com.produto_service.dto.RecoveryProdutoDTO;
import com.produto_service.entities.Produto;
import com.produto_service.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdutoService {

    @Autowired
    ProdutoRepository produtoRepository;

    public List<RecoveryProdutoDTO> listarProdutos() {
        List<Produto> produtos = produtoRepository.findAll();
        return produtos
                .stream()
                .map(p -> new RecoveryProdutoDTO(p.getNomeProduto(),
                        p.getDescricaoProduto(), p.getQuantidadeEmEstoque(),
                        p.getPrecoProduto()))
                .toList();
    }

    public CreateProdutoDTO criarProduto(CreateProdutoDTO novoProduto) {
        Produto produto = new Produto();
        produto.setDescricaoProduto(novoProduto.);
    }
}
