package com.retrobazar.catalog.infrastructure.adapter.out.persistence;

import com.retrobazar.catalog.domain.ProductCategory;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "products")
public class ProductJpaEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false, length = 2000)
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private int stock;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductCategory category;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "product_images",
            joinColumns = @JoinColumn(name = "product_id")
    )
    @OrderColumn(name = "image_order")
    private List<ProductImageJpaEmbeddable> images = new ArrayList<>();

    @Column(name = "wallapop_url", length = 1000)
    private String wallapopUrl;

    @Column(nullable = false)
    private boolean active;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    protected ProductJpaEntity() {
    }

    public ProductJpaEntity(
            UUID id,
            String name,
            String brand,
            String description,
            BigDecimal price,
            int stock,
            ProductCategory category,
            List<String> imageUrls,
            List<String> imageStorageKeys,
            boolean active,
            Instant createdAt
    ) {
        this(id, name, brand, description, price, stock, category, imageUrls,
                imageStorageKeys, null, active, createdAt);
    }

    public ProductJpaEntity(
            UUID id, String name, String brand, String description,
            BigDecimal price, int stock, ProductCategory category,
            List<String> imageUrls, List<String> imageStorageKeys,
            String wallapopUrl, boolean active, Instant createdAt
    ) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.category = category;
        this.images = new ArrayList<>();
        for (int index = 0; index < imageUrls.size(); index++) {
            this.images.add(new ProductImageJpaEmbeddable(
                    imageUrls.get(index),
                    imageStorageKeys.get(index)
            ));
        }
        this.wallapopUrl = wallapopUrl;
        this.active = active;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBrand() {
        return brand;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public List<String> getImageUrls() {
        return images.stream().map(ProductImageJpaEmbeddable::getUrl).toList();
    }

    public List<String> getImageStorageKeys() {
        return images.stream().map(ProductImageJpaEmbeddable::getStorageKey).toList();
    }

    public String getWallapopUrl() {
        return wallapopUrl;
    }

    public boolean isActive() {
        return active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
