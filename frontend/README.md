# Retro Bazar Frontend

Frontend de la tienda online Retro Bazar, orientada a gadgets tecnológicos, productos retro, gaming y accesorios para setups.

Este proyecto se construirá con **Angular + Vite** y consumirá la API REST desarrollada en `backend` con Spring Boot.

## Objetivo del MVP visual

El primer MVP debe presentar una experiencia de tienda moderna, responsive y suficientemente completa para mostrar el proyecto en un portfolio. La prioridad es que el catálogo sea usable, visualmente consistente y esté conectado con datos reales del backend.

El MVP incluirá:

- Cabecera con logotipo, buscador, navegación y accesos de cuenta y carrito.
- Sección principal con una identidad visual retro-futurista.
- Categorías destacadas.
- Cuadrícula de productos obtenidos desde el backend.
- Tarjetas con imagen, nombre, marca, precio y stock.
- Filtros y ordenación visibles.
- Acceso al detalle de cada producto.
- Footer completo con navegación y enlaces informativos.
- Estados de carga, catálogo vacío y error de conexión.
- Diseño adaptado a escritorio, tablet y móvil.

## Stack tecnológico

- **Angular** con componentes standalone.
- **Vite** como servidor de desarrollo y herramienta de build.
- **TypeScript** para el código de aplicación.
- **Angular Router** para la navegación entre vistas.
- **HttpClient** para consumir la API REST.
- **Signals** para el estado local del catálogo y del carrito.
- **RxJS** para gestionar las peticiones HTTP y sus estados asíncronos.
- **Tailwind CSS** para acelerar la construcción de la interfaz visual.
- **Angular CDK** para overlays, diálogos y patrones accesibles cuando sean necesarios.
- **Lucide Angular** para iconos consistentes.
- **Vitest** para pruebas unitarias.
- **Playwright** para pruebas end-to-end.

No se usará Angular Material como sistema visual principal. La tienda tendrá componentes propios para conservar una identidad diferenciada y evitar una apariencia de panel administrativo.

## Dirección visual

La interfaz seguirá una línea **retro-futurista, tecnológica y editorial**, evitando una tienda genérica basada únicamente en tarjetas.

Principios visuales:

- Fondo oscuro con textura o patrón sutil para crear atmósfera.
- Acentos en naranja cálido, cian y verde lima usados con moderación.
- Tipografía de títulos expresiva y tipografía de lectura muy legible.
- Contraste alto, jerarquía clara y controles fáciles de localizar.
- Bordes y radios contenidos, sin abusar de elementos redondeados.
- Animaciones breves para entrada de contenido, cambios de filtros y estados de interacción.
- Imágenes de producto visibles y con proporciones estables.
- La estética debe transmitir colección, descubrimiento y cultura retro sin perjudicar la lectura.

La dirección visual se mantendrá en variables de diseño para poder ajustar colores, espaciado, tipografía y sombras desde un punto central.

## Arquitectura prevista

La aplicación se organizará por funcionalidades, manteniendo separadas la presentación, el acceso a datos y el estado local:

```text
src/app/
├── core/
│   ├── config/          # Configuración de API y entorno
│   ├── models/          # Interfaces y tipos compartidos
│   └── services/        # Servicios HTTP y servicios globales
├── shared/
│   ├── components/      # Componentes reutilizables
│   └── ui/              # Botones, estados, iconos y elementos visuales
├── catalog/
│   ├── pages/           # Catálogo y detalle de producto
│   ├── components/      # Tarjeta, filtros y categorías
│   └── data-access/     # Consultas y estado del catálogo
├── cart/
│   ├── pages/           # Vista del carrito
│   └── data-access/     # Estado y operaciones del carrito
└── app.routes.ts        # Rutas de la aplicación
```

La lógica de negocio del catálogo no se duplicará en los componentes visuales. Los componentes recibirán datos y emitirán eventos; los servicios y stores locales coordinarán las peticiones y el estado.

## Integración con el backend

La API pública del catálogo se encuentra en:

```text
http://localhost:8080/api/products
```

Endpoints disponibles actualmente:

| Acción | Método | Endpoint |
|---|---|---|
| Listar productos activos | `GET` | `/api/products` |
| Filtrar por categoría | `GET` | `/api/products/category/{category}` |
| Buscar productos | `GET` | `/api/products/search?text={text}` |
| Consultar producto | `GET` | `/api/products/{id}` |

Las categorías actuales del backend son:

- `GADGETS`
- `GAMING`
- `SETUP_ACCESSORIES`
- `OTHERS`

El modelo de producto contiene `id`, `name`, `brand`, `description`, `price`, `stock`, `category`, `imageUrls`, `active` y `createdAt`.

