package com.ewallet.wallet_service.service;

import com.ewallet.wallet_service.exceptions.PinVerificationException;
import com.ewallet.wallet_service.feignconfig.WalletDB;
import com.ewallet.wallet_service.models.PinVerificationModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class PinVerificationService {

    Logger logger = LoggerFactory.getLogger(PinVerificationService.class);

    @Autowired
    private WalletDB walletClient;

    public void saveOrUpdatePin(String walletId, String rawPin) {
        System.out.println(walletId + " -- " + rawPin);
        try {
            String hash = BCrypt.hashpw(rawPin, BCrypt.gensalt());
            PinVerificationModel response = walletClient.getByWalletId(walletId);

            PinVerificationModel model;

            if (response != null) {
                model = response;
                model.setPinHash(hash);
            } else {
                model = new PinVerificationModel(walletId, hash);
            }

            walletClient.savePinToDb(model);
        } catch (PinVerificationException e) {
            logger.error("ERROR : Encountered Error while Saving/updating the Pin");
            throw new PinVerificationException("Failed to save or update PIN", e);
        }
    }

    public boolean verifyPin(String walletId, String rawPin) {
        try {
            PinVerificationModel response = walletClient.getByWalletId(walletId);

            if (response != null) {
                return BCrypt.checkpw(rawPin, response.getPinHash());
            }
        } catch (PinVerificationException e) {
            logger.error("Encountered Error while Verifying the PIN");
            throw new PinVerificationException("Unable to verify the Pin");
        }
        return false;
    }

    public boolean checkIfPinExists(String walletId){
        try{
            PinVerificationModel response = walletClient.getByWalletId(walletId);
            return response != null;
        } catch (Exception e) {
            logger.error("Error Occurred while fetching Pin {}",e.getMessage());
            return false;
        }
    }
}