package com.customer.reward.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TransactionDTO {
	 
	private Integer transactionId;
	private CustomerDTO customerDTO;
	private BigDecimal amount;
	private LocalDate transactionDate;
	
	public Integer getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(Integer transactionId) {
		this.transactionId = transactionId;
	}
	public CustomerDTO getCustomerDTO() {
		return customerDTO;
	}
	public void setCustomerDTOd(CustomerDTO customerDTO) {
		this.customerDTO = customerDTO;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public LocalDate getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(LocalDate transactionDate) {
		this.transactionDate = transactionDate;
	}
}
