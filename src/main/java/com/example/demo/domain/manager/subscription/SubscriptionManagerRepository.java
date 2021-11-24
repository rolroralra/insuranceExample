package com.example.demo.domain.manager.subscription;

import com.example.demo.domain.common.MockRepository;

public class SubscriptionManagerRepository extends MockRepository<SubscriptionManager> {

    @Override
    protected SubscriptionManager newEntity() {
        SubscriptionManager subscriptionManager = super.newEntity();
        subscriptionManager.setName(String.format("SubscriptionManager%d", subscriptionManager.getId()));
        return subscriptionManager;
    }
}
