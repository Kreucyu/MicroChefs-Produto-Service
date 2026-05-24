package com.produto_service.controller;

import com.produto_service.dto.CreateProdutoDTO;
import com.produto_service.dto.RecoveryProdutoDTO;
import com.produto_service.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/produtos")
public class ProdutoController {
    @Autowired
    ProdutoService produtoService;

    @PostMapping("/criar")
    public ResponseEntity<CreateProdutoDTO> criarProduto(@RequestBody CreateProdutoDTO novoProduto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoService.criarProduto(novoProduto));
    }

    @GetMapping("buscar/{id}")
    public ResponseEntity<RecoveryProdutoDTO> buscarProduto(@PathVariable long id) {
        return ResponseEntity.ok().body(produtoService.buscarProduto(id));
    }

    @GetMapping("/listar")
    public ResponseEntity<List<RecoveryProdutoDTO>> listarProdutos() {
        return ResponseEntity.ok().body(produtoService.listarProdutos());
    }
}
