# BiblioTech API — Sistema de gestion de biblioteca (Spring Boot)

API REST para gestionar el catalogo de libros, usuarios y prestamos de una biblioteca. Construida con **Java 17 y Spring Boot 3**, siguiendo una arquitectura en capas (controller / service / repository) y autenticacion **JWT** con Spring Security.

## Funcionalidades

- Registro e inicio de sesion de usuarios (contrasenas hasheadas con BCrypt, tokens JWT).
- Catalogo de libros publico (busqueda por titulo/autor) y administracion (crear, editar, eliminar).
- Prestamos: un usuario autenticado puede pedir un libro prestado y devolverlo; el sistema controla la disponibilidad de copias automaticamente.
- Manejo centralizado de errores y validacion de datos de entrada (Bean Validation).
- Documentacion interactiva con Swagger / OpenAPI.
- Base de datos **H2 en memoria por defecto** (cero configuracion) con perfil listo para **PostgreSQL** en produccion.
- Pagina HTML simple en `/` que consume la API publica de libros, a modo de demo.

## Stack tecnico

Java 17, Spring Boot 3 (Web, Data JPA, Security, Validation), JJWT, H2 / PostgreSQL, springdoc-openapi (Swagger UI), Lombok, Maven.

## Estructura del proyecto

```
02-bibliotech-api/
├── src/main/java/com/senati/bibliotech/
│   ├── config/        # SecurityConfig, CustomUserDetailsService, DataSeeder
│   ├── controller/     # AuthController, BookController, LoanController
│   ├── dto/            # Requests/Responses
│   ├── exception/      # Excepciones custom + manejador global
│   ├── model/          # Entidades JPA: User, Book, Loan
│   ├── repository/     # Spring Data JPA repositories
│   ├── security/        # JwtUtil, JwtAuthFilter
│   └── BibliotechApplication.java
├── src/main/resources/
│   ├── application.properties
│   └── static/index.html   # demo del catalogo
└── pom.xml
```

## Como correrlo en local

### Requisitos
- JDK 17+
- Maven 3.9+ (o usar el wrapper `mvnw` si lo agregas con `mvn -N wrapper:wrapper`)

### Pasos
```bash
mvn spring-boot:run
```

La app levanta en `http://localhost:8080` usando **H2 en memoria** (no requiere instalar base de datos). Al iniciar, `DataSeeder` carga 3 libros de ejemplo automaticamente.

- Demo del catalogo: http://localhost:8080/
- Swagger UI: http://localhost:8080/swagger-ui.html
- Consola H2: http://localhost:8080/h2-console (JDBC URL: `jdbc:h2:mem:bibliotech`, usuario `sa`, sin password)

### Usar PostgreSQL en vez de H2
Edita `src/main/resources/application.properties`: comenta el bloque de H2 y descomenta el bloque de PostgreSQL, ajustando usuario/contrasena a tu instalacion local.

## Endpoints principales

| Metodo | Ruta                    | Descripcion                              | Auth |
|--------|--------------------------|-------------------------------------------|------|
| POST   | /api/auth/register       | Crea un usuario                            | No   |
| POST   | /api/auth/login          | Inicia sesion, devuelve JWT                | No   |
| GET    | /api/books?search=       | Lista/busca libros                         | No   |
| POST   | /api/books                | Crea un libro                              | No*  |
| PUT    | /api/books/:id            | Actualiza un libro                         | No*  |
| DELETE | /api/books/:id            | Elimina un libro                           | No*  |
| GET    | /api/loans/mine           | Prestamos del usuario autenticado          | Si   |
| POST   | /api/loans/:bookId        | Pide prestado un libro                     | Si   |
| PUT    | /api/loans/:id/return     | Devuelve un libro prestado                 | Si   |

\* En esta version de portafolio, la administracion de libros esta abierta para simplificar las pruebas manuales; en un entorno real se restringiria a `ROLE_ADMIN` con `@PreAuthorize`.

## Posibles mejoras futuras

- Roles y permisos granulares (`ROLE_ADMIN` vs `ROLE_USER`) con `@PreAuthorize`.
- Tests unitarios de servicios con JUnit 5 + Mockito, y tests de integracion con `@SpringBootTest` + Testcontainers (PostgreSQL real).
- Paginacion (`Pageable`) en el listado de libros.
- Despliegue: Docker + Render/Railway con PostgreSQL gestionado.

---
Proyecto desarrollado como parte del portafolio de Jim Kleber Acosta Acosta, egresado de Desarrollo de Software - SENATI.
