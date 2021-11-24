package com.example.demo.domain.product;

public enum ProductType {
    LIFE_INSURANCE,
    NON_LIFE_INSURANCE;

    public static ProductType getDefault() {
        return NON_LIFE_INSURANCE;
    }
}
