package com.demo.reward.testservice;

import com.demo.reward.dto.CustomerRewardResponseDTO;
import com.demo.reward.entity.Customer;
import com.demo.reward.entity.Transaction;
import com.demo.reward.exception.InvalidDateException;
import com.demo.reward.exception.TransactionNotFoundException;
import com.demo.reward.repository.CustomerRepository;
import com.demo.reward.repository.TransactionRepository;
import com.demo.reward.service.RewardServiceImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RewardServiceImplTest {

    @InjectMocks
    private RewardServiceImpl rewardService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CustomerRepository customerRepository;


    @Test
    void testShouldCalculateRewardsSuccessfully_withDateRange() throws InvalidDateException, TransactionNotFoundException {

        Customer customer = new Customer(1, "John", "123", "Chennai");

        List<Transaction> transactions = List.of(
                new Transaction(1, BigDecimal.valueOf(120),
                        LocalDate.of(2026, 1, 10), customer)
        );

        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
        when(transactionRepository
                .findByCustomerCustomerIdAndTransactionDateBetween(any(), any(), any()))
                .thenReturn(transactions);

        CustomerRewardResponseDTO response =
                rewardService.getCustomerRewardPoints(
                        1, null,
                        LocalDate.of(2026,1,1),
                        LocalDate.of(2026,3,1));

        assertEquals(90, response.getTotalRewardPoints());
    }


    @Test
    void testShouldCalculateUsingMonths() throws InvalidDateException, TransactionNotFoundException {

        Customer customer = new Customer(1, "John", "123", "Chennai");

        List<Transaction> transactions = List.of(
                new Transaction(1, BigDecimal.valueOf(75),
                        LocalDate.now().minusDays(10), customer)
        );

        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
        when(transactionRepository
                .findByCustomerCustomerIdAndTransactionDateBetween(any(), any(), any()))
                .thenReturn(transactions);

        CustomerRewardResponseDTO response =
                rewardService.getCustomerRewardPoints(1, 2, null, null);

        assertEquals(25, response.getTotalRewardPoints());
    }


    @Test
    void testShouldUseDefaultLastThreeMonths() throws InvalidDateException, TransactionNotFoundException {

        Customer customer = new Customer(1, "John", "123", "Chennai");

        List<Transaction> transactions = List.of(
                new Transaction(1, BigDecimal.valueOf(60),
                        LocalDate.now().minusDays(5), customer)
        );

        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
        when(transactionRepository
                .findByCustomerCustomerIdAndTransactionDateBetween(any(), any(), any()))
                .thenReturn(transactions);

        CustomerRewardResponseDTO response =
                rewardService.getCustomerRewardPoints(1, null, null, null);

        assertEquals(10, response.getTotalRewardPoints());
    }

    // -------------------- CUSTOMER NOT FOUND --------------------

    @Test
    void testShouldThrowExceptionWhenCustomerNotFound() {

        when(customerRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(TransactionNotFoundException.class, () ->
                rewardService.getCustomerRewardPoints(
                        1, null,
                        LocalDate.of(2026,1,1),
                        LocalDate.of(2026,3,1)));
    }


    @Test
    void testShouldThrowExceptionWhenNoTransactions() {

        Customer customer = new Customer(1, "John", "123", "Chennai");

        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
        when(transactionRepository
                .findByCustomerCustomerIdAndTransactionDateBetween(any(), any(), any()))
                .thenReturn(List.of());

        assertThrows(TransactionNotFoundException.class, () ->
                rewardService.getCustomerRewardPoints(
                        1, null,
                        LocalDate.of(2026,1,1),
                        LocalDate.of(2026,3,1)));
    }


    @Test
    void testShouldThrowWhenMonthsAndDatesProvided() {

        assertThrows(InvalidDateException.class, () ->
                rewardService.getCustomerRewardPoints(
                        1, 2,
                        LocalDate.of(2026,1,1),
                        LocalDate.of(2026,3,1)));
    }

    @Test
    void testShouldThrowWhenOnlyOneDateProvided() {

        assertThrows(InvalidDateException.class, () ->
                rewardService.getCustomerRewardPoints(
                        1, null,
                        LocalDate.of(2026,1,1),
                        null));
    }

    @Test
    void testShouldThrowWhenStartAfterEnd() {

        assertThrows(InvalidDateException.class, () ->
                rewardService.getCustomerRewardPoints(
                        1, null,
                        LocalDate.of(2026,3,1),
                        LocalDate.of(2026,1,1)));
    }

    @Test
    void testShouldThrowWhenEndDateInFuture() {

        assertThrows(InvalidDateException.class, () ->
                rewardService.getCustomerRewardPoints(
                        1, null,
                        LocalDate.of(2026,1,1),
                        LocalDate.now().plusDays(5)));
    }


    @Test
    void testShouldReturnZeroPoints() throws InvalidDateException, TransactionNotFoundException {

        Customer customer = new Customer(1, "John", "123", "Chennai");

        List<Transaction> transactions = List.of(
                new Transaction(1, BigDecimal.valueOf(40),
                        LocalDate.of(2026, 1, 10), customer)
        );

        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
        when(transactionRepository
                .findByCustomerCustomerIdAndTransactionDateBetween(any(), any(), any()))
                .thenReturn(transactions);

        CustomerRewardResponseDTO response =
                rewardService.getCustomerRewardPoints(
                        1, null,
                        LocalDate.of(2026,1,1),
                        LocalDate.of(2026,3,1));

        assertEquals(0, response.getTotalRewardPoints());
    }

    @Test
    void testShouldHandleFractionalAmount() throws InvalidDateException, TransactionNotFoundException {

        Customer customer = new Customer(1, "John", "123", "Chennai");

        List<Transaction> transactions = List.of(
                new Transaction(1, new BigDecimal(120.0),
                        LocalDate.of(2026, 1, 10), customer)
        );

        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
        when(transactionRepository
                .findByCustomerCustomerIdAndTransactionDateBetween(any(), any(), any()))
                .thenReturn(transactions);

        CustomerRewardResponseDTO response =
                rewardService.getCustomerRewardPoints(
                        1, null,
                        LocalDate.of(2026,1,1),
                        LocalDate.of(2026,3,1));
        
        assertEquals(90, response.getTotalRewardPoints());
    }
}