package com.retrobazar.catalog.application.service;

import com.retrobazar.catalog.application.port.in.UploadProductImageUseCase;
import com.retrobazar.catalog.application.port.out.ProductImageStoragePort;
import com.retrobazar.catalog.domain.StoredProductImage;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UploadProductImageService implements UploadProductImageUseCase {

    private static final long MAX_IMAGE_SIZE = 10 * 1024 * 1024;
    private static final Set<String> ALLOWED_TYPES = Set.of(
            "image/jpeg", "image/png", "image/webp", "image/avif"
    );

    private final ProductImageStoragePort imageStoragePort;

    public UploadProductImageService(ProductImageStoragePort imageStoragePort) {
        this.imageStoragePort = imageStoragePort;
    }

    @Override
    public StoredProductImage upload(byte[] content, String originalFilename, String contentType) {
        if (content == null || content.length == 0) {
            throw new IllegalArgumentException("image file cannot be empty");
        }
        if (content.length > MAX_IMAGE_SIZE) {
            throw new IllegalArgumentException("image file cannot exceed 10 MB");
        }
        if (contentType == null || !ALLOWED_TYPES.contains(contentType.toLowerCase())) {
            throw new IllegalArgumentException("image format must be JPEG, PNG, WebP or AVIF");
        }

        return imageStoragePort.upload(content, originalFilename, contentType);
    }
}
