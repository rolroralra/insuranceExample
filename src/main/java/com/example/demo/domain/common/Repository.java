package com.example.demo.domain.common;

import java.util.List;
import java.util.function.Predicate;

public interface Repository<T extends CommonEntity> {
    List<T> findAll();

    List<T> findByPredicate(Predicate<T> predicate);

    T findById(Long entityId);

    T save(T entity);

    void deleteById(Long entityId);

    T findAny();
}
