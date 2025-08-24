package com.ewallet.payment_service.producer;

import com.ewallet.payment_service.dto.RefundTransactionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RefundProducer {
    // dead later queue
    @Value("${rabbitmq.exchange.startPayment.name}")
    private String startPaymentExchange;

    @Value("${rabbitmq.routing.refund.key}")
    private String refundRoutingKey;

    private static final Logger LOGGER = LoggerFactory.getLogger(RefundProducer.class);
    private final RabbitTemplate rabbitTemplate;

    public RefundProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void initiateRefund(RefundTransactionDTO transaction){
        LOGGER.info("Transaction Has been sent for Refund -> {}", transaction);
        rabbitTemplate.convertAndSend(startPaymentExchange,refundRoutingKey,transaction);
    }
}
