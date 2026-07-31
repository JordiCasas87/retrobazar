package com.retrobazar.catalog.application.implementation;

import com.retrobazar.catalog.application.port.in.ListActiveProductsUseCase;
import com.retrobazar.catalog.application.port.out.ProductRepositoryPort;
import com.retrobazar.catalog.domain.Product;

import java.util.List;
import java.util.Objects;

public class ListActiveProductsUseCaseImpl implements ListActiveProductsUseCase {

    private final ProductRepositoryPort productRepositoryPort;

    public ListActiveProductsUseCaseImpl(ProductRepositoryPort productRepositoryPort) {
        this.productRepositoryPort = Objects.requireNonNull(
                productRepositoryPort,
                "productRepositoryPort cannot be null"
        );
    }

    @Override
    public List<Product> listActiveProducts() {
        return productRepositoryPort.findAllActive();
    }
}
