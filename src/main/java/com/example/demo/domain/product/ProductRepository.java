package com.example.demo.domain.product;

import com.example.demo.domain.common.Repository;

import java.util.List;
import java.util.function.Predicate;

public interface ProductRepository {
    List<Product> findAll();

    List<Product> findByPredicate(Predicate<Product> searchCondition);

    Product findById(Long productId);

    Product save(Product product);

}
