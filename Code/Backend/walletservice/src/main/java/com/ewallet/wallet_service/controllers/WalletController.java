package com.ewallet.wallet_service.controllers;

import com.ewallet.wallet_service.dto.ChangeWalletPinDTO;
import com.ewallet.wallet_service.exceptions.PinVerificationException;
import com.ewallet.wallet_service.exceptions.ResourceNotFoundException;
import com.ewallet.wallet_service.exceptions.WalletException;
import com.ewallet.wallet_service.models.PinVerificationModel;
import com.ewallet.wallet_service.models.WalletModel;
import com.ewallet.wallet_service.dto.WalletInitDTO;
import com.ewallet.wallet_service.service.PinVerificationService;
import com.ewallet.wallet_service.service.WalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @Autowired
    private PinVerificationService pinService;

    Logger logger = LoggerFactory.getLogger(WalletController.class);

    @GetMapping("/test")
    public ResponseEntity<String> testing() {
        logger.info("Wallet service test endpoint hit");
        return ResponseEntity.ok("Hi, Wallet Service is working");
    }

    @PostMapping("/init")
    public ResponseEntity<WalletModel> initWallet(@RequestBody WalletModel walletInitData) {
        try {
            WalletModel wallet = walletService.initWallet(walletInitData);
            logger.info("Wallet has been Successfully Initialized");
            return new ResponseEntity<>(wallet, HttpStatus.CREATED);
        } catch (WalletException e) {
            logger.error("Error initializing wallet: {}", e.getMessage());
            throw e;
        }
    }

    @GetMapping("/details")
    public ResponseEntity<WalletModel> getWalletDetails(@RequestBody WalletInitDTO userId) {
        try {
            WalletModel wallet = walletService.getWalletDetails(userId);
            if (wallet != null) {
                return ResponseEntity.ok(wallet);  // 200 OK
            } else {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);  // 404 Not Found
            }
        } catch (WalletException e) {
            logger.error("Error Getting the Wallet Details");
            throw new WalletException("Error Getting the Wallet Details");
        }
    }

    @GetMapping("/balance/{userId}")
    public ResponseEntity<Double> getWalletBalance(@PathVariable String userId) {
        WalletModel userDetails = new WalletModel();
        userDetails.setUserId(userId);

        try {
            double balance = walletService.getWalletBalance(userDetails);
            logger.info("Balance Fetched Successfully");
            return ResponseEntity.ok(balance);
        } catch (WalletException e) {
            logger.error("ERROR : Unable to Fetch Balance");
            throw new WalletException("Error Fetching the Balance");
        }
    }

    @GetMapping("/wallet-id")
    public ResponseEntity<String> getWalletId(@RequestBody WalletModel userDetails) {
        try {
            String walletId = walletService.getWalletId(userDetails);
            logger.info("Walled Fetched Successfully");
            return walletId != null ? ResponseEntity.ok(walletId) : new ResponseEntity<>(null, HttpStatus.NOT_FOUND);  // 200 OK or 404 Not Found
        } catch (ResourceNotFoundException e) {
            logger.error("No Wallet Id Found");
            throw new ResourceNotFoundException("No Wallet Id Found");
        } catch (WalletException e) {
            logger.error("ERROR : Unable to fetch walletId");
            throw new WalletException("Unable TO fetch WalletId");
        }
    }

    @GetMapping("/limit")
    public ResponseEntity<Double> getWalletLimit(@RequestBody WalletModel userDetails) {
        try {
            Double limit = walletService.getWalletLimit(userDetails);
            logger.info("Wallet Limit Fetched Successfully");
            return limit != null ? ResponseEntity.ok(limit) : new ResponseEntity<>(null, HttpStatus.NOT_FOUND);  // 200 OK or 404 Not Found
        } catch (WalletException e) {
            logger.error("ERROR : Unable to fetch Wallet Limit");
            throw new WalletException("Unable to fetch Wallet Limit");
        }
    }

    @GetMapping("/exists")
    public ResponseEntity<Boolean> checkIfWalletExists(@RequestBody WalletModel userDetails) {
        try {
            Boolean exists = walletService.getWalletExists(userDetails);
            logger.info("Wallet Status Fetched Successfully");
            return ResponseEntity.ok(exists);  // 200 OK
        } catch (WalletException e) {
            logger.error("ERROR : Unable to fetch the wallet Status");
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/add/money")
    public ResponseEntity<Boolean> addMoneyToWallet(@RequestBody WalletModel moneyDetails){
        try{
            Boolean addMoneyStatus = walletService.addMoneyToWallet(moneyDetails);
            if(addMoneyStatus) {
                logger.info("Money has been Added to the wallet");
                return new ResponseEntity<>(true, HttpStatus.OK);
            }
            logger.info("Unable to add money to wallet");
            return new ResponseEntity<>(false,HttpStatus.CONTINUE);
        } catch (Exception e) {
            logger.error("ERROR : Unable to add money to the wallet : {}",e.getMessage());
            return new ResponseEntity<>(false,HttpStatus.NOT_MODIFIED);
        }
    }

    @PostMapping("/pin/set")
    public ResponseEntity<?> setPin(@RequestBody PinVerificationModel pinDetails) {
        try {
            pinService.saveOrUpdatePin(pinDetails.getWalletId(), pinDetails.getPinHash());
            logger.info("Pin has been Successfully Saved");
            return ResponseEntity.ok("PIN set/updated successfully");
        } catch (PinVerificationException e) {
            logger.error("ERROR : Unable to save the pin {}", e.getMessage());
            throw new PinVerificationException("Failed to set PIN: " + e.getMessage());
        }
    }

    @PostMapping("/pin/verify")
    public ResponseEntity<?> verifyPin(@RequestBody PinVerificationModel pinDetails) {
        try {
            boolean isValid = pinService.verifyPin(pinDetails.getWalletId(), pinDetails.getPinHash());
            if(isValid)logger.info("Verified the Pin");
            else logger.info("Pin verified but Invalid pin entered");
            return isValid
                    ? ResponseEntity.ok("PIN verified")
                    : ResponseEntity.status(403).body("Invalid PIN");
        } catch (PinVerificationException e) {
            logger.error("ERROR : Encountered Error while verifying the Pin {}",e.getMessage());
            throw new PinVerificationException("Failed to verify PIN");
        }
    }

    @GetMapping("/pin/exists/{walletId}")
    public ResponseEntity<?> checkIfPinExists(@PathVariable String walletId) {
        try {
            boolean isValid = pinService.checkIfPinExists(walletId);
            return isValid
                    ? ResponseEntity.ok(true)
                    : ResponseEntity.status(202).body(false);
        } catch (PinVerificationException e) {
            logger.error("ERROR : Encountered Error while Checking if the Pin exists {}",e.getMessage());
            throw new PinVerificationException("Encountered Error while Checking if the Pin exists");
        }
    }

    @PostMapping("/pin/change")
    public ResponseEntity<?> changeWalletPin(@RequestBody ChangeWalletPinDTO walletPinDetails){
        try{
            boolean isValid = pinService.verifyPin(walletPinDetails.getWalletId(), walletPinDetails.getOldPin());
            if(isValid) {
                logger.info("Verified the Old Pin");
            }
            else {
                logger.info("Old Pin verified but Invalid pin entered");
                throw new PinVerificationException("Wrong old Pin was Entered");
            }
            pinService.saveOrUpdatePin(walletPinDetails.getWalletId(), walletPinDetails.getNewPin());
            logger.info("New Pin has been Successfully Saved");
            return ResponseEntity.ok("New PIN set/updated successfully");

        } catch (PinVerificationException e) {
            logger.error("ERROR : Encountered Error while changing the wallet pin {}",e.getMessage());
            throw new PinVerificationException("Unable to change the Pin");
        }
    }
}
