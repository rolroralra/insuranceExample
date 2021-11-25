package com.example.demo.domain.reward;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class RewardInfo {
    private RewardType type;
    private Map<String, String> fileMap;
    private Long rewardAmount;
    private String description;

    private RewardInfo() {
        this(
                RewardType.getDefault(),
                new HashMap<>(),
                0L,
                ""
        );
    }

    public static class Builder {
        private final RewardInfo instance;

        public Builder() {
            this.instance = new RewardInfo();
        }

        public Builder type(RewardType type) {
            this.instance.setType(type);
            return this;
        }

        public Builder addFile(String key, String file) {
            this.instance.fileMap.put(key, file);
            return this;
        }

        public Builder rewardAmount(Long rewardAmount) {
            this.instance.setRewardAmount(rewardAmount);
            return this;
        }

        public Builder description(String description) {
            this.instance.setDescription(description);
            return this;
        }

        public RewardInfo build() {
            return instance;
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String toString() {
        return "RewardInfo{" +
                "type=" + type +
                ", fileMap=" + fileMap +
                ", rewardAmount=" + rewardAmount +
                ", description='" + description + '\'' +
                '}';
    }
}
