package com.retrobazar.catalog.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.Collections;

public class Product {

    private final UUID id;
    private String name;
    private String brand;
    private String description;
    private BigDecimal price;
    private int stock;
    private ProductCategory category;
    private List<String> imageUrls;
    private List<String> imageStorageKeys;
    private String wallapopUrl;
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
        this(
                id, name, brand, description, price, stock, category,
                imageUrls,
                imageUrls == null ? List.of() : Collections.nCopies(imageUrls.size(), null),
                null, active, createdAt
        );
    }

    public Product(
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

    public Product(
            UUID id,
            String name,
            String brand,
            String description,
            BigDecimal price,
            int stock,
            ProductCategory category,
            List<String> imageUrls,
            List<String> imageStorageKeys,
            String wallapopUrl,
            boolean active,
            Instant createdAt
    ) {
        this.id = Objects.requireNonNull(id, "id cannot be null");
        this.name = requireText(name, "name");
        this.brand = requireText(brand, "brand");
        this.description = requireText(description, "description");
        this.price = requirePositivePrice(price);
        this.stock = requireNonNegativeStock(stock);
        this.category = Objects.requireNonNull(category, "category cannot be null");
        this.imageUrls = requireImages(imageUrls);
        this.imageStorageKeys = requireStorageKeys(imageStorageKeys, this.imageUrls.size());
        this.wallapopUrl = requireWallapopUrl(wallapopUrl);
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

    public List<String> getImageStorageKeys() {
        return imageStorageKeys;
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

    public void updateDetails(
            String name,
            String brand,
            String description,
            BigDecimal price,
            int stock,
            ProductCategory category,
            List<String> imageUrls
    ) {
        updateDetails(
                name, brand, description, price, stock, category,
                imageUrls,
                imageUrls == null ? List.of() : Collections.nCopies(imageUrls.size(), null),
                wallapopUrl
        );
    }

    public void updateDetails(
            String name,
            String brand,
            String description,
            BigDecimal price,
            int stock,
            ProductCategory category,
            List<String> imageUrls,
            List<String> imageStorageKeys
    ) {
        updateDetails(name, brand, description, price, stock, category,
                imageUrls, imageStorageKeys, wallapopUrl);
    }

    public void updateDetails(
            String name,
            String brand,
            String description,
            BigDecimal price,
            int stock,
            ProductCategory category,
            List<String> imageUrls,
            List<String> imageStorageKeys,
            String wallapopUrl
    ) {
        String validName = requireText(name, "name");
        String validBrand = requireText(brand, "brand");
        String validDescription = requireText(description, "description");
        BigDecimal validPrice = requirePositivePrice(price);
        int validStock = requireNonNegativeStock(stock);
        ProductCategory validCategory = Objects.requireNonNull(
                category,
                "category cannot be null"
        );
        List<String> validImageUrls = requireImages(imageUrls);
        List<String> validStorageKeys = requireStorageKeys(
                imageStorageKeys,
                validImageUrls.size()
        );
        String validWallapopUrl = requireWallapopUrl(wallapopUrl);

        this.name = validName;
        this.brand = validBrand;
        this.description = validDescription;
        this.price = validPrice;
        this.stock = validStock;
        this.category = validCategory;
        this.imageUrls = validImageUrls;
        this.imageStorageKeys = validStorageKeys;
        this.wallapopUrl = validWallapopUrl;
    }

    private static String requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " cannot be blank");
        }
        return value;
    }

    private static BigDecimal requirePositivePrice(BigDecimal price) {
        Objects.requireNonNull(price, "price cannot be null");

        if (price.signum() <= 0) {
            throw new IllegalArgumentException("price must be greater than zero");
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

    private static List<String> requireStorageKeys(List<String> storageKeys, int imageCount) {
        Objects.requireNonNull(storageKeys, "imageStorageKeys cannot be null");
        if (storageKeys.size() != imageCount) {
            throw new IllegalArgumentException("each image must have a matching storage key");
        }
        return Collections.unmodifiableList(storageKeys.stream()
                .map(key -> key == null || key.isBlank() ? null : key)
                .toList());
    }

    private static String requireWallapopUrl(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        try {
            URI uri = URI.create(value.trim());
            String host = uri.getHost();
            if (!"https".equalsIgnoreCase(uri.getScheme())
                    || host == null
                    || !(host.equalsIgnoreCase("wallapop.com")
                    || host.toLowerCase().endsWith(".wallapop.com"))) {
                throw new IllegalArgumentException("wallapopUrl must be a valid HTTPS Wallapop URL");
            }
            return uri.toString();
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException("wallapopUrl must be a valid HTTPS Wallapop URL");
        }
    }

    //métodos de active

    public void activate() {
        this.active = true;
    }

    public void deactivate() {
        this.active = false;
    }


}
