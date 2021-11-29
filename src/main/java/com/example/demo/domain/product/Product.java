package com.example.demo.domain.product;

import com.example.demo.domain.common.CommonEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.Objects;

@Setter
@Getter
@AllArgsConstructor
public class Product extends CommonEntity {
    private String name;
    private ProductType type;
    private ProductInfo info;

    public Product() {
        this("Product");
    }

    public Product(String name) {
        this(name, ProductType.getDefault(), new ProductInfo());
    }

    public Product(ProductDto productDto) {
        BeanUtils.copyProperties(productDto, this);
    }

    public static Product parse(ProductDto productDto) {
        return new Product(productDto);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(name, product.name) && type == product.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + getId() +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", info=" + info +
                '}';
    }
}
