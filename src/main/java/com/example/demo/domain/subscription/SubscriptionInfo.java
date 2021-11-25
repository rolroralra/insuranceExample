package com.example.demo.domain.subscription;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionInfo {
    private LocalDate startDate;
    private LocalDate expireDate;
    private Long insuranceMoneyPerMonth;
    private Long totalAmount;

    public static class Builder {
        private SubscriptionInfo instance;

        private Builder() {
            instance = new SubscriptionInfo(
                    LocalDate.now(),
                    LocalDate.now(),
                    0L,
                    0L
            );
        }

        public Builder startDate(String startDate) {
            return startDate(LocalDate.parse(startDate));
        }

        public Builder startDate(LocalDate startDate) {
            this.instance.setStartDate(startDate);
            return this;
        }

        public Builder expireDate(String expireDate) {
            return expireDate(LocalDate.parse(expireDate));
        }

        public Builder expireDate(LocalDate expireDate) {
            this.instance.setExpireDate(expireDate);
            return this;
        }

        public Builder insuranceMoneyPerMonth(Long insuranceMoneyPerMonth) {
            this.instance.setInsuranceMoneyPerMonth(insuranceMoneyPerMonth);
            return this;
        }

        public SubscriptionInfo build() {
            return this.instance;
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String toString() {
        return "SubscriptionInfo{" +
                "startDate=" + startDate +
                ", expireDate=" + expireDate +
                ", insuranceMoneyPerMonth=" + insuranceMoneyPerMonth +
                ", totalAmount=" + totalAmount +
                '}';
    }
}
