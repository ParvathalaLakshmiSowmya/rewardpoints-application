package com.customer.reward.service;

import java.time.LocalDate;

import com.customer.reward.dto.CustomerRewardResponseDTO;
import com.customer.reward.exception.InvalidDateException;
import com.customer.reward.exception.TransactionNotFoundException;

public interface RewardService {
	public CustomerRewardResponseDTO getCustomerRewardPoints(Integer customerId, Integer months, LocalDate startDate, LocalDate endDate) throws InvalidDateException, TransactionNotFoundException;
}
