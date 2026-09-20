package com.retrobazar.productagent.domain;

import java.math.BigDecimal;
import java.util.List;

public record ProductAgentProposal(
        String suggestedTitle,
        String suggestedDescription,
        BigDecimal suggestedPrice,
        List<InternetProductReference> internetReferences,
        List<CatalogProductMatch> possibleCatalogMatches
) {
}
