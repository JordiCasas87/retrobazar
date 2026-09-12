package com.retrobazar.productagent.infrastructure.adapter.in.web.dto;

import com.retrobazar.productagent.domain.ConfidenceLevel;
import com.retrobazar.productagent.domain.ProductReference;
import com.retrobazar.productagent.domain.SimilarCatalogProduct;

import java.math.BigDecimal;
import java.util.List;

public record ProductAgentResponseDto(
        String suggestedTitle,
        String suggestedDescription,
        BigDecimal suggestedPrice,
        BigDecimal minimumReferencePrice,
        BigDecimal maximumReferencePrice,
        ConfidenceLevel confidence,
        String explanation,
        List<ProductReference> references,
        List<SimilarCatalogProduct> similarCatalogProducts
) {
}
