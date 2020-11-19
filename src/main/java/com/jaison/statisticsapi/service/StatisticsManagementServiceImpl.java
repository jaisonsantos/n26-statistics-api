package com.jaison.statisticsapi.service;

import com.jaison.statisticsapi.model.Statistics;
import com.jaison.statisticsapi.model.Transaction;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class StatisticsManagementServiceImpl implements StatisticsManagementService {

    protected static final int CONDITION_SECONDS = 60;
    private static final List<Transaction> TRANSACTION_LIST = Collections.synchronizedList(new ArrayList<>());

    @Override
    public void addTransaction(Transaction transaction) {
        synchronized (TRANSACTION_LIST) {
            TRANSACTION_LIST.add(transaction);
        }
    }

    /**
     * This method filter the last CONDITION_SECONDS seconds and
     * then calculates the statistics summary
     *
     * @return Statistics object with the relevant data
     */
    @Override
    public Statistics getLastMinuteStatistic() {
        return getStatisticsByTimeWindowInSecondsFromNow(CONDITION_SECONDS);
    }

    private Statistics getStatisticsByTimeWindowInSecondsFromNow(long timeWindow) {
        Statistics accumulated = new Statistics(
                BigDecimal.ZERO,
                BigDecimal.valueOf(Double.MIN_VALUE),
                BigDecimal.valueOf(Double.MAX_VALUE),
                BigDecimal.ZERO,
                0L);

        if (TRANSACTION_LIST.isEmpty()) {
            return scaleTheResult(new Statistics(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, 0L));
        }
        
        synchronized (TRANSACTION_LIST) {
            TRANSACTION_LIST.stream()
                    .filter(transaction -> transaction.getTimestamp().isAfter(OffsetDateTime.now().minusSeconds(timeWindow)))
                    .forEach(transaction -> doTheMaths(accumulated, transaction.getAmount()));
        }

        return scaleTheResult(accumulated);
    }

    private Statistics scaleTheResult(Statistics statistics) {
        Statistics scaled = new Statistics();

        scaled.setAvg(statistics.getAvg().setScale(2, BigDecimal.ROUND_UP));
        scaled.setMax(statistics.getMax().setScale(2, BigDecimal.ROUND_UP));
        scaled.setMin(statistics.getMin().setScale(2, BigDecimal.ROUND_UP));
        scaled.setSum(statistics.getSum().setScale(2, BigDecimal.ROUND_UP));
        scaled.setCount(statistics.getCount());
        return scaled;
    }

    /**
     * Adds to the accumulated value the current amount, calculates the new max, min,
     * and avg values and increases the counter of elements.
     *
     * @param accumulated Accumulated statistics object
     * @param amount      Current BigDecimal amount
     */
    private void doTheMaths(Statistics accumulated, BigDecimal amount) {

        accumulated.setSum(accumulated.getSum().add(amount));
        accumulated.setMax(accumulated.getMax().max(amount));
        accumulated.setMin(accumulated.getMin().min(amount));

        long count = accumulated.getCount() + 1;
        accumulated.setCount(count);

        accumulated.setAvg(calculateAverage(accumulated.getSum(), count));
    }

    @Override
    public void deleteStatistics() {
        TRANSACTION_LIST.clear();
    }

    @Override
    public boolean isOlderThan(OffsetDateTime offsetDateTime) {
        return System.currentTimeMillis() -
                offsetDateTime.toLocalDateTime().toInstant(OffsetDateTime.now().getOffset()).toEpochMilli() > CONDITION_SECONDS * 1000;
    }

    @Override
    public boolean isInTheFuture(OffsetDateTime offsetDateTime) {
        return System.currentTimeMillis()
                - offsetDateTime.toLocalDateTime().toInstant(OffsetDateTime.now().getOffset()).toEpochMilli() < 0;
    }

    private BigDecimal calculateAverage(BigDecimal amount, long count) {
        return amount.divide(BigDecimal.valueOf(count));
    }

    //Method used for testing purposes
    protected List<Transaction> getCollection() {
        return TRANSACTION_LIST;
    }
}
