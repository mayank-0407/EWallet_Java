package com.ewallet.auth_service.services;

import com.ewallet.auth_service.exceptions.BusinessDetailsNotFoundException;
import com.ewallet.auth_service.exceptions.BusinessDetailsValidationException;
import com.ewallet.auth_service.feignconfig.UserDB;
import com.ewallet.auth_service.models.BusinessDetailsModel;
import com.ewallet.auth_service.models.UserModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BusinessService {

    private static final Logger logger = LoggerFactory.getLogger(BusinessService.class);

    @Autowired
    private UserDB businessDetailsFeignClient;

    public BusinessDetailsModel saveBusinessDetails(BusinessDetailsModel businessDetails,String userId) {
        try {
            if (businessDetails.getBusinessName() == null || businessDetails.getBusinessName().isEmpty()) {
                throw new BusinessDetailsValidationException("Business name cannot be empty");
            }
            BusinessDetailsModel savedBusinessDetails = businessDetailsFeignClient.createBusinessDetails(businessDetails).getBody();

            UserModel thisUser = businessDetailsFeignClient.getUserByUsername(userId);
            System.out.println(thisUser);
            thisUser.setBusinessDetails(savedBusinessDetails);
            businessDetailsFeignClient.updateBusinessDetails(thisUser);
            return savedBusinessDetails;
        } catch (BusinessDetailsValidationException e) {
            logger.error("Validation error: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error saving business details", e);
            throw new RuntimeException("Error saving business details", e);
        }
    }

    public BusinessDetailsModel getBusinessDetailsById(Long businessId) {
        try {
            return businessDetailsFeignClient.getBusinessDetailsById(businessId).getBody();
        } catch (Exception e) {
            logger.error("Error retrieving business details for ID: " + businessId, e);
            throw new BusinessDetailsNotFoundException("Business details not found with ID: " + businessId);
        }
    }

    public Boolean getIfBusinessExistsByBusinessId(Long businessId) {
        try {
            BusinessDetailsModel thisBusinessDetails= businessDetailsFeignClient.getBusinessDetailsById(businessId).getBody();
            return thisBusinessDetails!=null;
        } catch (Exception e) {
            logger.error("Error retrieving business details for ID: {}", businessId, e);
            return false;
        }
    }
}
