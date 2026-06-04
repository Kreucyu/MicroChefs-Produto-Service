package com.produto_service.consumer;

import com.produto_service.producer.ProdutoProducer;
import com.produto_service.service.ProdutoService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import tools.jackson.core.exc.StreamReadException;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.exc.InvalidFormatException;

@Component
public class UpdateProductQuantity {

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProdutoProducer produtoProducer;

    @RabbitListener(queues = "pedido-queue")
    public void receberAtualizacao(@Payload String updateJson) {
        try {
            UpdatePedidoDTO update = converterMensagemJSON(updateJson);
            pedidoService.atualizarStatusPedido(update);
        } catch (ErroPedidoException | InfraException e) {
            pedidoService.processarErro(e, updateJson);
        }
    }

    private UpdatePedidoDTO converterMensagemJSON(String updateJson) {
        try {
            return objectMapper.readValue(updateJson, UpdatePedidoDTO.class);
        } catch (InvalidFormatException | StreamReadException e) {
            throw new ErroPedidoException("JSON contém dados inválidos");
        }
    }
}
