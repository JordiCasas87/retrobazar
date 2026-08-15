package com.retrobazar.catalog.application.service;

import com.retrobazar.catalog.application.exception.ProductNotFoundException;
import com.retrobazar.catalog.application.port.in.DeleteProductUseCase;
import com.retrobazar.catalog.application.port.out.ProductRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DeleteProductService implements DeleteProductUseCase {

    private final ProductRepositoryPort productRepositoryPort;

    public DeleteProductService(ProductRepositoryPort productRepositoryPort) {
        this.productRepositoryPort = productRepositoryPort;
    }

    @Override
    public void delete(UUID id) {
        productRepositoryPort.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        productRepositoryPort.deleteById(id);
    }
}
