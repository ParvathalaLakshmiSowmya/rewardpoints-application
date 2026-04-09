package com.customer.reward.dto;

import java.math.BigDecimal;

public class MonthlyRewardsDTO {

    private String month;
    private BigDecimal totalAmount;
    private int rewardPoints;
    
	public MonthlyRewardsDTO(String month, BigDecimal totalAmount, int rewardPoints) {
		super();
		this.month = month;
		this.totalAmount = totalAmount;
		this.rewardPoints = rewardPoints;
	}
	
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
	public int getRewardPoints() {
		return rewardPoints;
	}
	public void setRewardPoints(int rewardPoints) {
		this.rewardPoints = rewardPoints;
	}
    
}
