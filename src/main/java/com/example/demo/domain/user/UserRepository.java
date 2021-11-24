package com.example.demo.domain.user;

import com.example.demo.domain.common.Repository;

import java.util.List;
import java.util.function.Predicate;

public interface UserRepository {
    User findById(Long userId);

    List<User> findAll();

    List<User> findByPredicate(Predicate<User> predicate);

    User save(User user);
}
