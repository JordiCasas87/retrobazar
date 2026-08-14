package com.retrobazar.catalog.application.service;

import com.retrobazar.catalog.application.exception.ProductNotFoundException;
import com.retrobazar.catalog.application.port.in.DeactivateProductUseCase;
import com.retrobazar.catalog.application.port.out.ProductRepositoryPort;
import com.retrobazar.catalog.domain.Product;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DeactivateProductService implements DeactivateProductUseCase {

    private final ProductRepositoryPort productRepositoryPort;

    public DeactivateProductService(ProductRepositoryPort productRepositoryPort) {
        this.productRepositoryPort = productRepositoryPort;
    }

    @Override
    public Product deactivate(UUID id) {
        Product product = productRepositoryPort.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        if (!product.isActive()) {
            return product;
        }

        product.deactivate();

        return productRepositoryPort.save(product);
    }
}
