package com.jaison.statisticsapi.controller;

import com.jaison.statisticsapi.model.Statistics;
import com.jaison.statisticsapi.service.StatisticsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class StatisticsControllerTest {

    private static final String GET_STATISTICS = "/statistics";
    private final StatisticsService statisticsService = mock(StatisticsService.class);
    private MockMvc mvc;

    @BeforeEach
    public void setUp() {
        this.mvc = MockMvcBuilders
                .standaloneSetup(new StatisticsController(statisticsService))
                .build();
    }

    @Test
    void shouldReturnOkWhenGetStatistics() throws Exception {
        Statistics mockedResponse = generateMockedResponse();
        when(statisticsService.getStatistics()).thenReturn(mockedResponse);
        mvc.perform(MockMvcRequestBuilders
                .get(GET_STATISTICS)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.sum").value(1.00))
                .andExpect(MockMvcResultMatchers.jsonPath("$.max").value(1.00))
                .andExpect(MockMvcResultMatchers.jsonPath("$.min").value(1.00))
                .andExpect(MockMvcResultMatchers.jsonPath("$.avg").value(1.00))
                .andExpect(MockMvcResultMatchers.jsonPath("$.count").value(1.00));
    }

    private Statistics generateMockedResponse() {
        Statistics statistics = new Statistics();
        statistics.setSum(BigDecimal.valueOf(1.00).setScale(2, BigDecimal.ROUND_UP));
        statistics.setMax(BigDecimal.valueOf(1.00).setScale(2, BigDecimal.ROUND_UP));
        statistics.setMin(BigDecimal.valueOf(1.00).setScale(2, BigDecimal.ROUND_UP));
        statistics.setAvg(BigDecimal.valueOf(1.00).setScale(2, BigDecimal.ROUND_UP));
        statistics.setCount(1L);
        return statistics;
    }

}