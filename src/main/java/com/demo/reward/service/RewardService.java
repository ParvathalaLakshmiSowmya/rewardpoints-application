package com.demo.reward.service;

import java.time.LocalDate;

import com.demo.reward.dto.CustomerRewardResponseDTO;
import com.demo.reward.exception.InvalidDateException;
import com.demo.reward.exception.TransactionNotFoundException;

public interface RewardService {
	public CustomerRewardResponseDTO getCustomerRewardPoints(Integer customerId, Integer months, LocalDate startDate, LocalDate endDate) throws InvalidDateException, TransactionNotFoundException;
}
