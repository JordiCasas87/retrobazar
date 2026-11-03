package com.retrobazar.catalog.application.port.out;

import com.retrobazar.catalog.domain.StoredProductImage;

import java.util.List;

public interface ProductImageStoragePort {

    StoredProductImage upload(byte[] content, String originalFilename, String contentType);

    List<StoredProductImage> listAvailable();
}
