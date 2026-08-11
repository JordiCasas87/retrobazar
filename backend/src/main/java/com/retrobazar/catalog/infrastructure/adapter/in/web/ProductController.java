package com.retrobazar.catalog.infrastructure.adapter.in.web;

import com.retrobazar.catalog.application.command.CreateProductCommand;
import com.retrobazar.catalog.application.port.in.ActivateProductUseCase;
import com.retrobazar.catalog.application.port.in.CreateProductUseCase;
import com.retrobazar.catalog.application.port.in.ListActiveProductsUseCase;
import com.retrobazar.catalog.domain.Product;
import com.retrobazar.catalog.infrastructure.adapter.in.web.dto.CreateProductRequestDto;
import com.retrobazar.catalog.infrastructure.adapter.in.web.dto.ProductResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ListActiveProductsUseCase listActiveProductsUseCase;
    private final CreateProductUseCase createProductUseCase;
    private final ActivateProductUseCase activateProductUseCase;

    public ProductController(
            ListActiveProductsUseCase listActiveProductsUseCase,
            CreateProductUseCase createProductUseCase, ActivateProductUseCase activateProductUseCase
    ) {
        this.listActiveProductsUseCase = listActiveProductsUseCase;
        this.createProductUseCase = createProductUseCase;
        this.activateProductUseCase = activateProductUseCase;
    }

    @GetMapping
    public List<ProductResponseDto> listActiveProducts() {
        return listActiveProductsUseCase.listActiveProducts()
                .stream()
                .map(ProductResponseDto::fromProduct)
                .toList();
    }

    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(
            @RequestBody CreateProductRequestDto request
    ) {
        CreateProductCommand newCommandCreate = request.toCommand();
        Product product = createProductUseCase.createProduct(newCommandCreate);
        ProductResponseDto productDto = ProductResponseDto.fromProduct(product);

        return ResponseEntity.status(HttpStatus.CREATED).body(productDto);
    }

    @PatchMapping ("/{id}/activate")
    public ResponseEntity <ProductResponseDto> activateProduct (@PathVariable UUID id){
        Product product = activateProductUseCase.activate(id);
        ProductResponseDto productDto = ProductResponseDto.fromProduct(product);

        return ResponseEntity.status(HttpStatus.OK).body(productDto);
    }
}
