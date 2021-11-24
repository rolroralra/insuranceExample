package com.example.demo.domain.subscription.uw;

import com.example.demo.domain.common.CommonEntity;
import com.example.demo.domain.manager.TaskManagerConnectionPool;
import com.example.demo.domain.manager.uw.UnderWritingManager;
import com.example.demo.domain.subscription.Subscription;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class UnderWriting extends CommonEntity {
    private Subscription subscription;
    private UnderWritingState state;
    private UnderWritingManager underWritingManager;

    public UnderWriting() {
        this(null, null);
    }

    public UnderWriting(Subscription subscription) {
        this(subscription, null);
    }
    public UnderWriting(Subscription subscription, UnderWritingState state) {
        this.subscription = subscription;
        this.state = UnderWritingState.getDefaultOr(state);
    }

    public String getUnderWritingManagerName() {
        return underWritingManager.getName();
    }

    public void allocateManager(UnderWritingManager underWritingManager) {
        setUnderWritingManager(underWritingManager);
        setState(UnderWritingState.PROGRESS);
    }

    public enum UnderWritingState {
        NOT_READY,
        PROGRESS,
        COMPLETED,
        INVALID,
        FAILED;

        private static final UnderWritingState DEFAULT_VALUE = NOT_READY;

        public static UnderWritingState getDefault() {
            return DEFAULT_VALUE;
        }

        public static UnderWritingState getDefaultOr(UnderWritingState state) {
            return Objects.nonNull(state) ? state : getDefault();
        }
    }

    public void complete() {
        setState(UnderWritingState.COMPLETED);
        this.subscription.completeUW();
    }

    @Override
    public String toString() {
        return "UnderWriting{" +
                "id=" + getId() +
                ", subscription=" + subscription +
                ", state=" + state +
                ", underWritingManager=" + underWritingManager +
                '}';
    }
}
