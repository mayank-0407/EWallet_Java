package com.ewallet.wallet_service.service;

import com.ewallet.wallet_service.dto.TransactionDTO;
import com.ewallet.wallet_service.exceptions.WalletException;
import com.ewallet.wallet_service.feignconfig.TransactionService;
import com.ewallet.wallet_service.models.WalletModel;
import com.ewallet.wallet_service.dto.WalletInitDTO;
import com.ewallet.wallet_service.feignconfig.WalletDB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WalletService {

    @Autowired
    private WalletDB walletClient;

    @Autowired
    private TransactionService transactionService;

    Logger logger = LoggerFactory.getLogger(WalletService.class);

    public WalletModel initWallet(WalletModel walletInitData) {
        return walletClient.initWallet(walletInitData);
    }

    public WalletModel getWalletDetails(WalletInitDTO userId) {
        return walletClient.getWalletDetails(userId);
    }

    public double getWalletBalance(WalletModel userId) {
        return walletClient.getWalletBalance(userId);
    }

    public String getWalletId(WalletModel userDetails) {
        return walletClient.getWalletId(userDetails);
    }

    public Double getWalletLimit(WalletModel userDetails) {
        return walletClient.getWalletLimit(userDetails);
    }

    public Boolean getWalletExists(WalletModel userDetails){
        return walletClient.checkWalletExists(userDetails);
    }
    public Boolean addMoneyToWallet(WalletModel walletDetails){

        WalletInitDTO tempThisUserDetails = new WalletInitDTO();
        tempThisUserDetails.setUserId(walletDetails.getUserId());

        WalletModel thisWalletDetails = walletClient.getWalletDetails(tempThisUserDetails);
        double transferAmount = walletDetails.getBalance();

        if(transferAmount <=0 ) throw new WalletException("Transfer Amount should be greater than 0.");

        double newBalance = transferAmount + thisWalletDetails.getBalance();
        walletDetails.setBalance(newBalance);
        WalletModel updatedWalletDetails = walletClient.updateBalance(walletDetails).getBody();

        if(updatedWalletDetails!=null) {
            TransactionDTO transactionDetails = new TransactionDTO();
            transactionDetails.setReceiverId(updatedWalletDetails.getid());
            transactionDetails.setAmount(transferAmount);
            Boolean updatedTransaction = transactionService.addMoneyToWallet(transactionDetails);
            return updatedTransaction;
        }
        return false;
    }
}