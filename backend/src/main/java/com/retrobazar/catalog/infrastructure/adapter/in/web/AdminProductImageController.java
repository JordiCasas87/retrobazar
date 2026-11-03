package com.retrobazar.catalog.infrastructure.adapter.in.web;

import com.retrobazar.catalog.application.port.in.UploadProductImageUseCase;
import com.retrobazar.catalog.application.port.in.ListAvailableProductImagesUseCase;
import com.retrobazar.catalog.domain.StoredProductImage;
import com.retrobazar.catalog.infrastructure.adapter.in.web.dto.UploadedProductImageResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/admin/product-images")
public class AdminProductImageController {

    private final UploadProductImageUseCase uploadProductImageUseCase;
    private final ListAvailableProductImagesUseCase listAvailableProductImagesUseCase;

    public AdminProductImageController(
            UploadProductImageUseCase uploadProductImageUseCase,
            ListAvailableProductImagesUseCase listAvailableProductImagesUseCase
    ) {
        this.uploadProductImageUseCase = uploadProductImageUseCase;
        this.listAvailableProductImagesUseCase = listAvailableProductImagesUseCase;
    }

    @GetMapping
    public List<UploadedProductImageResponseDto> listAvailable() {
        return listAvailableProductImagesUseCase.listAvailable()
                .stream()
                .map(UploadedProductImageResponseDto::from)
                .toList();
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UploadedProductImageResponseDto> upload(
            @RequestPart("file") MultipartFile file
    ) throws IOException {
        StoredProductImage image = uploadProductImageUseCase.upload(
                file.getBytes(),
                file.getOriginalFilename(),
                file.getContentType()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(UploadedProductImageResponseDto.from(image));
    }
}
