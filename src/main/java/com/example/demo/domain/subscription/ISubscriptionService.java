package com.example.demo.domain.subscription;

import com.example.demo.domain.contract.Contract;
import com.example.demo.domain.subscription.uw.UnderWriting;

import java.util.List;
import java.util.function.Predicate;

public interface ISubscriptionService {

    List<Subscription> findAllSubscriptions();

    List<Subscription> findSubscriptions(Predicate<Subscription> predicate);

    Subscription findSubscriptionById(Long subscriptionId);

    List<Subscription> findSubscriptionsByManagerId(Long managerId);

    Subscription subscribeInsurance(Long productId, Long userId, SubscriptionInfo subscriptionInfo);

    UnderWriting requestUnderWriting(Long subscriptionId);

    Contract registerSubscriptionResult(Long subscriptionId);
}