package com.retrobazar.catalog.infrastructure.adapter.out.persistence;

import com.retrobazar.catalog.domain.ProductCategory;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductJpaEntityTest {

    @Test
    void shouldKeepTheWallapopUrlInThePersistenceEntity() {
        ProductJpaEntity entity = new ProductJpaEntity(
                UUID.randomUUID(), "Game Boy Color", "Nintendo", "Consola retro",
                new BigDecimal("89.90"), 1, ProductCategory.GAMING,
                List.of("https://example.com/product.jpg"),
                Collections.singletonList(null),
                "https://es.wallapop.com/item/pokemon-edicion-azul-game-boy-pal-alemania-1320387850",
                true, Instant.parse("2026-11-04T18:00:00Z")
        );

        assertEquals(
                "https://es.wallapop.com/item/pokemon-edicion-azul-game-boy-pal-alemania-1320387850",
                entity.getWallapopUrl()
        );
    }
}
