package com.retrobazar.catalog.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Product {

    private final UUID id;
    private String name;
    private String brand;
    private String description;
    private BigDecimal price;
    private int stock;
    private ProductCategory category;
    private List<String> imageUrls;
    private boolean active;
    private final Instant createdAt;

    public Product(
            UUID id,
            String name,
            String brand,
            String description,
            BigDecimal price,
            int stock,
            ProductCategory category,
            List<String> imageUrls,
            boolean active,
            Instant createdAt
    ) {
        this.id = Objects.requireNonNull(id, "id cannot be null");
        this.name = requireText(name, "name");
        this.brand = requireText(brand, "brand");
        this.description = requireText(description, "description");
        this.price = requireNonNegativePrice(price);
        this.stock = requireNonNegativeStock(stock);
        this.category = Objects.requireNonNull(category, "category cannot be null");
        this.imageUrls = requireImages(imageUrls);
        this.active = active;
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt cannot be null");
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
        return imageUrls;
    }

    public boolean isActive() {
        return active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    private static String requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " cannot be blank");
        }
        return value;
    }

    private static BigDecimal requireNonNegativePrice(BigDecimal price) {
        Objects.requireNonNull(price, "price cannot be null");

        if (price.signum() < 0) {
            throw new IllegalArgumentException("price cannot be negative");
        }

        return price;
    }

    private static int requireNonNegativeStock(int stock) {
        if (stock < 0) {
            throw new IllegalArgumentException("stock cannot be negative");
        }

        return stock;
    }

    private static List<String> requireImages(List<String> imageUrls) {
        Objects.requireNonNull(imageUrls, "imageUrls cannot be null");

        if (imageUrls.isEmpty()) {
            throw new IllegalArgumentException("a product must have at least one image");
        }

        imageUrls.forEach(imageUrl -> requireText(imageUrl, "imageUrl"));
        return List.copyOf(imageUrls);
    }
}
