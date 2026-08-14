package com.retrobazar.catalog.application.service;

import com.retrobazar.catalog.application.port.out.ProductRepositoryPort;
import com.retrobazar.catalog.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ListActiveProductsServiceTest {

    private ProductRepositoryPort productRepositoryPort;
    private ListActiveProductsService listActiveProductsService;

    @BeforeEach
    void setUp() {
        productRepositoryPort = mock(ProductRepositoryPort.class);
        listActiveProductsService = new ListActiveProductsService(productRepositoryPort);
    }

    @Test
    void shouldReturnAllActiveProducts() {
        Product firstProduct = mock(Product.class);
        Product secondProduct = mock(Product.class);
        List<Product> activeProducts = List.of(firstProduct, secondProduct);

        when(productRepositoryPort.findAllActive()).thenReturn(activeProducts);

        List<Product> result = listActiveProductsService.listActiveProducts();

        assertEquals(activeProducts, result);
        verify(productRepositoryPort).findAllActive();
    }

    @Test
    void shouldReturnAnEmptyListWhenThereAreNoActiveProducts() {
        when(productRepositoryPort.findAllActive()).thenReturn(List.of());

        List<Product> result = listActiveProductsService.listActiveProducts();

        assertTrue(result.isEmpty());
        verify(productRepositoryPort).findAllActive();
    }
}
