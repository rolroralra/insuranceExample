package com.example.demo.domain.product;

import java.util.function.Predicate;

public class productSearchCondition implements Predicate<Product> {
    @Override
    public boolean test(Product product) {
        return true;
    }
}
