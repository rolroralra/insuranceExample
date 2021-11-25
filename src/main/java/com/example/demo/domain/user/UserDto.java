package com.example.demo.domain.user;

import com.example.demo.domain.product.ProductType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class UserDto {
    private Long id;
    private String name;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    public UserDto(User user) {
        BeanUtils.copyProperties(user, this);
    }
}
