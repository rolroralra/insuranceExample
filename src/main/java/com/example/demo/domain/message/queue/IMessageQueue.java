package com.example.demo.domain.message.queue;

import com.example.demo.domain.message.queue.publish.IMessageQueuePublisher;
import com.example.demo.domain.message.queue.subscribe.IMessageQueueSubscriber;

public interface IMessageQueue extends IMessageQueuePublisher, IMessageQueueSubscriber {
    String joinToSubscribe(String domain);
    Boolean joinToPublish(String domain);
}
