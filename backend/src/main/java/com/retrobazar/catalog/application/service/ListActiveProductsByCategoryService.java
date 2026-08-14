package com.retrobazar.catalog.application.service;

import com.retrobazar.catalog.application.port.in.ListActiveProductsByCategoryUseCase;
import com.retrobazar.catalog.application.port.out.ProductRepositoryPort;
import com.retrobazar.catalog.domain.Product;
import com.retrobazar.catalog.domain.ProductCategory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ListActiveProductsByCategoryService
        implements ListActiveProductsByCategoryUseCase {

    private final ProductRepositoryPort productRepositoryPort;

    public ListActiveProductsByCategoryService(ProductRepositoryPort productRepositoryPort) {
        this.productRepositoryPort = Objects.requireNonNull(
                productRepositoryPort,
                "productRepositoryPort cannot be null"
        );
    }

    @Override
    public List<Product> listActiveProductsByCategory(ProductCategory category) {
        Objects.requireNonNull(category, "category cannot be null");
        return productRepositoryPort.findAllActiveByCategory(category);
    }
}
