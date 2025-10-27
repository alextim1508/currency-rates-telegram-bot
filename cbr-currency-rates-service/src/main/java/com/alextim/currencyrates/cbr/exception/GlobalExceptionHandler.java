package com.alextim.currencyrates.cbr.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CbrRequestException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ErrorResponse handleCbrRequestException(CbrRequestException ex) {
        return new ErrorResponse("CBR service unavailable: " + ex.getMessage());
    }

    @Data
    @AllArgsConstructor
    public static class ErrorResponse {
        private String message;
    }
}
