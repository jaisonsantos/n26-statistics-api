package com.jaison.statisticsapi.service;

import com.jaison.statisticsapi.model.Statistics;
import com.jaison.statisticsapi.model.Transaction;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class StatisticsManagementServiceImpl implements StatisticsManagementService {

    private static final int CONDITION_SECONDS = 60;
    private static Map<Instant, Transaction> allTransactions = new ConcurrentHashMap<>();

    @Override
    public void addTransaction(Transaction transaction) {
        allTransactions.put(Instant.now(), transaction);
    }

    @Override
    public Statistics getLastMinuteStatistic() {
        Statistics accumulated = new Statistics();
        allTransactions.entrySet().stream()
                //the stored instant  is greater or equals to now - 60
                .filter(entry -> entry.getKey().isAfter(Instant.now().minusSeconds(CONDITION_SECONDS)))
                .map(Map.Entry::getValue)
                .forEach(transaction -> doTheMaths(accumulated, transaction));
        return accumulated;
    }

    private void doTheMaths(Statistics accumulated, Transaction transaction) {
        accumulated.setSum(setScale(accumulated.getSum().add(transaction.getAmount())));
        accumulated.setMax(setScale(accumulated.getMax().max(transaction.getAmount())));
        accumulated.setMin(setScale(accumulated.getMin().min(transaction.getAmount())));

        long count = accumulated.getCount() + 1;
        accumulated.setCount(count++);
        accumulated.setAvg(setScale(accumulated.getSum().divide(BigDecimal.valueOf(count), RoundingMode.HALF_UP)));
    }

    @Override
    public void deleteStatistics() {
        allTransactions.clear();
    }

    @Override
    public boolean isOlderThan(OffsetDateTime offsetDateTime) {
        return offsetDateTime.isBefore(OffsetDateTime.now().minusSeconds(CONDITION_SECONDS));
    }

    public BigDecimal setScale(BigDecimal amount) {
        return amount.setScale(2, RoundingMode.CEILING);
    }
}
