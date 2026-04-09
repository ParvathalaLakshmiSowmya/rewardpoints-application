package com.customer.reward.testcontroller;

import com.customer.reward.controller.RewardController;
import com.customer.reward.dto.CustomerRewardResponseDTO;
import com.customer.reward.exception.InvalidDateException;
import com.customer.reward.exception.TransactionNotFoundException;
import com.customer.reward.service.RewardService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RewardController.class)
class RewardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RewardService rewardService;

    //SUCCESS CASE
    @Test
    void shouldReturnRewardSuccessfully() throws Exception {

        CustomerRewardResponseDTO response =
                new CustomerRewardResponseDTO(
                        1,
                        "John",
                        Collections.emptyList(),
                        null,
                        120
                );

        LocalDate start = LocalDate.parse("2026-01-01");
        LocalDate end = LocalDate.parse("2026-03-01");

        when(rewardService.getCustomerRewardPoints(
                1,
                null,
                start,
                end
        )).thenReturn(response);

        mockMvc.perform(get("/rewards/calculaterewardpoints/{customerId}", 1)
                        .param("startDate", "2026-01-01")
                        .param("endDate", "2026-03-01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value(1))
                .andExpect(jsonPath("$.customerName").value("John"))
                .andExpect(jsonPath("$.totalPoints").value(120));

        // verify exact parameters (NO any())
        verify(rewardService).getCustomerRewardPoints(
                1,
                null,
                start,
                end
        );
    }

    // MONTHS PARAM SUCCESS
    @Test
    void shouldReturnRewardsUsingMonths() throws Exception {

        CustomerRewardResponseDTO response =
                new CustomerRewardResponseDTO(
                        1,
                        "John",
                        Collections.emptyList(),
                        null,
                        50
                );

        when(rewardService.getCustomerRewardPoints(
                1,
                3,
                null,
                null
        )).thenReturn(response);

        mockMvc.perform(get("/rewards/calculaterewardpoints/{customerId}", 1)
                        .param("months", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPoints").value(50));

        verify(rewardService)
                .getCustomerRewardPoints(1, 3, null, null);
    }

    // INVALID CUSTOMER ID VALIDATION
    @Test
    void shouldFailWhenCustomerIdInvalid() throws Exception {

        mockMvc.perform(get("/rewards/calculaterewardpoints/{customerId}", -1))
                .andExpect(status().isBadRequest());
    }

    // INVALID MONTHS VALIDATION
    @Test
    void shouldFailWhenMonthsInvalid() throws Exception {

        mockMvc.perform(get("/rewards/calculaterewardpoints/{customerId}", 1)
                        .param("months", "0"))
                .andExpect(status().isBadRequest());
    }

    // TRANSACTION NOT FOUND
    @Test
    void shouldThrowTransactionNotFoundException() throws Exception {

        when(rewardService.getCustomerRewardPoints(
                1, 3, null, null))
                .thenThrow(new TransactionNotFoundException("No Transactions"));

        mockMvc.perform(get("/rewards/calculaterewardpoints/{customerId}", 1)
                        .param("months", "3"))
                .andExpect(status().isNotFound());
    }

    // INVALID DATE EXCEPTION FROM SERVICE
    @Test
    void shouldThrowInvalidDateException() throws Exception {

        when(rewardService.getCustomerRewardPoints(
                1,
                null,
                LocalDate.parse("2026-04-01"),
                LocalDate.parse("2026-01-01")
        )).thenThrow(new InvalidDateException("Invalid range"));

        mockMvc.perform(get("/rewards/calculaterewardpoints/{customerId}", 1)
                        .param("startDate", "2026-04-01")
                        .param("endDate", "2026-01-01"))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    void shouldFailWhenMonthsAndDateRangeProvidedTogether() throws Exception {

        LocalDate start = LocalDate.parse("2026-01-01");
        LocalDate end = LocalDate.parse("2026-03-01");

        when(rewardService.getCustomerRewardPoints(
                1,
                3,
                start,
                end
        )).thenThrow(new InvalidDateException(
                "Provide either month or time range"));

        mockMvc.perform(get("/rewards/calculaterewardpoints/{customerId}", 1)
                        .param("months", "3")
                        .param("startDate", "2026-01-01")
                        .param("endDate", "2026-03-01"))
                .andExpect(status().isBadRequest());

        verify(rewardService)
                .getCustomerRewardPoints(1, 3, start, end);
    }
}