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
- MySQL 8.
- Node.js y npm.

Docker no es necesario actualmente. Se añadirá más adelante junto con Docker
Compose para facilitar el arranque reproducible de MySQL, backend y frontend.

## Configuración del backend

El backend reconoce estas variables de entorno:

| Variable | Valor local por defecto | Descripción |
|---|---|---|
| `DB_URL` | `jdbc:mysql://localhost:3306/retro_bazar?...` | Conexión con MySQL |
| `DB_USERNAME` | `root` | Usuario de la base de datos |
| `DB_PASSWORD` | vacío | Contraseña de la base de datos |
| `FRONTEND_ORIGIN` | `http://localhost:4200` | Origen permitido por CORS |

[`backend/.env.example`](backend/.env.example) documenta estas variables sin
incluir credenciales reales. Spring Boot no carga archivos `.env`
automáticamente: las variables deben configurarse en IntelliJ, en la terminal o
mediante la herramienta utilizada para ejecutar la aplicación.

Crear la base de datos local:

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
infraestructura se completará en una fase posterior; actualmente la aplicación
se ejecuta contra MySQL local.

Comprobar el frontend:

```bash
cd frontend
npm run build
```

## Próximos pasos

- Separar el catálogo completo de la selección mostrada en la portada.
- Configurar la URL de la API mediante entornos de Angular.
- Añadir usuarios y autenticación.
- Configurar autorización por roles.
- Incorporar carrito y pedidos.
- Añadir Dockerfiles y Docker Compose.
- Preparar el despliegue público.

## Alcance

El proyecto es una demostración técnica y no procesa pagos ni envíos reales.
