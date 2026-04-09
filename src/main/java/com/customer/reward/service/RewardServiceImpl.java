package com.customer.reward.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.customer.reward.dto.CustomerRewardResponseDTO;
import com.customer.reward.dto.MonthlyRewardsDTO;
import com.customer.reward.dto.YearlyRewardsDTO;
import com.customer.reward.entity.Customer;
import com.customer.reward.entity.Transaction;
import com.customer.reward.exception.InvalidDateException;
import com.customer.reward.exception.TransactionNotFoundException;
import com.customer.reward.repository.TransactionRepository;

@Service
public class RewardServiceImpl implements RewardService {
	
	private TransactionRepository transactionRepository;
	
    private static final Logger log = LoggerFactory.getLogger(RewardServiceImpl.class);
    
    private static final BigDecimal FIFTY = BigDecimal.valueOf(50);
    private static final BigDecimal HUNDRED = BigDecimal.valueOf(100);
    
	 
	public RewardServiceImpl(TransactionRepository transactionRepository) {
		this.transactionRepository = transactionRepository;
	}
	
	@Override
	public CustomerRewardResponseDTO getCustomerRewardPoints(Integer customerId, Integer months, LocalDate startDate,
		LocalDate endDate) throws InvalidDateException, TransactionNotFoundException {
		
		log.info("Fetching reward points for customerId={}, months={}, startDate={}, endDate={}", customerId, months, startDate, endDate);
		
		validateDate(months, startDate, endDate);
		
		boolean customerExists = transactionRepository.existsCustomerById(customerId);
	    if (!customerExists) {
	        log.error("Customer with ID={} not found in customer table", customerId);
	        throw new TransactionNotFoundException("Service.CUSTOMER_NOT_FOUND");
	    }
		
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
			throw new TransactionNotFoundException("Service.TRANSACTION_NOT_FOUND");
		} 
		Customer customer = transactions.get(0).getCustomer();
		Map<Integer, Map<Integer, List<Transaction>>> grouped = 
	            transactions.stream()
	                    .collect(Collectors.groupingBy(
	                            t -> t.getTransactionDate().getYear(),
	                            Collectors.groupingBy(
	                                    t -> t.getTransactionDate().getMonthValue()
	                            )));

	    List<YearlyRewardsDTO> yearlySummary =
	            grouped.entrySet().stream()
	                    .sorted(Map.Entry.comparingByKey())
	                    .map(yearEntry -> {

	                        int year = yearEntry.getKey();

	                        List<MonthlyRewardsDTO> monthlyBreakdown =
	                                yearEntry.getValue().entrySet().stream()
	                                        .sorted(Map.Entry.comparingByKey())
	                                        .map(monthEntry -> {

	                                            String monthName =
	                                                    Month.of(monthEntry.getKey()).name();

	                                            BigDecimal amount =
	                                                    monthEntry.getValue().stream()
	                                                            .map(Transaction::getAmount)
	                                                            .reduce(BigDecimal.ZERO, BigDecimal::add);

	                                            int points =
	                                                    monthEntry.getValue().stream()
	                                                            .mapToInt(t -> calculatePoints(t.getAmount()))
	                                                            .sum();

	                                            log.debug("Month {}: totalAmount={}, points={}", monthName, amount, points);
	                                            return new MonthlyRewardsDTO(
	                                                    monthName,
	                                                    amount,
	                                                    points);
	                                        })
	                                        .toList();

	                        BigDecimal yearlyAmount =
	                                monthlyBreakdown.stream()
	                                        .map(MonthlyRewardsDTO::getTotalAmount)
	                                        .reduce(BigDecimal.ZERO, BigDecimal::add);

	                        int yearlyPoints =
	                                monthlyBreakdown.stream()
	                                        .mapToInt(MonthlyRewardsDTO::getRewardPoints)
	                                        .sum();

	                        log.info("Year {}: yearlyAmount={}, yearlyPoints={}", year, yearlyAmount, yearlyPoints);
	                        return new YearlyRewardsDTO(
	                                year,
	                                monthlyBreakdown,
	                                yearlyAmount,
	                                yearlyPoints);
	                    })
	                    .toList();

	    BigDecimal totalAmount =
	            yearlySummary.stream()
	                    .map(YearlyRewardsDTO::getTotalAmountPerYear)
	                    .reduce(BigDecimal.ZERO, BigDecimal::add);

	    int totalPoints =
	            yearlySummary.stream()
	                    .mapToInt(YearlyRewardsDTO::getTotalPointsPerYear)
	                    .sum();

	    log.info("Final reward summary for customerId={}: totalAmount={}, totalPoints={}", customerId, totalAmount, totalPoints);
	    return new CustomerRewardResponseDTO(
	            customerId,
	            customer.getCustomerName(),
	            yearlySummary,
	            totalAmount,
	            totalPoints
	    );
        
	}

	private void validateDate(Integer months, LocalDate startDate, LocalDate endDate) throws InvalidDateException {
		
		if (months != null && (startDate != null || endDate != null)) {
			log.error("Invalid input: months={} with startDate={} and endDate={}", months, startDate, endDate);
			throw new InvalidDateException("Service.INVALID_DATE_MONTH");
		}
		if ((startDate != null && endDate == null) || (startDate == null && endDate != null)) {
			log.error("Invalid input: only one of startDate or endDate provided");
			throw new InvalidDateException("Service.DATES_REQUIRED");
		}
		if (startDate != null && endDate != null) {
			if (startDate.isAfter(endDate)) {
				log.error("Invalid input: startDate {} is after endDate {}", startDate, endDate);
				throw new InvalidDateException("Service.INVALID_DATES");
			}
			if (LocalDate.now().isBefore(endDate)) {
				log.error("Invalid input: endDate {} is in the future", endDate);
				throw new InvalidDateException("Service.INVALID_ENDDATE");
			}
		} 
	}

	private int calculatePoints(BigDecimal amount) {
		
        int points = 0;
		
		if (amount.compareTo(HUNDRED) > 0) {
            points += amount.subtract(HUNDRED)
                    .multiply(BigDecimal.valueOf(2))
                    .intValue();
            amount = HUNDRED;
        }

        if (amount.compareTo(FIFTY) > 0) {
            points += amount.subtract(FIFTY).intValue();
        }

        log.debug("Calculated points={} for amount={}", points, amount);
        return points;
	}
}
