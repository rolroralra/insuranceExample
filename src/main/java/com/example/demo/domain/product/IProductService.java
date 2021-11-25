package com.example.demo.domain.product;

import java.util.List;
import java.util.function.Predicate;

public interface IProductService {
    List<Product> findAllProducts();

    List<Product> findProducts(Predicate<Product> searchCondition);

    Product findProductById(Long productId);

    Product addProduct(Product product);

    Product modifyProduct(Product product);

    void removeProductById(Long productId);
}
