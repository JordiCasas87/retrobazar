<div align="center">
  <img src="logoRB.png" alt="Logotipo de Retro Bazar" width="320">

  # Retro Bazar

  **E-commerce de productos retro, gaming, gadgets y accesorios para setups.**
</div>

## Estado del proyecto

Retro Bazar es un monolito modular en desarrollo. La primera versión funcional
del catálogo ya permite consultar productos reales desde una interfaz Angular
conectada a una API REST desarrollada con Spring Boot.

El repositorio contiene actualmente:

- Un catálogo público con listado, búsqueda, filtros por categoría y detalle.
- Gestión administrativa de productos mediante la API.
- Activación, desactivación y eliminación de productos.
- Validación de peticiones y respuestas de error estructuradas.
- Una interfaz responsive preparada como MVP visual.
- Diez productos de demostración distribuidos en cuatro categorías.

Usuarios, autenticación, carrito, pedidos y pagos quedan fuera de esta primera
versión.

## Tecnologías

### Backend

- Java 21.
- Spring Boot 4.1.
- Spring Web MVC.
- Spring Data JPA y Hibernate.
- Spring Security.
- Bean Validation.
- MySQL.
- Springdoc OpenAPI y Swagger UI.
- Maven.
- JUnit, Mockito y Testcontainers.

### Frontend

- Angular 20.
- TypeScript.
- Angular Router.
- HttpClient, Signals y RxJS.
- HTML y CSS responsive.

## Arquitectura

El backend utiliza arquitectura hexagonal dentro de un monolito modular. El
módulo `catalog` se divide en:

- **Dominio:** entidades y reglas del negocio independientes de Spring.
- **Aplicación:** casos de uso, comandos y puertos de entrada y salida.
- **Infraestructura:** controladores REST, persistencia, seguridad y
  configuración externa.

La infraestructura web común contiene el gestor global que transforma las
excepciones de aplicación y los errores HTTP en respuestas consistentes.

```text
retro-bazar/
├── backend/
│   └── src/
│       ├── main/java/com/retrobazar/
│       │   ├── catalog/
│       │   │   ├── domain/
│       │   │   ├── application/
│       │   │   └── infrastructure/
│       │   └── infrastructure/web/error/
│       └── test/
├── frontend/
│   └── src/app/
└── README.md
```

## Requisitos para desarrollo local

- JDK 21.
- Docker Desktop (recomendado para ejecutar MySQL 8.4) o una instalación local de MySQL 8.
- Node.js y npm.

Docker Compose se utiliza únicamente para la base de datos de desarrollo. El
backend y el frontend se siguen ejecutando directamente desde la terminal o el
IDE. No se han creado imágenes Docker para las aplicaciones.

## Configuración del backend

El backend reconoce estas variables de entorno:

| Variable | Valor local por defecto | Descripción |
|---|---|---|
| `DB_URL` | `jdbc:mysql://localhost:3307/retro_bazar?...` | Conexión con MySQL de Docker |
| `DB_USERNAME` | `root` | Usuario de la base de datos |
| `DB_PASSWORD` | `retro_bazar_local` | Contraseña local de la base de datos |
| `FRONTEND_ORIGIN` | `http://localhost:4200` | Origen permitido por CORS |

[`backend/.env.example`](backend/.env.example) documenta estas variables sin
incluir credenciales reales. Spring Boot no carga archivos `.env`
automáticamente: las variables deben configurarse en IntelliJ, en la terminal o
mediante la herramienta utilizada para ejecutar la aplicación.

Con Docker Desktop iniciado, levantar MySQL:

```bash
docker compose up -d
```

El contenedor crea la base de datos `retro_bazar`, escucha únicamente en
`localhost:3307` y conserva los datos en un volumen. Se utiliza el puerto 3307
para no interferir con una posible instalación local de MySQL en el 3306. Para
consultar su estado o detenerlo:

```bash
docker compose ps
docker compose down
```

`docker compose down` no elimina los datos. Solo se borran explícitamente con
`docker compose down --volumes`.

Si se utiliza una instalación local de MySQL en lugar de Docker, crear la base
de datos manualmente:

```sql
CREATE DATABASE retro_bazar;
```

Arrancar el backend:

```bash
cd backend
./mvnw spring-boot:run
```

La API se sirve en `http://localhost:8080`.

## Ejecución del frontend

Con el backend iniciado:

```bash
cd frontend
npm install
npm start
```

La aplicación se sirve en `http://localhost:4200`.

## API del catálogo

### Endpoints públicos

| Acción | Método | Endpoint |
|---|---|---|
| Listar productos activos | `GET` | `/api/products` |
| Filtrar por categoría | `GET` | `/api/products/category/{category}` |
| Buscar productos | `GET` | `/api/products/search?text={text}` |
| Consultar un producto activo | `GET` | `/api/products/{id}` |

### Endpoints administrativos

