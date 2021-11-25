package com.example.demo.domain.subscription.mock;

import com.example.demo.domain.common.MockRepository;
import com.example.demo.domain.manager.TaskManagerConnectionPool;
import com.example.demo.domain.product.Product;
import com.example.demo.domain.product.ProductRepository;
import com.example.demo.domain.product.mock.MockProductRepository;
import com.example.demo.domain.subscription.Subscription;
import com.example.demo.domain.subscription.SubscriptionRepository;
import com.example.demo.domain.subscription.SubscriptionInfo;
import com.example.demo.domain.user.User;
import com.example.demo.domain.user.UserRepository;
import com.example.demo.domain.user.mock.MockUserRepository;

public class MockSubscriptionRepository extends MockRepository<Subscription> implements SubscriptionRepository {
    private static final UserRepository USER_REPOSITORY = MockUserRepository.getInstance();
    private static final ProductRepository PRODUCT_REPOSITORY = MockProductRepository.getInstance();
    private static final TaskManagerConnectionPool TASK_MANAGER_CONNECTION_POOL = new TaskManagerConnectionPool();

    private MockSubscriptionRepository() {
    }

    private static class LazyHolder {
        private static final MockSubscriptionRepository INSTANCE = new MockSubscriptionRepository();
    }

    public static MockSubscriptionRepository getInstance() {
        return LazyHolder.INSTANCE;
    }

    @Override
    protected Subscription newEntity() {
        Subscription subscription = super.newEntity();
        subscription.setUser(getAnyUser());
        subscription.setProduct(getAnyProduct());
        subscription.setSubscriptionInfo(new SubscriptionInfo());
        subscription.allocateManager(TASK_MANAGER_CONNECTION_POOL.allocateSubscriptionManager());
        return subscription;
    }

    private User getAnyUser() {
        return USER_REPOSITORY.findAll().stream().findAny().orElse(null);
    }

    private Product getAnyProduct() {
        return PRODUCT_REPOSITORY.findAll().stream().findAny().orElse(null);
    }
}
