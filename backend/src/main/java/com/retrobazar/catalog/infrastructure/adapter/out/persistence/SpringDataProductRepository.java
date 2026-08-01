package com.retrobazar.catalog.infrastructure.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SpringDataProductRepository
        extends JpaRepository<ProductJpaEntity, UUID> {

    List<ProductJpaEntity> findByActiveTrue();
}
