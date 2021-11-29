package com.example.demo.domain.message.queue.publish;

import com.example.demo.domain.message.queue.IMessageQueue;
import com.example.demo.domain.message.queue.MockMessageQueue;

public class MockMessageQueuePublisher implements IMessageQueuePublisher {
    IMessageQueue messageQueue;

    public MockMessageQueuePublisher() {
        messageQueue = new MockMessageQueue();
    }

    @Override
    public Boolean publish(String domain, String message) {
        return messageQueue.publish(domain, message);
    }

    @Override
    public Boolean publish(String domain, String key, String message) {
        return messageQueue.publish(domain, key, message);
    }
}
