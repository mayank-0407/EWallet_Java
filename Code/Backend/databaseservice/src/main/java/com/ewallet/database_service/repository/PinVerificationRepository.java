package com.ewallet.database_service.repository;

import com.ewallet.database_service.models.PinVerificationModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PinVerificationRepository extends JpaRepository<PinVerificationModel, Long> {
    Optional<PinVerificationModel> findByWalletId(String walletId);
}