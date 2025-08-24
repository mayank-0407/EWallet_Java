package com.ewallet.refund_service.consumer;

import com.ewallet.refund_service.dto.RefundTransactionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ewallet.refund_service.services.RefundService;

@Service
public class RefundPaymentConsumer {

    @Autowired
    private RefundService paymentService;
    private static final Logger LOGGER = LoggerFactory.getLogger(RefundPaymentConsumer.class);
    private final RabbitTemplate rabbitTemplate;

    public RefundPaymentConsumer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = "${rabbitmq.queue.refund.name}")
    public void processRefund(RefundTransactionDTO refundDetails){
        LOGGER.info("Received the Refund Details -> {}", refundDetails);
        Boolean refundStatus = paymentService.processRefund(refundDetails);
        if(refundStatus)
            LOGGER.info("Refund Completed");
        else
            LOGGER.info("Refund Failed");
    }

}
