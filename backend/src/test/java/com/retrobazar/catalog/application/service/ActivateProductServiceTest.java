package com.retrobazar.catalog.application.service;

import com.retrobazar.catalog.application.exception.ProductNotFoundException;
import com.retrobazar.catalog.application.port.out.ProductRepositoryPort;
import com.retrobazar.catalog.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ActivateProductServiceTest {

    private ProductRepositoryPort productRepositoryPort;
    private ActivateProductService activateProductService;

    @BeforeEach
    void setUp() {
        productRepositoryPort = mock(ProductRepositoryPort.class);
        activateProductService = new ActivateProductService(productRepositoryPort);
    }

    @Test
    void shouldActivateAndSaveAnInactiveProduct() {
        UUID productId = UUID.randomUUID();
        Product product = mock(Product.class);

        when(productRepositoryPort.findById(productId)).thenReturn(Optional.of(product));
        when(product.isActive()).thenReturn(false);
        when(productRepositoryPort.save(product)).thenReturn(product);

        Product result = activateProductService.activate(productId);

        assertSame(product, result);
        verify(product).activate();
        verify(productRepositoryPort).save(product);
    }

    @Test
    void shouldReturnAnActiveProductWithoutSavingIt() {
        UUID productId = UUID.randomUUID();
        Product product = mock(Product.class);

        when(productRepositoryPort.findById(productId)).thenReturn(Optional.of(product));
        when(product.isActive()).thenReturn(true);

        Product result = activateProductService.activate(productId);

        assertSame(product, result);
        verify(product, never()).activate();
        verify(productRepositoryPort, never()).save(product);
    }

    @Test
    void shouldThrowWhenProductDoesNotExist() {
        UUID productId = UUID.randomUUID();

        when(productRepositoryPort.findById(productId)).thenReturn(Optional.empty());

        assertThrows(
                ProductNotFoundException.class,
                () -> activateProductService.activate(productId)
        );

        verify(productRepositoryPort, never()).save(any(Product.class));
    }
}
