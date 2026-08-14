package com.retrobazar.catalog.application.service;

import com.retrobazar.catalog.application.command.CreateProductCommand;
import com.retrobazar.catalog.application.port.out.ProductRepositoryPort;
import com.retrobazar.catalog.domain.Product;
import com.retrobazar.catalog.domain.ProductCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreateProductServiceTest {

    private ProductRepositoryPort productRepositoryPort;
    private CreateProductService createProductService;

    @BeforeEach
    void setUp() {
        productRepositoryPort = mock(ProductRepositoryPort.class);
        createProductService = new CreateProductService(productRepositoryPort);
    }

    @Test
    void shouldCreateAndSaveAProduct() {
        CreateProductCommand command = new CreateProductCommand(
                "Game Boy Color",
                "Nintendo",
                "Consola portátil retro",
                new BigDecimal("89.99"),
                3,
                ProductCategory.GAMING,
                List.of("https://example.com/game-boy-color.jpg"),
                true
        );

        when(productRepositoryPort.save(any(Product.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Product result = createProductService.createProduct(command);

        assertEquals(command.name(), result.getName());
        assertEquals(command.brand(), result.getBrand());
        assertEquals(command.description(), result.getDescription());
        assertEquals(command.price(), result.getPrice());
        assertEquals(command.stock(), result.getStock());
        assertEquals(command.category(), result.getCategory());
        assertEquals(command.imageUrls(), result.getImageUrls());
        assertEquals(command.active(), result.isActive());
        assertNotNull(result.getId());
        assertNotNull(result.getCreatedAt());

        verify(productRepositoryPort).save(any(Product.class));
    }

    @Test
    void shouldNotCreateAProductWithNegativePrice() {
        CreateProductCommand command = new CreateProductCommand(
                "Game Boy Color",
                "Nintendo",
                "Consola portátil retro",
                new BigDecimal("-9.99"),
                3,
                ProductCategory.GAMING,
                List.of("https://example.com/game-boy-color.jpg"),
                true
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> createProductService.createProduct(command)
        );

        verify(productRepositoryPort, never())
                .save(any(Product.class));
    }

}
