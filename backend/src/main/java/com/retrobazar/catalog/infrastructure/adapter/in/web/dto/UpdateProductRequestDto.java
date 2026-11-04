package com.retrobazar.catalog.infrastructure.adapter.in.web.dto;

import com.retrobazar.catalog.application.command.UpdateProductCommand;
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

public record UpdateProductRequestDto(
        @NotBlank @Size(max = 255) String name,
        @NotBlank @Size(max = 255) String brand,
        @NotBlank @Size(max = 2000) String description,
        @NotNull @Positive @Digits(integer = 8, fraction = 2) BigDecimal price,
        @NotNull @PositiveOrZero Integer stock,
        @NotNull ProductCategory category,
        @NotNull @Size(min = 1, max = 5)
        List<@NotBlank @Size(max = 1000)
                @Pattern(regexp = "^https?://.*$") String> imageUrls
        ,
        @NotNull @Size(min = 1, max = 5)
        List<@Size(max = 500) String> imageStorageKeys,
        @Size(max = 1000)
        @Pattern(
                regexp = "^https://([a-z0-9-]+\\.)*wallapop\\.com(?:/.*)?$",
                flags = Pattern.Flag.CASE_INSENSITIVE
        )
        String wallapopUrl
) {

    public UpdateProductRequestDto(
            String name, String brand, String description, BigDecimal price,
            Integer stock, ProductCategory category, List<String> imageUrls,
            List<String> imageStorageKeys
    ) {
        this(name, brand, description, price, stock, category, imageUrls,
                imageStorageKeys, null);
    }

    public UpdateProductRequestDto(
            String name, String brand, String description, BigDecimal price,
            Integer stock, ProductCategory category, List<String> imageUrls
    ) {
        this(name, brand, description, price, stock, category, imageUrls,
                imageUrls == null ? java.util.List.of()
                        : java.util.Collections.nCopies(imageUrls.size(), null));
    }

    public UpdateProductCommand toCommand() {
        return new UpdateProductCommand(
                name,
                brand,
                description,
                price,
                stock,
                category,
                imageUrls,
                imageStorageKeys,
                wallapopUrl
        );
    }
}
