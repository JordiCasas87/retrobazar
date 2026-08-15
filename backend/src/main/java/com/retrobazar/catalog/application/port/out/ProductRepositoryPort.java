package com.retrobazar.catalog.application.port.out;

import com.retrobazar.catalog.domain.Product;
import com.retrobazar.catalog.domain.ProductCategory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepositoryPort {

    List<Product> findAll();
    List<Product> findAllActive();
    List<Product> findAllActiveByCategory(ProductCategory category);
    List<Product> searchActiveProducts(List<String> words);
    Product save(Product newProduct);
    Optional<Product> findById(UUID id);
    void deleteById(UUID id);
}
