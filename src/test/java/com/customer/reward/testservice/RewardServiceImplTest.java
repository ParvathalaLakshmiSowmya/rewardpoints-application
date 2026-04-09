package com.customer.reward.testservice;

import com.customer.reward.dto.CustomerRewardResponseDTO;
import com.customer.reward.entity.Customer;
import com.customer.reward.entity.Transaction;
import com.customer.reward.exception.InvalidDateException;
import com.customer.reward.exception.TransactionNotFoundException;
import com.customer.reward.repository.TransactionRepository;
import com.customer.reward.service.RewardServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RewardServiceImplTest {

    @InjectMocks
    private RewardServiceImpl rewardService;

    @Mock
    private TransactionRepository transactionRepository;

    private Customer customer;

    @BeforeEach
    void setup() {
        customer = new Customer(1, "John", "123", "Chennai");
    }

    // =====================================================
    // SUCCESS FLOW
    // =====================================================

    @Test
    void shouldCalculateRewardsUsingDateRange() throws Exception {

        List<Transaction> transactions = List.of(
                new Transaction(1,
                        new BigDecimal("120.00"),
                        LocalDate.of(2026,1,10),
                        customer)
        );

        when(transactionRepository.existsCustomerById(1)).thenReturn(true);
        when(transactionRepository
                .findByCustomerCustomerIdAndTransactionDateBetween(
                        eq(1),
                        eq(LocalDate.of(2026,1,1)),
                        eq(LocalDate.of(2026,3,1))))
                .thenReturn(transactions);

        CustomerRewardResponseDTO response =
                rewardService.getCustomerRewardPoints(
                        1,null,
                        LocalDate.of(2026,1,1),
                        LocalDate.of(2026,3,1));

        assertEquals(90, response.getTotalPoints());
    }

    // =====================================================
    // DEFAULT LAST 3 MONTHS
    // =====================================================

    @Test
    void shouldUseDefaultThreeMonths() throws Exception {

        List<Transaction> transactions = List.of(
                new Transaction(1,
                        new BigDecimal("60"),
                        LocalDate.now().minusDays(5),
                        customer)
        );

        when(transactionRepository.existsCustomerById(1)).thenReturn(true);
        when(transactionRepository
                .findByCustomerCustomerIdAndTransactionDateBetween(any(),any(),any()))
                .thenReturn(transactions);

        CustomerRewardResponseDTO response =
                rewardService.getCustomerRewardPoints(1,null,null,null);

        assertEquals(10, response.getTotalPoints());
    }

    // =====================================================
    // MONTHS FLOW
    // =====================================================

    @Test
    void shouldCalculateUsingMonths() throws Exception {

        List<Transaction> transactions = List.of(
                new Transaction(1,
                        new BigDecimal("75"),
                        LocalDate.now().minusDays(10),
                        customer)
        );

        when(transactionRepository.existsCustomerById(1)).thenReturn(true);
        when(transactionRepository
                .findByCustomerCustomerIdAndTransactionDateBetween(any(),any(),any()))
                .thenReturn(transactions);

        CustomerRewardResponseDTO response =
                rewardService.getCustomerRewardPoints(1,2,null,null);

        assertEquals(25, response.getTotalPoints());
    }

    // =====================================================
    // CUSTOMER VALIDATION
    // =====================================================

    @Test
    void shouldThrowWhenCustomerNotFound() {

        when(transactionRepository.existsCustomerById(1))
                .thenReturn(false);

        assertThrows(TransactionNotFoundException.class,
                () -> rewardService.getCustomerRewardPoints(
                        1,null,
                        LocalDate.now().minusMonths(1),
                        LocalDate.now()));
    }

    // =====================================================
    // NO TRANSACTIONS
    // =====================================================

    @Test
    void shouldThrowWhenNoTransactions() {

        when(transactionRepository.existsCustomerById(1)).thenReturn(true);
        when(transactionRepository
                .findByCustomerCustomerIdAndTransactionDateBetween(any(),any(),any()))
                .thenReturn(List.of());

        assertThrows(TransactionNotFoundException.class,
                () -> rewardService.getCustomerRewardPoints(
                        1,null,
                        LocalDate.now().minusMonths(1),
                        LocalDate.now()));
    }

    // =====================================================
    // DATE VALIDATION
    // =====================================================

    @Test
    void shouldThrowWhenMonthsAndDatesProvided() {

        assertThrows(InvalidDateException.class,
                () -> rewardService.getCustomerRewardPoints(
                        1,2,
                        LocalDate.now(),
                        LocalDate.now()));
    }

    @Test
    void shouldThrowWhenOnlyOneDateProvided() {

        assertThrows(InvalidDateException.class,
                () -> rewardService.getCustomerRewardPoints(
                        1,null,
                        LocalDate.now(),
                        null));
    }

    @Test
    void shouldThrowWhenStartAfterEnd() {

        assertThrows(InvalidDateException.class,
                () -> rewardService.getCustomerRewardPoints(
                        1,null,
                        LocalDate.now(),
                        LocalDate.now().minusDays(5)));
    }

    @Test
    void shouldThrowWhenEndDateInFuture() {

        assertThrows(InvalidDateException.class,
                () -> rewardService.getCustomerRewardPoints(
                        1,null,
                        LocalDate.now().minusDays(2),
                        LocalDate.now().plusDays(1)));
    }

    // =====================================================
    // HELPER
    // =====================================================

    private CustomerRewardResponseDTO execute(BigDecimal amount)
            throws Exception {

        List<Transaction> transactions = List.of(
                new Transaction(1,
                        amount,
                        LocalDate.now().minusDays(5),
                        customer)
        );

        when(transactionRepository.existsCustomerById(1)).thenReturn(true);
        when(transactionRepository
                .findByCustomerCustomerIdAndTransactionDateBetween(any(),any(),any()))
                .thenReturn(transactions);

        return rewardService.getCustomerRewardPoints(1,2,null,null);
    }

    // =====================================================
    // ⭐ REWARD BOUNDARY TESTS ⭐
    // =====================================================

    @Test
    void boundary_49_99_shouldReturnZero() throws Exception {
        assertEquals(0,
                execute(new BigDecimal("49.99")).getTotalPoints());
    }

    @Test
    void boundary_50_shouldReturnZero() throws Exception {
        assertEquals(0,
                execute(new BigDecimal("50.00")).getTotalPoints());
    }

    @Test
    void boundary_100_shouldReturn50() throws Exception {
        assertEquals(50,
                execute(new BigDecimal("100.00")).getTotalPoints());
    }

    @Test
    void boundary_100_01_shouldReturn50() throws Exception {
        assertEquals(50,
                execute(new BigDecimal("100.01")).getTotalPoints());
    }

    // =====================================================
    // FRACTIONAL TEST (Reviewer Fix)
    // =====================================================

    @Test
    void shouldHandleFractionalAmount() throws Exception {

        CustomerRewardResponseDTO response =
                execute(new BigDecimal("120.99"));

        assertEquals(91, response.getTotalPoints());
    }

    // =====================================================
    // NEGATIVE AMOUNT
    // =====================================================

    @Test
    void shouldReturnZeroForNegativeAmount() throws Exception {

        CustomerRewardResponseDTO response =
                execute(new BigDecimal("-10"));

        assertEquals(0, response.getTotalPoints());
    }
}