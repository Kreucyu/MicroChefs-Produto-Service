package com.produto_service.consumer;

import com.produto_service.dto.UpdateProdutoDTO;
import com.produto_service.exceptions.ErroProdutoException;
import com.produto_service.exceptions.InfraException;
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

    @RabbitListener(queues = "produto-queue")
    public void receberAtualizacao(@Payload String updateJson) {
        try {
            System.out.println(updateJson);
            System.out.println(converterMensagemJSON(updateJson));
            UpdateProdutoDTO update = converterMensagemJSON(updateJson);
            System.out.println("update chegou");
            produtoService.removerDoEstoque(update.id(), update.quantidade());
        } catch (ErroProdutoException | InfraException e) {
            System.out.println("catch");
            produtoService.processarErro(e, updateJson);
        }
    }

    private UpdateProdutoDTO converterMensagemJSON(String updateJson) {
        try {
            return objectMapper.readValue(updateJson, UpdateProdutoDTO.class);
        } catch (InvalidFormatException | StreamReadException e) {
            throw new ErroProdutoException("JSON contém dados inválidos");
        }
    }
}
