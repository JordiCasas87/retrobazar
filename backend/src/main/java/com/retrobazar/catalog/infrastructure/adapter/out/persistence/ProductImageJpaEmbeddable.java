package com.retrobazar.catalog.infrastructure.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class ProductImageJpaEmbeddable {

    @Column(name = "image_url", nullable = false, length = 1000)
    private String url;

    @Column(name = "storage_key", length = 500)
    private String storageKey;

    protected ProductImageJpaEmbeddable() {
    }

    public ProductImageJpaEmbeddable(String url, String storageKey) {
        this.url = url;
        this.storageKey = storageKey;
    }

    public String getUrl() {
        return url;
    }

    public String getStorageKey() {
        return storageKey;
    }
}
