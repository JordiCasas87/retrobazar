package com.retrobazar.catalog.application.service;

import com.retrobazar.catalog.application.port.in.SearchProductsUseCase;
import com.retrobazar.catalog.application.port.out.ProductRepositoryPort;
import com.retrobazar.catalog.domain.Product;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Service
public class SearchProductsService implements SearchProductsUseCase {

    private static final int MAX_SEARCH_LENGTH = 100;

    private static final Set<String> STOP_WORDS = Set.of(
            "el", "la", "los", "las",
            "un", "una", "unos", "unas",
            "de", "del", "con", "sin",
            "para", "por", "en", "y", "o"
    );

    private final ProductRepositoryPort productRepositoryPort;

    public SearchProductsService(ProductRepositoryPort productRepositoryPort) {
        this.productRepositoryPort = productRepositoryPort;
    }

    @Override
    public List<Product> search(String text) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("search text cannot be blank");
        }

        String normalizedText = text.trim();

        if (normalizedText.length() > MAX_SEARCH_LENGTH) {
            throw new IllegalArgumentException(
                    "search text cannot contain more than 100 characters"
            );
        }

        List<String> words = Arrays.stream(normalizedText.split("\\s+"))
                .map(word -> word.toLowerCase(Locale.ROOT))
                .filter(word -> !STOP_WORDS.contains(word))
                .distinct()
                .toList();

        if (words.isEmpty()) {
            throw new IllegalArgumentException(
                    "search text must contain at least one relevant word"
            );
        }

        return productRepositoryPort.searchActiveProducts(words);
    }
}
