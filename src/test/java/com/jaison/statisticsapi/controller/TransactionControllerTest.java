package com.jaison.statisticsapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jaison.statisticsapi.model.Transaction;
import com.jaison.statisticsapi.service.StatisticsManagementService;
import com.jaison.statisticsapi.service.StatisticsManagementServiceImpl;
import com.jaison.statisticsapi.service.TransactionService;
import com.jaison.statisticsapi.service.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TransactionControllerTest {

    private static final String CREATE_TRANSACTIONS_URL = "/transactions";
    private final StatisticsManagementService statisticsManagementService = new StatisticsManagementServiceImpl();
    private final TransactionService statisticsService = new TransactionServiceImpl(statisticsManagementService);
    private MockMvc mvc;

    @BeforeEach
    public void setUp() {
        this.mvc = MockMvcBuilders
                .standaloneSetup(new TransactionController(statisticsService))
                .build();
    }

    @Test
    void returnStatusIsCreatedWhenCreateTransaction() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .post(CREATE_TRANSACTIONS_URL)
                .content(asJsonString(new Transaction(BigDecimal.valueOf(23.54), OffsetDateTime.now())))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void returnStatusBadRequestWhenInvalidTimeStamp() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .post(CREATE_TRANSACTIONS_URL)
                .content("{\"amount\":\"12.3\", \"timestamp\":20203210T012539000\"}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void returnUnprocessableEntityWhenCreateTransaction() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .post(CREATE_TRANSACTIONS_URL)
                .content(asJsonString(new Transaction(BigDecimal.valueOf(23.54), OffsetDateTime.now().plusDays(20))))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string("Transaction is in the future"));

    }

    @Test
    void returnBadRequestWhenCreateTransaction() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .post(CREATE_TRANSACTIONS_URL)
                .content(OffsetDateTime.now().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(""));
    }

    @Test
    void returnUnprocessableEntityWhenContentIsEmpty() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .post(CREATE_TRANSACTIONS_URL)
                .content("{}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string("Invalid json"));
    }

    @Test
    void returnNoContentWhenDeleteTransaction() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete(CREATE_TRANSACTIONS_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    private String asJsonString(final Object obj) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}