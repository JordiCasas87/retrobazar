package com.retrobazar.catalog.infrastructure.adapter.in.web;

import com.retrobazar.catalog.application.command.CreateProductCommand;
import com.retrobazar.catalog.application.command.UpdateProductCommand;
import com.retrobazar.catalog.application.port.in.ActivateProductUseCase;
import com.retrobazar.catalog.application.port.in.CreateProductUseCase;
import com.retrobazar.catalog.application.port.in.DeactivateProductUseCase;
import com.retrobazar.catalog.application.port.in.DeleteProductUseCase;
import com.retrobazar.catalog.application.port.in.GetProductByIdUseCase;
import com.retrobazar.catalog.application.port.in.ListAllProductsUseCase;
import com.retrobazar.catalog.application.port.in.UpdateProductUseCase;
import com.retrobazar.catalog.domain.Product;
import com.retrobazar.catalog.infrastructure.adapter.in.web.dto.CreateProductRequestDto;
import com.retrobazar.catalog.infrastructure.adapter.in.web.dto.ProductResponseDto;
import com.retrobazar.catalog.infrastructure.adapter.in.web.dto.UpdateProductRequestDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
    private final GetProductByIdUseCase getProductByIdUseCase;
    private final UpdateProductUseCase updateProductUseCase;
    private final DeleteProductUseCase deleteProductUseCase;

    public AdminProductController(
            ListAllProductsUseCase listAllProductsUseCase,
            CreateProductUseCase createProductUseCase,
            ActivateProductUseCase activateProductUseCase,
            DeactivateProductUseCase deactivateProductUseCase,
            GetProductByIdUseCase getProductByIdUseCase,
            UpdateProductUseCase updateProductUseCase,
            DeleteProductUseCase deleteProductUseCase
    ) {
        this.listAllProductsUseCase = listAllProductsUseCase;
        this.createProductUseCase = createProductUseCase;
        this.activateProductUseCase = activateProductUseCase;
        this.deactivateProductUseCase = deactivateProductUseCase;
        this.getProductByIdUseCase = getProductByIdUseCase;
        this.updateProductUseCase = updateProductUseCase;
        this.deleteProductUseCase = deleteProductUseCase;
    }

    @GetMapping
    public List<ProductResponseDto> listAllProducts() {
        return listAllProductsUseCase.listAllProducts()
                .stream()
                .map(product -> ProductResponseDto.fromProduct(product))
                .toList();
    }

    @GetMapping("/{id}")
    public ProductResponseDto getProductById(@PathVariable UUID id) {
        Product product = getProductByIdUseCase.getById(id);

        return ProductResponseDto.fromProduct(product);
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

    @PutMapping("/{id}")
    public ProductResponseDto updateProduct(
            @PathVariable UUID id,
            @RequestBody UpdateProductRequestDto request
    ) {
        UpdateProductCommand command = request.toCommand();
        Product product = updateProductUseCase.update(id, command);

        return ProductResponseDto.fromProduct(product);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        deleteProductUseCase.delete(id);

        return ResponseEntity.noContent().build();
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
