package com.example.demo.domain.message.mock;

import com.example.demo.domain.message.MessageService;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MockMessageServiceTest {
    private MessageService messageService;

    @BeforeEach
    void setUp() {
        messageService = new MockMessageService();
    }

    @Order(1)
    @DisplayName("1. FCM/GCM을 통한 SMS 푸시 메시지 전송이 가능하다.")
    @Test
    void test_send() {
        messageService.send("수신자", "테스트 메시지 입니다.");
        Assertions.assertTrue(true);
    }

    @Order(2)
    @DisplayName("2. FCM/GCM을 통한 SMS 푸시 메시지 전송이 가능하다.")
    @Test
    void test_send2() {
        messageService.send("수신자", "테스트 메시지 입니다. [%s]", this.getClass());
        Assertions.assertTrue(true);
    }
}