package com.example.demo.domain.manager.reward;

import com.example.demo.domain.common.MockRepository;

public class RewardManagerRepository extends MockRepository<RewardManager> {
    @Override
    protected RewardManager newEntity() {
        RewardManager rewardManager = super.newEntity();
        rewardManager.setName(String.format("RewardManager%d", rewardManager.getId()));
        return rewardManager;
    }
}
