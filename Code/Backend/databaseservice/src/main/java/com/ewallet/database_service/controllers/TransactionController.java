package com.ewallet.database_service.controllers;

import com.ewallet.database_service.dto.TransactionStatusDTO;
import com.ewallet.database_service.exceptions.ResourceNotFoundException;
import com.ewallet.database_service.models.TransactionModel;
import com.ewallet.database_service.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/db/transact")
public class TransactionController {

    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);

    @Autowired
    private TransactionRepository transactionRepository;

    @GetMapping("/test")
    public String testing() {
        logger.info("Transaction service is working.");
        return "Hi Transaction is working";
    }

    @PostMapping("/create")
    public ResponseEntity<TransactionModel> saveTransaction(@RequestBody TransactionModel transactionDetails) {
        logger.info("Attempting to save transaction: {}", transactionDetails);
        try {
            transactionRepository.save(transactionDetails);
            logger.info("Transaction saved successfully: {}", transactionDetails);
            return new ResponseEntity<>(transactionDetails, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error while saving transaction: {}", transactionDetails, e);
            throw new RuntimeException("Error while saving transaction");
        }
    }

    @PostMapping("/get/transaction")
    public ResponseEntity<TransactionModel> getTransactionById(@RequestBody TransactionStatusDTO idDetails) {
        logger.info("Fetching transaction with ID: {}", idDetails.getTransactionId());
        try{
            Optional<TransactionModel> thisTransaction = transactionRepository.findById(idDetails.getTransactionId());
            if (thisTransaction.isPresent()) {
                logger.info("Transaction found: {}", thisTransaction.get());
                return new ResponseEntity<>(thisTransaction.get(), HttpStatus.OK);
            } else {
                logger.warn("Transaction not found for ID : {}", idDetails.getTransactionId());
                throw new ResourceNotFoundException("Transaction Not Found for this Id");
            }
        } catch (Exception e) {
            logger.warn("Transaction not found for ID  : {}", idDetails.getTransactionId());
            throw new ResourceNotFoundException("Transaction Not Found for this Id");
        }
    }

    @PostMapping("/change/status")
    public ResponseEntity<TransactionModel> updateTransactionStatus(@RequestBody TransactionStatusDTO transactionStatusDetails) {
        logger.info("Updating transaction status for ID: {}", transactionStatusDetails.getTransactionId());
        try{
            Optional<TransactionModel> thisTransaction = transactionRepository.findById(transactionStatusDetails.getTransactionId());
            if (thisTransaction.isPresent()) {
                TransactionModel myTransaction = thisTransaction.get();
                myTransaction.setStatus(transactionStatusDetails.getStatus());
                transactionRepository.save(myTransaction);
                logger.info("Transaction status updated successfully for ID: {}", transactionStatusDetails.getTransactionId());
                return new ResponseEntity<>(myTransaction, HttpStatus.OK);
            } else {
                logger.warn("Transaction  not found for ID: {}", transactionStatusDetails.getTransactionId());
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            throw new ResourceNotFoundException("Transaction not found for This ID");
        }
    }

    @PostMapping("/change/status/to/complete")
    public ResponseEntity<TransactionModel> changeTransactionStatusToComplete(@RequestBody TransactionStatusDTO transactionStatusDetails) {
        logger.info("Changing transaction status to complete for ID: {}", transactionStatusDetails.getTransactionId());
        try{
            Optional<TransactionModel> thisTransaction = transactionRepository.findById(transactionStatusDetails.getTransactionId());
            if (thisTransaction.isPresent()) {
                try {
                    TransactionModel myTransaction = thisTransaction.get();
                    myTransaction.setStatus(transactionStatusDetails.getStatus());
                    myTransaction.setTransactionCompletedTime(LocalDateTime.now());
                    transactionRepository.save(myTransaction);
                    logger.info("Transaction status updated to complete for ID: {}", transactionStatusDetails.getTransactionId());
                    return new ResponseEntity<>(myTransaction, HttpStatus.OK);
                } catch (RuntimeException e) {
                    throw new RuntimeException("Unable to Update Transaction status updated to complete");
                }
            } else {
                logger.warn("Transaction not found for ID: {}", transactionStatusDetails.getTransactionId());
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        }catch (Exception e) {
            throw new ResourceNotFoundException("Transaction not found for This ID");
        }
    }

    @GetMapping("/get/one/recent/{receiverWalletId}")
    public ResponseEntity<TransactionModel> getOneRecentTransaction(@PathVariable String receiverWalletId) {
        logger.info("Fetching the most recent transaction for receiver wallet ID: {}", receiverWalletId);
        TransactionModel thisTransaction = transactionRepository.findTopByReceiverWalletIdOrderByIdDesc(receiverWalletId);
        if (thisTransaction != null) {
            logger.info(" Recent transaction found: {}", thisTransaction);
            return new ResponseEntity<>(thisTransaction, HttpStatus.OK);
        } else {
            logger.warn("No recent transactions found for receiver wallet ID: {}", receiverWalletId);
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/get/one/recent/by/sender/{senderWalletId}")
    public ResponseEntity<TransactionModel> getOneRecentTransactionBySender(@PathVariable String senderWalletId) {
        logger.info("Fetching the most recent transaction for sender wallet ID: {}", senderWalletId);
        TransactionModel thisTransaction = transactionRepository.findTopBySenderWalletIdOrderByIdDesc(senderWalletId);
        if (thisTransaction != null) {
            logger.info("Recent transaction found: {}", thisTransaction);
            return new ResponseEntity<>(thisTransaction, HttpStatus.OK);
        } else {
            logger.warn("No recent transactions found for sender wallet ID: {}", senderWalletId);
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/get/five/recent/by/sender/{senderWalletId}")
    public List<TransactionModel> getFiveRecentTransactions(@PathVariable String senderWalletId) {
        logger.info("Fetching the top 5 recent transactions for sender wallet ID: {}", senderWalletId);
        return transactionRepository.findTop5UniqueTransactionsByReceiverWalletId(senderWalletId);
    }

    @GetMapping("/get/all/by/sender/{senderWalletId}")
    public List<TransactionModel> getAllTransactionsBySenderWalletId(@PathVariable String senderWalletId) {
        logger.info("Fetching all transactions for sender wallet ID: {}", senderWalletId);
        return transactionRepository.findBySenderWalletId(senderWalletId);
    }

    @GetMapping("/get/all/by/receiver/{receiverWalletId}")
    public List<TransactionModel> getAllTransactionsByReceiverWalletId(@PathVariable String receiverWalletId) {
        logger.info("Fetching all transactions for receiver wallet ID: {}", receiverWalletId);
        return transactionRepository.findByReceiverWalletId(receiverWalletId);
    }

    @GetMapping("/get/five/transactions/for/dashboard/{walletId}")
    public List<TransactionModel> getTop5TransactionsBySenderOrReceiverWalletId(@PathVariable String walletId){
        logger.info("Fetching top five transactions for dashboard by walletId");
        return transactionRepository.findTop5TransactionsBySenderOrReceiverWalletId(walletId);
    }

    @GetMapping("/get/all/transactions/for/dashboard/{walletId}")
    public List<TransactionModel> getAllTransactionsWithoutRefundForReceiver(@PathVariable String walletId){
        logger.info("Fetching All transactions for dashboard by walletId without Refund Transactions for Receiver WalletID");
        return transactionRepository.findAllTransactionsWithoutRefund(walletId);
    }
}
