package com.example.demo.domain.contract;

import com.example.demo.domain.common.MockRepository;

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
