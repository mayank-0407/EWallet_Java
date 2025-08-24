package com.ewallet.transaction_service.feignconfig;

import com.ewallet.transaction_service.models.TransactionModel;
import com.ewallet.transaction_service.dto.TransactionStatusDTO;
import com.ewallet.transaction_service.models.UserModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


@FeignClient("DATABASESERVICE")  // Adjust the URL to match the Database Service
public interface TransactionDB {

    @PostMapping("/db/user/get/by/walletid")
    public ResponseEntity<UserModel> getUserByWalletId(@RequestBody UserModel walletIdDetails);

    @PostMapping("/db/transact/create")
    public ResponseEntity<TransactionModel> saveTransaction(@RequestBody TransactionModel transactionDetails);

    @PostMapping("/db/transact/get/transaction")
    public ResponseEntity<TransactionModel> getTransactionById(@RequestBody TransactionStatusDTO idDetails);

    @PostMapping("/db/transact/change/status")
    public ResponseEntity<TransactionModel> updateTransactionStatus(@RequestBody TransactionStatusDTO transactionStatusDetails);

    @PostMapping("/db/transact/change/status/to/complete")
    public ResponseEntity<TransactionModel> changeTransactionStatusToComplete(@RequestBody TransactionStatusDTO transactionStatusDetails);

    @GetMapping("db/transact/get/one/recent/{receiverWalletId}")
    public ResponseEntity<TransactionModel> getOneRecentTransaction(@PathVariable String receiverWalletId);

    @GetMapping("db/transact/get/one/recent/by/sender/{senderWalletId}")
    public ResponseEntity<TransactionModel> getOneRecentTransactionBySender(@PathVariable String senderWalletId);

    @GetMapping("/db/transact/get/five/recent/by/sender/{senderWalletId}")
    public List<TransactionModel> getFiveRecentTransactions(@PathVariable String senderWalletId);

    @GetMapping("/db/transact/get/all/by/sender/{senderWalletId}")
    public List<TransactionModel> getAllTransactionsBySenderWalletId(@PathVariable String senderWalletId);

    @GetMapping("/db/transact/get/all/by/receiver/{receiverWalletId}")
    public List<TransactionModel> getAllTransactionsByReceiverWalletId(@PathVariable String receiverWalletId);

    @GetMapping("/db/transact/get/five/transactions/for/dashboard/{walletId}")
    public List<TransactionModel> getTop5TransactionsBySenderOrReceiverWalletId(@PathVariable String walletId);

    @GetMapping("/db/transact/get/all/transactions/for/dashboard/{walletId}")
    public List<TransactionModel> getAllTransactionsWithoutRefundForReceiver(@PathVariable String walletId);
}
