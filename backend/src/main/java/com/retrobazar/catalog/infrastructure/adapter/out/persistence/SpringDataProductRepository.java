package com.retrobazar.catalog.infrastructure.adapter.out.persistence;

import com.retrobazar.catalog.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpringDataProductRepository
        extends JpaRepository<ProductJpaEntity, UUID> {

    List<ProductJpaEntity> findByActiveTrue();
   Optional <ProductJpaEntity> findByID(UUID id);
}
