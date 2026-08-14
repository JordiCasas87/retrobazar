package com.retrobazar.catalog.application.port.in;

import com.retrobazar.catalog.domain.Product;
import com.retrobazar.catalog.domain.ProductCategory;

import java.util.List;

public interface ListActiveProductsByCategoryUseCase {

    List<Product> listActiveProductsByCategory(ProductCategory category);
}
