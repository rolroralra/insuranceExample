package com.example.demo.domain.product;

import java.util.List;
import java.util.function.Predicate;

public interface IProductService {
    List<Product> searchAllProducts();

    List<Product> searchProducts(Predicate<Product> searchCondition);

    Product searchProductById(Long productId);

    Product addProduct(Product product);

    Product modifyProduct(Product product);
}
