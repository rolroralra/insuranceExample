package com.example.demo.domain.manager.contract;

import com.example.demo.domain.common.CommonEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContractManager extends CommonEntity {
    private String name;

    @Override
    public String toString() {
        return "ContractManager{" +
                "id=" + getId() +
                ", name='" + name + '\'' +
                '}';
    }
}
