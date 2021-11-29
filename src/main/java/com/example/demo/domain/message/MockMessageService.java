package com.example.demo.domain.message;

import com.example.demo.domain.message.queue.IMessageQueue;
import com.example.demo.domain.message.queue.MockMessageQueue;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MockMessageService implements MessageService {
    private static final String BASE_FORMAT = "%s님, %s%n";
    private final IMessageQueue messageQueue;

    public MockMessageService() {
        this(new MockMessageQueue());
    }

    @Override
    public void send(String receiver, String message) {
        System.out.printf(BASE_FORMAT, receiver, message);
        messageQueue.publish("push",  String.format("%s,%s", receiver, message));
    }

    @Override
    public void send(String receiver, String format, Object... args) {
        System.out.printf(BASE_FORMAT, receiver, String.format(format, args));
        messageQueue.publish("push",  String.format("%s,%s", receiver, String.format(format, args)));
    }
}
