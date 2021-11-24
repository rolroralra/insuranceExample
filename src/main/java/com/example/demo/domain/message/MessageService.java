package com.example.demo.domain.message;

public interface MessageService {
    void send(String receiver, String message);
    void send(String receiver, String format, Object... args);
}
