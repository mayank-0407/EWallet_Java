package com.ewallet.auth_service.services;

import com.ewallet.auth_service.dto.*;
import com.ewallet.auth_service.exceptions.UserCreationException;
import com.ewallet.auth_service.feignconfig.UserDB;
import com.ewallet.auth_service.models.UserModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    @Autowired
    private UserDB userdb;

    @Autowired
    private PasswordEncoder passwordEncoder;

    Logger logger = LoggerFactory.getLogger(ProfileService.class);

    public Boolean changePassword(ChangePasswordwithCurrPasswordDTO passwordDetails) {
        UserModel tempUserDetails = new UserModel();
        tempUserDetails.setPhoneNumber(passwordDetails.getPhoneNumber());

        try {
            UserModel thisUser = userdb.getUserByPhoneNumber(tempUserDetails).getBody();
            if (thisUser == null) {
                logger.warn("No user found with phone number: {}", passwordDetails.getPhoneNumber());
                throw new UserCreationException("User not found.");
            }

            if (passwordEncoder.matches(passwordDetails.getCurrentPassword(), thisUser.getPassword())) {
                ChangePasswordDTO newPasswordDetails = new ChangePasswordDTO(passwordDetails.getPhoneNumber(),
                        passwordEncoder.encode(passwordDetails.getNewPassword()));

                Boolean passwordChanged = userdb.changePassword(newPasswordDetails).getBody();

                if (Boolean.TRUE.equals(passwordChanged)) {
                    return true;
                } else {
                    logger.error("Failed to change password for phone number: {}", passwordDetails.getPhoneNumber());
                    return false;
                }
            } else {
                logger.warn("Current password does not match for phone number: {}", passwordDetails.getPhoneNumber());
                return false;
            }
        } catch (Exception e) {
            logger.error("Error occurred while changing password for phone number: {}. Error: {}", passwordDetails.getPhoneNumber(), e.getMessage(), e);
            return false;
        }
    }

    public Boolean updateProfile(UpdateUserDTO userDetails) {
        try {
            Boolean userUpdateStatus = userdb.updateUserDetails(userDetails).getBody();
            if (Boolean.TRUE.equals(userUpdateStatus)) {
                return true;
            } else {
                logger.error("Failed to update profile for user: {}", userDetails);
                return false;
            }
        } catch (Exception e) {
            logger.error("Error occurred while updating profile for user: {}. Error: {}", userDetails, e.getMessage(), e);
            return false;
        }
    }

    public UserModel fetchUserDetailsFromPhoneNumber(UserModel userDetails){
        try{
            UserModel thisUser = userdb.getUserByPhoneNumber(userDetails).getBody();
            if (thisUser != null) {
                return thisUser;
            } else {
                logger.warn("No user found with phone number : {}", userDetails.getPhoneNumber());
                return null;
            }
        } catch (Exception e) {
            logger.error("Error occurred while fetching user details for phone number: {}. Error: {}", userDetails.getPhoneNumber(), e.getMessage(), e);
            return null;
        }
    }

    public UserModel fetchUserDetailsFromWalletId(UserModel userDetails){
       try {
            UserModel thisUser = userdb.getUserByWalletId(userDetails).getBody();
            if (thisUser != null) {
                return thisUser;
            } else {
                logger.warn("No user found with wallet ID: {}", userDetails.getWalletId());
                return null;
            }
        } catch (Exception e) {
            logger.error("Error occurred while fetching user details for wallet ID: {}. Error: {}", userDetails.getWalletId(), e.getMessage(), e);
            return null;
        }
    }
}
