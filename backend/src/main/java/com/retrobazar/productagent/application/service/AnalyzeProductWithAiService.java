package com.retrobazar.productagent.application.service;

import com.retrobazar.catalog.application.port.in.SearchProductsUseCase;
import com.retrobazar.catalog.domain.Product;
import com.retrobazar.productagent.application.command.AnalyzeProductCommand;
import com.retrobazar.productagent.application.port.in.AnalyzeProductWithAiUseCase;
import com.retrobazar.productagent.application.port.out.ProductAgentPort;
import com.retrobazar.productagent.domain.CatalogProductMatch;
import com.retrobazar.productagent.domain.ProductAgentProposal;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnalyzeProductWithAiService implements AnalyzeProductWithAiUseCase {

    private final SearchProductsUseCase searchProductsUseCase;
    private final ProductAgentPort productAgentPort;

    public AnalyzeProductWithAiService(
            SearchProductsUseCase searchProductsUseCase,
            ProductAgentPort productAgentPort
    ) {
        this.searchProductsUseCase = searchProductsUseCase;
        this.productAgentPort = productAgentPort;
    }

    @Override
    public ProductAgentProposal analyze(AnalyzeProductCommand command) {
        ProductAgentProposal initialProposal = productAgentPort.analyze(command, List.of());

        List<CatalogProductMatch> catalogMatches = searchProductsUseCase
                .search(initialProposal.suggestedTitle())
                .stream()
                .map(AnalyzeProductWithAiService::toCatalogProductMatch)
                .toList();

        if (catalogMatches.isEmpty()) {
            return initialProposal;
        }

        return productAgentPort.analyze(command, catalogMatches);
    }

    private static CatalogProductMatch toCatalogProductMatch(Product product) {
        return new CatalogProductMatch(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImageUrls().getFirst()
        );
    }
}
