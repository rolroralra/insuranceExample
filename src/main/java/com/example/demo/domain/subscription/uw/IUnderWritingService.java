package com.example.demo.domain.subscription.uw;

import java.util.List;

public interface IUnderWritingService {
    UnderWriting requestUnderWriting(Long subscriptionId);

    UnderWriting registerUnderWritingResult(Long underWritingId, Boolean result);

    List<UnderWriting> findAllUnderWritings();

    List<UnderWriting> findUnderWritingsByManagerId(Long managerId);
}
