package com.ewallet.transaction_service.mqproducer;

import com.ewallet.transaction_service.models.TransactionModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StartTransactionProducer {

    @Value("${rabbitmq.exchange.startPayment.name}")
    private String startPaymentExchange;

    @Value("${rabbitmq.routing.startPayment.key}")
    private String startPaymentRoutingKey;

    private static final Logger LOGGER = LoggerFactory.getLogger(StartTransactionProducer.class);
    private final RabbitTemplate rabbitTemplate;

    public StartTransactionProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void initiatePayment(TransactionModel transaction){
        LOGGER.info("Transaction Has been sent for processing -> {}", transaction);
        rabbitTemplate.convertAndSend(startPaymentExchange,startPaymentRoutingKey,transaction);
    }
}
