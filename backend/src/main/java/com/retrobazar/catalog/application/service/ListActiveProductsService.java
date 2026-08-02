package com.retrobazar.catalog.application.service;

import com.retrobazar.catalog.application.port.in.ListActiveProductsUseCase;
import com.retrobazar.catalog.application.port.out.ProductRepositoryPort;
import com.retrobazar.catalog.domain.Product;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ListActiveProductsService implements ListActiveProductsUseCase {

    private final ProductRepositoryPort productRepositoryPort;

    public ListActiveProductsService(ProductRepositoryPort productRepositoryPort) {
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
