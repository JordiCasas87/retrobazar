package com.retrobazar.productagent.infrastructure.adapter.in.web.dto;

import com.retrobazar.productagent.domain.CatalogProductMatch;
import com.retrobazar.productagent.domain.InternetProductReference;

import java.math.BigDecimal;
import java.util.List;

public record ProductAgentResponseDto(
        String suggestedTitle,
        String suggestedDescription,
        BigDecimal suggestedPrice,
        List<InternetProductReference> internetReferences,
        List<CatalogProductMatch> possibleCatalogMatches
) {
}
