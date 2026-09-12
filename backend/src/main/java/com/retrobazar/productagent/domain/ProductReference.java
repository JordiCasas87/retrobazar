package com.retrobazar.productagent.domain;

import java.math.BigDecimal;

public record ProductReference(
        String title,
        String url,
        BigDecimal price
) {
}
