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
        List<String> imageUrls,
        List<String> imageStorageKeys,
        String wallapopUrl
) {
    public UpdateProductCommand(
            String name, String brand, String description, BigDecimal price,
            int stock, ProductCategory category, List<String> imageUrls,
            List<String> imageStorageKeys
    ) {
        this(name, brand, description, price, stock, category, imageUrls,
                imageStorageKeys, null);
    }

    public UpdateProductCommand(
            String name, String brand, String description, BigDecimal price,
            int stock, ProductCategory category, List<String> imageUrls
    ) {
        this(name, brand, description, price, stock, category, imageUrls,
                imageUrls == null ? java.util.List.of()
                        : java.util.Collections.nCopies(imageUrls.size(), null), null);
    }
}
