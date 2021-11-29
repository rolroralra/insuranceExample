package com.example.demo.domain.message.queue.subscribe;

import com.example.demo.domain.message.queue.IMessageQueue;
import com.example.demo.domain.message.queue.MockMessageQueue;

public class MockMessageQueueSubscriber implements IMessageQueueSubscriber {
    private IMessageQueue messageQueue;

    public MockMessageQueueSubscriber() {
        messageQueue = new MockMessageQueue();
    }

    @Override
    public String subscribe(String domain, String key) {
        return messageQueue.subscribe(domain, key);
    }
}
