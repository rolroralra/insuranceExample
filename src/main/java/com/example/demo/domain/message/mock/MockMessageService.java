package com.example.demo.domain.message.mock;

import com.example.demo.domain.message.MessageService;

public class MockMessageService implements MessageService {
    private static final String BASE_FORMAT = "%s님, %s%n";

    @Override
    public void send(String receiver, String message) {
        System.out.printf(BASE_FORMAT, receiver, message);
    }

    @Override
    public void send(String receiver, String format, Object... args) {
        System.out.printf(BASE_FORMAT, receiver, String.format(format, args));
    }
}
