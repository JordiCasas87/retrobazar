package com.retrobazar.productagent.infrastructure.adapter.in.web;

import com.retrobazar.catalog.domain.ProductCategory;
import com.retrobazar.productagent.application.command.AnalyzeProductCommand;
import com.retrobazar.productagent.application.port.in.AnalyzeProductWithAiUseCase;
import com.retrobazar.productagent.domain.ProductAgentProposal;
import com.retrobazar.productagent.infrastructure.adapter.in.web.dto.ProductAgentRequestDto;
import com.retrobazar.productagent.infrastructure.adapter.in.web.dto.ProductAgentResponseDto;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ProductAgentControllerTest {

    @Test
    void convertsTheRequestAndReturnsTheAiProposal() {
        ProductAgentProposal expectedProposal = new ProductAgentProposal(
                "Game Boy Color Nintendo",
                "Consola portátil retro en buen estado.",
                new BigDecimal("89.99"),
                List.of(),
                List.of()
        );
        FakeAnalyzeProductWithAiUseCase useCase =
                new FakeAnalyzeProductWithAiUseCase(expectedProposal);
        ProductAgentController controller = new ProductAgentController(useCase);
        ProductAgentRequestDto request = new ProductAgentRequestDto(
                "Game Boy Color",
                "Nintendo",
                "Consola usada",
                new BigDecimal("80.00"),
                ProductCategory.GAMING,
                List.of("https://example.com/game-boy.jpg")
        );

        ProductAgentResponseDto response = controller.analyzeProduct(request);

        assertThat(useCase.receivedCommand).isEqualTo(request.toCommand());
        assertThat(response).isEqualTo(ProductAgentResponseDto.fromProposal(expectedProposal));
    }

    private static final class FakeAnalyzeProductWithAiUseCase
            implements AnalyzeProductWithAiUseCase {

        private final ProductAgentProposal proposal;
        private AnalyzeProductCommand receivedCommand;

        private FakeAnalyzeProductWithAiUseCase(ProductAgentProposal proposal) {
            this.proposal = proposal;
        }

        @Override
        public ProductAgentProposal analyze(AnalyzeProductCommand command) {
            receivedCommand = command;
            return proposal;
        }
    }
}
