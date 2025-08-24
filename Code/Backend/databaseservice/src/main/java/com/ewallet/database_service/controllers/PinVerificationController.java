package com.ewallet.database_service.controllers;

import com.ewallet.database_service.exceptions.PinVerificationException;
import com.ewallet.database_service.exceptions.ResourceNotFoundException;
import com.ewallet.database_service.models.PinVerificationModel;
import com.ewallet.database_service.repository.PinVerificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@RestController
@RequestMapping("/db/pin")
public class PinVerificationController {

    private static final Logger logger = LoggerFactory.getLogger(PinVerificationController.class);

    @Autowired
    private PinVerificationRepository repository;

    @GetMapping("/getbywalletid/{walletId}")
    public PinVerificationModel getByWalletId(@PathVariable String walletId) {
        try {
            Optional<PinVerificationModel> wallet = repository.findByWalletId(walletId);
            if (wallet.isPresent()) {
                return wallet.get();
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping("/save")
    public ResponseEntity<?> savePinToDb(@RequestBody PinVerificationModel pinDetails) {
        try {
            logger.info("Saving PIN verification details for walletId: {}", pinDetails.getWalletId());
            PinVerificationModel saved = repository.save(pinDetails);
            if (saved == null) {
                logger.warn("Failed to save PIN details for walletId: {}", pinDetails.getWalletId());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            }
            logger.info("PIN details saved successfully for walletId: {}", pinDetails.getWalletId());
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception e) {
            logger.error("Error saving PIN verification details for walletId: {}", pinDetails.getWalletId(), e);
            throw new PinVerificationException("Error saving PIN to DB");
        }
    }
}
