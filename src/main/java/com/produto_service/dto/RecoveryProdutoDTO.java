package com.produto_service.dto;

import lombok.NonNull;

import java.io.Serializable;

public record RecoveryProdutoDTO(
        Long id,
        String nomeProduto,
        String descricaoProduto,
        int quantidadeEmEstoque,
        double precoProduto
) implements Serializable {
}
