package com.retrobazar.productagent.infrastructure.adapter.in.web.dto;

import java.math.BigDecimal;
import java.util.List;

public record ProductAgentRequestDto(
        String title,
        String brand,
        String description,
        BigDecimal currentPrice,
        String category,
        List<String> imageUrls
) {
}
