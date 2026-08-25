package com.retrobazar.catalog.infrastructure.adapter.in.web.dto;

import com.retrobazar.catalog.application.command.CreateProductCommand;
import com.retrobazar.catalog.domain.ProductCategory;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;

public record CreateProductRequestDto(
        @NotBlank @Size(max = 255) String name,
        @NotBlank @Size(max = 255) String brand,
        @NotBlank @Size(max = 2000) String description,
        @NotNull @Positive @Digits(integer = 8, fraction = 2) BigDecimal price,
        @NotNull @PositiveOrZero Integer stock,
        @NotNull ProductCategory category,
        @NotNull @Size(min = 1, max = 5)
        List<@NotBlank @Size(max = 1000)
                @Pattern(regexp = "^https?://.*$") String> imageUrls,
        @NotNull Boolean active
) {

    public CreateProductCommand toCommand() {
        return new CreateProductCommand(
                name,
                brand,
                description,
                price,
                stock,
                category,
                imageUrls,
                active
        );

    }
}
