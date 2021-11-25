package com.example.demo.domain.subscription.uw;

import com.example.demo.domain.common.CommonEntity;
import com.example.demo.domain.manager.uw.UnderWritingManager;
import com.example.demo.domain.subscription.Subscription;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class UnderWriting extends CommonEntity {
    private Subscription subscription;
    private State state;
    private UnderWritingManager manager;
    private Boolean result;

    public UnderWriting() {
        this(null, null);
    }

    public UnderWriting(Subscription subscription) {
        this(subscription, null);
    }

    public UnderWriting(Subscription subscription, State state) {
        this.subscription = subscription;
        this.state = State.getDefaultOr(state);
        this.result = false;
    }

    public enum State {
        NOT_READY,
        PROGRESS,
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

    public String getUnderWritingManagerName() {
        return manager.getName();
    }

    public void allocateManager(UnderWritingManager underWritingManager) {
        setManager(underWritingManager);
        setState(State.PROGRESS);
    }

    public Boolean isNotReady() {
        return state == State.NOT_READY;
    }

    public Boolean isProgress() {
        return state == State.PROGRESS;
    }

    public Boolean isCompleted() {
        return state == State.COMPLETED;
    }

    public void complete() {
        setState(State.COMPLETED);
        this.subscription.completeUW();
    }

    @Override
    public String toString() {
        return "UnderWriting{" +
                "id=" + getId() +
                ", subscription=" + subscription +
                ", state=" + state +
                ", underWritingManager=" + manager +
                '}';
    }
}
