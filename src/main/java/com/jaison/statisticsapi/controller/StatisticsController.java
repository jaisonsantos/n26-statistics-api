package com.jaison.statisticsapi.controller;

import com.jaison.statisticsapi.model.Statistics;
import com.jaison.statisticsapi.service.StatisticsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatisticsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatisticsController.class);

    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping("/statistics")
    public ResponseEntity<Statistics> getStatistics() {
        LOGGER.debug("Request for get statistics received");
        return new ResponseEntity<>(statisticsService.getStatistics(), HttpStatus.OK);
    }
}
