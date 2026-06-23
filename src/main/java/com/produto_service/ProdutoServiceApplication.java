package com.produto_service;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableRabbit
@EnableCaching
@SpringBootApplication
public class ProdutoServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProdutoServiceApplication.class, args);
	}

}
