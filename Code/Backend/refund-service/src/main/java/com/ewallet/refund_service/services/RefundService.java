package com.ewallet.refund_service.services;

import com.ewallet.refund_service.dto.RefundTransactionDTO;
import com.ewallet.refund_service.dto.TransactionStatusDTO;
import com.ewallet.refund_service.enums.TransactionStatus;
import com.ewallet.refund_service.exceptions.RefundProcessingException;
import com.ewallet.refund_service.exceptions.TransactionNotFoundException;
import com.ewallet.refund_service.exceptions.WalletUpdateException;
import com.ewallet.refund_service.feign.FeignDBService;
import com.ewallet.refund_service.models.TransactionModel;
import com.ewallet.refund_service.models.WalletModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RefundService {


    @Autowired
    private FeignDBService dbService;


    private final Logger logger = LoggerFactory.getLogger(RefundService.class);

    public Boolean processRefund(RefundTransactionDTO refundDetails){
        logger.info("Processing the Refund");
        try{
            WalletModel senderWalletDetails = refundDetails.getSenderWallet();
            senderWalletDetails.setBalance(senderWalletDetails.getBalance() + refundDetails.getRefundAmount());

            WalletModel returnedMoneyInWallet = dbService.updateBalance(senderWalletDetails).getBody();
            if (returnedMoneyInWallet == null) {
                throw new WalletUpdateException("Failed to update wallet balance for wallet id: " + senderWalletDetails.getId());
            }

            TransactionStatusDTO userTransactionDto = new TransactionStatusDTO();
            userTransactionDto.setTransactionId(refundDetails.getTransactionId());
            TransactionModel thisUserTransaction = dbService.getTransactionById(userTransactionDto).getBody();

            // Payment Failed (Refunded)
            if (thisUserTransaction == null) {
                throw new TransactionNotFoundException("Transaction not found with id: " + refundDetails.getTransactionId());
            }

            thisUserTransaction.setRemarks("Refund Processed!");
            thisUserTransaction.setStatus(TransactionStatus.REFUNDED);

            TransactionModel savedTransaction = dbService.saveTransaction(thisUserTransaction).getBody();

            if (savedTransaction == null) {
                logger.error("Failed to Process Refund");
                throw new RefundProcessingException("Failed to save refund transaction status");
            }
            logger.info("Refund Processed Successfully");
            return true;
        } catch (WalletUpdateException | TransactionNotFoundException | RefundProcessingException e) {
            logger.error("Refund processing failed: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            logger.error("Unexpected error during refund processing: {}", e.getMessage(), e);
            return false;
        }
    }
}
