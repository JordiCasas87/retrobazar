package com.retrobazar.catalog.application.port.out;

import com.retrobazar.catalog.domain.Product;

import java.util.List;

public interface ProductRepositoryPort {

    List<Product> findAllActive();
    Product save(Product newProduct);
}
