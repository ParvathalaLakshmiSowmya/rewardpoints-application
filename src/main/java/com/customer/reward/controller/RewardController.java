package com.customer.reward.controller;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.customer.reward.dto.CustomerRewardResponseDTO;
import com.customer.reward.exception.InvalidDateException;
import com.customer.reward.exception.TransactionNotFoundException;
import com.customer.reward.service.RewardService;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping(value = "/rewards")
@Validated
public class RewardController {

    private static final Logger log = LoggerFactory.getLogger(RewardController.class);
    
	private final RewardService rewardService;
	
	public RewardController(RewardService rewardService) {
		this.rewardService = rewardService;
	} 
	
	//to calculate reward points by customer
	@GetMapping("/calculaterewardpoints/{customerId}")
	public ResponseEntity<CustomerRewardResponseDTO> getRewardPointsDetails(
			@PathVariable
            @Positive(message = "{validator.CUSTOMER_ID_POSITIVE}")
            Integer customerId,

            @RequestParam(required = false)
            @Min(value = 1, message = "{validator.INVALID_MONTHS}")
            Integer months,

            @RequestParam(required = false)
            @PastOrPresent(message = "{validator.START_DATE_INVALID}")
            LocalDate startDate,

            @RequestParam(required = false)
            @PastOrPresent(message = "{validator.END_DATE_INVALID}")
            LocalDate endDate) throws TransactionNotFoundException, InvalidDateException { 
		log.info("Received request from the user");
		CustomerRewardResponseDTO response = rewardService.getCustomerRewardPoints(customerId, months, startDate, endDate);
		log.info("Successfully calculated rewards Points");
		return new ResponseEntity<CustomerRewardResponseDTO> (response, HttpStatus.OK);
	}
}
