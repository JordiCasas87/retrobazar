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
),
(
    UNHEX(REPLACE('4a46398f-b9a7-46f7-96e7-b646427ef09d', '-', '')),
    'Game Boy Light Lamp',
    'RetroGlow',
    'Lampara LED inspirada en la consola portatil clasica.',
    34.99,
    20,
    'GADGETS',
    TRUE,
    '2026-08-04 10:20:00'
),
(
    UNHEX(REPLACE('91f28f81-42c9-4ca3-abfb-802be7f74be4', '-', '')),
    'Arcade Mini Station',
    'NeoPlay',
    'Mini recreativa de sobremesa con controles de estilo arcade.',
    99.90,
    7,
    'GAMING',
    TRUE,
    '2026-08-04 10:25:00'
),
(
    UNHEX(REPLACE('b52ddf54-a7e5-48ce-9987-10332ddebd87', '-', '')),
    'Cassette Bluetooth Speaker',
    'Rewind Audio',
    'Altavoz Bluetooth con diseño inspirado en una cinta de casete.',
    44.50,
    14,
    'GADGETS',
    TRUE,
    '2026-08-04 10:30:00'
),
(
    UNHEX(REPLACE('d211f3cb-6ae2-4c74-9d89-51c044173649', '-', '')),
    'Pixel Desk Mat',
    'Level Up',
    'Alfombrilla de escritorio extragrande con ilustraciones pixel art.',
    27.95,
    25,
    'SETUP_ACCESSORIES',
    TRUE,
    '2026-08-04 10:35:00'
),
(
    UNHEX(REPLACE('7e554451-e875-4b9f-bf2b-2afc81dd4874', '-', '')),
    'Retro Controller USB',
    'BitWave',
    'Mando USB de estilo clasico compatible con ordenador y Raspberry Pi.',
    24.90,
    18,
    'GAMING',
    TRUE,
    '2026-08-04 10:40:00'
),
(
    UNHEX(REPLACE('29939ced-416b-446a-8456-0c3ab7409f2e', '-', '')),
    'VHS Storage Box',
    'Tape Club',
    'Caja organizadora decorativa con apariencia de cinta VHS.',
    19.99,
    30,
    'OTHERS',
    TRUE,
    '2026-08-04 10:45:00'
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
    UNHEX(REPLACE('4a46398f-b9a7-46f7-96e7-b646427ef09d', '-', '')),
    0,
    'https://placehold.co/800x800?text=Game+Boy+Light+Lamp'
WHERE NOT EXISTS (
    SELECT 1
    FROM product_images
    WHERE product_id = UNHEX(REPLACE('4a46398f-b9a7-46f7-96e7-b646427ef09d', '-', ''))
      AND image_order = 0
);

INSERT INTO product_images (product_id, image_order, image_url)
SELECT
    UNHEX(REPLACE('91f28f81-42c9-4ca3-abfb-802be7f74be4', '-', '')),
    0,
    'https://placehold.co/800x800?text=Arcade+Mini+Station'
WHERE NOT EXISTS (
    SELECT 1
    FROM product_images
    WHERE product_id = UNHEX(REPLACE('91f28f81-42c9-4ca3-abfb-802be7f74be4', '-', ''))
      AND image_order = 0
);

INSERT INTO product_images (product_id, image_order, image_url)
SELECT
    UNHEX(REPLACE('b52ddf54-a7e5-48ce-9987-10332ddebd87', '-', '')),
    0,
    'https://placehold.co/800x800?text=Cassette+Bluetooth+Speaker'
WHERE NOT EXISTS (
    SELECT 1
    FROM product_images
    WHERE product_id = UNHEX(REPLACE('b52ddf54-a7e5-48ce-9987-10332ddebd87', '-', ''))
      AND image_order = 0
);

INSERT INTO product_images (product_id, image_order, image_url)
SELECT
    UNHEX(REPLACE('d211f3cb-6ae2-4c74-9d89-51c044173649', '-', '')),
    0,
    'https://placehold.co/800x800?text=Pixel+Desk+Mat'
WHERE NOT EXISTS (
    SELECT 1
    FROM product_images
    WHERE product_id = UNHEX(REPLACE('d211f3cb-6ae2-4c74-9d89-51c044173649', '-', ''))
      AND image_order = 0
);

INSERT INTO product_images (product_id, image_order, image_url)
SELECT
    UNHEX(REPLACE('7e554451-e875-4b9f-bf2b-2afc81dd4874', '-', '')),
    0,
    'https://placehold.co/800x800?text=Retro+Controller+USB'
WHERE NOT EXISTS (
    SELECT 1
    FROM product_images
    WHERE product_id = UNHEX(REPLACE('7e554451-e875-4b9f-bf2b-2afc81dd4874', '-', ''))
      AND image_order = 0
);

INSERT INTO product_images (product_id, image_order, image_url)
SELECT
    UNHEX(REPLACE('29939ced-416b-446a-8456-0c3ab7409f2e', '-', '')),
    0,
    'https://placehold.co/800x800?text=VHS+Storage+Box'
WHERE NOT EXISTS (
    SELECT 1
    FROM product_images
    WHERE product_id = UNHEX(REPLACE('29939ced-416b-446a-8456-0c3ab7409f2e', '-', ''))
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
