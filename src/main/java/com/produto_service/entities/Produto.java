package com.produto_service.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "produtos")
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Produto implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "nome")
    private String nomeProduto;
    @Column(name = "descricao")
    private String descricaoProduto;
    @Column(name = "quantidade")
    private int quantidadeEmEstoque;

    @Column(name = "quantidade_minima")
    private Integer quantidadeMinima = 0;
    @Column(name = "preco")
    private double precoProduto;
    @Column(name = "imagem", columnDefinition = "TEXT")
    private String imagem;
}
