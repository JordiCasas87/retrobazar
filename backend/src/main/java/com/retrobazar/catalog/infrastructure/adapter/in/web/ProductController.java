package com.retrobazar.catalog.infrastructure.adapter.in.web;

import com.retrobazar.catalog.application.port.in.ListActiveProductsByCategoryUseCase;
import com.retrobazar.catalog.application.port.in.ListActiveProductsUseCase;
import com.retrobazar.catalog.application.port.in.SearchProductsUseCase;
import com.retrobazar.catalog.domain.ProductCategory;
import com.retrobazar.catalog.infrastructure.adapter.in.web.dto.ProductResponseDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ListActiveProductsUseCase listActiveProductsUseCase;
    private final ListActiveProductsByCategoryUseCase listActiveProductsByCategoryUseCase;
    private final SearchProductsUseCase searchProductsUseCase;

    public ProductController(
            ListActiveProductsUseCase listActiveProductsUseCase,
            ListActiveProductsByCategoryUseCase listActiveProductsByCategoryUseCase,
            SearchProductsUseCase searchProductsUseCase
    ) {
        this.listActiveProductsUseCase = listActiveProductsUseCase;
        this.listActiveProductsByCategoryUseCase = listActiveProductsByCategoryUseCase;
        this.searchProductsUseCase = searchProductsUseCase;
    }

    @GetMapping
    public List<ProductResponseDto> listActiveProducts() {
        return listActiveProductsUseCase.listActiveProducts()
                .stream()
                .map(product -> ProductResponseDto.fromProduct(product))
                .toList();
    }

    @GetMapping("/category/{category}")
    public List<ProductResponseDto> listActiveProductsByCategory(
            @PathVariable ProductCategory category
    ) {
        return listActiveProductsByCategoryUseCase.listActiveProductsByCategory(category)
                .stream()
                .map(product -> ProductResponseDto.fromProduct(product))
                .toList();
    }

    @GetMapping("/search")
    public List<ProductResponseDto> searchProducts(@RequestParam String text) {
        return searchProductsUseCase.search(text)
                .stream()
                .map(product -> ProductResponseDto.fromProduct(product))
                .toList();
    }

}
