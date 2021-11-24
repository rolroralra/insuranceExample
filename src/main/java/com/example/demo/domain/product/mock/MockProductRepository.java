package com.example.demo.domain.product.mock;

import com.example.demo.domain.common.MockRepository;
import com.example.demo.domain.product.Product;
import com.example.demo.domain.product.ProductRepository;
import com.example.demo.domain.product.ProductType;

public class MockProductRepository extends MockRepository<Product> implements ProductRepository {
    private MockProductRepository() {

    }

    private static class LazyHolder {
        private static final MockProductRepository INSTANCE = new MockProductRepository();
    }

    public static MockProductRepository getInstance() {
        return LazyHolder.INSTANCE;
    }

    @Override
    protected Product newEntity() {
        Product product = super.newEntity();
        product.setName(String.format("Product%d", product.getId()));
        product.setType(ProductType.getDefault());
        return product;
    }
}
