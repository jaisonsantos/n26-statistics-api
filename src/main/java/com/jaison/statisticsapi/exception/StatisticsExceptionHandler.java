package com.jaison.statisticsapi.exception;

import com.fasterxml.jackson.core.JsonParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class StatisticsExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatisticsExceptionHandler.class);

    @ResponseBody
    @ExceptionHandler(value = JsonParseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleException(JsonParseException exception) {
        LOGGER.error(exception.getMessage(), exception);
        return exception.getMessage();
    }

}
