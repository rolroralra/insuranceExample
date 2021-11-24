package com.example.demo.domain.subscription.uw;

import com.example.demo.domain.subscription.Subscription;
import com.example.demo.domain.subscription.SubscriptionRepository;
import com.example.demo.domain.subscription.mock.MockSubscriptionRepository;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

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
        Long subscriptionId = SUBSCRIPTION_REPOSITORY.findByPredicate(Subscription::isNotReady).stream().mapToLong(Subscription::getId).findAny().getAsLong();
        UnderWriting underWriting = underWritingService.requestUnderWriting(subscriptionId);

        assertThat(underWriting)
                .isNotNull()
                .hasFieldOrPropertyWithValue("state", UnderWriting.UnderWritingState.PROGRESS);

        assertThat(underWriting.getSubscription())
                .isNotNull()
                .hasFieldOrPropertyWithValue("state", Subscription.SubscriptionState.PROGRESS_UW);
    }

    @Order(2)
    @DisplayName("2. 보험가입 인가 담당자는 보험가입 인가 처리 결과 등록할 수 있다.")
    @Test
    void registerUnderWritingResult() {
        UnderWriting underWriting = SUBSCRIPTION_REPOSITORY.findByPredicate(Subscription::isInProgressUW).stream().map(Subscription::getUnderWriting).findAny().get();
        UnderWriting result = underWritingService.registerUnderWritingResult(underWriting);

        assertThat(result)
                .isNotNull()
                .hasFieldOrPropertyWithValue("state", UnderWriting.UnderWritingState.COMPLETED);

        assertThat(result.getSubscription())
                .isNotNull()
                .hasFieldOrPropertyWithValue("state", Subscription.SubscriptionState.COMPLETED_UW);
    }
}