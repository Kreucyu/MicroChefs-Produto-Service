package com.produto_service.producer;

import com.produto_service.dto.DLQSupportDTO;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
public class ProdutoProducer {

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public void dlqSender(DLQSupportDTO dlqSupportDTO) {
        System.out.println(1);
        amqpTemplate.convertAndSend(
                "dead-letter-exchange",
                "dead-message",
                objectMapper.writeValueAsString(dlqSupportDTO)
        );
    }
}
