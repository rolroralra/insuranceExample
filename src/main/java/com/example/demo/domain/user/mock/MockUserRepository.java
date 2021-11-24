package com.example.demo.domain.user.mock;

import com.example.demo.domain.common.MockRepository;
import com.example.demo.domain.user.User;
import com.example.demo.domain.user.UserRepository;

public class MockUserRepository extends MockRepository<User> implements UserRepository {
    private MockUserRepository() {
    }

    private static class LazyHolder {
        private static final MockUserRepository INSTANCE = new MockUserRepository();
    }

    public static MockUserRepository getInstance() {
        return LazyHolder.INSTANCE;
    }

    @Override
    protected User newEntity() {
        User user = super.newEntity();
        user.setName(String.format("User%d", user.getId()));
        return user;
    }
}
