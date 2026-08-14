package com.retrobazar.catalog.infrastructure.adapter.in.web;

import com.retrobazar.catalog.application.command.CreateProductCommand;
import com.retrobazar.catalog.application.port.in.ActivateProductUseCase;
import com.retrobazar.catalog.application.port.in.CreateProductUseCase;
import com.retrobazar.catalog.application.port.in.DeactivateProductUseCase;
import com.retrobazar.catalog.application.port.in.ListActiveProductsByCategoryUseCase;
import com.retrobazar.catalog.application.port.in.ListActiveProductsUseCase;
import com.retrobazar.catalog.domain.Product;
import com.retrobazar.catalog.domain.ProductCategory;
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
    private final ListActiveProductsByCategoryUseCase listActiveProductsByCategoryUseCase;
    private final CreateProductUseCase createProductUseCase;
    private final ActivateProductUseCase activateProductUseCase;
    private final DeactivateProductUseCase deactivateProductUseCase;

    public ProductController(
            ListActiveProductsUseCase listActiveProductsUseCase,
            ListActiveProductsByCategoryUseCase listActiveProductsByCategoryUseCase,
            CreateProductUseCase createProductUseCase,
            ActivateProductUseCase activateProductUseCase,
            DeactivateProductUseCase deactivateProductUseCase
    ) {
        this.listActiveProductsUseCase = listActiveProductsUseCase;
        this.listActiveProductsByCategoryUseCase = listActiveProductsByCategoryUseCase;
        this.createProductUseCase = createProductUseCase;
        this.activateProductUseCase = activateProductUseCase;
        this.deactivateProductUseCase = deactivateProductUseCase;
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

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<ProductResponseDto> deactivateProduct(@PathVariable UUID id) {
        Product product = deactivateProductUseCase.deactivate(id);
        ProductResponseDto productDto = ProductResponseDto.fromProduct(product);

        return ResponseEntity.status(HttpStatus.OK).body(productDto);
    }
}
