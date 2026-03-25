package com.demo.reward.dto;

import java.util.Map;

public class CustomerRewardResponseDTO {
	 
	private Integer customerId;
	private String customerName;
	private Map<String, Double> monthlyRewardPintss;
	private Double totalRewardPoints;
	
	public CustomerRewardResponseDTO(Integer customerId, String customerName, Map<String, Double> monthlyRewardPintss,
			Double totalRewardPoints) {
		super();
		this.customerId = customerId;
		this.customerName = customerName;
		this.monthlyRewardPintss = monthlyRewardPintss;
		this.totalRewardPoints = totalRewardPoints;
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
	public Map<String, Double> getMonthlyRewardPoints() {
		return monthlyRewardPintss;
	}
	public void setMonthlyRewardPoints(Map<String, Double> monthlyRewardPintss) {
		this.monthlyRewardPintss = monthlyRewardPintss;
	}
	public Double getTotalRewardPoints() {
		return totalRewardPoints;
	}
	public void setTotalRewardPoints(Double totalRewardPoints) {
		this.totalRewardPoints = totalRewardPoints;
	}

}
