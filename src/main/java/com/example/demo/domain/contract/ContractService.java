package com.example.demo.domain.contract;

import com.example.demo.domain.contract.mock.MockContractRepository;
import com.example.demo.domain.manager.TaskManagerConnectionPool;
import com.example.demo.domain.manager.contract.ContractManager;
import com.example.demo.domain.subscription.Subscription;
import com.example.demo.domain.subscription.SubscriptionRepository;
import com.example.demo.domain.subscription.mock.MockSubscriptionRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

@RequiredArgsConstructor
public class ContractService implements IContractService{
    private final ContractRepository contractRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final TaskManagerConnectionPool taskManagerConnectionPool;

    public ContractService() {
        this(
                MockContractRepository.getInstance(),
                MockSubscriptionRepository.getInstance(),
                new TaskManagerConnectionPool()
        );
    }

    @Override
    public Contract createContract(Long subscriptionId) {
        return createAndSaveContract(subscriptionId);
    }

    @Override
    public List<Contract> findContractsByUserId(Long userId) {
        return contractRepository.findByPredicate(contract -> Objects.equals(contract.getUser().getId(), userId));
    }

    @Override
    public List<Contract> findContractsByManagerId(Long managerId) {
        return contractRepository.findByPredicate(contract -> Objects.equals(contract.getManager().getId(), managerId));

    }

    @Override
    public List<Contract> findAllContracts() {
        return contractRepository.findAll();
    }

    @Override
    public List<Contract> findContractsByPredicate(Predicate<Contract> predicate) {
        return contractRepository.findByPredicate(predicate);
    }

    @Override
    public Contract findContractById(Long contractId) {
        return contractRepository.findById(contractId);
    }

    @Override
    public Contract addContract(Contract contract) {
        return contractRepository.save(contract);
    }

    @Override
    public Contract modifyContract(Contract contract) {
        return contractRepository.save(contract);
    }

    private Contract createAndSaveContract(Long subscriptionId) {
        Subscription subscription = subscriptionRepository.findById(subscriptionId);
        if (subscription.isNotValid()) {
            return null;
        }

        Contract contract = new Contract(subscription); // TODO: Builder Pattern (Subscription -> Contract)

        ContractManager manager = taskManagerConnectionPool.allocateContractManager();
        contract.allocate(manager);

        return contractRepository.save(contract);
    }

}
