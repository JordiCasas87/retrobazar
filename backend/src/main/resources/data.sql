INSERT IGNORE INTO products (
    id,
    name,
    brand,
    description,
    price,
    stock,
    category,
    active,
    created_at
) VALUES
(
    UNHEX(REPLACE('8d72ed6e-7650-4b56-bbae-e017ee7609d1', '-', '')),
    'Pixoo 64',
    'Divoom',
    'Pantalla pixel art para decorar un setup retro.',
    129.99,
    8,
    'GADGETS',
    TRUE,
    '2026-08-04 10:00:00'
),
(
    UNHEX(REPLACE('ed3986a0-4768-4114-b6bd-2acb66155361', '-', '')),
    'RG35XX Plus',
    'Anbernic',
    'Consola portatil de estilo retro con pantalla IPS.',
    74.90,
    12,
    'GAMING',
    TRUE,
    '2026-08-04 10:05:00'
),
(
    UNHEX(REPLACE('590469f4-955b-428b-aa69-07475aa7ab7f', '-', '')),
    'Teclado mecanico K2',
    'Keychron',
    'Teclado mecanico compacto para completar un setup retro.',
    89.00,
    6,
    'SETUP_ACCESSORIES',
    FALSE,
    '2026-08-04 10:10:00'
),
(
    UNHEX(REPLACE('396445f9-b04e-4820-8a37-a1a6285a0552', '-', '')),
    'Retro Mystery Box',
    'Retro Bazar',
    'Caja sorpresa con una seleccion de articulos de inspiracion retro.',
    39.95,
    15,
    'OTHERS',
    TRUE,
    '2026-08-04 10:15:00'
);

INSERT INTO product_images (product_id, image_order, image_url)
SELECT
    UNHEX(REPLACE('8d72ed6e-7650-4b56-bbae-e017ee7609d1', '-', '')),
    0,
    'https://placehold.co/800x800?text=Pixoo+64'
WHERE NOT EXISTS (
    SELECT 1
    FROM product_images
    WHERE product_id = UNHEX(REPLACE('8d72ed6e-7650-4b56-bbae-e017ee7609d1', '-', ''))
      AND image_order = 0
);

INSERT INTO product_images (product_id, image_order, image_url)
SELECT
    UNHEX(REPLACE('ed3986a0-4768-4114-b6bd-2acb66155361', '-', '')),
    0,
    'https://placehold.co/800x800?text=RG35XX+Plus'
WHERE NOT EXISTS (
    SELECT 1
    FROM product_images
    WHERE product_id = UNHEX(REPLACE('ed3986a0-4768-4114-b6bd-2acb66155361', '-', ''))
      AND image_order = 0
);

INSERT INTO product_images (product_id, image_order, image_url)
SELECT
    UNHEX(REPLACE('590469f4-955b-428b-aa69-07475aa7ab7f', '-', '')),
    0,
    'https://placehold.co/800x800?text=Keychron+K2'
WHERE NOT EXISTS (
    SELECT 1
    FROM product_images
    WHERE product_id = UNHEX(REPLACE('590469f4-955b-428b-aa69-07475aa7ab7f', '-', ''))
      AND image_order = 0
);

INSERT INTO product_images (product_id, image_order, image_url)
SELECT
    UNHEX(REPLACE('396445f9-b04e-4820-8a37-a1a6285a0552', '-', '')),
    0,
    'https://placehold.co/800x800?text=Retro+Mystery+Box'
WHERE NOT EXISTS (
    SELECT 1
    FROM product_images
    WHERE product_id = UNHEX(REPLACE('396445f9-b04e-4820-8a37-a1a6285a0552', '-', ''))
      AND image_order = 0
);
