package com.retrobazar.catalog.application.service;

import com.retrobazar.catalog.application.exception.ProductNotFoundException;
import com.retrobazar.catalog.application.port.in.ActivateProductUseCase;
import com.retrobazar.catalog.application.port.out.ProductRepositoryPort;
import com.retrobazar.catalog.domain.Product;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ActivateProductService implements ActivateProductUseCase {

private final ProductRepositoryPort productRepositoryPort;

    public ActivateProductService(ProductRepositoryPort productRepositoryPort) {
        this.productRepositoryPort = productRepositoryPort;
    }

    @Override
    public Product activate(UUID id) {

        Product product = productRepositoryPort.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        product.activate();

        // producto encontrado, falta cambiar de inactivo a activo o dejarlo igual y guardar si es necesario
        //luego devolver el producto
        return product;
    }
}
