package com.retrobazar.catalog.application.port.in;

import com.retrobazar.catalog.domain.Product;

import java.util.UUID;

public interface ActivateProductUseCase {

   Product activate(UUID id);
}
