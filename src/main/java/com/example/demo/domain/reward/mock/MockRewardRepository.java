package com.example.demo.domain.reward.mock;

import com.example.demo.domain.common.MockRepository;
import com.example.demo.domain.reward.Reward;
import com.example.demo.domain.reward.RewardRepository;

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
