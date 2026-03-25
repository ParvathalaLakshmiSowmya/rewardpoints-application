package com.demo.reward.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.demo.reward.entity.Transaction;


@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Integer> {

	public List<Transaction> findByCustomerCustomerIdAndTransactionDateBetween(Integer customerId, LocalDate startDate, LocalDate endDate);
}