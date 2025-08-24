package com.ewallet.payment_service.feignconfig;

import com.ewallet.payment_service.dto.*;
import com.ewallet.payment_service.models.TransactionModel;
import com.ewallet.payment_service.models.UserModel;
import com.ewallet.payment_service.models.WalletModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("DATABASESERVICE")  // Adjust the URL to match the Database Service
public interface FeignDBService {

    @PostMapping("/db/transact/get/transaction")
    public ResponseEntity<TransactionModel> getTransactionById(@RequestBody TransactionStatusDTO idDetails);

    @PostMapping("/db/transact/change/status")
    public ResponseEntity<TransactionModel> updateTransactionStatus(@RequestBody TransactionStatusDTO transactionStatusDetails);

    @PostMapping("/db/transact/change/status/to/complete")
    public ResponseEntity<TransactionModel> changeTransactionStatusToComplete(@RequestBody TransactionStatusDTO transactionStatusDetails);

    @PostMapping("/db/wallet/details")
    public ResponseEntity<WalletModel> getWalletDetails(@RequestBody WalletInitDTO userId);

    @PostMapping("/db/wallet/update/balance")
    public ResponseEntity<WalletModel> updateBalance(@RequestBody WalletModel walletDetails);

    @PostMapping("/db/user/get/by/walletid")
    public ResponseEntity<UserModel> getUserByWalletId(@RequestBody UserModel walletIdDetails);

    @PostMapping("db/transact/create")
    public ResponseEntity<TransactionModel> saveTransaction(@RequestBody TransactionModel transactionDetails);
}
