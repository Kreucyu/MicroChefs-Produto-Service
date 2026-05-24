package com.produto_service.service;

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
        return produtos.stream().forEach(p -> new RecoveryProdutoDTO());
    }
}
