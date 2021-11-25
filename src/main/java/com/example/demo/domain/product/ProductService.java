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

    @Override
    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> findProducts(Predicate<Product> searchCondition) {
        return productRepository.findByPredicate(searchCondition);
    }

    @Override
    public Product findProductById(Long productId) {
        return productRepository.findById(productId);
    }

    @Override
    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product modifyProduct(Product product) { return productRepository.save(product); }

    @Override
    public void removeProductById(Long productId) {
        productRepository.deleteById(productId);
    }
}
