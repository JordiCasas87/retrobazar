package com.retrobazar.catalog.infrastructure.adapter.in.web.dto;

import com.retrobazar.catalog.domain.StoredProductImage;

public record UploadedProductImageResponseDto(String url, String storageKey) {

    public static UploadedProductImageResponseDto from(StoredProductImage image) {
        return new UploadedProductImageResponseDto(image.url(), image.storageKey());
    }
}
