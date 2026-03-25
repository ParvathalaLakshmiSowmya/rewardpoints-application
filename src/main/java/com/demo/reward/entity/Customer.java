package com.demo.reward.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "customers")
public class Customer {
	 
	@Id
	@GeneratedValue(strategy =  GenerationType.IDENTITY)
	@Column(name="customer_id")
	private Integer customerId;

	private String customerName;

	private String phoneNumber;

	private String address;

	public Customer() {}
	public Customer(Integer customerId, String customerName, String phoneNumber, String address) {
		super();
		this.customerId = customerId; 
		this.customerName = customerName;
		this.phoneNumber = phoneNumber;
		this.address = address;
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

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
