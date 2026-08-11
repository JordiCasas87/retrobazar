package com.retrobazar.catalog.infrastructure.adapter.out.persistence;

import com.retrobazar.catalog.application.port.out.ProductRepositoryPort;
import com.retrobazar.catalog.domain.Product;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    @Override
    public Product save(Product newProduct) {
        ProductJpaEntity productEntity = toEntity(newProduct);
        ProductJpaEntity savedProductEntity = springDataProductRepository.save(productEntity);
        Product savedProduct = toDomain(savedProductEntity);
        return savedProduct;
    }

    @Override
    public Optional <Product> findById(UUID id){
        Optional <ProductJpaEntity> savedProductEntity = springDataProductRepository.findByID(id);
        Optional <Product> product = savedProductEntity.map(this::toDomain);

        return product;
    }

    private ProductJpaEntity toEntity(Product product) {
        return new ProductJpaEntity(
                product.getId(),
                product.getName(),
                product.getBrand(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getCategory(),
                product.getImageUrls(),
                product.isActive(),
                product.getCreatedAt()
        );
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
