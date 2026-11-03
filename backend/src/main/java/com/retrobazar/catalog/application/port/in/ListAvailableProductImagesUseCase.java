package com.retrobazar.catalog.application.port.in;

import com.retrobazar.catalog.domain.StoredProductImage;

import java.util.List;

public interface ListAvailableProductImagesUseCase {

    List<StoredProductImage> listAvailable();
}
