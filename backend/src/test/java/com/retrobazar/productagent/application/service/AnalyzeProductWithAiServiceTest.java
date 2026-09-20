package com.retrobazar.productagent.application.service;

import com.retrobazar.catalog.application.port.in.SearchProductsUseCase;
import com.retrobazar.catalog.domain.Product;
import com.retrobazar.catalog.domain.ProductCategory;
import com.retrobazar.productagent.application.command.AnalyzeProductCommand;
import com.retrobazar.productagent.application.port.out.ProductAgentPort;
import com.retrobazar.productagent.domain.CatalogProductMatch;
import com.retrobazar.productagent.domain.ProductAgentProposal;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class AnalyzeProductWithAiServiceTest {

    @Test
    void shouldSearchTheCatalogAndSendMatchesToTheAgent() {
        AnalyzeProductCommand command = command();
        Product catalogProduct = product();
        FakeSearchProductsUseCase searchProductsUseCase =
                new FakeSearchProductsUseCase(List.of(catalogProduct));
        ProductAgentProposal expectedProposal = proposal();
        FakeProductAgentPort productAgentPort =
                new FakeProductAgentPort(expectedProposal);
        AnalyzeProductWithAiService service = new AnalyzeProductWithAiService(
                searchProductsUseCase,
                productAgentPort
        );

        ProductAgentProposal result = service.analyze(command);

        CatalogProductMatch expectedMatch = new CatalogProductMatch(
                catalogProduct.getId(),
                catalogProduct.getName(),
                catalogProduct.getPrice(),
                catalogProduct.getImageUrls().getFirst()
        );

        assertEquals("Game Boy Color Nintendo", searchProductsUseCase.receivedText);
        assertSame(command, productAgentPort.receivedCommand);
        assertEquals(List.of(expectedMatch), productAgentPort.receivedMatches);
        assertSame(expectedProposal, result);
    }

    @Test
    void shouldSendAnEmptyMatchListWhenTheCatalogHasNoMatches() {
        AnalyzeProductCommand command = command();
        FakeSearchProductsUseCase searchProductsUseCase =
                new FakeSearchProductsUseCase(List.of());
        ProductAgentProposal expectedProposal = proposal();
        FakeProductAgentPort productAgentPort =
                new FakeProductAgentPort(expectedProposal);
        AnalyzeProductWithAiService service = new AnalyzeProductWithAiService(
                searchProductsUseCase,
                productAgentPort
        );

        ProductAgentProposal result = service.analyze(command);

        assertEquals(List.of(), productAgentPort.receivedMatches);
        assertSame(expectedProposal, result);
    }

    private static AnalyzeProductCommand command() {
        return new AnalyzeProductCommand(
                "Game Boy Color",
                "Nintendo",
                "Consola portátil en buen estado",
                new BigDecimal("80.00"),
                ProductCategory.GAMING,
                List.of("https://example.com/game-boy.jpg")
        );
    }

    private static Product product() {
        return new Product(
                UUID.fromString("0e102b0f-09d0-45d0-8e9d-ae912c817085"),
                "Nintendo Game Boy Color Atomic Purple",
                "Nintendo",
                "Consola portátil revisada",
                new BigDecimal("69.99"),
                1,
                ProductCategory.GAMING,
                List.of("https://example.com/catalog-game-boy.jpg"),
                true,
                Instant.parse("2026-09-20T10:00:00Z")
        );
    }

    private static ProductAgentProposal proposal() {
        return new ProductAgentProposal(
                "Nintendo Game Boy Color Atomic Purple",
                "Consola portátil Nintendo revisada y en buen estado",
                new BigDecimal("74.99"),
                List.of(),
                List.of()
        );
    }

    // Clases creadas para hacer fake en los tests de esta clase; no usamos Mockito.
    private static final class FakeSearchProductsUseCase
            implements SearchProductsUseCase {

        private final List<Product> productsToReturn;
        private String receivedText;

        private FakeSearchProductsUseCase(List<Product> productsToReturn) {
            this.productsToReturn = productsToReturn;
        }

        @Override
        public List<Product> search(String text) {
            receivedText = text;
            return productsToReturn;
        }
    }

    private static final class FakeProductAgentPort implements ProductAgentPort {

        private final ProductAgentProposal proposalToReturn;
        private AnalyzeProductCommand receivedCommand;
        private List<CatalogProductMatch> receivedMatches;

        private FakeProductAgentPort(ProductAgentProposal proposalToReturn) {
            this.proposalToReturn = proposalToReturn;
        }

        @Override
        public ProductAgentProposal analyze(
                AnalyzeProductCommand command,
                List<CatalogProductMatch> catalogMatches
        ) {
            receivedCommand = command;
            receivedMatches = catalogMatches;
            return proposalToReturn;
        }
    }
}