| Acción | Método | Endpoint |
|---|---|---|
| Listar todos los productos | `GET` | `/api/admin/products` |
| Consultar un producto | `GET` | `/api/admin/products/{id}` |
| Crear un producto | `POST` | `/api/admin/products` |
| Actualizar un producto | `PUT` | `/api/admin/products/{id}` |
| Eliminar un producto | `DELETE` | `/api/admin/products/{id}` |
| Activar un producto | `PATCH` | `/api/admin/products/{id}/activate` |
| Desactivar un producto | `PATCH` | `/api/admin/products/{id}/deactivate` |

La documentación interactiva está disponible con el backend iniciado en:

```text
http://localhost:8080/swagger-ui/index.html
```

Los endpoints administrativos permanecen temporalmente abiertos hasta que se
implemente el módulo de usuarios y autorización por roles.

## Pruebas

Ejecutar las pruebas del backend:

```bash
cd backend
./mvnw test
```

Las pruebas unitarias no requieren Docker. La prueba de carga completa del
contexto utiliza Testcontainers y necesita un entorno Docker disponible. Esta
infraestructura se completará en una fase posterior. Para el desarrollo habitual,
la aplicación se ejecuta contra el MySQL levantado mediante Docker Compose.

Comprobar el frontend:

```bash
cd frontend
npm run build
```

## Agente de IA para el alta de productos

La primera incorporación de IA prevista será un agente integrado en el flujo
administrativo de creación de productos. Su objetivo será preparar una ficha de
producto coherente, detectar posibles duplicados y reducir el trabajo manual sin
tomar decisiones irreversibles.

La implementación utilizará Spring AI en el backend y tendrá inicialmente a
Gemini como proveedor del modelo. No será una única petición para generar texto:
el agente recibirá un objetivo, decidirá qué herramientas autorizadas necesita y
podrá utilizarlas en varias etapas antes de producir el resultado.

### Flujo previsto

1. El administrador aporta fotografías y los datos disponibles del producto.
2. El agente analiza la información recibida y determina qué consultas necesita.
3. Utiliza herramientas internas para buscar coincidencias, consultar detalles y
   comparar precios del catálogo.
4. Advierte si encuentra un producto igual o suficientemente similar.
5. Devuelve un borrador estructurado con nombre, categoría, descripción y precio
   orientativo, acompañado de sus advertencias y referencias.
6. El administrador revisa, modifica y confirma el borrador mediante el flujo
   normal de creación de productos.

### Herramientas iniciales

- `searchCatalog`: busca coincidencias por nombre, marca o descripción.
- `getProductDetails`: obtiene la información completa de un producto encontrado.
- `getCategoryPriceStatistics`: calcula el rango y el promedio de precios de una
  categoría utilizando únicamente datos del catálogo.

Estas herramientas serán métodos controlados del backend, conectados con los
casos de uso existentes. El agente no tendrá acceso a SQL libre ni a operaciones
arbitrarias sobre la base de datos.

### Límites y trazabilidad

- El agente no podrá crear, modificar ni eliminar productos directamente.
- Toda propuesta requerirá confirmación humana antes de persistirse.
- La respuesta tendrá una estructura validable en lugar de texto libre.
- Se conservarán los pasos, herramientas utilizadas y resultados relevantes de
  cada ejecución para facilitar pruebas, diagnóstico y explicación.
- Las sugerencias de precio se identificarán como orientativas y se basarán en
  los datos disponibles, sin presentarse como precios de mercado verificados.

Una primera versión se considerará agéntica cuando el modelo pueda seleccionar
de forma controlada qué herramientas utilizar y en qué orden según el producto,
en lugar de ejecutar siempre una secuencia fija programada.

## Hoja de ruta

1. **Estructura del frontend — completado.** La portada vive en un
   `HomeComponent` enrutado y `AppComponent` conserva únicamente el layout y la
   interacción global.
2. **Catálogo público.** Crear `/catalogo` con búsqueda, filtros por categoría y
   paginación inicial de diez productos en Angular. La portada mantendrá solo una
   selección reducida y enlazará al catálogo completo.
3. **Administración abierta de demostración.** Crear el listado administrativo y
   las pantallas de alta, edición, activación y eliminación utilizando los
   endpoints actuales. El acceso se identificará como `Administración · Demo`.
4. **Catálogo propio.** Preparar fotografías cuadradas, optimizarlas a WebP y
   guardarlas en `frontend/src/assets/products/`. Sustituir después en `data.sql`
   las imágenes y descripciones de demostración.
5. **Agente de IA.** Integrar en el alta de productos el agente definido en la
   sección anterior, con análisis de imágenes, herramientas del catálogo,
   detección de duplicados, propuesta editable y confirmación humana.
6. **Autenticación y autorización.** Añadir usuarios, login y roles; proteger las
   rutas administrativas de Angular y los endpoints `/api/admin/**`.
7. **Evolución del e-commerce.** Incorporar carrito y pedidos cuando el catálogo y
   la administración estén consolidados.
8. **Preparación para publicación.** Configurar los entornos de Angular, añadir
   Dockerfiles para backend y frontend, ampliar Docker Compose y preparar el
   despliegue público.

## Alcance

El proyecto es una demostración técnica y no procesa pagos ni envíos reales.
