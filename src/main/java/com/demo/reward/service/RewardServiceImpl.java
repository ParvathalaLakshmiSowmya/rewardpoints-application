package com.demo.reward.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.demo.reward.dto.CustomerRewardResponseDTO;
import com.demo.reward.entity.Customer;
import com.demo.reward.entity.Transaction;
import com.demo.reward.exception.InvalidDateException;
import com.demo.reward.exception.TransactionNotFoundException;
import com.demo.reward.repository.CustomerRepository;
import com.demo.reward.repository.TransactionRepository;

@Service
public class RewardServiceImpl implements RewardService {
	
	private TransactionRepository transactionRepository;
	
	private CustomerRepository customerRepository;
	
    private static final Logger log = LoggerFactory.getLogger(RewardServiceImpl.class);
	 
	public RewardServiceImpl(TransactionRepository transactionRepository, CustomerRepository customerRepository) {
		this.transactionRepository = transactionRepository;
		this.customerRepository = customerRepository;
	}
	
	@Override
	public CustomerRewardResponseDTO getCustomerRewardPoints(Integer customerId, Integer months, LocalDate startDate,
		LocalDate endDate) throws InvalidDateException, TransactionNotFoundException {
		
		log.info("Fetching reward points for customerId={}, months={}, startDate={}, endDate={}", customerId, months, startDate, endDate);
		
		validateDate(months, startDate, endDate);
		
		Customer customer = customerRepository.findById(customerId)
				.orElseThrow(() -> {
                    log.error("Customer with id={} not found", customerId);
                    return new TransactionNotFoundException("Customer not found");
                });
		
		if (months == null && startDate == null && endDate == null) {
			endDate = LocalDate.now();
			startDate = endDate.minusMonths(3);
            log.debug("Defaulting to last 3 months: startDate={}, endDate={}", startDate, endDate);
		}
		if (months != null) {
			endDate = LocalDate.now();
			startDate = endDate.minusMonths(months);
            log.debug("Using months={} range: startDate={}, endDate={}", months, startDate, endDate);
		}
		
		List<Transaction> transactions = transactionRepository.findByCustomerCustomerIdAndTransactionDateBetween(customerId, startDate, endDate);
				
		if (transactions.isEmpty()) {
            log.warn("No transactions found for customerId={} between {} and {}", customerId, startDate, endDate);
			throw new TransactionNotFoundException("No Transactions Found");
		} 
	
		Map<String, Double> monthlyPoints = transactions.stream()
				.collect(Collectors.groupingBy(
						t -> t.getTransactionDate().getMonth().name(),
						Collectors.summingDouble(t ->calculatePoints(t.getAmount()))
						));
		Double totalPoints = monthlyPoints.values().stream().mapToDouble(Double::doubleValue).sum();
		
        log.info("Calculated totalPoints={} for customerId={}", totalPoints, customerId);
        
		return new CustomerRewardResponseDTO(customerId, customer.getCustomerName(), monthlyPoints, totalPoints);
	}

	private void validateDate(Integer months, LocalDate startDate, LocalDate endDate) throws InvalidDateException {
		System.out.println(startDate);
		System.out.println(endDate);
		System.out.println(months);
		if (months != null && (startDate != null || endDate != null)) {
			throw new InvalidDateException("Provide either month or time range");
		}
		if ((startDate != null && endDate == null) || (startDate == null && endDate != null)) {
			throw new InvalidDateException("Both startdate and enddate must be provided");
		}
		if (startDate != null && endDate != null) {
			if (startDate.isAfter(endDate)) {
				throw new InvalidDateException("start date cannot be after the end date");
			}
			if (LocalDate.now().isBefore(endDate)) {
				throw new InvalidDateException("end date cannot be in future");
			}
		} 
	}

	private double calculatePoints(BigDecimal amount) {
		double amt = amount.doubleValue();
		double points = 0; 
		if (amt > 100) {
			points += (amt - 100)* 2;
			amt = 100;
		}
		if (amt > 50) {
			points += (amt - 50);
		} 
		return points;
	}
}
