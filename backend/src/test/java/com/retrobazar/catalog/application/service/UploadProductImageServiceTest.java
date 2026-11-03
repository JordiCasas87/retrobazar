package com.retrobazar.catalog.application.service;

import com.retrobazar.catalog.application.port.out.ProductImageStoragePort;
import com.retrobazar.catalog.domain.StoredProductImage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UploadProductImageServiceTest {

    private final ProductImageStoragePort storage = new ProductImageStoragePort() {
        @Override
        public StoredProductImage upload(byte[] content, String filename, String contentType) {
            return new StoredProductImage(
                    "https://res.cloudinary.com/retro-bazar/image/upload/example.webp",
                    "retro-bazar/products/example"
            );
        }

        @Override
        public java.util.List<StoredProductImage> listAvailable() {
            return java.util.List.of();
        }
    };
    private final UploadProductImageService service = new UploadProductImageService(storage);

    @Test
    void shouldUploadAValidProductImage() {
        StoredProductImage image = service.upload(
                new byte[]{1, 2, 3},
                "gameboy.webp",
                "image/webp"
        );

        assertEquals("retro-bazar/products/example", image.storageKey());
    }

    @Test
    void shouldRejectAnEmptyImage() {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.upload(new byte[0], "empty.jpg", "image/jpeg")
        );
    }

    @Test
    void shouldRejectAnUnsupportedFormat() {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.upload(new byte[]{1}, "image.gif", "image/gif")
        );
    }
}
