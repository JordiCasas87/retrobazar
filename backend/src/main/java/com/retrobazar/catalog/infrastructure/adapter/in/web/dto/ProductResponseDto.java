package com.retrobazar.catalog.infrastructure.adapter.in.web.dto;

import com.retrobazar.catalog.domain.Product;
import com.retrobazar.catalog.domain.ProductCategory;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ProductResponseDto(
        UUID id,
        String name,
        String brand,
        String description,
        BigDecimal price,
        int stock,
        ProductCategory category,
        List<String> imageUrls,
        boolean active,
        Instant createdAt
) {

    public static ProductResponseDto fromProduct(Product product) {
        return new ProductResponseDto(
                product.getId(),
                product.getName(),
                product.getBrand(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getCategory(),
                product.getImageUrls(),
                product.isActive(),
                product.getCreatedAt()
        );
    }
}
