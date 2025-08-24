package com.ewallet.transaction_service.controllers;


import com.ewallet.transaction_service.dto.*;
import com.ewallet.transaction_service.models.TransactionModel;
import com.ewallet.transaction_service.services.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    @Autowired
    private TransactionService service;

    Logger logger = LoggerFactory.getLogger(TransactionController.class);

    @GetMapping("/test")
    public String testing(){
        return "Hi Transaction is working";
    }

    @PostMapping("/initiate")
    public Boolean initiateTransaction(@RequestBody TransactionDTO transactionDetails) {
        //    senderId, receiverId, amount(double) ,remarks
        logger.info("Received request to initiate transaction.");
        return service.initiateTransaction(transactionDetails);
    }

    @PostMapping("/complete/{transactionId}")
    public TransactionModel completeTransaction(@PathVariable Long transactionId) {
        logger.info("Received request to complete transaction with ID: {}", transactionId);
        return service.completeTransaction(transactionId);
    }

    @PostMapping("/change/transaction/status")
    public TransactionModel updateTransactionStatus(@RequestBody TransactionStatusDTO transactionDetails){
        logger.info("Received request to update transaction status.");
        TransactionModel updatedTransaction = service.changePaymentStatus(transactionDetails);
        return updatedTransaction;
    }

    @PostMapping("/add/money/to/wallet")
    public Boolean addMoneyToWallet(@RequestBody TransactionDTO transactionDetails){
        logger.info("Received request to add money to wallet.");
        Boolean updatedTransaction = service.addMoneyToWalletTransaction(transactionDetails);
        return updatedTransaction;
    }

    @GetMapping("/get/one/recent/transaction/{receiverWalletId}")
    public TransactionModel getRecentTransactions(@PathVariable String receiverWalletId){
        logger.info("Fetching One recent transaction for receiver wallet ID: {}", receiverWalletId);
        TransactionModel oneRecentTransactions = service.getOneRecentTransaction(receiverWalletId);
        return oneRecentTransactions;
    }

    @GetMapping("/get/one/recent/transaction/by/sender/{senderWalletId}")
    public TransactionModel getRecentTransactionBySenderWalletId(@PathVariable String senderWalletId){
        logger.info("Fetching One recent transaction for Sender wallet ID: {}", senderWalletId);
        TransactionModel oneRecentTransactions = service.getOneRecentTransactionFromSenderId(senderWalletId);
        return oneRecentTransactions;
    }

    @GetMapping("/get/five/recent/transaction/by/sender/{senderWalletId}")
    public List<SearchHistoryTransactionResponse> getFiveRecentTransactionsBySenderWalletId(@PathVariable String senderWalletId) {
        logger.info("Fetching Five recent transaction for Sender wallet ID: {}", senderWalletId);
        return service.getFiveRecentTransactionsBySenderWalletId(senderWalletId);
    }

    @GetMapping("/get/all/transaction/{walletId}")
    public List<RecentTransactionsResponse> getAllTransactions(@PathVariable String walletId) {
        logger.info("Fetching all transactions for wallet ID: {}", walletId);
        return service.getAllTransactions(walletId);
    }

    @GetMapping("/get/five/recent/transactions/{walletId}")
    public List<FiveRecentTransactionResponse> getFiveRecentTransactionsByWalletId(@PathVariable String walletId) {
        logger.info("Fetching Five recent transaction for wallet ID for Dashboard: {}", walletId);
        return service.getFiveRecentTransactionsByWalletId(walletId);
    }
}
