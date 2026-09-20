package com.retrobazar.productagent.infrastructure.adapter.in.web;

import com.retrobazar.productagent.application.command.AnalyzeProductCommand;
import com.retrobazar.productagent.application.port.in.AnalyzeProductWithAiUseCase;
import com.retrobazar.productagent.domain.ProductAgentProposal;
import com.retrobazar.productagent.infrastructure.adapter.in.web.dto.ProductAgentRequestDto;
import com.retrobazar.productagent.infrastructure.adapter.in.web.dto.ProductAgentResponseDto;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/product-agent")
public class ProductAgentController {

    private final AnalyzeProductWithAiUseCase analyzeProductWithAiUseCase;

    public ProductAgentController(AnalyzeProductWithAiUseCase analyzeProductWithAiUseCase) {
        this.analyzeProductWithAiUseCase = analyzeProductWithAiUseCase;
    }

    @PostMapping("/analyze")
    public ProductAgentResponseDto analyzeProduct(
            @Valid @RequestBody ProductAgentRequestDto request
    ) {
        AnalyzeProductCommand command = request.toCommand();
        ProductAgentProposal proposal = analyzeProductWithAiUseCase.analyze(command);

        return ProductAgentResponseDto.fromProposal(proposal);
    }
}
