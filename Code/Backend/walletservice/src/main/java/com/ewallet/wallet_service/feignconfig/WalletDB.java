package com.ewallet.wallet_service.feignconfig;

import com.ewallet.wallet_service.models.PinVerificationModel;
import com.ewallet.wallet_service.models.WalletModel;
import com.ewallet.wallet_service.dto.WalletInitDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("DATABASESERVICE")  // Adjust the URL to match the Database Service
public interface WalletDB {

    @PostMapping("/db/wallet/init")
    public WalletModel initWallet(@RequestBody WalletModel walletInitData);

    @PostMapping("/db/wallet/details")
    public WalletModel getWalletDetails(@RequestBody WalletInitDTO userId);

    @PostMapping("/db/wallet/balance")
    public double getWalletBalance(@RequestBody WalletModel userIdDetails);

    @PostMapping("/db/wallet/wallet-id")
    public String getWalletId(@RequestBody WalletModel userDetails);

    @PostMapping("/db/wallet/limit")
    public Double getWalletLimit(@RequestBody WalletModel userDetails);

    @PostMapping("/db/wallet/exists")
    public Boolean checkWalletExists(@RequestBody WalletModel userDetails);

    @PostMapping("/db/wallet/update/balance")
    public ResponseEntity<WalletModel> updateBalance(@RequestBody WalletModel walletDetails);

    @GetMapping("/db/pin/getbywalletid/{walletId}")
    public PinVerificationModel getByWalletId(@PathVariable String walletId);

    @PostMapping("db/pin/save")
    public ResponseEntity<?> savePinToDb(@RequestBody PinVerificationModel pinDetails);
}
