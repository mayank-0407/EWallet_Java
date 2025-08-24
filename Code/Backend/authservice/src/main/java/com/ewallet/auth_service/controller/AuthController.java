package com.ewallet.auth_service.controller;

import com.ewallet.auth_service.dto.*;
import com.ewallet.auth_service.exceptions.UserNotFoundException;
import com.ewallet.auth_service.exceptions.UserUpdateException;
import com.ewallet.auth_service.models.BusinessDetailsModel;
import com.ewallet.auth_service.models.UserModel;
import com.ewallet.auth_service.services.AuthService;
import com.ewallet.auth_service.services.BusinessService;
import com.ewallet.auth_service.services.JwtService;
import com.ewallet.auth_service.services.ProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService service;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtservice;

    @Autowired
    private BusinessService businessService;

    @GetMapping("test")
    public ResponseEntity<String> testing(){
        logger.info("The Test Controller Is Working");
        return new ResponseEntity<>("Testing The Auth Controller",HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<String> addNewUser(@RequestBody UserModel user) {
        if(service.saveUser(user)) {
            logger.info("User Credentials Saved Successfully");
            return new ResponseEntity<>("User Created SuccessFully", HttpStatus.OK);
        }
        else {
            logger.error("Error - User Not Created");
            return new ResponseEntity<>("Error User Not Created", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> getToken(@RequestBody AuthRequestDTO authRequest) {
        System.out.println(authRequest);
        try {
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
            System.out.println("!!! : "+authenticate);
            if (authenticate.isAuthenticated()) {
                String token = service.generateToken(authRequest.getUsername());
                logger.info("Authentication Request Verified : User Logged");
                return new ResponseEntity<>(token, HttpStatus.OK);
            } else {
                logger.error("Encountered Bad Credentials Exception :- Incorrect Username or Password");
                throw new BadCredentialsException("Incorrect username or password");
            }
        } catch (BadCredentialsException e) {
            logger.error("Encountered Bad Credentials Exception :- {}", e.getMessage());
            throw new BadCredentialsException("Incorrect username or password");
        }catch (Exception e) {
            logger.error("Encountered User Not Found Exception :- {}", e.getMessage());
            throw new UserNotFoundException("User Not Found error: " + e.getMessage());
        }

    }

    @GetMapping("/validate")
    public ResponseEntity<String> validateToken(@RequestParam("token") String token) {
        try{
            service.validateToken(token);
            logger.info("Token Is Verified");
            return new ResponseEntity<>("Token is Valid",HttpStatus.OK);
        }
        catch (Exception e){
            logger.error("Error - Token is Invalid");
            throw new BadCredentialsException("Token is Invalid");
        }
    }

    @PostMapping("/forgot/password")
    public ResponseEntity<String> forgotPassword(@RequestBody ChangePasswordwithCurrPasswordDTO passwordDetails) {
        // phoneNumber and newPassword
        Boolean passwordChanged = service.forgotPassword(passwordDetails);
        if (passwordChanged) {
            logger.info("New Password has been set Successfully");
            return new ResponseEntity<>("New Password Has been Saved SuccessFully", HttpStatus.OK);
        } else {
            logger.error("Error - Password was not changed!");
            throw new BadCredentialsException("Error - Password was not changed");
        }
    }

    @PostMapping("/change/password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordwithCurrPasswordDTO passwordDetails) {
        // phoneNumber and newPassword and currentPassword
        Boolean passwordChanged = profileService.changePassword(passwordDetails);
        if (passwordChanged) {
            logger.info("Password Changed Successfully");
            return new ResponseEntity<>("Password Changed SuccessFully", HttpStatus.OK);
        } else {
            logger.error("Error - Password was not changed");
            throw new BadCredentialsException("Error - Password was not changed");
        }
    }
    @PostMapping("/profile/update")
    public ResponseEntity<String> updateTheProfile(@RequestBody UpdateUserDTO userDetails) {
        // username, email and name
        try {
            Boolean userUpdateStatus = profileService.updateProfile(userDetails);
            if (userUpdateStatus) {
                return new ResponseEntity<>("User Updated Successfully", HttpStatus.OK);
            } else {
                logger.error("Error - Unable to Update User Details");
                throw new UserUpdateException("Unable to update user details");
            }
        } catch (UserUpdateException e) {
            logger.error("User update error: {}", e.getMessage(), e);
            return new ResponseEntity<>("Error - Unable to Update User Details", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error("Unexpected error occurred while updating profile: {}", e.getMessage(), e);
            return new ResponseEntity<>("Unexpected error occurred while updating profile", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/extract/data")
    public ResponseEntity<String> getTokenData(@RequestParam("token") String token){
        String Data = jwtservice.extractUsername(token);
        if(Data!=null) {
            logger.info("Token Data Fetched Successfully");
            return new ResponseEntity<>(Data,HttpStatus.OK);
        }
        logger.error("Unable to Fetch Data from Token");
        return new ResponseEntity<>("",HttpStatus.NOT_FOUND);
    }

    @GetMapping("/get/userdetails/{phoneNumber}")
    public ResponseEntity<UserDetailsResponseDTO> getUserDetailsFromPhoneNumber(@PathVariable String phoneNumber){
        try {
            UserModel userDetails = new UserModel();
            userDetails.setPhoneNumber(phoneNumber);
            UserModel thisUser = profileService.fetchUserDetailsFromPhoneNumber(userDetails);
            if (thisUser == null) {
                logger.error("User not found with phone number: {}", phoneNumber);
                throw new UserNotFoundException("User not found with phone number: " + phoneNumber);
            }
            UserDetailsResponseDTO response = new UserDetailsResponseDTO(thisUser);
            return new ResponseEntity<>(response,HttpStatus.OK);
        } catch (UserNotFoundException e) {
            logger.error("User not found with phone number: {}", phoneNumber, e);
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error occurred while fetching user details by phone number: {}", phoneNumber, e);
            throw new RuntimeException("Unexpected error occurred while fetching user details.");
        }
    }

    @GetMapping("/get/userdetails/from/walletid/{walletId}")
    public ResponseEntity<UserDetailsResponseDTO> getUserDetailsFromWalletId(@PathVariable String walletId){
        try {
            UserModel userDetails = new UserModel();
            userDetails.setWalletId(walletId);
            UserModel thisUser = profileService.fetchUserDetailsFromWalletId(userDetails);
            if (thisUser == null) {
                throw new UserNotFoundException("User not found with wallet ID: " + walletId);
            }
            UserDetailsResponseDTO response = new UserDetailsResponseDTO(thisUser);
            return new ResponseEntity<>(response,HttpStatus.OK);
        } catch (UserNotFoundException e) {
            logger.error("User not found with wallet ID: {}", walletId, e);
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error occurred while fetching user details by wallet ID: {}", walletId, e);
            throw new RuntimeException("Unexpected error occurred while fetching user details.");
        }
    }

    @PostMapping("/business/{userId}")
    public ResponseEntity<BusinessDetailsModel> createBusinessDetails(@RequestBody BusinessDetailsModel businessDetails,@PathVariable String userId) {
        try {
            BusinessDetailsModel savedBusinessDetails = businessService.saveBusinessDetails(businessDetails,userId);
            return new ResponseEntity<>(savedBusinessDetails, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Error creating business details", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/business/{businessId}")
    public ResponseEntity<BusinessDetailsModel> getBusinessDetailsById(@PathVariable Long businessId) {
        try {
            BusinessDetailsModel businessDetails = businessService.getBusinessDetailsById(businessId);
            return new ResponseEntity<>(businessDetails, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error retrieving business details by ID: " + businessId, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/business/exists/{businessId}")
    public ResponseEntity<Boolean> getIfBusinessDetailsExistsById(@PathVariable Long businessId) {
        try {
            Boolean businessDetails = businessService.getIfBusinessExistsByBusinessId(businessId);
            return new ResponseEntity<>(businessDetails, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error retrieving business details by ID : {}", businessId, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
