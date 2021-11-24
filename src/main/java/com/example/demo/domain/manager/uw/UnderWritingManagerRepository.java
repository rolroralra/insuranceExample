package com.example.demo.domain.manager.uw;

import com.example.demo.domain.common.MockRepository;

public class UnderWritingManagerRepository extends MockRepository<UnderWritingManager> {
    @Override
    protected UnderWritingManager newEntity() {
        UnderWritingManager underWritingManager = super.newEntity();
        underWritingManager.setName(String.format("UnderWritingManager%d", underWritingManager.getId()));
        return underWritingManager;
    }
}
