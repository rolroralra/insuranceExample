package com.example.demo.domain.reward;

import com.example.demo.domain.common.CommonEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
public class RewardHistory extends CommonEntity {
    private Map<Long, Reward> rewardMap;

    public RewardHistory() {
        rewardMap = new HashMap<>();
    }

    public Collection<Reward> getAllRewards() {
        return rewardMap.values();
    }

    public void addReward(Reward reward) {
        rewardMap.put(reward.getId(), reward);
    }

    public void deleteReward(Long rewardId) {
        rewardMap.remove(rewardId);
    }

    public int totalRewardHistoryCount() {
        return rewardMap.keySet().size();
    }
}
