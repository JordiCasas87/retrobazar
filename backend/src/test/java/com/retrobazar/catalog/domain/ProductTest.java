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
    void shouldRejectAZeroPriceInConstructor() {
        assertThrows(
                IllegalArgumentException.class,
                () -> createProduct(
                        BigDecimal.ZERO,
                        10,
                        List.of("https://example.com/product.jpg")
                )
        );
    }

    @Test
    void shouldAcceptAPositivePriceInConstructor() {
        Product product = createProduct(
                new BigDecimal("0.01"),
                10,
                List.of("https://example.com/product.jpg")
        );

        assertEquals(new BigDecimal("0.01"), product.getPrice());
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

    @Test
    void shouldUpdateProductDetailsWithoutChangingIdentityStatusOrCreationDate() {
        Product product = createProduct(
                new BigDecimal("49.99"),
                10,
                List.of("https://example.com/product.jpg")
        );

        product.updateDetails(
                "Mechanical Keyboard",
                "RetroKeys",
                "A mechanical keyboard with RGB lighting",
                new BigDecimal("79.99"),
                5,
                ProductCategory.SETUP_ACCESSORIES,
                List.of("https://example.com/keyboard.jpg")
        );

        assertEquals(PRODUCT_ID, product.getId());
        assertEquals("Mechanical Keyboard", product.getName());
        assertEquals("RetroKeys", product.getBrand());
        assertEquals("A mechanical keyboard with RGB lighting", product.getDescription());
        assertEquals(new BigDecimal("79.99"), product.getPrice());
        assertEquals(5, product.getStock());
        assertEquals(ProductCategory.SETUP_ACCESSORIES, product.getCategory());
        assertEquals(List.of("https://example.com/keyboard.jpg"), product.getImageUrls());
        assertTrue(product.isActive());
        assertEquals(CREATED_AT, product.getCreatedAt());
    }

    @Test
    void shouldNotPartiallyUpdateProductWhenNewDataIsInvalid() {
        Product product = createProduct(
                new BigDecimal("49.99"),
                10,
                List.of("https://example.com/product.jpg")
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> product.updateDetails(
                        "New name",
                        "New brand",
                        "New description",
                        new BigDecimal("-1.00"),
                        5,
                        ProductCategory.GAMING,
                        List.of("https://example.com/new.jpg")
                )
        );

        assertEquals("Pixel Clock", product.getName());
        assertEquals("Divoom", product.getBrand());
        assertEquals("A pixel art clock for a retro setup", product.getDescription());
        assertEquals(new BigDecimal("49.99"), product.getPrice());
        assertEquals(10, product.getStock());
        assertEquals(ProductCategory.GADGETS, product.getCategory());
        assertEquals(List.of("https://example.com/product.jpg"), product.getImageUrls());
    }

    @Test
    void shouldRejectANegativePriceWhenUpdatingDetails() {
        Product product = createProduct(
                new BigDecimal("49.99"),
                10,
                List.of("https://example.com/product.jpg")
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> updatePrice(product, new BigDecimal("-0.01"))
        );
    }

    @Test
    void shouldRejectAZeroPriceWhenUpdatingDetails() {
        Product product = createProduct(
                new BigDecimal("49.99"),
                10,
                List.of("https://example.com/product.jpg")
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> updatePrice(product, BigDecimal.ZERO)
        );
    }

    @Test
    void shouldAcceptAPositivePriceWhenUpdatingDetails() {
        Product product = createProduct(
                new BigDecimal("49.99"),
                10,
                List.of("https://example.com/product.jpg")
        );

        updatePrice(product, new BigDecimal("0.01"));

        assertEquals(new BigDecimal("0.01"), product.getPrice());
    }

    private void updatePrice(Product product, BigDecimal price) {
        product.updateDetails(
                "Pixel Clock",
                "Divoom",
                "A pixel art clock for a retro setup",
                price,
                10,
                ProductCategory.GADGETS,
                List.of("https://example.com/product.jpg")
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
