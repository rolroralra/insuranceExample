package com.example.demo.domain.reward;

import com.example.demo.domain.contract.Contract;
import com.example.demo.domain.contract.ContractService;
import com.example.demo.domain.contract.IContractService;
import com.example.demo.domain.manager.TaskManagerConnectionPool;
import com.example.demo.domain.manager.reward.RewardManager;
import com.example.demo.domain.message.MessageService;
import com.example.demo.domain.message.MockMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RewardService implements IRewardService {
    private final RewardRepository rewardRepository;
    private final IContractService contractService;
    private final TaskManagerConnectionPool taskManagerConnectionPool;
    private final MessageService messageService;

    public RewardService() {
        this(
                MockRewardRepository.getInstance(),
                new ContractService(),
                new TaskManagerConnectionPool(),
                new MockMessageService()
        );
    }

    @Override
    public Reward requestReward(Long contractId, RewardInfo rewardInfo) {
        Contract contract = contractService.findContractById(contractId);

        Reward reward = _createAndSaveReward(contract, rewardInfo);

        messageService.send(reward.getManagerName(), "[신규 보상청구 요청] %s", reward);
        messageService.send(reward.getUserName(), "[신규 보상청구 요청 처리 중] %s", reward);

        return reward;
    }

    @Override
    public Reward registerRewardResult(Long rewardId, RewardResult rewardResult) {
        Reward reward = rewardRepository.findById(rewardId);

        reward.complete(rewardResult);
        Reward resultReward = rewardRepository.save(reward);

        messageService.send(resultReward.getManagerName(), "[보상청구 요청 처리 등록 완료] %s", reward);
        messageService.send(resultReward.getUserName(), "[보상청구 요청 처리 결과: %s] %s", reward.isValid() ? "성공" : "실패", reward.getResult());

        return resultReward;
    }

    @Override
    public List<Reward> findAllRewards() {
        return rewardRepository.findAll();
    }

    @Override
    public List<Reward> findRewardsByManagerId(Long managerId) {
        return rewardRepository.findByPredicate(reward -> Objects.equals(managerId, reward.getManager().getId()));
    }

    @Override
    public List<Reward> findRewardsByUserId(Long userId) {
        return rewardRepository.findByPredicate(reward -> Objects.equals(userId, reward.getUser().getId()));
    }

    private Reward _createAndSaveReward(Contract contract, RewardInfo rewardInfo) {
        Reward reward = new Reward(contract, rewardInfo);

        RewardManager rewardManager = taskManagerConnectionPool.allocateRewardManager();
        reward.allocateManager(rewardManager);

        return rewardRepository.save(reward);
    }
}
