package com.produto_service.config;

import com.produto_service.dto.CreateProdutoDTO;
import com.produto_service.service.ProdutoService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProdutoDataSeeder {

    @Bean
    CommandLineRunner seedProdutos(ProdutoService produtoService) {
        return args -> {
            if (!produtoService.listarProdutos().isEmpty()) {
                return;
            }

            var produtos = new CreateProdutoDTO[]{
                    new CreateProdutoDTO("X-Burger Classic", "Hamburguer artesanal com queijo cheddar", 50, 24.90),
                    new CreateProdutoDTO("Pizza Margherita", "Molho de tomate, mussarela e manjericao", 30, 39.90),
                    new CreateProdutoDTO("Batata Frita", "Porcao crocante com tempero especial", 80, 14.90),
                    new CreateProdutoDTO("Refrigerante Lata", "350ml - sabor cola", 120, 6.50),
                    new CreateProdutoDTO("Suco Natural", "Laranja espremida na hora 500ml", 40, 9.90),
                    new CreateProdutoDTO("Brownie", "Brownie de chocolate com nozes", 25, 12.90),
            };

            for (var produto : produtos) {
                produtoService.criarProduto(produto);
            }
        };
    }
}
