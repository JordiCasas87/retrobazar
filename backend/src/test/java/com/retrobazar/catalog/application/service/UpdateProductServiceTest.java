package com.retrobazar.catalog.application.service;

import com.retrobazar.catalog.application.command.UpdateProductCommand;
import com.retrobazar.catalog.application.exception.ProductNotFoundException;
import com.retrobazar.catalog.application.port.out.ProductRepositoryPort;
import com.retrobazar.catalog.domain.Product;
import com.retrobazar.catalog.domain.ProductCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UpdateProductServiceTest {

    private ProductRepositoryPort productRepositoryPort;
    private UpdateProductService updateProductService;

    @BeforeEach
    void setUp() {
        productRepositoryPort = mock(ProductRepositoryPort.class);
        updateProductService = new UpdateProductService(productRepositoryPort);
    }

    @Test
    void shouldUpdateAndSaveAnExistingProduct() {
        UUID productId = UUID.randomUUID();
        Product product = createProduct(productId);
        UpdateProductCommand command = validCommand();

        when(productRepositoryPort.findById(productId)).thenReturn(Optional.of(product));
        when(productRepositoryPort.save(product)).thenReturn(product);

        Product result = updateProductService.update(productId, command);

        assertSame(product, result);
        assertEquals(command.name(), result.getName());
        assertEquals(command.price(), result.getPrice());
        assertEquals(command.stock(), result.getStock());
        verify(productRepositoryPort).save(product);
    }

    @Test
    void shouldThrowWhenProductDoesNotExist() {
        UUID productId = UUID.randomUUID();

        when(productRepositoryPort.findById(productId)).thenReturn(Optional.empty());

        assertThrows(
                ProductNotFoundException.class,
                () -> updateProductService.update(productId, validCommand())
        );

        verify(productRepositoryPort, never()).save(any(Product.class));
    }

    @Test
    void shouldNotSaveWhenNewProductDataIsInvalid() {
        UUID productId = UUID.randomUUID();
        Product product = createProduct(productId);
        UpdateProductCommand invalidCommand = new UpdateProductCommand(
                "Mechanical Keyboard",
                "RetroKeys",
                "A mechanical keyboard with RGB lighting",
                new BigDecimal("-1.00"),
                5,
                ProductCategory.SETUP_ACCESSORIES,
                List.of("https://example.com/keyboard.jpg")
        );

        when(productRepositoryPort.findById(productId)).thenReturn(Optional.of(product));

        assertThrows(
                IllegalArgumentException.class,
                () -> updateProductService.update(productId, invalidCommand)
        );

        verify(productRepositoryPort, never()).save(any(Product.class));
    }

    private UpdateProductCommand validCommand() {
        return new UpdateProductCommand(
                "Mechanical Keyboard",
                "RetroKeys",
                "A mechanical keyboard with RGB lighting",
                new BigDecimal("79.99"),
                5,
                ProductCategory.SETUP_ACCESSORIES,
                List.of("https://example.com/keyboard.jpg")
        );
    }

    private Product createProduct(UUID id) {
        return new Product(
                id,
                "Pixel Clock",
                "Divoom",
                "A pixel art clock for a retro setup",
                new BigDecimal("49.99"),
                10,
                ProductCategory.GADGETS,
                List.of("https://example.com/product.jpg"),
                true,
                Instant.parse("2026-07-29T12:00:00Z")
        );
    }
}
