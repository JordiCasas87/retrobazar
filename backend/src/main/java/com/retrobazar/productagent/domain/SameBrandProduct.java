package com.retrobazar.productagent.domain;

import java.math.BigDecimal;
import java.util.UUID;

public record SameBrandProduct(
        UUID productId,
        String title,
        BigDecimal price,
        String imageUrl
) {
}
