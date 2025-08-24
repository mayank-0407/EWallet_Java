package com.ewallet.transaction_service.services;

import com.ewallet.transaction_service.dto.*;
import com.ewallet.transaction_service.exceptions.InvalidTransactionDetailsException;
import com.ewallet.transaction_service.exceptions.RabbitMQNowWorkingException;
import com.ewallet.transaction_service.exceptions.TransactionFailureException;
import com.ewallet.transaction_service.exceptions.TransactionNotFoundException;
import com.ewallet.transaction_service.models.TransactionModel;
import com.ewallet.transaction_service.enums.TransactionStatus;
import com.ewallet.transaction_service.feignconfig.TransactionDB;
import com.ewallet.transaction_service.models.UserModel;
import com.ewallet.transaction_service.mqproducer.StartTransactionProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class TransactionService {

    @Autowired
    private TransactionDB transactionDB;

    private final StartTransactionProducer startTransactionMQProducer;

    Logger logger = LoggerFactory.getLogger(TransactionService.class);

    public TransactionService(StartTransactionProducer startTransactionMQProducer) {
        this.startTransactionMQProducer = startTransactionMQProducer;
    }

    public Boolean initiateTransaction(TransactionDTO transactionDetails) {
//        {
//            "senderId":"1",
//                "receiverId":"2",
//                "amount": 1000.0 ,
//                "remarks":"Nothing"
//        }
        // Create a new transaction

        if (transactionDetails.getAmount() <= 0) {
            logger.error("Invalid transaction details. Amount must be greater than zero.");
            throw new InvalidTransactionDetailsException("Transaction amount must be greater than zero.");
        }

        TransactionModel thisTransaction = new TransactionModel();
        thisTransaction.setTransactionInitTime(LocalDateTime.now());
        thisTransaction.setStatus(TransactionStatus.PENDING);
        thisTransaction.setSenderWalletId(transactionDetails.getSenderId());
        thisTransaction.setReceiverWalletId(transactionDetails.getReceiverId());
        thisTransaction.setAmount(transactionDetails.getAmount());
        thisTransaction.setRemarks(transactionDetails.getRemarks());

        TransactionModel savedTransaction = transactionDB.saveTransaction(thisTransaction).getBody();
        if(savedTransaction!=null){
            logger.info("Transaction initiated successfully. Transaction ID: {}", savedTransaction.getId());
            try{
                startTransactionMQProducer.initiatePayment(savedTransaction);
            } catch (Exception e) {
                TransactionStatusDTO failedInitTransaction = new TransactionStatusDTO();
                failedInitTransaction.setTransactionId(savedTransaction.getId());
                failedInitTransaction.setStatus(TransactionStatus.FAILED);
                changePaymentStatus(failedInitTransaction);
                throw new RabbitMQNowWorkingException("RabbitMQ Server is Down");
            }
            return true;
        }
        logger.error("Transaction initiation failed.");
//        throw new TransactionFailureException("Failed to initiate the transaction.");
        return false;
    }

    public Boolean addMoneyToWalletTransaction(TransactionDTO transactionDetails) {
//        {
//            "senderId":,
//                "receiverId":"2",
//                "amount": 1000.0 ,
//                "remarks":"Added Money TO Wallet"
//        }
        // Create a new transaction
        TransactionModel thisTransaction = new TransactionModel();
        thisTransaction.setTransactionInitTime(LocalDateTime.now());
        thisTransaction.setStatus(TransactionStatus.COMPLETED);
        thisTransaction.setSenderWalletId("default@wallet");
        thisTransaction.setReceiverWalletId(transactionDetails.getReceiverId());
        thisTransaction.setAmount(transactionDetails.getAmount());
        thisTransaction.setRemarks("Added Money To Wallet");
        TransactionModel savedTransaction = transactionDB.saveTransaction(thisTransaction).getBody();
        if(savedTransaction!=null){
            return true;
        }
        logger.error("Failed to Add Money to the Wallet");
        return false;
    }

    public TransactionModel completeTransaction(Long id) {
        TransactionStatusDTO transactionIDDetails = new TransactionStatusDTO();
        transactionIDDetails.setTransactionId(id);
        TransactionModel transaction = transactionDB.getTransactionById(transactionIDDetails).getBody();

        if (transaction == null) {
            logger.error("Transaction not found for ID: {}", id);
            throw new TransactionNotFoundException("Transaction not found for ID: " + id);
        }
//        assert transaction != null;
        transaction.setTransactionCompletedTime(LocalDateTime.now());
        transaction.setStatus(TransactionStatus.COMPLETED);


        return transactionDB.changeTransactionStatusToComplete(new TransactionStatusDTO(transactionIDDetails.getTransactionId(), TransactionStatus.COMPLETED)).getBody();
    }

    public TransactionModel changePaymentStatus(TransactionStatusDTO statusDetails){
        TransactionModel updatedStatus = transactionDB.updateTransactionStatus(statusDetails).getBody();
        if (updatedStatus == null) {
            logger.error("Failed to update transaction status for ID: {}", statusDetails.getTransactionId());
            throw new TransactionFailureException("Failed to update transaction status.");
        }
        return updatedStatus;
    }

    public TransactionModel getOneRecentTransaction(String receiverId){
        ResponseEntity<TransactionModel> thisTransaction = transactionDB.getOneRecentTransaction(receiverId);
        if (thisTransaction!=null) {
            return thisTransaction.getBody();
        } else {
            logger.error("Failed to Fetch One Recent Transaction from Receiver Id");
            return null;
        }
    }

    public TransactionModel getOneRecentTransactionFromSenderId(String senderId){
        ResponseEntity<TransactionModel> thisTransaction = transactionDB.getOneRecentTransactionBySender(senderId);
        if (thisTransaction!=null) {
            return thisTransaction.getBody();
        } else {
            logger.error("Failed to Fetch One Recent Transaction from Sender Id");
            return null;
        }
    }

    public List<SearchHistoryTransactionResponse> getFiveRecentTransactionsBySenderWalletId(String senderWalletId) {
        List<TransactionModel> fetchedTransactions = transactionDB.getFiveRecentTransactions(senderWalletId);

        List<SearchHistoryTransactionResponse> responseList = new ArrayList<>();

        for (TransactionModel transaction : fetchedTransactions) {
            String walletId = transaction.getReceiverWalletId();

            UserModel tempUserWalletIdDetails = new UserModel();
            tempUserWalletIdDetails.setWalletId(walletId);
            UserModel userDetails = transactionDB.getUserByWalletId(tempUserWalletIdDetails).getBody();

            SearchHistoryTransactionResponse response = new SearchHistoryTransactionResponse();

            response.setTransaction(transaction);
            assert userDetails != null;
            response.setName(userDetails.getName());

            responseList.add(response);
        }

        return responseList;
    }

    public List<FiveRecentTransactionResponse> getFiveRecentTransactionsByWalletId(String walletId) {
        List<TransactionModel> myTransactions = transactionDB.getTop5TransactionsBySenderOrReceiverWalletId(walletId);
        List<FiveRecentTransactionResponse> responseList = new ArrayList<>();

        for (TransactionModel transaction : myTransactions) {
            String senderwalletId = transaction.getSenderWalletId();
            String receiverWalletId = transaction.getReceiverWalletId();

            UserModel tempUserWalletIdDetails = new UserModel();

            String senderFullName;
            if(!Objects.equals(senderwalletId, "default@wallet")) {
                tempUserWalletIdDetails.setWalletId(senderwalletId);
                UserModel senderUserDetails = transactionDB.getUserByWalletId(tempUserWalletIdDetails).getBody();
                assert senderUserDetails != null;
                senderFullName = senderUserDetails.getName();
            }else{
                senderFullName = "Default Wallet";
            }

            String receiverFullName;
            if(!Objects.equals(receiverWalletId, "default@wallet")) {
                tempUserWalletIdDetails.setWalletId(receiverWalletId);
                UserModel receiverUserDetails = transactionDB.getUserByWalletId(tempUserWalletIdDetails).getBody();
                assert receiverUserDetails != null;
                receiverFullName = receiverUserDetails.getName();
            }else{
                receiverFullName = "Default Wallet";
            }
            FiveRecentTransactionResponse response = new FiveRecentTransactionResponse();

            response.setTransaction(transaction);
            response.setSenderName(senderFullName);
            response.setReceiverName(receiverFullName);

            responseList.add(response);
        }
        return responseList;
    }

    public List<RecentTransactionsResponse> getAllTransactions(String walletId) {
        List<TransactionModel> myTransactions = transactionDB.getAllTransactionsWithoutRefundForReceiver(walletId);

        List<RecentTransactionsResponse> responseList = new ArrayList<>();

        for (TransactionModel transaction : myTransactions) {
            String senderWalletId = transaction.getSenderWalletId();
            String receiverWalletId = transaction.getReceiverWalletId();

            UserModel tempUserWalletIdDetails = new UserModel();

            String senderFullName;
            if(!Objects.equals(senderWalletId, "default@wallet")) {
                tempUserWalletIdDetails.setWalletId(senderWalletId);
                UserModel senderUserDetails = transactionDB.getUserByWalletId(tempUserWalletIdDetails).getBody();
                assert senderUserDetails != null;
                senderFullName = senderUserDetails.getName();
            }else{
                senderFullName="Default Wallet";
            }

            String receiverFullName;
            if(!Objects.equals(receiverWalletId, "default@wallet")) {
                tempUserWalletIdDetails.setWalletId(receiverWalletId);
                UserModel receiverUserDetails = transactionDB.getUserByWalletId(tempUserWalletIdDetails).getBody();
                assert receiverUserDetails != null;
                receiverFullName = receiverUserDetails.getName();
            }else{
                receiverFullName="Default Wallet";
            }

            RecentTransactionsResponse response = new RecentTransactionsResponse();

            response.setTransaction(transaction);
            response.setSenderName(senderFullName);
            response.setReceiverName(receiverFullName);

            responseList.add(response);
        }

        return responseList;
    }

}
