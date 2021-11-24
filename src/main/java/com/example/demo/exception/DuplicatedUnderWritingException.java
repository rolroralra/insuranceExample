package com.example.demo.exception;

public class DuplicatedUnderWritingException extends RuntimeException {

    public DuplicatedUnderWritingException(String format) {
        super(format);
    }
}
