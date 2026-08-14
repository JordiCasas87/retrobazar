package com.retrobazar.catalog.infrastructure.adapter.out.persistence;

import com.retrobazar.catalog.application.port.out.ProductRepositoryPort;
import com.retrobazar.catalog.domain.Product;
import com.retrobazar.catalog.domain.ProductCategory;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

import jakarta.persistence.criteria.Predicate;

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
    public List<Product> findAll() {
        return springDataProductRepository.findAll()
                .stream()
                .map(productEntity -> toDomain(productEntity))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findAllActive() {
        return springDataProductRepository.findByActiveTrue()
                .stream()
                .map(productEntity -> toDomain(productEntity))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findAllActiveByCategory(ProductCategory category) {
        return springDataProductRepository.findByCategoryAndActiveTrue(category)
                .stream()
                .map(productEntity -> toDomain(productEntity))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> searchActiveProducts(List<String> words) {
        Specification<ProductJpaEntity> specification = (root, query, criteriaBuilder) -> {
            Predicate searchConditions = criteriaBuilder.isTrue(root.get("active"));

            for (String word : words) {
                String pattern = "%" + word.toLowerCase(Locale.ROOT) + "%";

                Predicate wordCondition = criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), pattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("brand")), pattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), pattern)
                );

                searchConditions = criteriaBuilder.and(searchConditions, wordCondition);
            }

            return searchConditions;
        };

        return springDataProductRepository.findAll(specification)
                .stream()
                .map(productEntity -> toDomain(productEntity))
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
    @Transactional(readOnly = true)
    public Optional <Product> findById(UUID id){
        Optional <ProductJpaEntity> savedProductEntity = springDataProductRepository.findById(id);
        Optional <Product> product = savedProductEntity
                .map(productEntity -> toDomain(productEntity));

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
