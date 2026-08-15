package com.retrobazar.catalog.application.command;

import com.retrobazar.catalog.domain.ProductCategory;

import java.math.BigDecimal;
import java.util.List;

public record UpdateProductCommand(
        String name,
        String brand,
        String description,
        BigDecimal price,
        int stock,
        ProductCategory category,
        List<String> imageUrls
) {
}
