package com.jaison.statisticsapi.service;

import com.jaison.statisticsapi.model.Statistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatisticsServiceImpl.class);
    private final StatisticsManagementService statisticsManagementService;

    public StatisticsServiceImpl(StatisticsManagementService statisticsManagementService) {
        this.statisticsManagementService = statisticsManagementService;
    }

    @Override
    public Statistics getStatistics() {
        LOGGER.debug("Last minute statistics");
        return statisticsManagementService.getLastMinuteStatistic();

    }
}