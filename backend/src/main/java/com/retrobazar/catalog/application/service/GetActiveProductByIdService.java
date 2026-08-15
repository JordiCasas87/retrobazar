package com.retrobazar.catalog.application.service;

import com.retrobazar.catalog.application.exception.ProductNotFoundException;
import com.retrobazar.catalog.application.port.in.GetActiveProductByIdUseCase;
import com.retrobazar.catalog.application.port.out.ProductRepositoryPort;
import com.retrobazar.catalog.domain.Product;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GetActiveProductByIdService implements GetActiveProductByIdUseCase {

    private final ProductRepositoryPort productRepositoryPort;

    public GetActiveProductByIdService(ProductRepositoryPort productRepositoryPort) {
        this.productRepositoryPort = productRepositoryPort;
    }

    @Override
    public Product getById(UUID id) {
        Product product = productRepositoryPort.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        if (!product.isActive()) {
            throw new ProductNotFoundException(id);
        }

        return product;
    }
}
