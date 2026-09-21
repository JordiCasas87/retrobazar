package com.retrobazar.productagent.infrastructure.adapter.in.web.dto;

import com.retrobazar.productagent.domain.SameBrandProduct;
import com.retrobazar.productagent.domain.InternetProductReference;
import com.retrobazar.productagent.domain.ProductAgentProposal;

import java.math.BigDecimal;
import java.util.List;

public record ProductAgentResponseDto(
        String suggestedTitle,
        String suggestedDescription,
        BigDecimal suggestedPrice,
        List<InternetProductReference> internetReferences,
        List<SameBrandProduct> sameBrandProducts
) {

    public static ProductAgentResponseDto fromProposal(ProductAgentProposal proposal) {
        return new ProductAgentResponseDto(
                proposal.suggestedTitle(),
                proposal.suggestedDescription(),
                proposal.suggestedPrice(),
                proposal.internetReferences(),
                proposal.sameBrandProducts()
        );
    }
}
