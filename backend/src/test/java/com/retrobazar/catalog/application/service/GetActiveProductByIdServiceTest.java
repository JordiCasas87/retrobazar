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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GetActiveProductByIdServiceTest {

    private ProductRepositoryPort productRepositoryPort;
    private GetActiveProductByIdService getActiveProductByIdService;

    @BeforeEach
    void setUp() {
        productRepositoryPort = mock(ProductRepositoryPort.class);
        getActiveProductByIdService = new GetActiveProductByIdService(productRepositoryPort);
    }

    @Test
    void shouldReturnAnActiveProduct() {
        UUID productId = UUID.randomUUID();
        Product product = mock(Product.class);

        when(productRepositoryPort.findById(productId)).thenReturn(Optional.of(product));
        when(product.isActive()).thenReturn(true);

        Product result = getActiveProductByIdService.getById(productId);

        assertSame(product, result);
        verify(productRepositoryPort).findById(productId);
    }

    @Test
    void shouldThrowWhenProductIsInactive() {
        UUID productId = UUID.randomUUID();
        Product product = mock(Product.class);

        when(productRepositoryPort.findById(productId)).thenReturn(Optional.of(product));
        when(product.isActive()).thenReturn(false);

        assertThrows(
                ProductNotFoundException.class,
                () -> getActiveProductByIdService.getById(productId)
        );
    }

    @Test
    void shouldThrowWhenProductDoesNotExist() {
        UUID productId = UUID.randomUUID();

        when(productRepositoryPort.findById(productId)).thenReturn(Optional.empty());

        assertThrows(
                ProductNotFoundException.class,
                () -> getActiveProductByIdService.getById(productId)
        );
    }
}
