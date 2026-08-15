package com.retrobazar.catalog.infrastructure.adapter.in.web.dto;

import com.retrobazar.catalog.application.command.UpdateProductCommand;
import com.retrobazar.catalog.domain.ProductCategory;

import java.math.BigDecimal;
import java.util.List;

public record UpdateProductRequestDto(
        String name,
        String brand,
        String description,
        BigDecimal price,
        int stock,
        ProductCategory category,
        List<String> imageUrls
) {

    public UpdateProductCommand toCommand() {
        return new UpdateProductCommand(
                name,
                brand,
                description,
                price,
                stock,
                category,
                imageUrls
        );
    }
}
