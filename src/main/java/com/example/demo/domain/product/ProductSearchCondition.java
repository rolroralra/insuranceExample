package com.example.demo.domain.product;

import java.util.function.Predicate;

public class ProductSearchCondition implements Predicate<Product> {
    @Override
    public boolean test(Product product) {
        return true;
    }
}
