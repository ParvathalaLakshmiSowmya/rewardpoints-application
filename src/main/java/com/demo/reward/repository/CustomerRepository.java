package com.demo.reward.repository;

import org.springframework.data.repository.CrudRepository;

import com.demo.reward.entity.Customer;

public interface CustomerRepository extends CrudRepository<Customer, Integer> {
	
}