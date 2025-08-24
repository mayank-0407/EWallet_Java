package com.ewallet.wallet_service.feignconfig;

import com.ewallet.wallet_service.dto.TransactionDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("TRANSACTIONSERVICE")
public interface TransactionService {
    @PostMapping("/api/transaction/add/money/to/wallet")
    public Boolean addMoneyToWallet(@RequestBody TransactionDTO transactionDetails);
}
