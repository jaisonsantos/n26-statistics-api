package com.jaison.statisticsapi.service;

import com.jaison.statisticsapi.dto.ResponseMessageDto;
import com.jaison.statisticsapi.model.Transaction;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TransactionServiceImplTest {

    private final StatisticsManagementService statisticsManagementService = mock(StatisticsManagementService.class);
    private final TransactionService transactionService = new TransactionServiceImpl(statisticsManagementService);

    @Test
    void addTransaction_youngerThan60Seconds() {
        //Given
        Transaction transaction = new Transaction(BigDecimal.valueOf(12.345), OffsetDateTime.now());
        doNothing().when(statisticsManagementService).addTransaction(any());
        when(statisticsManagementService.isOlderThan(any())).thenReturn(false);

        //When
        ResponseMessageDto responseMessage = transactionService.addTransaction(transaction);

        //Then
        verify(statisticsManagementService, times(1)).addTransaction(any());
        assertThat(responseMessage.getCode()).isEqualByComparingTo(HttpStatus.CREATED);
        assertThat(responseMessage.getMessage()).isNull();
    }

    @Test
    void addTransaction_olderThan60Seconds() {
        //Given
        Transaction transaction = new Transaction(BigDecimal.valueOf(12.345), OffsetDateTime.parse("2020-07-17T09:59:51.312Z"));
        doNothing().when(statisticsManagementService).addTransaction(any());
        when(statisticsManagementService.isOlderThan(any())).thenReturn(true);

        //When
        ResponseMessageDto responseMessage = transactionService.addTransaction(transaction);

        //Then
        verify(statisticsManagementService, times(0)).addTransaction(any());
        assertThat(responseMessage.getCode()).isEqualByComparingTo(HttpStatus.NO_CONTENT);
        assertThat(responseMessage.getMessage()).isNull();
    }

    @Test
    void addTransaction_validateException() {
        //Given
        Transaction transaction = new Transaction(null, OffsetDateTime.parse("2020-07-17T09:59:51.312Z"));

        //When
        ResponseMessageDto responseMessage = transactionService.addTransaction(transaction);

        //Then
        verify(statisticsManagementService, times(0)).addTransaction(any());
        assertThat(responseMessage.getCode()).isEqualByComparingTo(HttpStatus.UNPROCESSABLE_ENTITY);
        assertThat(responseMessage.getMessage()).contains("Invalid json");
    }

    @Test
    void whenEmptyRequestBody_validateException() {
        //Given
        Transaction transaction = new Transaction(null, null);

        //When
        ResponseMessageDto responseMessage = transactionService.addTransaction(transaction);

        //Then
        verify(statisticsManagementService, times(0)).addTransaction(any());
        assertThat(responseMessage.getCode()).isEqualByComparingTo(HttpStatus.UNPROCESSABLE_ENTITY);
        assertThat(responseMessage.getMessage()).isEqualTo("Invalid json");

    }

    @Test
    void whenMissingTimestamp_exceptionThrown() {
        //Given
        Transaction transaction = new Transaction(BigDecimal.valueOf(1164.5455), null);

        //When
        ResponseMessageDto responseMessage = transactionService.addTransaction(transaction);

        //Then
        verify(statisticsManagementService, times(0)).addTransaction(any());
        assertThat(responseMessage.getCode()).isEqualByComparingTo(HttpStatus.UNPROCESSABLE_ENTITY);
        assertThat(responseMessage.getMessage()).contains("Invalid json");
    }

    @Test
    void whenMissingAmount_validateException() {
        //Given
        Transaction transaction = new Transaction(null, OffsetDateTime.now());

        //When
        ResponseMessageDto responseMessage = transactionService.addTransaction(transaction);

        //Then
        verify(statisticsManagementService, times(0)).addTransaction(any());
        assertThat(responseMessage.getCode()).isEqualByComparingTo(HttpStatus.UNPROCESSABLE_ENTITY);
        assertThat(responseMessage.getMessage()).contains("Invalid json");
    }
}