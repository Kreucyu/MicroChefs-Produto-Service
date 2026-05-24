package com.produto_service.dto;

import lombok.NonNull;

public record CreateProdutoDTO(
        @NonNull String nomeProduto,
        @NonNull String descricaoProduto,
        @NonNull int quantidadeEmEstoque,
        @NonNull double precoProduto
) {
}
