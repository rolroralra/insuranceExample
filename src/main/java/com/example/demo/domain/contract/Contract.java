package com.example.demo.domain.contract;

import com.example.demo.domain.common.CommonEntity;
import com.example.demo.domain.manager.contract.ContractManager;
import com.example.demo.domain.product.Product;
import com.example.demo.domain.reward.Reward;
import com.example.demo.domain.reward.RewardHistory;
import com.example.demo.domain.subscription.Subscription;
import com.example.demo.domain.user.User;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class Contract extends CommonEntity {
    private Product product;
    private User user;
    private ContractManager manager;
    private ContractInfo contractInfo;
    private State state;
    private RewardHistory rewardHistory;

    public Contract(Subscription subscription) {
        this(subscription.getProduct(), subscription.getUser(), null, new ContractInfo(subscription.getSubscriptionInfo()));
    }

    private Contract(Product product, User user, ContractManager manager, ContractInfo contractInfo) {
        this.product = product;
        this.user = user;
        this.manager = manager;
        this.contractInfo = contractInfo;
        this.state = State.getDefault();
        this.rewardHistory = new RewardHistory();
    }

    public enum State {
        NOT_READY,
        PROGRESS,
        EXPIRED;

        private static final State DEFAULT_VALUE = NOT_READY;

        public static State getDefault() {
            return DEFAULT_VALUE;
        }

        public static State getDefaultOr(State state) {
            return Objects.nonNull(state) ? state : getDefault();
        }
    }

    public void allocate(ContractManager manager) {
        setManager(manager);
        setState(State.PROGRESS);
    }

    public void addRewardHistory(Reward reward) {
        rewardHistory.addReward(reward);
    }

    public Integer totalRewardHistoryCount() {
        return rewardHistory.totalRewardHistoryCount();
    }

    @Override
    public String toString() {
        return "Contract{" +
                "id=" + getId() +
                ", product=" + product +
                ", user=" + user +
                ", manager=" + manager +
                ", contractInfo=" + contractInfo +
                ", state=" + state +
                '}';
    }
}
