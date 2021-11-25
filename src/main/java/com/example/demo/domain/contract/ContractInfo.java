package com.example.demo.domain.contract;

import com.example.demo.domain.subscription.SubscriptionInfo;
import lombok.*;
import org.springframework.beans.BeanUtils;

import java.time.LocalDate;

@Getter
@Setter
public class ContractInfo {
    private LocalDate startDate;
    private LocalDate expireDate;
    private Long insuranceMoneyPerMonth;
    private Long totalAmount;

    public ContractInfo(SubscriptionInfo subscriptionInfo) {
        BeanUtils.copyProperties(subscriptionInfo, this);
    }

    @Override
    public String toString() {
        return "ContractInfo{" +
                "startDate=" + startDate +
                ", expireDate=" + expireDate +
                ", insuranceMoneyPerMonth=" + insuranceMoneyPerMonth +
                ", totalAmount=" + totalAmount +
                '}';
    }
}
