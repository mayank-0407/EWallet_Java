package com.ewallet.auth_service.feignconfig;

import com.ewallet.auth_service.dto.ChangePasswordDTO;
import com.ewallet.auth_service.dto.UpdateUserDTO;
import com.ewallet.auth_service.models.BusinessDetailsModel;
import com.ewallet.auth_service.models.UserModel;
import com.ewallet.auth_service.dto.UserWalletIdUpdateDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("DATABASESERVICE")
public interface UserDB {

    @PostMapping("/db/user/create")
    ResponseEntity<Boolean> saveUser(@RequestBody UserModel credential);

    @GetMapping("/db/user/getbyusername/{username}")
    public UserModel getUserByUsername(@PathVariable String username);

    @PostMapping("/db/user/updatewalletid")
    ResponseEntity<Boolean> updateWalletIdofUser(@RequestBody UserWalletIdUpdateDTO userDetails);

    @PostMapping("/db/user/get/by/phonenumber")
    public ResponseEntity<UserModel> getUserByPhoneNumber(@RequestBody UserModel phoneNumber);

    @PostMapping("/db/user/change/password")
    public ResponseEntity<Boolean> changePassword(@RequestBody ChangePasswordDTO userDetails);

    @PostMapping("/db/user/update")
    public ResponseEntity<Boolean> updateUserDetails(@RequestBody UpdateUserDTO credential);

    @PostMapping("/db/user/get/by/walletid")
    public ResponseEntity<UserModel> getUserByWalletId(@RequestBody UserModel walletIdDetails);

    @PostMapping("/db/businessdetails")
    public ResponseEntity<BusinessDetailsModel> createBusinessDetails(@RequestBody BusinessDetailsModel businessDetails);

    @GetMapping("/db/businessdetails/{id}")
    public ResponseEntity<BusinessDetailsModel> getBusinessDetailsById(@PathVariable Long id);

    @PostMapping("/db/user/update/businessdetails")
    public ResponseEntity<Boolean> updateBusinessDetails(@RequestBody UserModel credential);
}