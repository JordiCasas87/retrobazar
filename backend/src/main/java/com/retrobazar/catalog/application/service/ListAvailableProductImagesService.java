package com.retrobazar.catalog.application.service;

import com.retrobazar.catalog.application.port.in.ListAvailableProductImagesUseCase;
import com.retrobazar.catalog.application.port.out.ProductImageStoragePort;
import com.retrobazar.catalog.domain.StoredProductImage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListAvailableProductImagesService implements ListAvailableProductImagesUseCase {

    private final ProductImageStoragePort imageStoragePort;

    public ListAvailableProductImagesService(ProductImageStoragePort imageStoragePort) {
        this.imageStoragePort = imageStoragePort;
    }

    @Override
    public List<StoredProductImage> listAvailable() {
        return imageStoragePort.listAvailable();
    }
}
