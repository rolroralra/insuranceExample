package com.example.demo.domain.manager;

import com.example.demo.domain.manager.contract.ContractManager;
import com.example.demo.domain.manager.reward.RewardManager;
import com.example.demo.domain.manager.subscription.SubscriptionManager;
import com.example.demo.domain.manager.uw.UnderWritingManager;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TaskManagerConnectionPoolTest {
    private TaskManagerConnectionPool taskManagerConnectionPool;

    @BeforeEach
    void setUp() {
        taskManagerConnectionPool = new TaskManagerConnectionPool();
    }

    @Order(1)
    @DisplayName("1. 보험가입 담당자 할당할 수 있다.")
    @RepeatedTest(10)
    public void test_allocate_subscription_manager() {
        SubscriptionManager subscriptionManager = taskManagerConnectionPool.allocateSubscriptionManager();

        assertThat(subscriptionManager)
                .isNotNull()
                .hasNoNullFieldsOrProperties().hasFieldOrProperty("id");

        System.out.println(subscriptionManager);
    }

    @Order(2)
    @DisplayName("2. 보험가입 인가 담당자 할당할 수 있다.")
    @RepeatedTest(10)
    public void test_allocate_under_writing_manager() {
        UnderWritingManager underWritingManager = taskManagerConnectionPool.allocateUnderWritingManager();

        assertThat(underWritingManager)
                .isNotNull()
                .hasNoNullFieldsOrProperties().hasFieldOrProperty("id");

        System.out.println(underWritingManager);
    }

    @Order(3)
    @DisplayName("3. 보험계약 담당자 할당할 수 있다.")
    @RepeatedTest(10)
    public void test_allocate_contract_manager() {
        ContractManager contractManager = taskManagerConnectionPool.allocateContractManager();

        assertThat(contractManager)
                .isNotNull()
                .hasNoNullFieldsOrProperties().hasFieldOrProperty("id");

        System.out.println(contractManager);
    }

    @Order(4)
    @DisplayName("4. 보상청구 담당자 할당할 수 있다.")
    @RepeatedTest(10)
    public void test_allocate_reward_manager() {
        RewardManager rewardManager = taskManagerConnectionPool.allocateRewardManager();

        assertThat(rewardManager)
                .isNotNull()
                .hasNoNullFieldsOrProperties().hasFieldOrProperty("id");

        System.out.println(rewardManager);
    }
}