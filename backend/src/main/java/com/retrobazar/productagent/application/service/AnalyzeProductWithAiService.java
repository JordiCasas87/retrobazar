package com.retrobazar.productagent.application.service;

import com.retrobazar.catalog.application.port.in.SearchProductsUseCase;
import com.retrobazar.catalog.domain.Product;
import com.retrobazar.productagent.application.command.AnalyzeProductCommand;
import com.retrobazar.productagent.application.port.in.AnalyzeProductWithAiUseCase;
import com.retrobazar.productagent.application.port.out.ProductAgentPort;
import com.retrobazar.productagent.domain.CatalogProductMatch;
import com.retrobazar.productagent.domain.ProductAgentProposal;

import java.util.List;

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
        String searchText = command.title() + " " + command.brand();

        List<CatalogProductMatch> catalogMatches = searchProductsUseCase
                .search(searchText)
                .stream()
                .map(AnalyzeProductWithAiService::toCatalogProductMatch)
                .toList();

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
