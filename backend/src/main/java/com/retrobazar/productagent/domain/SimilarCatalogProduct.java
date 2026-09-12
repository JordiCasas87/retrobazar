package com.retrobazar.productagent.domain;

import java.math.BigDecimal;
import java.util.UUID;

public record SimilarCatalogProduct(
        UUID productId,
        String title,
        BigDecimal price,
        String imageUrl,
        SimilarityLevel similarity,
        String reason
) {
}
