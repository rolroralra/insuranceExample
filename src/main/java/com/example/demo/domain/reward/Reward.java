package com.example.demo.domain.reward;

import com.example.demo.domain.common.CommonEntity;
import com.example.demo.domain.contract.Contract;
import com.example.demo.domain.manager.reward.RewardManager;
import com.example.demo.domain.subscription.Subscription;
import com.example.demo.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
public class Reward extends CommonEntity {
    private RewardInfo rewardInfo;
    private Contract contract;
    private User user;
    private RewardManager manager;
    private State state;
    private RewardResult result;

    public Reward(Contract contract, RewardInfo rewardInfo) {
        this(
                rewardInfo,
                contract,
                contract.getUser(),
                null,
                State.getDefault(),
                RewardResult.builder().build()
        );
    }

    public void allocateManager(RewardManager rewardManager) {
        setManager(rewardManager);
        setState(State.PROGRESS);
    }

    public String getManagerName() {
        return manager.getName();
    }

    public String getUserName() {
        return user.getName();
    }

    public void complete(RewardResult rewardResult) {
        setResult(rewardResult);
        contract.addRewardHistory(this);
        setState(State.COMPLETED);
    }

    public Boolean isValid() {
        return result.getResult();
    }

    public enum State {
        NOT_READY,
        PROGRESS,
        COMPLETED;

        private static final State DEFAULT_VALUE = NOT_READY;

        public static State getDefault() {
            return DEFAULT_VALUE;
        }

        public static State getDefaultOr(State state) {
            return Objects.nonNull(state) ? state : getDefault();
        }
    }

    @Override
    public String toString() {
        return "Reward{" +
                "id=" + getId() +
                ", rewardInfo=" + rewardInfo +
                ", contract=" + contract +
                ", user=" + user +
                ", manager=" + manager +
                ", state=" + state +
                '}';
    }
}
