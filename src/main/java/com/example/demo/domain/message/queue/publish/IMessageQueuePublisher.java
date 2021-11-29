package com.example.demo.domain.message.queue.publish;

public interface IMessageQueuePublisher {
        Boolean publish(String domain, String message);
        Boolean publish(String domain, String key, String message);
}
