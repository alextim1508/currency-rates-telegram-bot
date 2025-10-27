package com.alextim.currencyrates.cbr.exception;

public class CbrRequestException extends RuntimeException {
    public CbrRequestException(String message) {
        super(message);
    }

    public CbrRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}