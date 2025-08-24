package com.ewallet.database_service.repository;

import com.ewallet.database_service.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserModel,Long> {
    Optional<UserModel> findByUsername(String username);
    Optional<UserModel> findById(Long userId);
    Optional<UserModel> findByPhoneNumber(String phoneNumber);
    Optional<UserModel> findByWalletId(String walletId);
}
