package com.retrobazar.catalog.infrastructure.adapter.in.web;

import com.retrobazar.catalog.application.command.CreateProductCommand;
import com.retrobazar.catalog.application.port.in.ActivateProductUseCase;
import com.retrobazar.catalog.application.port.in.CreateProductUseCase;
import com.retrobazar.catalog.application.port.in.DeactivateProductUseCase;
import com.retrobazar.catalog.application.port.in.ListAllProductsUseCase;
import com.retrobazar.catalog.domain.Product;
import com.retrobazar.catalog.infrastructure.adapter.in.web.dto.CreateProductRequestDto;
import com.retrobazar.catalog.infrastructure.adapter.in.web.dto.ProductResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/products")
public class AdminProductController {

    private final ListAllProductsUseCase listAllProductsUseCase;
    private final CreateProductUseCase createProductUseCase;
    private final ActivateProductUseCase activateProductUseCase;
    private final DeactivateProductUseCase deactivateProductUseCase;

    public AdminProductController(
            ListAllProductsUseCase listAllProductsUseCase,
            CreateProductUseCase createProductUseCase,
            ActivateProductUseCase activateProductUseCase,
            DeactivateProductUseCase deactivateProductUseCase
    ) {
        this.listAllProductsUseCase = listAllProductsUseCase;
        this.createProductUseCase = createProductUseCase;
        this.activateProductUseCase = activateProductUseCase;
        this.deactivateProductUseCase = deactivateProductUseCase;
    }

    @GetMapping
    public List<ProductResponseDto> listAllProducts() {
        return listAllProductsUseCase.listAllProducts()
                .stream()
                .map(product -> ProductResponseDto.fromProduct(product))
                .toList();
    }

    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(
            @RequestBody CreateProductRequestDto request
    ) {
        CreateProductCommand command = request.toCommand();
        Product product = createProductUseCase.createProduct(command);
        ProductResponseDto productDto = ProductResponseDto.fromProduct(product);

        return ResponseEntity.status(HttpStatus.CREATED).body(productDto);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<ProductResponseDto> activateProduct(@PathVariable UUID id) {
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
