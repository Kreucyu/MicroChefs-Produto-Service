package com.produto_service.controller;

import com.produto_service.dto.CreateProdutoDTO;
import com.produto_service.dto.RecoveryProdutoDTO;
import com.produto_service.dto.UpdateProdutoDTO;
import com.produto_service.exceptions.ErroProdutoException;
import com.produto_service.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {
    @Autowired
    ProdutoService produtoService;

    @PostMapping("/criar")
    public ResponseEntity<CreateProdutoDTO> criarProduto(@RequestBody CreateProdutoDTO novoProduto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(produtoService.criarProduto(novoProduto));
    }

    @PutMapping("/editar/{id}")
    public ResponseEntity<RecoveryProdutoDTO> editarProduto(@PathVariable long id, @RequestBody CreateProdutoDTO dto) {
        try {
            return ResponseEntity.ok(produtoService.editarProduto(id, dto));
        } catch (ErroProdutoException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<RecoveryProdutoDTO> buscarProduto(@PathVariable long id) {
        try {
            return ResponseEntity.ok(produtoService.buscarProduto(id));
        } catch (ErroProdutoException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/listar")
    public ResponseEntity<List<RecoveryProdutoDTO>> listarProdutos() {
        return ResponseEntity.ok().body(produtoService.listarProdutos());
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<String> deletarProduto(@PathVariable long id) {
        produtoService.deletarProduto(id);
        return ResponseEntity.ok().body("Produto deletado com sucesso");
    }

    @PostMapping("/estoque/debitar")
    public ResponseEntity<String> debitarEstoque(@RequestBody UpdateProdutoDTO dto) {
        try {
            produtoService.removerDoEstoque(dto.id(), dto.quantidade());
            return ResponseEntity.ok("Estoque atualizado");
        } catch (ErroProdutoException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
