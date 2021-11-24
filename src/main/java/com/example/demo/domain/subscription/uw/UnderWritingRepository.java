package com.example.demo.domain.subscription.uw;

import java.util.List;
import java.util.function.Predicate;

public interface UnderWritingRepository {
    UnderWriting findById(Long underWritingId);

    List<UnderWriting> findAll();

    List<UnderWriting> findByPredicate(Predicate<UnderWriting> predicate);

    UnderWriting save(UnderWriting underWriting);
}
