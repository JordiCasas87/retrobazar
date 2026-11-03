package com.retrobazar.catalog.domain;

public record StoredProductImage(String url, String storageKey) {

    public StoredProductImage {
        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException("image url cannot be blank");
        }
        if (storageKey == null || storageKey.isBlank()) {
            throw new IllegalArgumentException("image storage key cannot be blank");
        }
    }
}
