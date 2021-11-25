package com.example.demo.domain.reward;

import com.example.demo.domain.common.MockRepository;

public class MockRewardRepository extends MockRepository<Reward> implements RewardRepository {
    private MockRewardRepository() {
        super(0);
    }

    private static class LazyHolder {
        private static final MockRewardRepository INSTANCE = new MockRewardRepository();
    }

    public static MockRewardRepository getInstance() {
        return LazyHolder.INSTANCE;
    }
}
