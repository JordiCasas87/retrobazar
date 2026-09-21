package com.retrobazar.productagent.application.service;

import com.retrobazar.catalog.application.port.in.SearchProductsUseCase;
import com.retrobazar.catalog.domain.Product;
import com.retrobazar.productagent.application.command.AnalyzeProductCommand;
import com.retrobazar.productagent.application.port.in.AnalyzeProductWithAiUseCase;
import com.retrobazar.productagent.application.port.out.ProductAgentPort;
import com.retrobazar.productagent.domain.SameBrandProduct;
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
        List<SameBrandProduct> sameBrandProducts = searchProductsUseCase
                .searchByBrand(command.brand())
                .stream()
                .map(AnalyzeProductWithAiService::toSameBrandProduct)
                .toList();

        ProductAgentProposal aiProposal = productAgentPort.analyze(command);

        return new ProductAgentProposal(
                aiProposal.suggestedTitle(),
                aiProposal.suggestedDescription(),
                aiProposal.suggestedPrice(),
                aiProposal.internetReferences(),
                sameBrandProducts
        );
    }

    private static SameBrandProduct toSameBrandProduct(Product product) {
        return new SameBrandProduct(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getImageUrls().getFirst()
        );
    }
}
