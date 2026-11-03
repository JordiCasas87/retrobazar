package com.retrobazar.catalog.infrastructure.adapter.out.media;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.retrobazar.catalog.application.port.out.ProductImageStoragePort;
import com.retrobazar.catalog.domain.StoredProductImage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.List;

@Component
public class CloudinaryProductImageStorageAdapter implements ProductImageStoragePort {

    private final Cloudinary cloudinary;
    private final String folder;

    public CloudinaryProductImageStorageAdapter(
            @Value("${app.cloudinary.cloud-name}") String cloudName,
            @Value("${app.cloudinary.api-key}") String apiKey,
            @Value("${app.cloudinary.api-secret}") String apiSecret,
            @Value("${app.cloudinary.folder}") String folder
    ) {
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret,
                "secure", true
        ));
        this.folder = folder;
    }

    @Override
    public StoredProductImage upload(
            byte[] content,
            String originalFilename,
            String contentType
    ) {
        try {
            Map<?, ?> result = cloudinary.uploader().upload(
                    content,
                    ObjectUtils.asMap(
                            "folder", folder,
                            "resource_type", "image",
                            "use_filename", true,
                            "unique_filename", true,
                            "overwrite", false
                    )
            );

            return new StoredProductImage(
                    requiredResult(result, "secure_url"),
                    requiredResult(result, "public_id")
            );
        } catch (IOException exception) {
            throw new IllegalStateException("Cloudinary image upload failed", exception);
        }
    }

    @Override
    public List<StoredProductImage> listAvailable() {
        try {
            Map<?, ?> result = cloudinary.search()
                    .expression("folder:\"" + folder.replace("\"", "\\\"") + "\"")
                    .sortBy("created_at", "desc")
                    .maxResults(100)
                    .execute();
            Object resourcesValue = result.get("resources");
            if (!(resourcesValue instanceof List<?> resources)) {
                return List.of();
            }

            return resources.stream()
                    .filter(Map.class::isInstance)
                    .map(Map.class::cast)
                    .map(resource -> new StoredProductImage(
                            requiredResult(resource, "secure_url"),
                            requiredResult(resource, "public_id")
                    ))
                    .toList();
        } catch (Exception exception) {
            throw new IllegalStateException("Cloudinary image listing failed", exception);
        }
    }

    private String requiredResult(Map<?, ?> result, String key) {
        Object value = result.get(key);
        if (value == null || value.toString().isBlank()) {
            throw new IllegalStateException("Cloudinary response is missing " + key);
        }
        return value.toString();
    }
}
