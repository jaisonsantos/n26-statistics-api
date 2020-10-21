package com.jaison.statisticsapi.service;

import com.jaison.statisticsapi.model.Statistics;
import com.jaison.statisticsapi.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static com.jaison.statisticsapi.service.StatisticsManagementServiceImpl.CONDITION_SECONDS;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class StatisticsManagementServiceImplTest {

    private final StatisticsManagementServiceImpl statisticsManagementService = new StatisticsManagementServiceImpl();

    @BeforeEach
    public void setUp() {
        statisticsManagementService.deleteStatistics();
    }

    @Test
    void addTransaction() {
        //When 
        statisticsManagementService.getCollection().add(new Transaction(BigDecimal.ONE, OffsetDateTime.now()));

        //Then
        assertThat(statisticsManagementService.getCollection()).hasSize(1);
    }

    @Test
    void getLastMinuteStatistic() {
        //Given
        statisticsManagementService.getCollection().add(new Transaction(BigDecimal.ONE, OffsetDateTime.now()));
        statisticsManagementService.getCollection().add(new Transaction(BigDecimal.TEN, OffsetDateTime.now()));

        //When 
        Statistics lastMinuteStatistic = statisticsManagementService.getLastMinuteStatistic();

        //Then
        assertThat(lastMinuteStatistic).isNotNull();
        assertThat(lastMinuteStatistic.getAvg()).isEqualByComparingTo(new BigDecimal(5.5).setScale(2, BigDecimal.ROUND_UP));
        assertThat(lastMinuteStatistic.getMax()).isEqualByComparingTo(BigDecimal.TEN.setScale(2, BigDecimal.ROUND_UP));
        assertThat(lastMinuteStatistic.getMin()).isEqualByComparingTo(BigDecimal.ONE.setScale(2, BigDecimal.ROUND_UP));
        assertThat(lastMinuteStatistic.getSum()).isEqualByComparingTo(new BigDecimal(11.0).setScale(2, BigDecimal.ROUND_UP));
        assertThat(lastMinuteStatistic.getCount()).isEqualByComparingTo(2L);
    }

    @Test
    void deleteStatistics() {
        //Given
        statisticsManagementService.getCollection().add(new Transaction(BigDecimal.ONE, OffsetDateTime.now()));
        //When 
        statisticsManagementService.deleteStatistics();
        //Then
        assertThat(statisticsManagementService.getCollection()).isEmpty();
    }

    @Test
    void isOlderThan() throws InterruptedException {
        //Given
        OffsetDateTime offsetDateTime = OffsetDateTime.now(Clock.systemDefaultZone().getZone()).minusSeconds(CONDITION_SECONDS + 1);

        //When
        boolean isOlder = statisticsManagementService.isOlderThan(offsetDateTime);

        //Then
        assertThat(isOlder).isTrue();
    }

    @Test
    void isInTheFuture() {
        //Given
        OffsetDateTime futureOffsetDateTime = OffsetDateTime.of(9999, 12, 31, 12, 45, 10, 0, ZoneOffset.UTC);

        //When
        boolean isInTheFuture = statisticsManagementService.isInTheFuture(futureOffsetDateTime);

        //Then
        assertThat(isInTheFuture).isTrue();
    }

}