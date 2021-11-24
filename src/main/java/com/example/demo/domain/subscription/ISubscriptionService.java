package com.example.demo.domain.subscription;

import com.example.demo.domain.subscription.dto.SubscriptionDto;
import com.example.demo.domain.subscription.uw.UnderWriting;

import java.util.List;

public interface ISubscriptionService {

    List<Subscription> findAll();

    Subscription findSubscriptionById(Long subscriptionId);

    List<Subscription> findSubscriptionsByManagerId(Long managerId);

    Subscription subscribeInsurance(Long productId, Long userId, SubscriptionDto subscriptionDto);

    UnderWriting requestUnderWriting(Long subscriptionId);
}