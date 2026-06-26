package com.produto_service.dto;

import lombok.NonNull;

public record RecoveryProdutoDTO(
        long id,
        @NonNull String nomeProduto,
        @NonNull String descricaoProduto,
        @NonNull int quantidadeEmEstoque,
        int quantidadeMinima,
        @NonNull double precoProduto,
        String imagem,
        boolean disponivel,
        boolean estoqueBaixo
) {
    public RecoveryProdutoDTO(long id, String nomeProduto, String descricaoProduto, int quantidadeEmEstoque, double precoProduto) {
        this(id, nomeProduto, descricaoProduto, quantidadeEmEstoque, 0, precoProduto, null, quantidadeEmEstoque > 0, false);
    }
}
