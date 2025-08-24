package com.ewallet.database_service.controllers;

import com.ewallet.database_service.exceptions.BusinessDetailsNotFoundException;
import com.ewallet.database_service.exceptions.BusinessDetailsValidationException;
import com.ewallet.database_service.models.BusinessDetailsModel;
import com.ewallet.database_service.repository.BusinessDetailsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/db/businessdetails")
public class BusinessDetailsController {

    private static final Logger logger = LoggerFactory.getLogger(BusinessDetailsController.class);

    @Autowired
    private BusinessDetailsRepository businessDetailsRepository;

    @PostMapping
    public ResponseEntity<BusinessDetailsModel> createBusinessDetails(@RequestBody BusinessDetailsModel businessDetails) {
        try {
            if (businessDetails.getBusinessName() == null || businessDetails.getBusinessName().isEmpty()) {
                throw new BusinessDetailsValidationException("Business name cannot be empty");
            }

            BusinessDetailsModel savedBusinessDetails = businessDetailsRepository.save(businessDetails);
            return new ResponseEntity<>(savedBusinessDetails, HttpStatus.CREATED);
        } catch (BusinessDetailsValidationException e) {
            logger.error("Invalid business details: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error("Error saving business details", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<BusinessDetailsModel> getBusinessDetailsById(@PathVariable Long id) {
        try {
            Optional<BusinessDetailsModel> businessDetails = businessDetailsRepository.findById(id);
            if (businessDetails.isEmpty()) {
                throw new BusinessDetailsNotFoundException("Business details not found with ID: " + id);
            }
            return new ResponseEntity<>(businessDetails.get(), HttpStatus.OK);
        } catch (BusinessDetailsNotFoundException e) {
            logger.error("Business details not found with ID: " + id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error retrieving business details by ID", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}