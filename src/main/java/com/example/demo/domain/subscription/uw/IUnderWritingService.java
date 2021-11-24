package com.example.demo.domain.subscription.uw;

import com.example.demo.domain.subscription.Subscription;

import java.util.List;

public interface IUnderWritingService {
    UnderWriting requestUnderWriting(Long subscriptionId);
    UnderWriting registerUnderWritingResult(UnderWriting underWriting);

    List<UnderWriting> findAll();
    List<UnderWriting> findUnderWritingsByManagerId(Long managerId);
}
