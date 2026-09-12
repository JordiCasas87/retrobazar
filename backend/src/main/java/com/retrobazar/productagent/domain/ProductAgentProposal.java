package com.retrobazar.productagent.domain;

import java.math.BigDecimal;
import java.util.List;

public record ProductAgentProposal(
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
