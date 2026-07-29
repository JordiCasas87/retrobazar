package com.retrobazar.catalog.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProductTest {

    private static final UUID PRODUCT_ID =
            UUID.fromString("f7e017c5-8f43-493c-a88b-8a603b2cae52");
    private static final Instant CREATED_AT =
            Instant.parse("2026-07-29T12:00:00Z");

    @Test
    void shouldCreateAValidProduct() {
        Product product = createProduct(
                new BigDecimal("49.99"),
                10,
                List.of("https://example.com/product.jpg")
        );

        assertEquals(PRODUCT_ID, product.getId());
        assertEquals("Pixel Clock", product.getName());
        assertEquals("Divoom", product.getBrand());
        assertEquals("A pixel art clock for a retro setup", product.getDescription());
        assertEquals(new BigDecimal("49.99"), product.getPrice());
        assertEquals(10, product.getStock());
        assertEquals(ProductCategory.GADGETS, product.getCategory());
        assertEquals(List.of("https://example.com/product.jpg"), product.getImageUrls());
        assertTrue(product.isActive());
        assertEquals(CREATED_AT, product.getCreatedAt());
    }

    @Test
    void shouldRejectANegativePrice() {
        assertThrows(
                IllegalArgumentException.class,
                () -> createProduct(
                        new BigDecimal("-0.01"),
                        10,
                        List.of("https://example.com/product.jpg")
                )
        );
    }

    @Test
    void shouldRejectANegativeStock() {
        assertThrows(
                IllegalArgumentException.class,
                () -> createProduct(
                        new BigDecimal("49.99"),
                        -1,
                        List.of("https://example.com/product.jpg")
                )
        );
    }

    @Test
    void shouldRejectAProductWithoutImages() {
        assertThrows(
                IllegalArgumentException.class,
                () -> createProduct(
                        new BigDecimal("49.99"),
                        10,
                        List.of()
                )
        );
    }

    private Product createProduct(
            BigDecimal price,
            int stock,
            List<String> imageUrls
    ) {
        return new Product(
                PRODUCT_ID,
                "Pixel Clock",
                "Divoom",
                "A pixel art clock for a retro setup",
                price,
                stock,
                ProductCategory.GADGETS,
                imageUrls,
                true,
                CREATED_AT
        );
    }
}
