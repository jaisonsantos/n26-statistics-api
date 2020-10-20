package com.jaison.statisticsapi.dto;

import org.springframework.http.HttpStatus;

/**
 * Responsible for returning the code and message if there are any exceptions
 */
public class ResponseMessageDto {
    private final HttpStatus code;
    private final String message;

    public ResponseMessageDto(HttpStatus code, String message) {
        this.code = code;
        this.message = message;
    }

    public HttpStatus getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
