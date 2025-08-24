package com.ewallet.database_service.controllers;

import com.ewallet.database_service.dto.ChangePasswordDTO;
import com.ewallet.database_service.dto.UpdateUserDTO;
import com.ewallet.database_service.dto.UserWalletIdUpdateDTO;
import com.ewallet.database_service.exceptions.UserAlreadyExistsException;
import com.ewallet.database_service.exceptions.UserNotFoundException;
import com.ewallet.database_service.models.UserModel;
import com.ewallet.database_service.repository.UserRepository;
import org.apache.catalina.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/db/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class); // SLF4J Logger

    @Autowired
    private UserRepository repository;

    @PostMapping("/create")
    public ResponseEntity<Boolean> saveUser(@RequestBody UserModel credential) {
        try {
            logger.debug("Attempting to create user with username: {}", credential.getUsername());
            if (credential.getUsername() == null || credential.getUsername().isEmpty()) {
                logger.error("Username cannot be null or empty ");
                throw new IllegalArgumentException("Username cannot be null or empty");
            }
            repository.save(credential);
            logger.info("User created successfully: {}", credential.getUsername());
            return new ResponseEntity<>(true, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            logger.error("Invalid input : {}", e.getMessage());
            throw new RuntimeException("Invalid input: " + e.getMessage(), e);
        } catch (DataIntegrityViolationException e) {
            logger.error("Unique constraint violation: {}", e.getMessage());
            // return proper HTTP status or message for duplicate entry
            throw new UserAlreadyExistsException("User Already Exists");

        } catch (NullPointerException e) {
            logger.error("NullPointerException: One or more required fields are missing: {}", e.getMessage());
            throw new RuntimeException("NullPointerException: One or more required fields are missing", e);
        } catch (Exception e) {
            logger.error("Error creating user: {}", e.getMessage());
            throw new RuntimeException("Error creating user", e);
        }
    }

    @GetMapping("/getbyusername/{username}")
    public UserModel getUserByUsername(@PathVariable String username) {
        try {
            logger.debug("Fetching user by username: {}", username);
            if (username == null || username.isEmpty()) {
                logger.error("Username cannot be null or empty  ");
                throw new IllegalArgumentException("Username cannot be null or empty");
            }
            Optional<UserModel> thisUser = repository.findByUsername(username);
            if (thisUser.isEmpty()) {
                logger.error("User not found with username : {}", username);
                throw new UserNotFoundException("User not found with username: " + username);
            }
            logger.info("User found: {}", username);
            return thisUser.get();
        } catch (Exception e) {
            logger.error("Error fetching user: {}", e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error fetching user", e);
        }
    }

    @PostMapping("/updatewalletid")
    public ResponseEntity<Boolean> updateWalletIdofUser(@RequestBody UserWalletIdUpdateDTO userDetails) {
        try {
            logger.debug("Updating wallet ID for user ID: {}", userDetails.getUserId());
            if (userDetails.getUserId() == null || userDetails.getWalletId() == null) {
                logger.error("User ID and Wallet ID cannot be null");
                throw new IllegalArgumentException("User ID and Wallet ID cannot be null");
            }
            UserModel user = repository.findById(userDetails.getUserId()).orElseThrow(() ->
                    new UserNotFoundException("User not found with ID: " + userDetails.getUserId()));
            user.setWalletId(userDetails.getWalletId());
            repository.save(user);
            logger.info("User wallet ID updated successfully for user ID: {}", userDetails.getUserId());
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            logger.error("User not found with ID: {}", userDetails.getUserId());
            throw e;
        } catch (Exception e) {
            logger.error("Error updating wallet ID for user ID: {}", userDetails.getUserId());
            e.printStackTrace();
            throw new RuntimeException("Error updating wallet ID", e);
        }
    }

    @PostMapping("/get/by/phonenumber")
    public ResponseEntity<UserModel> getUserByPhoneNumber(@RequestBody UserModel phoneNumber) {
        logger.debug("Fetching user by phone number: {}", phoneNumber.getPhoneNumber());
        if (phoneNumber.getPhoneNumber() == null || phoneNumber.getPhoneNumber().isEmpty()) {
            logger.error("Phone number cannot be null or empty");
            throw new IllegalArgumentException("Phone number cannot be null or empty");
        }
        Optional<UserModel> userData = repository.findByPhoneNumber(phoneNumber.getPhoneNumber());
        if (userData.isPresent()) {
            logger.info("User found with phone number: {}", phoneNumber.getPhoneNumber());
            return ResponseEntity.ok(userData.get());
        } else {
            logger.error("User not found with phone number : {}", phoneNumber.getPhoneNumber());
            throw new UserNotFoundException("User not found with phone number");
        }
    }

    @PostMapping("/get/by/walletid")
    public ResponseEntity<UserModel> getUserByWalletId(@RequestBody UserModel walletIdDetails) {
        logger.debug("Fetching user by wallet ID: {}", walletIdDetails.getWalletId());
        if (walletIdDetails.getWalletId() == null || walletIdDetails.getWalletId().isEmpty()) {
            logger.error("Wallet ID cannot be null or empty");
            throw new IllegalArgumentException("Wallet ID cannot be null or empty");
        }
        Optional<UserModel> userData = repository.findByWalletId(walletIdDetails.getWalletId());
        if (userData.isPresent()) {
            logger.info("User found with wallet ID: {}", walletIdDetails.getWalletId());
            return ResponseEntity.ok(userData.get());
        } else {
            logger.error("User not found with wallet ID: {}", walletIdDetails.getWalletId());
            throw new UserNotFoundException("User not found with wallet ID");
        }
    }

    @PostMapping("/change/password")
    public ResponseEntity<Boolean> changePassword(@RequestBody ChangePasswordDTO userDetails) {
        logger.debug("Changing password for phone number: {}", userDetails.getPhoneNumber());
        if (userDetails.getPhoneNumber() == null || userDetails.getNewPassword() == null) {
            logger.error("Phone number and new password cannot be null");
            throw new IllegalArgumentException("Phone number and new password cannot be null");
        }
        Optional<UserModel> userData = repository.findByPhoneNumber(userDetails.getPhoneNumber());
        if (userData.isPresent()) {
            UserModel user = userData.get();
            user.setPassword(userDetails.getNewPassword());
            repository.save(user);
            logger.info("Password changed successfully for phone number: {}", userDetails.getPhoneNumber());
            return new ResponseEntity<>(true, HttpStatus.OK);
        }
        logger.error("User not found with phone number: {}", userDetails.getPhoneNumber());
        throw new UserNotFoundException("User not found with phone number: " + userDetails.getPhoneNumber());
    }

    @PostMapping("/update")
    public ResponseEntity<Boolean> updateUserDetails(@RequestBody UpdateUserDTO credential) {
        logger.debug("Updating user details for username: {}", credential.getUsername());
        try {
            if (credential.getUsername() == null || credential.getUsername().isEmpty()) {
                logger.error("Username cannot be null or empty");
                throw new IllegalArgumentException("Username cannot be null or empty");
            }

            Optional<UserModel> thisUserOptional = repository.findByUsername(credential.getUsername());
            if (!thisUserOptional.isPresent()) {
                logger.error("User not found with username: {}", credential.getUsername());
                throw new UserNotFoundException("User not found with username: " + credential.getUsername());
            }

            UserModel thisUser = thisUserOptional.get();
            thisUser.setName(credential.getName());
            thisUser.setEmail(credential.getEmail());
            repository.save(thisUser);
            logger.info("User details updated successfully for username: {}", credential.getUsername());
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            logger.error("Invalid input: {}", e.getMessage());
            throw new RuntimeException("Invalid input: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Error updating user details: {}", e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error updating user details", e);
        }
    }

    @PostMapping("/update/businessdetails")
    public ResponseEntity<Boolean> updateBusinessDetails(@RequestBody UserModel credential) {
        logger.debug("Updating business details for username: {}", credential.getUsername());
        try {
            if (credential.getUsername() == null || credential.getUsername().isEmpty()) {
                logger.error("Username cannot be null or empty!");
                throw new IllegalArgumentException("Username cannot be null or empty");
            }

            Optional<UserModel> thisUserOptional = repository.findByUsername(credential.getUsername());
            if (!thisUserOptional.isPresent()) {
                logger.error("User  not found with username: {}", credential.getUsername());
                throw new UserNotFoundException("User not found with username: " + credential.getUsername());
            }

            UserModel thisUser = thisUserOptional.get();
            thisUser.setBusinessDetails(credential.getBusinessDetails());
            repository.save(thisUser);
            logger.info("Business details updated successfully for username: {}", credential.getUsername());
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            logger.error("Invalid input  : {}", e.getMessage());
            throw new RuntimeException("Invalid input: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Error updating Business details: {}", e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error updating user details", e);
        }
    }
}
