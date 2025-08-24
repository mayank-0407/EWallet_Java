package com.ewallet.refund_service.feign;

import com.ewallet.refund_service.dto.TransactionStatusDTO;
import com.ewallet.refund_service.models.TransactionModel;
import com.ewallet.refund_service.models.WalletModel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("DATABASESERVICE")  // Adjust the URL to match the Database Service
public interface FeignDBService {


    @PostMapping("/db/transact/change/status")
    public ResponseEntity<TransactionModel> updateTransactionStatus(@RequestBody TransactionStatusDTO transactionStatusDetails);

    @PostMapping("/db/transact/change/status/to/complete")
    public ResponseEntity<TransactionModel> changeTransactionStatusToComplete(@RequestBody TransactionStatusDTO transactionStatusDetails);

    @PostMapping("/db/wallet/update/balance")
    public ResponseEntity<WalletModel> updateBalance(@RequestBody WalletModel walletDetails);

    @PostMapping("/db/transact/create")
    public ResponseEntity<TransactionModel> saveTransaction(@RequestBody TransactionModel transactionDetails);

    @PostMapping("/db/transact/get/transaction")
    public ResponseEntity<TransactionModel> getTransactionById(@RequestBody TransactionStatusDTO idDetails);
}
