package com.example.demo.domain.contract;

import java.util.List;
import java.util.function.Predicate;

public interface IContractService {
    Contract createContract(Long subscriptionId);

    List<Contract> findContractsByUserId(Long userId);

    List<Contract> findContractsByManagerId(Long managerId);

    List<Contract> findAllContracts();

    List<Contract> findContractsByPredicate(Predicate<Contract> searchCondition);

    Contract findContractById(Long contractId);

    Contract addContract(Contract contract);

    Contract modifyContract(Contract contract);
}
