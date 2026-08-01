package com.retrobazar.catalog.infrastructure.adapter.out.persistence;

import com.retrobazar.catalog.application.port.out.ProductRepositoryPort;
import com.retrobazar.catalog.domain.Product;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class ProductPersistenceAdapter implements ProductRepositoryPort {

    private final SpringDataProductRepository springDataProductRepository;

    public ProductPersistenceAdapter(
            SpringDataProductRepository springDataProductRepository
    ) {
        this.springDataProductRepository = springDataProductRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findAllActive() {
        return springDataProductRepository.findByActiveTrue()
                .stream()
                .map(this::toDomain)
                .toList();
    }

    private Product toDomain(ProductJpaEntity entity) {
        return new Product(
                entity.getId(),
                entity.getName(),
                entity.getBrand(),
                entity.getDescription(),
                entity.getPrice(),
                entity.getStock(),
                entity.getCategory(),
                entity.getImageUrls(),
                entity.isActive(),
                entity.getCreatedAt()
        );
    }
}
