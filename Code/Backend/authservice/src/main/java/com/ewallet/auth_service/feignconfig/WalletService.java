package com.ewallet.auth_service.feignconfig;

import com.ewallet.auth_service.dto.WalletModelDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("WALLETSERVICE")
public interface WalletService {

    @PostMapping("api/wallet/init")
    public ResponseEntity<WalletModelDTO> initWallet(@RequestBody WalletModelDTO walletInitData);
}
