package com.jaison.statisticsapi.service;

import com.jaison.statisticsapi.model.Statistics;
import com.jaison.statisticsapi.model.Transaction;

import java.time.Instant;

public interface StatisticsManagementService {

    void addTransaction(Transaction transaction);

    Statistics getLastMinuteStatistic();

    void deleteStatistics();

    boolean isOlderThan(Instant instant);

    boolean isInTheFuture(Instant instant);
}
