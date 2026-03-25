package com.demo.reward.testcontroller;
import com.demo.reward.controller.RewardController;
import com.demo.reward.dto.CustomerRewardResponseDTO;
import com.demo.reward.exception.InvalidDateException;
import com.demo.reward.exception.TransactionNotFoundException;
import com.demo.reward.service.RewardService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RewardController.class)
class RewardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("removal")
	@MockBean
    private RewardService rewardService;

    @Test
    void testReturnRewardSuccessfully() throws Exception {
        // Arrange
        CustomerRewardResponseDTO response = new CustomerRewardResponseDTO(
                1,
                "John Doe",
                new HashMap<>() {{
                    put("MARCH", 120.0); 
                }},
                120.0
        );

        Mockito.when(rewardService.getCustomerRewardPoints(eq(1), eq(3), any(), any()))
                .thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/rewards/calculaterewardpoints/{customerId}", 1)
                        .param("months", "3")
                        .param("startDate", "2026-01-01")
                        .param("endDate", "2026-03-01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value(1))
                .andExpect(jsonPath("$.customerName").value("John Doe"))
                .andExpect(jsonPath("$.totalRewardPoints").value(120.0));
    }

    @Test
    void testShouldThrowTransactionNotFoundException() throws Exception {
        // Arrange
        Mockito.when(rewardService.getCustomerRewardPoints(eq(1), any(), any(), any()))
                .thenThrow(new TransactionNotFoundException("No Transactions Found"));

        // Act & Assert
        mockMvc.perform(get("/rewards/calculaterewardpoints/{customerId}", 1)
                        .param("months", "3"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testShouldThrowInvalidDateException() throws Exception {
        // Arrange
        Mockito.when(rewardService.getCustomerRewardPoints(eq(1), any(), any(), any()))
                .thenThrow(new InvalidDateException("Invalid date range"));

        // Act & Assert
        mockMvc.perform(get("/rewards/calculaterewardpoints/{customerId}", 1)
                        .param("startDate", "2026-04-01")
                        .param("endDate", "2026-01-01"))
                .andExpect(status().isBadRequest());
    }
}
