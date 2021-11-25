package com.example.demo.domain.contract.mock;

import com.example.demo.domain.common.MockRepository;
import com.example.demo.domain.contract.Contract;
import com.example.demo.domain.contract.ContractRepository;

public class MockContractRepository extends MockRepository<Contract> implements ContractRepository {

    private MockContractRepository() {
        super(0);
    }

    private static class LazyHolder {
        private static final MockContractRepository INSTANCE = new MockContractRepository();
    }

    public static MockContractRepository getInstance() {
        return LazyHolder.INSTANCE;
    }
}
