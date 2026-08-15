package com.retrobazar.catalog.application.service;

import com.retrobazar.catalog.application.command.UpdateProductCommand;
import com.retrobazar.catalog.application.exception.ProductNotFoundException;
import com.retrobazar.catalog.application.port.in.UpdateProductUseCase;
import com.retrobazar.catalog.application.port.out.ProductRepositoryPort;
import com.retrobazar.catalog.domain.Product;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UpdateProductService implements UpdateProductUseCase {

    private final ProductRepositoryPort productRepositoryPort;

    public UpdateProductService(ProductRepositoryPort productRepositoryPort) {
        this.productRepositoryPort = productRepositoryPort;
    }

    @Override
    public Product update(UUID id, UpdateProductCommand command) {
        Product product = productRepositoryPort.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        product.updateDetails(
                command.name(),
                command.brand(),
                command.description(),
                command.price(),
                command.stock(),
                command.category(),
                command.imageUrls()
        );

        return productRepositoryPort.save(product);
    }
}
