package com.example.demo.domain.manager.subscription;

import com.example.demo.domain.common.CommonEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionManager extends CommonEntity {
    private String name;

    @Override
    public String toString() {
        return "SubscriptionManager{" +
                "id=" + getId() +
                ", name='" + name + '\'' +
                '}';
    }
}
