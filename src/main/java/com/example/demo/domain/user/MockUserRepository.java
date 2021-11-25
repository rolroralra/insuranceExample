package com.example.demo.domain.user;

import com.example.demo.domain.common.MockRepository;

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
