package com.example.demo.domain.subscription.uw;

import com.example.demo.domain.common.MockRepository;
import com.example.demo.domain.subscription.MockSubscriptionRepository;
import com.example.demo.domain.subscription.Subscription;
import com.example.demo.domain.subscription.SubscriptionRepository;

public class MockUnderWritingRepository extends MockRepository<UnderWriting> implements UnderWritingRepository {
    private static final SubscriptionRepository subscriptionRepository = MockSubscriptionRepository.getInstance();

    private MockUnderWritingRepository() {
        super(0);
    }

    private static class LazyHolder {
        private static final MockUnderWritingRepository INSTANCE = new MockUnderWritingRepository();
    }

    public static MockUnderWritingRepository getInstance() {
        return LazyHolder.INSTANCE;
    }

    @Override
    protected UnderWriting newEntity() {
        UnderWriting underWriting = super.newEntity();
        Subscription subscription = getAnySubscription();
        subscription.setUnderWriting(underWriting);
        Subscription modifiedSubscription = subscriptionRepository.save(subscription);

        underWriting.setSubscription(modifiedSubscription);

        return underWriting;
    }

    private Subscription getAnySubscription() {
        return subscriptionRepository.findAll().stream().findAny().orElse(null);
    }
}
