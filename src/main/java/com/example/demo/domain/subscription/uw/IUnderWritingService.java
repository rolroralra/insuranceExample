package com.example.demo.domain.subscription.uw;

import com.example.demo.domain.contract.Contract;

import java.util.List;

public interface IUnderWritingService {
    UnderWriting requestUnderWriting(Long subscriptionId);

    UnderWriting registerUnderWritingResult(Long underWritingId, Boolean result);

    List<UnderWriting> findAllUnderWritings();

    UnderWriting findUnderWritingById(Long underWritingId);

    List<UnderWriting> findUnderWritingsByManagerId(Long managerId);
}
