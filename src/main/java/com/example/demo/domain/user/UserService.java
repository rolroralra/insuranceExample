package com.example.demo.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;

    public UserService() {
        this(MockUserRepository.getInstance());
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public List<User> findUsersByPredicate(Predicate<User> searchCondition) {
        return userRepository.findAll().stream().filter(searchCondition).collect(Collectors.toList());
    }

    public User findUserById(Long userId) {
        return userRepository.findById(userId);
    }

    public User addUser(User user) {
        return userRepository.save(user);
    }

    public User modifyUser(User user) {
        return userRepository.save(user);
    }


}
