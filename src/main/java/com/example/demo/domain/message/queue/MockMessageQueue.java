package com.example.demo.domain.message.queue;

import lombok.AllArgsConstructor;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

@AllArgsConstructor
public class MockMessageQueue implements IMessageQueue {
    private Map<String, Map<String, Queue<String>>> messageQueueMap;

    public MockMessageQueue() {
        this(new HashMap<>());
    }

    @Override
    public String joinToSubscribe(String domain) {
        if (!messageQueueMap.containsKey(domain)) {
            return null;
        }

       return _subscribe(domain);
    }

    @Override
    public Boolean joinToPublish(String domain) {
        if (messageQueueMap.containsKey(domain)) {
            return false;
        }

        messageQueueMap.put(domain, new HashMap<>());
        return true;
    }

    @Override
    public Boolean publish(String domain, String message) {
        if (!messageQueueMap.containsKey(domain)) {
            return false;
        }

        messageQueueMap.get(domain).values().forEach(q -> q.add(message));
        return true;
    }

    @Override
    public Boolean publish(String domain, String key, String message) {
        if (!_checkMessageQueue(domain, key)) {
            return false;
        }

        messageQueueMap.get(domain).get(key).add(message);
        return true;
    }

    @Override
    public String subscribe(String domain, String key) {
        if (!_checkMessageQueue(domain, key)) {
            return null;
        }

        return messageQueueMap.get(domain).get(key).poll();
    }

    private Boolean _checkMessageQueue(String domain, String key) {
        return messageQueueMap.containsKey(domain) && messageQueueMap.get(domain).containsKey(key);
    }

    private String _subscribe(String domain) {
        String generatedKey = _generateKey(domain);
        messageQueueMap.get(domain).put(generatedKey, new LinkedList<>());
        return generatedKey;
    }

    private String _generateKey(String domain) {
        return String.format("%d", messageQueueMap.get(domain).size() + 1);
    }
}
