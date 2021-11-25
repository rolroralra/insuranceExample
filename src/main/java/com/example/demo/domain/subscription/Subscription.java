package com.example.demo.domain.subscription;

import com.example.demo.domain.common.CommonEntity;
import com.example.demo.domain.contract.Contract;
import com.example.demo.domain.manager.subscription.SubscriptionManager;
import com.example.demo.domain.product.Product;
import com.example.demo.domain.subscription.uw.UnderWriting;
import com.example.demo.domain.user.User;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class Subscription extends CommonEntity {
    private Product product;
    private User user;
    private SubscriptionManager manager;
    private SubscriptionInfo subscriptionInfo;
    private UnderWriting underWriting;
    private Contract contract;
    private State state;

    public enum State {
        NOT_READY,
        PROGRESS,
        PROGRESS_UW,
        COMPLETED_UW,
        COMPLETED,
        INVALID,
        FAILED;

        private static final State DEFAULT_VALUE = NOT_READY;

        public static State getDefault() {
            return DEFAULT_VALUE;
        }

        public static State getDefaultOr(State state) {
            return Objects.nonNull(state) ? state : getDefault();
        }
    }

    public Subscription() {
        this(null, null, null, null);
    }

    public Subscription(Product product, User user, SubscriptionInfo subscriptionInfo) {
        this(product, user, subscriptionInfo, null);
    }

    public Subscription(Product product, User user, SubscriptionInfo subscriptionInfo, State state) {
        this.product = product;
        this.user = user;
        this.subscriptionInfo = subscriptionInfo;
        this.state = State.getDefaultOr(state);
    }

    public Long getProductId() {return product.getId(); }

    public String getProductName() { return product.getName(); }

    public Long getUserId() { return user.getId(); }

    public String getUserName() {
        return user.getName();
    }

    public Long getSubscriptionManagerId() { return manager.getId(); }

    public String getSubscriptionManagerName() {
        return manager.getName();
    }

    public Long getUnderWritingManagerId() { return underWriting.getManager().getId(); }

    public String getUnderWritingManagerName() {
        return underWriting.getManager().getName();
    }

    public void allocateManager(SubscriptionManager subscriptionManager) {
        setManager(subscriptionManager);
        setState(State.PROGRESS);
    }

    public void allocateUnderWriting(UnderWriting underWriting) {
        setUnderWriting(underWriting);
        setState(State.PROGRESS_UW);
    }

    public void progressUW() {
        if (state == State.PROGRESS && underWriting.isProgress()) {
            setState(State.PROGRESS_UW);
        }
    }

    public void complete() {
        if (state == State.COMPLETED_UW) {
            setState(State.COMPLETED);
        }
    }

    public void completeUW() {
        if (state == State.PROGRESS_UW) {
            setState(State.COMPLETED_UW);
        }
    }

    public Boolean isNotReady() {
        return State.NOT_READY == state;
    }

    public Boolean isProgress() {
        return State.PROGRESS == state;
    }

    public Boolean isProgressUW() {
        return State.PROGRESS_UW == state;
    }

    public Boolean isCompletedUW() {
        return State.COMPLETED_UW == state;
    }

    public Boolean isCompleted() { return State.COMPLETED == state; }

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
