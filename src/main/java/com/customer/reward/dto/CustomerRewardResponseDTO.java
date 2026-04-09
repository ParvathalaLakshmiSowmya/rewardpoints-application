package com.customer.reward.dto;

import java.math.BigDecimal;
import java.util.List;

public class CustomerRewardResponseDTO {
	 
	private Integer customerId;
	private String customerName;
	private List<YearlyRewardsDTO> yearlyRewardsDetails;
    private BigDecimal totalAmount;
    private int totalPoints;
    
	public CustomerRewardResponseDTO(Integer customerId, String customerName, List<YearlyRewardsDTO> yearlyRewardsDetails,
			BigDecimal totalAmount, int totalPoints) {
		super();
		this.customerId = customerId;
		this.customerName = customerName;
		this.yearlyRewardsDetails = yearlyRewardsDetails;
		this.totalAmount = totalAmount;
		this.totalPoints = totalPoints;
	}
	 
	public Integer getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public List<YearlyRewardsDTO> getYearlyRewardsDetails() {
		return yearlyRewardsDetails;
	}
	public void setYearlyRewardsDetails(List<YearlyRewardsDTO> yearlyRewardsDetails) {
		this.yearlyRewardsDetails = yearlyRewardsDetails;
	}
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
	public int getTotalPoints() {
		return totalPoints;
	}
	public void setTotalPoints(int totalPoints) {
		this.totalPoints = totalPoints;
	}
	
	
}
