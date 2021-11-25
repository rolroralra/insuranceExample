package com.example.demo.domain.reward;

import java.util.List;

public interface IRewardService {
    Reward requestReward(Long contractId, RewardInfo rewardInfo);
    Reward registerRewardResult(Long rewardId, RewardResult rewardResult);

    List<Reward> findAllRewards();
    List<Reward> findRewardsByManagerId(Long managerId);
    List<Reward> findRewardsByUserId(Long userId);
}
