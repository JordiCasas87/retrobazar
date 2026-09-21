package com.retrobazar.productagent.application.service;

import com.retrobazar.catalog.application.port.in.SearchProductsUseCase;
import com.retrobazar.catalog.domain.Product;
import com.retrobazar.catalog.domain.ProductCategory;
import com.retrobazar.productagent.application.command.AnalyzeProductCommand;
import com.retrobazar.productagent.application.port.out.ProductAgentPort;
import com.retrobazar.productagent.domain.SameBrandProduct;
import com.retrobazar.productagent.domain.ProductAgentProposal;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class AnalyzeProductWithAiServiceTest {

    @Test
    void shouldSearchByTheFormBrandAndAnalyzeWithSameBrandProducts() {
        AnalyzeProductCommand command = command();
        Product catalogProduct = product();
        FakeSearchProductsUseCase searchProductsUseCase =
                new FakeSearchProductsUseCase(List.of(catalogProduct));
        ProductAgentProposal expectedProposal = proposal(
                "Nintendo Game Boy Color Atomic Purple",
                List.of()
        );
        FakeProductAgentPort productAgentPort =
                new FakeProductAgentPort(expectedProposal);
        AnalyzeProductWithAiService service = new AnalyzeProductWithAiService(
                searchProductsUseCase,
                productAgentPort
        );

        ProductAgentProposal result = service.analyze(command);

        SameBrandProduct expectedProduct = new SameBrandProduct(
                catalogProduct.getId(),
                catalogProduct.getName(),
                catalogProduct.getPrice(),
                catalogProduct.getImageUrls().getFirst()
        );

        assertEquals("Nintendo", searchProductsUseCase.receivedBrand);
        assertEquals(1, productAgentPort.receivedCommands.size());
        assertSame(command, productAgentPort.receivedCommands.get(0));
        assertEquals(List.of(expectedProduct), result.sameBrandProducts());
        assertEquals(expectedProposal.suggestedTitle(), result.suggestedTitle());
        assertEquals(expectedProposal.suggestedDescription(), result.suggestedDescription());
        assertEquals(expectedProposal.suggestedPrice(), result.suggestedPrice());
    }

    @Test
    void shouldAnalyzeWithAnEmptyListWhenTheBrandHasNoProducts() {
        AnalyzeProductCommand command = command();
        FakeSearchProductsUseCase searchProductsUseCase =
                new FakeSearchProductsUseCase(List.of());
        ProductAgentProposal expectedProposal = proposal(
                "Nintendo Game Boy Color",
                List.of()
        );
        FakeProductAgentPort productAgentPort =
                new FakeProductAgentPort(expectedProposal);
        AnalyzeProductWithAiService service = new AnalyzeProductWithAiService(
                searchProductsUseCase,
                productAgentPort
        );

        ProductAgentProposal result = service.analyze(command);

        assertEquals("Nintendo", searchProductsUseCase.receivedBrand);
        assertEquals(List.of(), result.sameBrandProducts());
        assertEquals(expectedProposal.suggestedTitle(), result.suggestedTitle());
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

    private static ProductAgentProposal proposal(
            String suggestedTitle,
            List<SameBrandProduct> sameBrandProducts
    ) {
        return new ProductAgentProposal(
                suggestedTitle,
                "Consola portátil Nintendo revisada y en buen estado",
                new BigDecimal("74.99"),
                List.of(),
                sameBrandProducts
        );
    }

    // Clases creadas para hacer fake en los tests de esta clase; no usamos Mockito.
    private static final class FakeSearchProductsUseCase
            implements SearchProductsUseCase {

        private final List<Product> productsToReturn;
        private String receivedBrand;

        private FakeSearchProductsUseCase(List<Product> productsToReturn) {
            this.productsToReturn = productsToReturn;
        }

        @Override
        public List<Product> search(String text) {
            throw new AssertionError("Text search should not be used");
        }

        @Override
        public List<Product> searchByBrand(String brand) {
            receivedBrand = brand;
            return productsToReturn;
        }
    }

    private static final class FakeProductAgentPort implements ProductAgentPort {

        private final List<ProductAgentProposal> proposalsToReturn;
        private final List<AnalyzeProductCommand> receivedCommands = new ArrayList<>();
        private int invocationIndex;

        private FakeProductAgentPort(ProductAgentProposal... proposalsToReturn) {
            this.proposalsToReturn = List.of(proposalsToReturn);
        }

        @Override
        public ProductAgentProposal analyze(AnalyzeProductCommand command) {
            receivedCommands.add(command);
            return proposalsToReturn.get(invocationIndex++);
        }
    }
}
