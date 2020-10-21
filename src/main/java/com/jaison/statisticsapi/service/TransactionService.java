package com.jaison.statisticsapi.service;

import com.jaison.statisticsapi.dto.ResponseMessageDto;
import com.jaison.statisticsapi.model.Transaction;

public interface TransactionService {

    ResponseMessageDto addTransaction(Transaction transaction);

    void deleteAllTransactions();
}
