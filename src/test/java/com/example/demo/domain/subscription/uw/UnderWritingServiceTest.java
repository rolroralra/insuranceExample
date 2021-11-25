package com.example.demo.domain.subscription.uw;

import com.example.demo.domain.subscription.Subscription;
import com.example.demo.domain.subscription.SubscriptionRepository;
import com.example.demo.domain.subscription.mock.MockSubscriptionRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UnderWritingServiceTest {
    private UnderWritingService underWritingService;
    private static final SubscriptionRepository SUBSCRIPTION_REPOSITORY = MockSubscriptionRepository.getInstance();

    @BeforeEach
    void setUp() {
        underWritingService = new UnderWritingService();
    }

    @Order(1)
    @DisplayName("1. 보험가입 인가 요청을 처리할 수 있다.")
    @Test
    void test_request_under_writing() {
        Long subscriptionId = SUBSCRIPTION_REPOSITORY.findByPredicate(Subscription::isProgress).stream().mapToLong(Subscription::getId).findAny().getAsLong();
        UnderWriting underWriting = underWritingService.requestUnderWriting(subscriptionId);

        assertThat(underWriting)
                .isNotNull()
                .hasFieldOrPropertyWithValue("state", UnderWriting.State.PROGRESS);

        assertThat(underWriting.getSubscription())
                .isNotNull()
                .hasFieldOrPropertyWithValue("state", Subscription.State.PROGRESS_UW);
    }

    @Order(2)
    @DisplayName("2. 보험가입 인가 담당자는 보험가입 인가 처리 결과 등록할 수 있다.")
    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void registerUnderWritingResult(Boolean underWritingResult) {
        test_request_under_writing();
        System.out.println();

        UnderWriting underWriting = SUBSCRIPTION_REPOSITORY.findByPredicate(Subscription::isProgressUW).stream().map(Subscription::getUnderWriting).findAny().get();
        UnderWriting result = underWritingService.registerUnderWritingResult(underWriting.getId(), underWritingResult);
        System.out.println();

        assertThat(result)
                .isNotNull()
                .hasFieldOrPropertyWithValue("state", UnderWriting.State.COMPLETED)
                .hasFieldOrPropertyWithValue("result", underWritingResult);

        assertThat(result.getSubscription())
                .isNotNull()
                .hasFieldOrPropertyWithValue("state", Subscription.State.COMPLETED_UW);
    }
}