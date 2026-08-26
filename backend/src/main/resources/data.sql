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
    'https://loremflickr.com/800/800/pixel,art,led?lock=101'
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
    'https://loremflickr.com/800/800/gameboy,retro,lamp?lock=102'
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
    'https://loremflickr.com/800/800/arcade,machine,retro?lock=103'
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
    'https://loremflickr.com/800/800/cassette,speaker,retro?lock=104'
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
    'https://loremflickr.com/800/800/desk,setup,keyboard?lock=105'
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
    'https://loremflickr.com/800/800/game,controller,retro?lock=106'
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
    'https://loremflickr.com/800/800/vhs,tape,retro?lock=107'
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
    'https://loremflickr.com/800/800/handheld,console,retro?lock=108'
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
    'https://loremflickr.com/800/800/mechanical,keyboard?lock=109'
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
    'https://loremflickr.com/800/800/gift,box,retro?lock=110'
WHERE NOT EXISTS (
    SELECT 1
    FROM product_images
    WHERE product_id = UNHEX(REPLACE('396445f9-b04e-4820-8a37-a1a6285a0552', '-', ''))
      AND image_order = 0
);

UPDATE product_images
SET image_url = CASE CONCAT(
        LOWER(HEX(product_id)),
        '-',
        image_order
    )
    WHEN '8d72ed6e76504b56bbaee017ee7609d1-0'
        THEN 'https://loremflickr.com/800/800/pixel,art,led?lock=101'
    WHEN '4a46398fb9a746f796e7b646427ef09d-0'
        THEN 'https://loremflickr.com/800/800/gameboy,retro,lamp?lock=102'
    WHEN '91f28f8142c94ca3abfb802be7f74be4-0'
        THEN 'https://loremflickr.com/800/800/arcade,machine,retro?lock=103'
    WHEN 'b52ddf54a7e548ce998710332ddebd87-0'
        THEN 'https://loremflickr.com/800/800/cassette,speaker,retro?lock=104'
    WHEN 'd211f3cb6ae24c749d8951c044173649-0'
        THEN 'https://loremflickr.com/800/800/desk,setup,keyboard?lock=105'
    WHEN '7e554451e8754b9fbf2b2afc81dd4874-0'
        THEN 'https://loremflickr.com/800/800/game,controller,retro?lock=106'
    WHEN '29939ced416b446a84560c3ab7409f2e-0'
        THEN 'https://loremflickr.com/800/800/vhs,tape,retro?lock=107'
    WHEN 'ed3986a047684114b6bd2acb66155361-0'
        THEN 'https://loremflickr.com/800/800/handheld,console,retro?lock=108'
    WHEN '590469f4955b428baa6907475aa7ab7f-0'
        THEN 'https://loremflickr.com/800/800/mechanical,keyboard?lock=109'
    WHEN '396445f9b04e48208a37a1a6285a0552-0'
        THEN 'https://loremflickr.com/800/800/gift,box,retro?lock=110'
    ELSE image_url
END
WHERE image_order = 0;

INSERT INTO product_images (product_id, image_order, image_url)
SELECT
    UNHEX(REPLACE('ed3986a0-4768-4114-b6bd-2acb66155361', '-', '')),
    1,
    'https://loremflickr.com/800/800/portable,gaming,console?lock=111'
WHERE NOT EXISTS (
    SELECT 1 FROM product_images
    WHERE product_id = UNHEX(REPLACE('ed3986a0-4768-4114-b6bd-2acb66155361', '-', ''))
      AND image_order = 1
);

INSERT INTO product_images (product_id, image_order, image_url)
SELECT
    UNHEX(REPLACE('ed3986a0-4768-4114-b6bd-2acb66155361', '-', '')),
    2,
    'https://loremflickr.com/800/800/retro,video,games?lock=112'
WHERE NOT EXISTS (
    SELECT 1 FROM product_images
    WHERE product_id = UNHEX(REPLACE('ed3986a0-4768-4114-b6bd-2acb66155361', '-', ''))
      AND image_order = 2
);

INSERT INTO product_images (product_id, image_order, image_url)
SELECT
    UNHEX(REPLACE('590469f4-955b-428b-aa69-07475aa7ab7f', '-', '')),
    1,
    'https://loremflickr.com/800/800/keyboard,computer,desk?lock=113'
WHERE NOT EXISTS (
    SELECT 1 FROM product_images
    WHERE product_id = UNHEX(REPLACE('590469f4-955b-428b-aa69-07475aa7ab7f', '-', ''))
      AND image_order = 1
);

INSERT INTO product_images (product_id, image_order, image_url)
SELECT
    UNHEX(REPLACE('590469f4-955b-428b-aa69-07475aa7ab7f', '-', '')),
    2,
    'https://loremflickr.com/800/800/keyboard,keys,technology?lock=114'
WHERE NOT EXISTS (
    SELECT 1 FROM product_images
    WHERE product_id = UNHEX(REPLACE('590469f4-955b-428b-aa69-07475aa7ab7f', '-', ''))
      AND image_order = 2
);

INSERT INTO product_images (product_id, image_order, image_url)
SELECT
    UNHEX(REPLACE('91f28f81-42c9-4ca3-abfb-802be7f74be4', '-', '')),
    1,
    'https://loremflickr.com/800/800/arcade,cabinet?lock=115'
WHERE NOT EXISTS (
    SELECT 1 FROM product_images
    WHERE product_id = UNHEX(REPLACE('91f28f81-42c9-4ca3-abfb-802be7f74be4', '-', ''))
      AND image_order = 1
);

INSERT INTO product_images (product_id, image_order, image_url)
SELECT
    UNHEX(REPLACE('91f28f81-42c9-4ca3-abfb-802be7f74be4', '-', '')),
    2,
    'https://loremflickr.com/800/800/video,arcade,games?lock=116'
WHERE NOT EXISTS (
    SELECT 1 FROM product_images
    WHERE product_id = UNHEX(REPLACE('91f28f81-42c9-4ca3-abfb-802be7f74be4', '-', ''))
      AND image_order = 2
);
