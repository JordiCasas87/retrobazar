package com.retrobazar.catalog.application.port.in;

import com.retrobazar.catalog.domain.StoredProductImage;

public interface UploadProductImageUseCase {

    StoredProductImage upload(byte[] content, String originalFilename, String contentType);
}
