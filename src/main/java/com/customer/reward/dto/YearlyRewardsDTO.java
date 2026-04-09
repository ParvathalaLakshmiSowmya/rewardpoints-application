package com.customer.reward.dto;

import java.math.BigDecimal;
import java.util.List;

public class YearlyRewardsDTO {

    private int year;
    private List<MonthlyRewardsDTO> monthlyRewardsDetails;
    private BigDecimal totalAmountPerYear;
    private int totalPointsPerYear;

	public YearlyRewardsDTO(int year, List<MonthlyRewardsDTO> monthlyRewardsDetails, BigDecimal totalAmountPerYear,
			int totalPointsPerYear) {
		super();
		this.year = year;
		this.monthlyRewardsDetails = monthlyRewardsDetails;
		this.totalAmountPerYear = totalAmountPerYear;
		this.totalPointsPerYear = totalPointsPerYear;
	}
	
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public List<MonthlyRewardsDTO> getMonthlyRewardsDetails() {
		return monthlyRewardsDetails;
	}
	public void setMonthlyRewardsDetails(List<MonthlyRewardsDTO> monthlyRewardsDetails) {
		this.monthlyRewardsDetails = monthlyRewardsDetails;
	}
	public BigDecimal getTotalAmountPerYear() {
		return totalAmountPerYear;
	}
	public void setTotalAmountPerYear(BigDecimal totalAmountPerYear) {
		this.totalAmountPerYear = totalAmountPerYear;
	}
	public int getTotalPointsPerYear() {
		return totalPointsPerYear;
	}
	public void setTotalPointsPerYear(int totalPointsPerYear) {
		this.totalPointsPerYear = totalPointsPerYear;
	}
    
    
}
