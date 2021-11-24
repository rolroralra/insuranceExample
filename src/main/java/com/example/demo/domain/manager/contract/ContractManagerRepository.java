package com.example.demo.domain.manager.contract;

import com.example.demo.domain.common.MockRepository;

public class ContractManagerRepository extends MockRepository<ContractManager> {
    @Override
    protected ContractManager newEntity() {
        ContractManager contractManager = super.newEntity();
        contractManager.setName(String.format("ContractManager%d", contractManager.getId()));
        return contractManager;
    }
}
