package com.retrobazar.catalog.application.port.in;

import com.retrobazar.catalog.application.command.CreateProductCommand;
import com.retrobazar.catalog.domain.Product;

public interface CreateProductUseCase {

    Product createProduct(CreateProductCommand command);
}
