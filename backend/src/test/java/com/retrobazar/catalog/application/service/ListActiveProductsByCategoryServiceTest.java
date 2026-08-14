package com.retrobazar.catalog.application.service;

import com.retrobazar.catalog.application.port.out.ProductRepositoryPort;
import com.retrobazar.catalog.domain.Product;
import com.retrobazar.catalog.domain.ProductCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ListActiveProductsByCategoryServiceTest {

    private ProductRepositoryPort productRepositoryPort;
    private ListActiveProductsByCategoryService service;

    @BeforeEach
    void setUp() {
        productRepositoryPort = mock(ProductRepositoryPort.class);
        service = new ListActiveProductsByCategoryService(productRepositoryPort);
    }

    @Test
    void shouldReturnActiveProductsFromTheSelectedCategory() {
        Product product = mock(Product.class);
        List<Product> products = List.of(product);

        when(productRepositoryPort.findAllActiveByCategory(ProductCategory.GAMING))
                .thenReturn(products);

        List<Product> result = service.listActiveProductsByCategory(ProductCategory.GAMING);

        assertEquals(products, result);
        verify(productRepositoryPort).findAllActiveByCategory(ProductCategory.GAMING);
    }
}
