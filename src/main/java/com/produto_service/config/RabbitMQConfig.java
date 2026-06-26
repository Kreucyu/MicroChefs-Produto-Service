package com.produto_service.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String PRODUTO_QUEUE = "produto-queue";

    @Bean
    public Queue produtoQueue() {
        return new Queue(PRODUTO_QUEUE, true);
    }
}
