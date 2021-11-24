package com.example.demo.domain.product;

import com.example.demo.domain.product.mock.MockProductRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.function.Predicate;

@RequiredArgsConstructor
public class ProductService implements IProductService {
    private final ProductRepository productRepository;

    public ProductService() {
        this(MockProductRepository.getInstance());
    }

    public List<Product> searchAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> searchProducts(Predicate<Product> searchCondition) {
        return productRepository.findByPredicate(searchCondition);
    }

    public Product searchProductById(Long productId) {
        return productRepository.findById(productId);
    }

    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    public Product modifyProduct(Product product) { return productRepository.save(product); }

}
