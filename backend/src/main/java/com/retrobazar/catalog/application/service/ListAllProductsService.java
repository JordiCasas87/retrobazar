package com.retrobazar.catalog.application.service;

import com.retrobazar.catalog.application.port.in.ListAllProductsUseCase;
import com.retrobazar.catalog.application.port.out.ProductRepositoryPort;
import com.retrobazar.catalog.domain.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListAllProductsService implements ListAllProductsUseCase {

    private final ProductRepositoryPort productRepositoryPort;

    public ListAllProductsService(ProductRepositoryPort productRepositoryPort) {
        this.productRepositoryPort = productRepositoryPort;
    }

    @Override
    public List<Product> listAllProducts() {
        return productRepositoryPort.findAll();
    }
}
