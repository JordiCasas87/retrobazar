package com.retrobazar.catalog.infrastructure.adapter.in.web.dto;

import com.retrobazar.catalog.application.command.CreateProductCommand;
import com.retrobazar.catalog.domain.ProductCategory;

import java.math.BigDecimal;
import java.util.List;

public record CreateProductRequestDto(
        String name,
        String brand,
        String description,
        BigDecimal price,
        int stock,
        ProductCategory category,
        List<String> imageUrls,
        boolean active
) {

    public CreateProductCommand toCommand() {
        return new CreateProductCommand(
                name,
                brand,
                description,
                price,
                stock,
                category,
                imageUrls,
                active
        );

    }
}
