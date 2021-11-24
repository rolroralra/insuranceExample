package com.example.demo.domain.subscription;

import com.example.demo.domain.common.CommonEntity;
import com.example.demo.domain.manager.TaskManagerConnectionPool;
import com.example.demo.domain.manager.subscription.SubscriptionManager;
import com.example.demo.domain.product.Product;
import com.example.demo.domain.subscription.dto.SubscriptionDto;
import com.example.demo.domain.subscription.uw.UnderWriting;
import com.example.demo.domain.user.User;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
public class Subscription extends CommonEntity {
    private Product product;
    private User user;
    private SubscriptionDto subscriptionDto;
    private SubscriptionManager subscriptionManager;
    private UnderWriting underWriting;
    private SubscriptionState state;

    public enum SubscriptionState {
        NOT_READY,
        PROGRESS,
        PROGRESS_UW,
        COMPLETED_UW,
        COMPLETED,
        INVALID,
        FAILED;

        private static final SubscriptionState DEFAULT_VALUE = NOT_READY;

        public static SubscriptionState getDefault() {
            return DEFAULT_VALUE;
        }

        public static SubscriptionState getDefaultOr(SubscriptionState state) {
            return Objects.nonNull(state) ? state : getDefault();
        }
    }

    public Subscription() {
        this(null, null, null, null);
    }

    public Subscription(Product product, User user, SubscriptionDto subscriptionDto) {
        this(product, user, subscriptionDto, null);
    }

    public Subscription(Product product, User user, SubscriptionDto subscriptionDto, SubscriptionState state) {
        this.product = product;
        this.user = user;
        this.subscriptionDto = subscriptionDto;
        this.state = SubscriptionState.getDefaultOr(state);
    }

    public String getSubscriptionManagerName() {
        return subscriptionManager.getName();
    }

    public String getUnderWritingManagerName() {
        return underWriting.getUnderWritingManager().getName();
    }

    public String getUserName() {
        return user.getName();
    }

    public void allocateManager(SubscriptionManager subscriptionManager) {
        setSubscriptionManager(subscriptionManager);
        setState(SubscriptionState.PROGRESS);
    }

    public void allocateUnderWriting(UnderWriting underWriting) {
        setUnderWriting(underWriting);
        setState(SubscriptionState.PROGRESS_UW);
    }

    public void complete() {
        setState(SubscriptionState.COMPLETED);
    }

    public void completeUW() {
        setState(SubscriptionState.COMPLETED_UW);
    }

    public Boolean isNotReady() {
        return SubscriptionState.NOT_READY == state;
    }

    public Boolean isInProgress() {
        return SubscriptionState.PROGRESS == state;
    }

    public Boolean isInProgressUW() {
        return SubscriptionState.PROGRESS_UW == state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subscription that = (Subscription) o;
        return Objects.equals(product, that.product) && Objects.equals(user, that.user) && Objects.equals(subscriptionDto, that.subscriptionDto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product, user, subscriptionDto);
    }

    @Override
    public String toString() {
        return "Subscription{" +
                "id=" + getId() +
                ", product=" + product +
                ", user=" + user +
                ", subscriptionManager=" + subscriptionManager +
                ", state=" + state +
                '}';
    }
}
