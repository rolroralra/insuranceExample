package com.example.demo.exception;

public class InvalidRequestException extends RuntimeException {
    public InvalidRequestException(String format) {
        super(format);
    }
}
