package com.retrobazar.catalog.application.command;

import com.retrobazar.catalog.domain.ProductCategory;

import java.math.BigDecimal;
import java.util.List;

public record CreateProductCommand(
        String name,
        String brand,
        String description,
        BigDecimal price,
        int stock,
        ProductCategory category,
        List<String> imageUrls,
        List<String> imageStorageKeys,
        String wallapopUrl,
        boolean active
) {
    public CreateProductCommand(
            String name, String brand, String description, BigDecimal price,
            int stock, ProductCategory category, List<String> imageUrls,
            List<String> imageStorageKeys, boolean active
    ) {
        this(name, brand, description, price, stock, category, imageUrls,
                imageStorageKeys, null, active);
    }

    public CreateProductCommand(
            String name, String brand, String description, BigDecimal price,
            int stock, ProductCategory category, List<String> imageUrls, boolean active
    ) {
        this(name, brand, description, price, stock, category, imageUrls,
                imageUrls == null ? java.util.List.of()
                        : java.util.Collections.nCopies(imageUrls.size(), null), null, active);
    }
}
