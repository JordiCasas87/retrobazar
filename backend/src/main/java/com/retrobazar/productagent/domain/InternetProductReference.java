package com.retrobazar.productagent.domain;

import java.math.BigDecimal;

public record InternetProductReference(
        String title,
        String url,
        BigDecimal price
) {
}
