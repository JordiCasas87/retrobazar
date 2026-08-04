package com.retrobazar.catalog.infrastructure.adapter.in.web;

import com.retrobazar.catalog.application.port.in.ListActiveProductsUseCase;
import com.retrobazar.catalog.infrastructure.adapter.in.web.dto.ProductResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ListActiveProductsUseCase listActiveProductsUseCase;

    public ProductController(ListActiveProductsUseCase listActiveProductsUseCase) {
        this.listActiveProductsUseCase = listActiveProductsUseCase;
    }

    @GetMapping
    public List<ProductResponseDto> listActiveProducts() {
        return listActiveProductsUseCase.listActiveProducts()
                .stream()
                .map(ProductResponseDto::fromProduct)
                .toList();
    }
}
