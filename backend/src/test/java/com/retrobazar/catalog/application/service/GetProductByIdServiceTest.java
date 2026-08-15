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

class GetProductByIdServiceTest {

    private ProductRepositoryPort productRepositoryPort;
    private GetProductByIdService getProductByIdService;

    @BeforeEach
    void setUp() {
        productRepositoryPort = mock(ProductRepositoryPort.class);
        getProductByIdService = new GetProductByIdService(productRepositoryPort);
    }

    @Test
    void shouldReturnAProductRegardlessOfItsStatus() {
        UUID productId = UUID.randomUUID();
        Product inactiveProduct = mock(Product.class);

        when(productRepositoryPort.findById(productId))
                .thenReturn(Optional.of(inactiveProduct));

        Product result = getProductByIdService.getById(productId);

        assertSame(inactiveProduct, result);
        verify(productRepositoryPort).findById(productId);
    }

    @Test
    void shouldThrowWhenProductDoesNotExist() {
        UUID productId = UUID.randomUUID();

        when(productRepositoryPort.findById(productId)).thenReturn(Optional.empty());

        assertThrows(
                ProductNotFoundException.class,
                () -> getProductByIdService.getById(productId)
        );
    }
}
