package com.produto_service.controller;

import com.produto_service.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProdutoController {
    @Autowired
    ProdutoService produtoService;
}
