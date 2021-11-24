package com.example.demo.domain.manager;

import com.example.demo.domain.manager.contract.ContractManager;
import com.example.demo.domain.manager.contract.ContractManagerRepository;
import com.example.demo.domain.manager.reward.RewardManager;
import com.example.demo.domain.manager.reward.RewardManagerRepository;
import com.example.demo.domain.manager.subscription.SubscriptionManager;
import com.example.demo.domain.manager.subscription.SubscriptionManagerRepository;
import com.example.demo.domain.manager.uw.UnderWritingManager;
import com.example.demo.domain.manager.uw.UnderWritingManagerRepository;

public class TaskManagerConnectionPool {
    private final SubscriptionManagerRepository subscriptionManagerRepository;
    private final UnderWritingManagerRepository underWritingManagerRepository;
    private final RewardManagerRepository rewardManagerRepository;
    private final ContractManagerRepository contractManagerRepository;

    public TaskManagerConnectionPool() {
        subscriptionManagerRepository = new SubscriptionManagerRepository();
        underWritingManagerRepository = new UnderWritingManagerRepository();
        rewardManagerRepository = new RewardManagerRepository();
        contractManagerRepository = new ContractManagerRepository();
    }

    public SubscriptionManager allocateSubscriptionManager() {
        return subscriptionManagerRepository.getAny();
    }

    public UnderWritingManager allocateUnderWritingManager() {
        return underWritingManagerRepository.getAny();
    }

    public RewardManager allocateRewardManager() {
        return rewardManagerRepository.getAny();
    }

    public ContractManager allocateContractManager() {
        return contractManagerRepository.getAny();
    }

}
