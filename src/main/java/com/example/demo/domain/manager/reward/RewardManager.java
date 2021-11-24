package com.example.demo.domain.manager.reward;


import com.example.demo.domain.common.CommonEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RewardManager extends CommonEntity {
    private String name;

    @Override
    public String toString() {
        return "RewardManager{" +
                "id=" + getId() +
                ", name='" + name + '\'' +
                '}';
    }
}
