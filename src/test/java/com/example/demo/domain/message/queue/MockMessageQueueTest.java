package com.example.demo.domain.message.queue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

class MockMessageQueueTest {
    private IMessageQueue messageQueue;

    @BeforeEach
    void setUp() {
        messageQueue = new MockMessageQueue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"batch", "product", "user", "message", "subscription", "uw", "reward", "contract"})
    void test_join_to_subscribe(String domain) {
        assertThat(messageQueue.joinToPublish(domain)).isTrue();

        String key = messageQueue.joinToSubscribe(domain);

        assertThat(key).isNotNull().isNotBlank();
    }

    @ParameterizedTest
    @ValueSource(strings = {"batch", "product", "user", "message", "subscription", "uw", "reward", "contract"})
    void test_join_to_publish(String domain) {
        assertThat(messageQueue.joinToPublish(domain)).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"batch", "product", "user", "message", "subscription", "uw", "reward", "contract"})
    void test_publish_to_all(String domain) {
        assertThat(messageQueue.joinToPublish(domain)).isTrue();

        int totalSubscriberCount = 5;
        List<String> keyList = IntStream.range(0, totalSubscriberCount).mapToObj(i -> messageQueue.joinToSubscribe(domain)).collect(Collectors.toList());

        assertThat(keyList).isNotNull().isNotEmpty().hasSize(totalSubscriberCount);

        String message = String.format("%s test message", domain);
        assertThat(messageQueue.publish(domain, message)).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"batch", "product", "user", "message", "subscription", "uw", "reward", "contract"})
    void test_publish(String domain) {
        assertThat(messageQueue.joinToPublish(domain)).isTrue();

        int totalSubscriberCount = 5;
        List<String> keyList = IntStream.range(0, totalSubscriberCount).mapToObj(i -> messageQueue.joinToSubscribe(domain)).collect(Collectors.toList());

        assertThat(keyList).isNotNull().isNotEmpty().hasSize(totalSubscriberCount);

        String message = String.format("%s test message", domain);
        String targetKey = String.format("%d", new Random().nextInt(totalSubscriberCount) + 1);
        assertThat(messageQueue.publish(domain, targetKey, message)).isTrue();

        IntStream.range(0, totalSubscriberCount).mapToObj(String::valueOf).filter(key -> !key.equals(targetKey))
                .forEach(key -> assertThat(messageQueue.subscribe(domain, key)).isNull());

        assertThat(messageQueue.subscribe(domain, targetKey)).isSameAs(message);
    }

    @ParameterizedTest
    @ValueSource(strings = {"batch", "product", "user", "message", "subscription", "uw", "reward", "contract"})
    void test_subscribe(String domain) {
        assertThat(messageQueue.joinToPublish(domain)).isTrue();

        int totalSubscriberCount = 5;
        List<String> keyList = IntStream.range(0, totalSubscriberCount).mapToObj(i -> messageQueue.joinToSubscribe(domain)).collect(Collectors.toList());

        assertThat(keyList).isNotNull().isNotEmpty().hasSize(totalSubscriberCount);

        String message = String.format("%s test message", domain);
        assertThat(messageQueue.publish(domain, message)).isTrue();

        keyList.forEach(key -> {
            assertThat(messageQueue.subscribe(domain, key)).isSameAs(message);
        });
    }
}