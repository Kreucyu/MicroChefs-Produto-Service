package com.produto_service.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

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
    @Column(name = "preco")
    private double precoProduto;
}
