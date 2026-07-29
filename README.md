<div align="center">
  <img src="logoRB.png" alt="Logotipo de Retro Bazar" width="320">

  # Retro Bazar

  **E-commerce de gadgets tecnológicos, productos retro y accesorios para setups.**
</div>

## Sobre el proyecto

Retro Bazar es una plataforma e-commerce en desarrollo especializada en productos
retro, gamer y geek. El catálogo inicial estará formado por aproximadamente 40
productos distribuidos en cuatro categorías:

- Robots y gadgets de escritorio.
- Retro gaming.
- Iluminación y pixel art.
- Accesorios para setup.

La aplicación no procesará pagos ni realizará envíos reales: cuando un cliente confirme su carrito, se
registrará un pedido para que posteriormente lo gestione un administrador.

## Tecnologías

### Backend

- Java 21.
- Spring Boot 4.1.
- Maven.
- Spring Web MVC.
- Spring Data JPA y Hibernate.
- Spring Security.
- MySQL.
- Java Mail Sender.
- Springdoc OpenAPI y Swagger UI.
- JUnit, Mockito y Testcontainers.

### Frontend

- Angular.

### Imágenes

- Cloudinary.

## Arquitectura

El backend seguirá una arquitectura hexagonal dentro de un monolito modular.
Los módulos se crearán a medida que aparezcan las funcionalidades, comenzando
por `catalog`.

En cada módulo se diferenciarán:

- **Dominio:** reglas y conceptos del negocio, independientes de Spring.
- **Aplicación:** casos de uso y puertos que coordinan las operaciones.
- **Infraestructura:** adaptadores REST, persistencia y otras integraciones
  externas.

Recorrido previsto para la consulta del catálogo:

## Estructura del repositorio

```text
retro-bazar/
├── backend/     # API REST con Spring Boot
├── frontend/    # Aplicación Angular (pendiente)
└── README.md
```

## Preparación del backend

Requisitos:

- JDK 21.
- Docker para las futuras pruebas de integración con Testcontainers.

Validar el proyecto:

```bash
cd backend
./mvnw validate
```

La configuración local de MySQL se añadirá cuando se implemente la persistencia
del catálogo. Las credenciales no se almacenarán en el repositorio.

## Funcionalidades previstas

### Cliente

- Registro e inicio de sesión.
- Consulta del catálogo.
- Gestión del carrito.
- Creación de pedidos simulados.
- Consulta de sus pedidos.

### Administración

- Creación y edición de productos.
- Gestión del precio, stock y disponibilidad.
- Consulta y actualización del estado de los pedidos.

## Eventos internos previstos

- `OrderCreatedEvent`: enviará al cliente la confirmación del pedido.
- `LowStockDetectedEvent`: avisará al administrador cuando el stock cruce el
  umbral definido.

Los eventos serán internos y se implementarán con Spring. El proyecto no
utilizará Kafka, RabbitMQ, microservicios ni una pasarela de pago.
