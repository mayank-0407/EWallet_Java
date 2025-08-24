package com.ewallet.auth_service.services;

import com.ewallet.auth_service.config.CustomUserDetails;
import com.ewallet.auth_service.models.UserModel;
import com.ewallet.auth_service.feignconfig.UserDB;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserDB userdb;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            UserModel user = userdb.getUserByUsername(username);
            return Optional.ofNullable(user)
                    .map(CustomUserDetails::new)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        } catch (FeignException.NotFound e) {
            throw new UsernameNotFoundException("User not found with username: " + username, e);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching user from external service", e);
        }
    }


}