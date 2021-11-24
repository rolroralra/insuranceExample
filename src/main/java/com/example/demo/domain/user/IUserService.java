package com.example.demo.domain.user;

import java.util.List;
import java.util.function.Predicate;

public interface IUserService {
    List<User> findAllUsers();

    List<User> findUsersByPredicate(Predicate<User> searchCondition);

    User findUserById(Long userId);

    User addUser(User user);

    User modifyUser(User user);
}
