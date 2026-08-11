package com.retrobazar.catalog.application.service;

import com.retrobazar.catalog.application.command.CreateProductCommand;
import com.retrobazar.catalog.application.port.in.CreateProductUseCase;
import com.retrobazar.catalog.application.port.out.ProductRepositoryPort;
import com.retrobazar.catalog.domain.Product;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class CreateProductService implements CreateProductUseCase {

    private final ProductRepositoryPort productRepositoryPort;

    public CreateProductService(ProductRepositoryPort productRepositoryPort) {
        this.productRepositoryPort = productRepositoryPort;
    }

    @Override
    public Product createProduct(CreateProductCommand command) {
        Instant createdAt = Instant.now();
        Product newProduct = new Product(
                UUID.randomUUID(),
                command.name(),
                command.brand(),
                command.description(),
                command.price(),
                command.stock(),
                command.category(),
                command.imageUrls(),
                command.active(),
                createdAt
        );

        Product savedProduct = productRepositoryPort.save(newProduct);

        return savedProduct;
    }
}
