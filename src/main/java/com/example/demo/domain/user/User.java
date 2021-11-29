package com.example.demo.domain.user;

import com.example.demo.domain.common.CommonEntity;
import com.example.demo.domain.product.ProductDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
public class User extends CommonEntity {
    private String name;
    private UserInfo info;

    public User() {
        this("User");
    }

    public User(String name) {
        this(name, new UserInfo());
    }

    public User(UserDto userDto) {
        BeanUtils.copyProperties(userDto, this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(name, user.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + getId() +
                ", name='" + name + '\'' +
                ", info=" + info +
                '}';
    }
}
