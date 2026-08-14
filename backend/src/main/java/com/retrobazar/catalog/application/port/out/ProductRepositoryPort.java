package com.retrobazar.catalog.application.port.out;

import com.retrobazar.catalog.domain.Product;
import com.retrobazar.catalog.domain.ProductCategory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepositoryPort {

    List<Product> findAllActive();
    List<Product> findAllActiveByCategory(ProductCategory category);
    Product save(Product newProduct);
    Optional<Product> findById(UUID id);
}
