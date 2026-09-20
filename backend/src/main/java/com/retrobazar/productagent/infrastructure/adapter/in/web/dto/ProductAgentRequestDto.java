package com.retrobazar.productagent.infrastructure.adapter.in.web.dto;

import com.retrobazar.catalog.domain.ProductCategory;
import com.retrobazar.productagent.application.command.AnalyzeProductCommand;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;

public record ProductAgentRequestDto(
        @NotBlank @Size(max = 255) String title,
        @NotBlank @Size(max = 255) String brand,
        @NotBlank @Size(max = 2000) String description,
        @NotNull @Positive @Digits(integer = 8, fraction = 2) BigDecimal currentPrice,
        @NotNull ProductCategory category,
        @NotNull @Size(min = 1, max = 5)
        List<@NotBlank @Size(max = 1000)
                @Pattern(regexp = "^https?://.*$") String> imageUrls
) {

    public AnalyzeProductCommand toCommand() {
        return new AnalyzeProductCommand(
                title,
                brand,
                description,
                currentPrice,
                category,
                imageUrls
        );
    }
}
