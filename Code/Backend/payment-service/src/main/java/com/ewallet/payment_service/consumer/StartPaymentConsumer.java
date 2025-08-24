package com.ewallet.payment_service.consumer;

import com.ewallet.payment_service.models.TransactionModel;
import com.ewallet.payment_service.services.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StartPaymentConsumer {

    @Autowired
    private PaymentService paymentService;
    private static final Logger LOGGER = LoggerFactory.getLogger(StartPaymentConsumer.class);
    private final RabbitTemplate rabbitTemplate;

    public StartPaymentConsumer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = "${rabbitmq.queue.startPayment.name}")
    public void startPaymentProcess(TransactionModel transactionDetails){
        LOGGER.info("Received the Transaction Details -> {}", transactionDetails);
        TransactionModel updatedTransactionDetails = paymentService.changePaymentStatusToProcessing(transactionDetails.getId());
        LOGGER.info("Transaction Status Changed -> {}", updatedTransactionDetails);

        if(updatedTransactionDetails!=null) {
            Boolean transactionStatus = paymentService.processTransaction(transactionDetails);
            LOGGER.info("Transaction Status In if Main {}",transactionStatus);
            if (transactionStatus)
                LOGGER.info("Transaction Completed");
        }else{
            TransactionModel cancelledTransactionDetails = paymentService.changePaymentStatusToFailed(transactionDetails.getId());
            LOGGER.info("Transaction Status Changed to Failed -> {}", cancelledTransactionDetails);
            LOGGER.info("Transaction Failed");
        }
    }
}
