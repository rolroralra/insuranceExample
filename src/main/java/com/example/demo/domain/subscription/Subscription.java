package com.example.demo.domain.subscription;

import com.example.demo.domain.common.CommonEntity;
import com.example.demo.domain.contract.Contract;
import com.example.demo.domain.manager.subscription.SubscriptionManager;
import com.example.demo.domain.product.Product;
import com.example.demo.domain.subscription.uw.UnderWriting;
import com.example.demo.domain.user.User;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
public class Subscription extends CommonEntity {
    private Product product;
    private User user;
    private SubscriptionInfo subscriptionInfo;
    private SubscriptionManager manager;
    private UnderWriting underWriting;
    private SubscriptionState state;
    private Contract contract;

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

    public Subscription(Product product, User user, SubscriptionInfo subscriptionInfo) {
        this(product, user, subscriptionInfo, null);
    }

    public Subscription(Product product, User user, SubscriptionInfo subscriptionInfo, SubscriptionState state) {
        this.product = product;
        this.user = user;
        this.subscriptionInfo = subscriptionInfo;
        this.state = SubscriptionState.getDefaultOr(state);
    }

    public String getSubscriptionManagerName() {
        return manager.getName();
    }

    public String getUnderWritingManagerName() {
        return underWriting.getManager().getName();
    }

    public String getUserName() {
        return user.getName();
    }

    public void allocateManager(SubscriptionManager subscriptionManager) {
        setManager(subscriptionManager);
        setState(SubscriptionState.PROGRESS);
    }

    public void allocateUnderWriting(UnderWriting underWriting) {
        setUnderWriting(underWriting);
        setState(SubscriptionState.PROGRESS_UW);
    }

    public void progressUW() {
        if (state == SubscriptionState.PROGRESS && underWriting.isProgress()) {
            setState(Subscription.SubscriptionState.PROGRESS_UW);
        }
    }

    public void complete() {
        if (state == SubscriptionState.COMPLETED_UW) {
            setState(SubscriptionState.COMPLETED);
        }
    }

    public void completeUW() {
        if (state == SubscriptionState.PROGRESS_UW) {
            setState(SubscriptionState.COMPLETED_UW);
        }
    }

    public Boolean isNotReady() {
        return SubscriptionState.NOT_READY == state;
    }

    public Boolean isProgress() {
        return SubscriptionState.PROGRESS == state;
    }

    public Boolean isProgressUW() {
        return SubscriptionState.PROGRESS_UW == state;
    }

    public Boolean isCompletedUW() {
        return SubscriptionState.COMPLETED_UW == state;
    }

    public Boolean isCompleted() { return SubscriptionState.COMPLETED == state; }

    public Boolean isValid() {
        if (Objects.isNull(underWriting) || !isCompleted()) {
            return false;
        }

        return underWriting.getResult();
    }

    public Boolean isNotValid() {
        return !isValid();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subscription that = (Subscription) o;
        return Objects.equals(product, that.product) && Objects.equals(user, that.user) && Objects.equals(subscriptionInfo, that.subscriptionInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product, user, subscriptionInfo);
    }

    @Override
    public String toString() {
        return "Subscription{" +
                "id=" + getId() +
                ", product=" + product +
                ", user=" + user +
                ", subscriptionManager=" + manager +
                ", state=" + state +
                '}';
    }
}
