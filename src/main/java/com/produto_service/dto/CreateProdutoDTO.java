package com.produto_service.dto;

import lombok.NonNull;

import java.io.Serializable;

public record CreateProdutoDTO(
        @NonNull String nomeProduto,
        @NonNull String descricaoProduto,
        @NonNull int quantidadeEmEstoque,
        int quantidadeMinima,
        @NonNull double precoProduto,
        String imagem
) {
    public CreateProdutoDTO(String nomeProduto, String descricaoProduto, int quantidadeEmEstoque, double precoProduto) {
        this(nomeProduto, descricaoProduto, quantidadeEmEstoque, 0, precoProduto, null);
    }
}
