package com.example.demo.domain.subscription;

import com.example.demo.domain.common.Repository;

import java.util.List;
import java.util.function.Predicate;

public interface SubscriptionRepository {
    Subscription findById(Long subscriptionId);

    List<Subscription> findAll();

    List<Subscription> findByPredicate(Predicate<Subscription> predicate);

    Subscription save(Subscription subscription);
}
