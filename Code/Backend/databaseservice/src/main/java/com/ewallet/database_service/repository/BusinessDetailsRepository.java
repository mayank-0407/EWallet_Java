package com.ewallet.database_service.repository;

import com.ewallet.database_service.models.BusinessDetailsModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessDetailsRepository extends JpaRepository<BusinessDetailsModel, Long> {
}