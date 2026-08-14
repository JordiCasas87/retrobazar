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

class DeactivateProductServiceTest {

    private ProductRepositoryPort productRepositoryPort;
    private DeactivateProductService deactivateProductService;

    @BeforeEach
    void setUp() {
        productRepositoryPort = mock(ProductRepositoryPort.class);
        deactivateProductService = new DeactivateProductService(productRepositoryPort);
    }

    @Test
    void shouldDeactivateAndSaveAnActiveProduct() {
        UUID productId = UUID.randomUUID();
        Product product = mock(Product.class);

        when(productRepositoryPort.findById(productId)).thenReturn(Optional.of(product));
        when(product.isActive()).thenReturn(true);
        when(productRepositoryPort.save(product)).thenReturn(product);

        Product result = deactivateProductService.deactivate(productId);

        assertSame(product, result);
        verify(product).deactivate();
        verify(productRepositoryPort).save(product);
    }

    @Test
    void shouldReturnAnInactiveProductWithoutSavingIt() {
        UUID productId = UUID.randomUUID();
        Product product = mock(Product.class);

        when(productRepositoryPort.findById(productId)).thenReturn(Optional.of(product));
        when(product.isActive()).thenReturn(false);

        Product result = deactivateProductService.deactivate(productId);

        assertSame(product, result);
        verify(product, never()).deactivate();
        verify(productRepositoryPort, never()).save(product);
    }

    @Test
    void shouldThrowWhenProductDoesNotExist() {
        UUID productId = UUID.randomUUID();

        when(productRepositoryPort.findById(productId)).thenReturn(Optional.empty());

        assertThrows(
                ProductNotFoundException.class,
                () -> deactivateProductService.deactivate(productId)
        );

        verify(productRepositoryPort, never()).save(any(Product.class));
    }
}
