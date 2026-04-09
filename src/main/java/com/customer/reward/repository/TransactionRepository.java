package com.customer.reward.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.customer.reward.entity.Transaction;


@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Integer> {

	// Custom query to check if customer exists
    @Query("SELECT COUNT(c) > 0 FROM Customer c WHERE c.customerId = :customerId")
    boolean existsCustomerById(@Param("customerId") Integer customerId);
    
	public List<Transaction> findByCustomerCustomerIdAndTransactionDateBetween(Integer customerId, LocalDate startDate, LocalDate endDate);
}