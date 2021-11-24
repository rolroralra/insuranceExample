package com.example.demo.domain.manager.uw;

import com.example.demo.domain.common.CommonEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UnderWritingManager extends CommonEntity {
    private String name;

    @Override
    public String toString() {
        return "UnderWritingManager{" +
                "id=" + getId() +
                ", name='" + name + '\'' +
                '}';
    }
}
