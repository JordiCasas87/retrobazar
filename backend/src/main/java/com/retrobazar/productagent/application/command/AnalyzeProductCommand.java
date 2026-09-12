package com.retrobazar.productagent.application.command;

import com.retrobazar.catalog.domain.ProductCategory;

import java.math.BigDecimal;
import java.util.List;

public record AnalyzeProductCommand(
        String title,
        String brand,
        String description,
        BigDecimal currentPrice,
        ProductCategory category,
        List<String> imageUrls
) {
}
