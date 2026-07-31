package com.retrobazar.catalog.application.port.in;

import com.retrobazar.catalog.domain.Product;

import java.util.List;

public interface ListActiveProductsUseCase {

    List<Product> listActiveProducts();
}
