package com.ewallet.auth_service.services;

import com.ewallet.auth_service.dto.*;
import com.ewallet.auth_service.exceptions.UserCreationException;
import com.ewallet.auth_service.feignconfig.UserDB;
import com.ewallet.auth_service.feignconfig.WalletService;
import com.ewallet.auth_service.models.UserModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthService {

    Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private UserDB userdb;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private WalletService walletService;

    public Boolean saveUser(UserModel credential) {
        try {
            logger.debug("Received user credentials: {}", credential);

            if (credential == null) {
                throw new UserCreationException("User data cannot be null.");
            }
            if (credential.getEmail() == null || credential.getEmail().trim().isEmpty()) {
                throw new UserCreationException("Email cannot be null or empty.");
            }
            if (credential.getPhoneNumber() == null || credential.getPhoneNumber().trim().isEmpty()) {
                throw new UserCreationException("Phone number cannot be null or empty.");
            }
            if (credential.getPassword() == null || credential.getPassword().trim().isEmpty()) {
                throw new UserCreationException("Password cannot be null or empty.");
            }

            credential.setUsername(credential.getPhoneNumber());
            credential.setPassword(passwordEncoder.encode(credential.getPassword()));

            Boolean userCreated = userdb.saveUser(credential).getBody();
            if (Boolean.FALSE.equals(userCreated)) {
                logger.error("User creation failed for phone number: {}", credential.getPhoneNumber());
                throw new UserCreationException("Failed to create user.");
            }

            UserModel thisUserFromDb = userdb.getUserByUsername(credential.getUsername());

            String namePrefix = credential.getName().substring(0, 2).toLowerCase();
            String phoneNumber = credential.getPhoneNumber();
            String walletId = namePrefix + phoneNumber + "@wallet";
            double walletLimit = 5000.0;
            if (credential.getMerchant()) {
                walletLimit = 10000.0;
            }

            WalletModelDTO walletData = new WalletModelDTO(thisUserFromDb.getId().toString(), 0.0, walletLimit, walletId);
            WalletModelDTO myWallet = walletService.initWallet(walletData).getBody();

            if (myWallet == null) {
                logger.error("Failed to initialize wallet for user: {}", thisUserFromDb.getId());
                throw new UserCreationException("Failed to initialize wallet.");
            }

            UserWalletIdUpdateDTO walletIdUpdateDetails = new UserWalletIdUpdateDTO(thisUserFromDb.getId(), myWallet.getid());
            Boolean walletUpdated = userdb.updateWalletIdofUser(walletIdUpdateDetails).getBody();

            if (Boolean.FALSE.equals(walletUpdated)) {
                logger.error("Failed to update wallet ID for user: {}", thisUserFromDb.getId());
                throw new UserCreationException("Failed to update wallet ID.");
            }

            return userCreated;

        } catch (Exception e) {
            logger.error("Error occurred while saving user with phone number: {}. Error: {}", credential.getPhoneNumber(), e.getMessage(), e);
            throw new UserCreationException("An error occurred while processing the request.");
        }
    }

    public String generateToken(String username) {
        return jwtService.generateToken(username);
    }

    public void validateToken(String token) {
        jwtService.validateToken(token);
    }
    public Boolean forgotPassword(ChangePasswordwithCurrPasswordDTO passwordDetails) {
        UserModel tempUserDetails = new UserModel();
        tempUserDetails.setPhoneNumber(passwordDetails.getPhoneNumber());

        try {
            UserModel thisUser = userdb.getUserByPhoneNumber(tempUserDetails).getBody();

            if (thisUser == null) {
                logger.warn("No user found for phone number: {}", passwordDetails.getPhoneNumber());
                throw new UserCreationException("User not found with the provided phone number.");
            }

            ChangePasswordDTO newPasswordDetails = new ChangePasswordDTO(passwordDetails.getPhoneNumber(),
                    passwordEncoder.encode(passwordDetails.getNewPassword()));

            Boolean passwordChanged = userdb.changePassword(newPasswordDetails).getBody();

            if (!Boolean.TRUE.equals(passwordChanged)) {
                logger.error("Failed to change password for phone number: {}", passwordDetails.getPhoneNumber());
                throw new UserCreationException("Password change failed.");
            }

            return true;

        } catch (Exception e) {
            logger.error("Error occurred while processing password change for phone number: {}. Error: {}",
                    passwordDetails.getPhoneNumber(), e.getMessage(), e);
            return false;
        }
    }

}
