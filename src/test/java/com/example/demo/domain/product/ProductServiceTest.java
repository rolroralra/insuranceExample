package com.example.demo.domain.product;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductServiceTest {
    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductService();
    }

    @Order(1)
    @DisplayName("1. 보험상품 전체 목록을 검색할 수 있다.")
    @Test
    public void test_search_all_products() {
        List<Product> productList = productService.searchAllProducts();
        assertThat(productList).isNotEmpty();

        System.out.println(productList);
    }

    @Order(2)
    @DisplayName("2. 보험상품 검색 조건으로 보험상품 목록을 검색할 수 있다.")
    @Test
    public void test_search_products() {
        List<Product> productList = productService.searchProducts(product -> true);
        assertThat(productList).isNotEmpty();

        System.out.println(productList);
    }

    @Order(3)
    @DisplayName("3. 보험상품 id를 통해 보험상품을 검색할 수 있다.")
    @ParameterizedTest
    @ValueSource(longs = {1, 2, 3, 4})
    public void test_search_product_by_id(Long productId) {
        Product product = productService.searchProductById(productId);
        assertThat(product)
                .isNotNull()
                .extracting(Product::getId).isEqualTo(productId);

        System.out.println(product);
    }

    @Order(4)
    @DisplayName("4. 보험상품을 추가할 수 있다.")
    @Test
    public void test_add_product() {
        Product product = new Product("New Added Product");

        Product savedProduct = productService.addProduct(product);

        assertThat(savedProduct)
                .isNotNull()
                .extracting(Product::getId).isNotNull();

        assertThat(productService.searchProductById(savedProduct.getId())).isNotNull();

        System.out.println(savedProduct);
    }

    @Order(5)
    @DisplayName("5. 보험상품을 수정할 수 있다.")
    @ParameterizedTest
    @ValueSource(longs = {1, 2, 3, 4})
    public void test_modify_product(Long productId) {
        Product product = productService.searchProductById(productId);
        assertThat(product)
                .isNotNull()
                .extracting(Product::getId).isEqualTo(productId);

        product.setName("Modified_" + product.getName());

        Product modifiedProduct = productService.modifyProduct(product);

        assertThat(modifiedProduct)
                .isNotNull();

        assertThat(productService.searchProductById(productId))
                .isNotNull()
                .isEqualTo(modifiedProduct);

        System.out.println(modifiedProduct);
    }
}