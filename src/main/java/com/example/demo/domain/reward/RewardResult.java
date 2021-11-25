package com.example.demo.domain.reward;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RewardResult {
    @Builder.Default
    private String receiver = "Anonymous";
    @Builder.Default
    private Long rewardAmount = 0L;
    @Builder.Default
    private Boolean result = false;

    @Override
    public String toString() {
        return "RewardResult{" +
                "receiver='" + receiver + '\'' +
                ", rewardAmount=" + rewardAmount +
                ", result=" + result +
                '}';
    }

}
