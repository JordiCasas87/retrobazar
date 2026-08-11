package com.retrobazar.catalog.application.port.out;

import com.retrobazar.catalog.domain.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepositoryPort {

    List<Product> findAllActive();
    Product save(Product newProduct);
    Optional<Product> findById(UUID id);
}
