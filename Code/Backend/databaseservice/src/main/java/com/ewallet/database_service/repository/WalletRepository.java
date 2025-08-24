package com.ewallet.database_service.repository;

import com.ewallet.database_service.models.WalletModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WalletRepository  extends JpaRepository<WalletModel,String> {
    Optional<WalletModel> findByUserId(String userId);
}