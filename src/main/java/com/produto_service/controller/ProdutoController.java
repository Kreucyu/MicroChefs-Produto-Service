package com.produto_service.controller;

import com.produto_service.dto.RecoveryProdutoDTO;
import com.produto_service.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProdutoController {
    @Autowired
    ProdutoService produtoService;
    @GetMapping
    public ResponseEntity<List<RecoveryProdutoDTO>> listarProdutos() {
        return ResponseEntity.ok().body(produtoService.listarProdutos());
    }
}
