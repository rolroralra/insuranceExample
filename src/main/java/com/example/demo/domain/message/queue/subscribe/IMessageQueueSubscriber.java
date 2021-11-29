package com.example.demo.domain.message.queue.subscribe;

public interface IMessageQueueSubscriber {
    String subscribe(String domain, String key);
}
