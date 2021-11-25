package com.example.demo.domain.subscription.uw;

import com.example.demo.domain.subscription.SubscriptionDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UnderWritingDto {
    private Long id;
    private Long managerId;
    private Boolean result;
    private UnderWriting.State state;
    private SubscriptionDto subscriptionDto;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    public UnderWritingDto(UnderWriting underWriting) {
        BeanUtils.copyProperties(underWriting, this);
        setManagerId(underWriting.getManagerId());
        setSubscriptionDto(new SubscriptionDto(underWriting.getSubscription()));
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
