package com.example.demo.domain.reward;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public class RewardInfo {
    private RewardType type;
    private Map<String, String> fileMap;
    private String description;

    private RewardInfo() {
        this(
                RewardType.getDefault(),
                new HashMap<>(),
                ""
        );
    }

    public static class Builder {
        private final RewardInfo instance;

        public Builder() {
            this.instance = new RewardInfo();
        }

        public Builder type(RewardType type) {
            this.instance.type = type;
            return this;
        }

        public Builder addFile(String key, String file) {
            this.instance.fileMap.put(key, file);
            return this;
        }

        public Builder description(String description) {
            this.instance.description = description;
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
                ", description='" + description + '\'' +
                '}';
    }
}
