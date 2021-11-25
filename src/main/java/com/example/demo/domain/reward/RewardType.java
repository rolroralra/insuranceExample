package com.example.demo.domain.reward;

public enum RewardType {
    DEFAULT_TYPE;

    public static RewardType getDefault() {
        return DEFAULT_TYPE;
    }

    public static RewardType getDefaultOr(RewardType rewardType) {
        return rewardType == null ? getDefault() : rewardType;
    }
}
