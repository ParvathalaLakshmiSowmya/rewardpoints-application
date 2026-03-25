package com.demo.reward.controller;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.demo.reward.dto.CustomerRewardResponseDTO;
import com.demo.reward.exception.InvalidDateException;
import com.demo.reward.exception.TransactionNotFoundException;
import com.demo.reward.service.RewardService;

@RestController
@RequestMapping(value = "/rewards")
public class RewardController {

    private static final Logger log = LoggerFactory.getLogger(RewardController.class);
    
	private final RewardService rewardService;
	
	public RewardController(RewardService rewardService) {
		this.rewardService = rewardService;
	}
	
	//to calculate reward points by customer
	@GetMapping("/calculaterewardpoints/{customerId}")
	public ResponseEntity<CustomerRewardResponseDTO> getRewardPointsDetails(
			@PathVariable Integer customerId,
			@RequestParam(required = false) Integer months,
			@RequestParam(required = false) LocalDate startDate, 
			@RequestParam(required = false) LocalDate endDate) throws TransactionNotFoundException, InvalidDateException { 
		log.info("Received request from the user");
		CustomerRewardResponseDTO response = rewardService.getCustomerRewardPoints(customerId, months, startDate, endDate);
		log.info("Successfully calculated rewards Points");
		return new ResponseEntity<CustomerRewardResponseDTO> (response, HttpStatus.OK);
	}
}
