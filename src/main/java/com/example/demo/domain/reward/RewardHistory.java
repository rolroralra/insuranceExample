package com.example.demo.domain.reward;

import com.example.demo.domain.common.CommonEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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

    public int totalRewardHistoryCount() {
        return getAllRewards().size();
    }

    @Override
    public String toString() {
        return "RewardHistory{" +
                "rewardMap=" + rewardMap +
                '}';
    }
}
