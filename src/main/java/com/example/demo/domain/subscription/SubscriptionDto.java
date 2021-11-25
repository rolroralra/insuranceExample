package com.example.demo.domain.subscription;

import com.example.demo.domain.contract.Contract;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionDto {
    private Long id;
    private Long userId;
    private Long productId;
    private Long managerId;
    private Subscription.State state;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    public SubscriptionDto(Subscription subscription) {
        BeanUtils.copyProperties(subscription, this);
        setProductId(subscription.getProductId());
        setUserId(subscription.getUserId());
        setManagerId(subscription.getSubscriptionManagerId());
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
