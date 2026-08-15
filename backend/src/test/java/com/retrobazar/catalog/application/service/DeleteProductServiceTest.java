package com.retrobazar.catalog.application.service;

import com.retrobazar.catalog.application.exception.ProductNotFoundException;
import com.retrobazar.catalog.application.port.out.ProductRepositoryPort;
import com.retrobazar.catalog.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DeleteProductServiceTest {

    private ProductRepositoryPort productRepositoryPort;
    private DeleteProductService deleteProductService;

    @BeforeEach
    void setUp() {
        productRepositoryPort = mock(ProductRepositoryPort.class);
        deleteProductService = new DeleteProductService(productRepositoryPort);
    }

    @Test
    void shouldDeleteAnExistingProduct() {
        UUID productId = UUID.randomUUID();
        Product product = mock(Product.class);

        when(productRepositoryPort.findById(productId)).thenReturn(Optional.of(product));

        deleteProductService.delete(productId);

        verify(productRepositoryPort).findById(productId);
        verify(productRepositoryPort).deleteById(productId);
    }

    @Test
    void shouldThrowWhenProductDoesNotExist() {
        UUID productId = UUID.randomUUID();

        when(productRepositoryPort.findById(productId)).thenReturn(Optional.empty());

        assertThrows(
                ProductNotFoundException.class,
                () -> deleteProductService.delete(productId)
        );

        verify(productRepositoryPort, never()).deleteById(productId);
    }
}
