package com.retrobazar.catalog.application.service;

import com.retrobazar.catalog.application.port.out.ProductRepositoryPort;
import com.retrobazar.catalog.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class SearchProductsServiceTest {

    private ProductRepositoryPort productRepositoryPort;
    private SearchProductsService searchProductsService;

    @BeforeEach
    void setUp() {
        productRepositoryPort = mock(ProductRepositoryPort.class);
        searchProductsService = new SearchProductsService(productRepositoryPort);
    }

    @Test
    void shouldSearchUsingRelevantNormalizedWords() {
        Product product = mock(Product.class);
        List<String> expectedWords = List.of("teclado", "pokemon");

        when(productRepositoryPort.searchActiveProducts(expectedWords))
                .thenReturn(List.of(product));

        List<Product> result = searchProductsService.search(
                "  TECLADO con Pokemon  "
        );

        assertEquals(List.of(product), result);
        verify(productRepositoryPort).searchActiveProducts(expectedWords);
    }

    @Test
    void shouldRejectABlankSearch() {
        assertThrows(
                IllegalArgumentException.class,
                () -> searchProductsService.search("   ")
        );

        verifyNoInteractions(productRepositoryPort);
    }

    @Test
    void shouldRejectSearchesLongerThanOneHundredCharacters() {
        String text = "a".repeat(101);

        assertThrows(
                IllegalArgumentException.class,
                () -> searchProductsService.search(text)
        );

        verifyNoInteractions(productRepositoryPort);
    }

    @Test
    void shouldRejectASearchContainingOnlyStopWords() {
        assertThrows(
                IllegalArgumentException.class,
                () -> searchProductsService.search("el de la y")
        );

        verifyNoInteractions(productRepositoryPort);
    }
}
