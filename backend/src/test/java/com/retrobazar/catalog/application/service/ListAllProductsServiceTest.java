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

class ListAllProductsServiceTest {

    private ProductRepositoryPort productRepositoryPort;
    private ListAllProductsService listAllProductsService;

    @BeforeEach
    void setUp() {
        productRepositoryPort = mock(ProductRepositoryPort.class);
        listAllProductsService = new ListAllProductsService(productRepositoryPort);
    }

    @Test
    void shouldReturnAllProducts() {
        Product activeProduct = mock(Product.class);
        Product inactiveProduct = mock(Product.class);
        List<Product> products = List.of(activeProduct, inactiveProduct);

        when(productRepositoryPort.findAll()).thenReturn(products);

        List<Product> result = listAllProductsService.listAllProducts();

        assertEquals(products, result);
        verify(productRepositoryPort).findAll();
    }

    @Test
    void shouldReturnAnEmptyListWhenThereAreNoProducts() {
        when(productRepositoryPort.findAll()).thenReturn(List.of());

        List<Product> result = listAllProductsService.listAllProducts();

        assertTrue(result.isEmpty());
        verify(productRepositoryPort).findAll();
    }
}
