package com.ewallet.database_service.controllers;

import com.ewallet.database_service.dto.WalletInitDTO;
import com.ewallet.database_service.models.WalletModel;
import com.ewallet.database_service.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@RestController
@RequestMapping("/db/wallet")
public class WalletController {

    private static final Logger logger = LoggerFactory.getLogger(WalletController.class);
    @Autowired
    private WalletRepository walletRepository;

    @GetMapping("/test")
    public String testing(){
        logger.info("Testing validation endpoint");
        return "Hi validation is working";
    }

    @PostMapping("/init")
    public ResponseEntity<WalletModel> initWallet(@RequestBody WalletModel walletInitData) {
        try {
            logger.info("Initializing wallet for userId: {}", walletInitData.getUserId());
            Optional<WalletModel> existingWallet = walletRepository.findByUserId(walletInitData.getUserId());
            if (existingWallet.isPresent()) {
                logger.warn("Wallet already exists for userId: {}", walletInitData.getUserId());
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);  // Wallet already exists
            }
            WalletModel wallet = new WalletModel(walletInitData.getUserId(), 0.0, walletInitData.getLimit(), walletInitData.getid());
            walletRepository.save(wallet);
            logger.info("Wallet created successfully for userId: {}", walletInitData.getUserId());
            return new ResponseEntity<>(wallet, HttpStatus.CREATED);  // Wallet created successfully
        } catch (Exception ex) {
            logger.error("Error initializing wallet for userId: {}", walletInitData.getUserId(), ex);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/details")
    public ResponseEntity<WalletModel> getWalletDetails(@RequestBody WalletInitDTO requestBody) {
        try {
            String userId = requestBody.getUserId();
            logger.info("Fetching wallet details for userId: {}", userId);
            Optional<WalletModel> wallet = walletRepository.findByUserId(userId);
            if (wallet.isPresent()) {
                logger.info("Wallet details found for userId: {}", userId);
                return new ResponseEntity<>(wallet.get(), HttpStatus.OK);  // Wallet found
            } else {
                logger.warn("No wallet found for userId: {}", userId);
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);  // Wallet not found
            }
        } catch (Exception ex) {
            logger.error("Error fetching wallet details for userId: {}", requestBody.getUserId(), ex);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/balance")
    public ResponseEntity<Double> getWalletBalance(@RequestBody WalletModel userIdDetails) {
        try {
            logger.info("Fetching wallet balance for userId: {}", userIdDetails.getUserId());
            Optional<WalletModel> wallet = walletRepository.findByUserId(userIdDetails.getUserId());
            if (wallet.isPresent()) {
                logger.info("Wallet balance retrieved for userId: {}", userIdDetails.getUserId());
                return new ResponseEntity<>(wallet.get().getBalance(), HttpStatus.OK);
            } else {
                logger.warn(" No wallet found for userId: {}", userIdDetails.getUserId());
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        } catch (Exception ex) {
            logger.error("Error fetching wallet balance for userId: {}", userIdDetails.getUserId(), ex);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/wallet-id")
    public ResponseEntity<String> getWalletId(@RequestBody WalletModel userDetails) {
        try {
            logger.info("Fetching wallet ID for userId: {}", userDetails.getUserId());
            Optional<WalletModel> wallet = walletRepository.findByUserId(userDetails.getUserId());
            if (wallet.isPresent()) {
                logger.info("Wallet ID retrieved for userId: {}", userDetails.getUserId());
                return new ResponseEntity<>(wallet.get().getid(), HttpStatus.OK);  // Return wallet ID
            } else {
                logger.warn("No wallet found for userId : {} !", userDetails.getUserId());
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);  // Wallet not found
            }
        } catch (Exception ex) {
            logger.error("Error fetching wallet ID for userId: {}", userDetails.getUserId(), ex);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/limit")
    public ResponseEntity<Double> getWalletLimit(@RequestBody WalletModel userDetails) {
        try {
            logger.info("Fetching wallet limit for userId: {}", userDetails.getUserId());
            Optional<WalletModel> wallet = walletRepository.findByUserId(userDetails.getUserId());
            if (wallet.isPresent()) {
                logger.info("Wallet limit retrieved for userId: {}", userDetails.getUserId());
                return new ResponseEntity<>(wallet.get().getLimit(), HttpStatus.OK);  // Return wallet limit
            } else {
                logger.warn("No wallet found for userId : {}", userDetails.getUserId());
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);  // Wallet not found
            }
        } catch (Exception ex) {
            logger.error("Error fetching wallet limit for userId: {}", userDetails.getUserId(), ex);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/exists")
    public ResponseEntity<Boolean> checkWalletExists(@RequestBody WalletModel userDetails) {
        try {
            logger.info("Checking if wallet exists for userId: {}", userDetails.getUserId());
            Optional<WalletModel> existingWallet = walletRepository.findByUserId(userDetails.getUserId());
            if (existingWallet.isPresent()) {
                logger.info("Wallet exists for userId: {}", userDetails.getUserId());
                return new ResponseEntity<>(true, HttpStatus.OK);  // Wallet exists
            } else {
                logger.warn("No wallet found for userId :: {}", userDetails.getUserId());
                return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);  // Wallet doesn't exist
            }
        } catch (Exception ex) {
            logger.error("Error checking wallet existence for userId: {}", userDetails.getUserId(), ex);
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/update/balance")
    public ResponseEntity<WalletModel> updateBalance(@RequestBody WalletModel walletDetails) {
        try {
            logger.info("Updating balance for userId: {}", walletDetails.getUserId());
            Optional<WalletModel> existingWallet = walletRepository.findByUserId(walletDetails.getUserId());
            if (existingWallet.isPresent()) {
                WalletModel wallet = existingWallet.get();
                wallet.setBalance(walletDetails.getBalance());
                walletRepository.save(wallet);
                logger.info("Balance updated successfully for userId: {}", walletDetails.getUserId());
                return new ResponseEntity<>(wallet, HttpStatus.CREATED);  // Balance updated successfully
            } else {
                logger.warn(" No wallet found for userId : {}", walletDetails.getUserId());
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);  // Wallet not found
            }
        } catch (Exception ex) {
            logger.error("Error updating balance for userId: {}", walletDetails.getUserId(), ex);
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
