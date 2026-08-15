package com.retrobazar.catalog.application.port.in;

import com.retrobazar.catalog.application.command.UpdateProductCommand;
import com.retrobazar.catalog.domain.Product;

import java.util.UUID;

public interface UpdateProductUseCase {

    Product update(UUID id, UpdateProductCommand command);
}
