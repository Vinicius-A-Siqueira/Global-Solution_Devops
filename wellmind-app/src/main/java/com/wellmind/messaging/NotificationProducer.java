package com.wellmind.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationProducer {

    private final RabbitTemplate rabbitTemplate;

    private final String exchange = "wellness.exchange";
    private final String routingKey = "wellness.notification";

    public void enviarNotificacao(String mensagem) {
        rabbitTemplate.convertAndSend(exchange, routingKey, mensagem);
    }

    public void enviarAlerta(String emailUsuario, List<String> alertas) {
        String mensagem = "Alerta para " + emailUsuario + ": " + String.join(", ", alertas);
        rabbitTemplate.convertAndSend(exchange, routingKey, mensagem);
    }
}