La URL de la API debe configurarse mediante entornos de Angular y no escribirse directamente dentro de los componentes.

## Estados de interfaz

Cada vista que dependa del backend debe contemplar explícitamente estos estados:

### Carga

- Mostrar una estructura de carga estable para evitar saltos de diseño.
- Mantener la jerarquía visual de la cuadrícula mientras llegan los productos.
- Evitar mostrar mensajes de error durante una petición que sigue activa.

### Catálogo vacío

- Informar claramente de que no hay productos disponibles para la selección actual.
- Permitir volver al catálogo completo o limpiar los filtros.
- No mostrar una cuadrícula vacía sin contexto.

### Error de conexión

- Mostrar un mensaje comprensible y visible.
- Incluir una acción para reintentar la petición.
- Mantener la navegación disponible aunque el backend no responda.
- No terminar en rutas rotas ni dejar errores técnicos sin explicar al usuario.

### Interacciones en desarrollo

Las funciones aún no implementadas deben comunicarse de forma clara:

- Desactivar controles cuando la acción no esté disponible.
- Usar la etiqueta `Próximamente` en funciones planificadas.
- Mostrar un aviso breve si el usuario pulsa una acción todavía no disponible.
- Evitar enlaces que apunten a páginas inexistentes.
- No simular confirmaciones de compra, autenticación o pedidos como si ya fueran reales.

## Funcionalidades del MVP

### Terminadas en la primera fase

- [x] Estructura base Angular.
- [x] Layout responsive.
- [x] Cabecera y footer.
- [x] Sección principal y categorías destacadas.
- [x] Consumo real de `GET /api/products`.
- [x] Cuadrícula y tarjetas de producto.
- [x] Buscador conectado al backend.
- [x] Filtro por categoría.
- [ ] Ordenación visual del catálogo.
- [x] Vista de detalle de producto.
- [x] Estados de carga, vacío y error.

### Demostrativas o pendientes

- [ ] Carrito persistido en el navegador.
- [ ] Registro e inicio de sesión.
- [ ] Cuenta de cliente.
- [ ] Creación de pedidos.
- [ ] Panel de administración.
- [ ] Pago y envío reales.
- [ ] Favoritos.
- [ ] Filtros avanzados por precio, stock o marca.

Las funciones pendientes deben conservar el lenguaje visual del producto, pero nunca deben generar expectativas falsas sobre su disponibilidad.

## Rutas iniciales

| Ruta | Estado | Descripción |
|---|---|---|
| `/` | MVP | Página principal y catálogo destacado |
| `/catalogo` | MVP | Catálogo completo con búsqueda y filtros |
| `/producto/:id` | MVP | Detalle de un producto |
| `/carrito` | En desarrollo | Preparada para el carrito local |
| `/cuenta` | Próximamente | Acceso de cuenta deshabilitado hasta implementar autenticación |

## Criterios de calidad

Para que el frontend resulte convincente como portfolio se priorizará:

1. Responsive real en escritorio, tablet y móvil.
2. Datos reales consumidos desde el backend.
3. Dirección visual consistente en todas las vistas.
4. Estados de carga, vacío y error bien resueltos.
5. Accesibilidad básica: foco visible, botones con nombres claros, contraste suficiente y navegación por teclado.
6. Rendimiento razonable: imágenes controladas, componentes pequeños y peticiones evitables minimizadas.
7. Código organizado por funcionalidades y fácil de ampliar.
8. README actualizado con capturas, arquitectura y estado del proyecto.
9. Pruebas de los flujos principales antes del despliegue.
10. Despliegue público del frontend y backend cuando el MVP sea estable.

## Desarrollo local

Requisitos previstos:

- Node.js LTS.
- npm.
- Backend ejecutándose en `http://localhost:8080`.

Comandos previstos:

```bash
cd frontend
npm install
npm start
```

La aplicación se servirá inicialmente en `http://localhost:4200`.

Para validar el frontend antes de publicar:

```bash
npm run build
npm test
npm run e2e
```

Los nombres exactos de los scripts se ajustarán durante la inicialización del proyecto Angular.

## Despliegue

El despliegue se realizará en dos piezas independientes:

- Frontend Angular generado como archivos estáticos.
- Backend Spring Boot desplegado como API pública.

Antes de desplegar se deberán configurar:

- URL pública de la API.
- CORS en el backend.
- Variables de entorno de producción.
- Gestión de imágenes mediante las URLs proporcionadas por Cloudinary.
- Dominio, HTTPS y estrategia de caché.

El despliegue público se considera parte de la fase de estabilización, no del primer scaffolding local.
