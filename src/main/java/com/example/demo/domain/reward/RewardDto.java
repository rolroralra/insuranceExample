package com.example.demo.domain.reward;

import com.example.demo.domain.contract.Contract;
import com.example.demo.domain.manager.reward.RewardManager;
import com.example.demo.domain.user.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
public class RewardDto {
    private Long id;
    private Long contractId;
    private Long userId;
    private Long managerId;
    private Reward.State state;

    private RewardType type;
    private Map<String, String> fileMap;
    private String description;

    private String receiver;
    private Long rewardAmount;
    private Boolean result;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    public RewardDto(Reward reward) {
        BeanUtils.copyProperties(reward, this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
