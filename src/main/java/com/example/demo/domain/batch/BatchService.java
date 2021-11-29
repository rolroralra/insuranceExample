package com.example.demo.domain.batch;

import com.example.demo.domain.message.queue.IMessageQueue;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BatchService implements IBatchService {
    private final IMessageQueue subscriber;
    private String domain;
    private String key;

    @Override
    public void executeSchedule() {
        String message = subscriber.subscribe(domain, key);
        parseMessage(message);

        // TODO: implementation
    }

    private void parseMessage(String message) {

    }
}
