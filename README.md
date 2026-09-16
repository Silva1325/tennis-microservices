# Tennis Microservices

A small microservices-based REST API for managing tennis players and tennis courts, built with [Quarkus](https://quarkus.io). This started as a single monolithic Quarkus app and was split into two independent services as a learning exercise in microservices architecture and the Boundary-Control-Entity (BCE) design pattern.

## Architecture

The project is made up of two completely independent Quarkus applications, each with its own `pom.xml`, its own database, and its own HTTP port. A root `pom.xml` aggregates both as Maven modules purely for build convenience (`./mvnw install` from the root builds both) — it does **not** create a shared runtime dependency between them; each service is still built, versioned, and deployed independently:

| Service       | Path           | Port | Resource        |
|---------------|----------------|------|------------------|
| Players       | `players/`     | 8080 | `/players`       |
| Tennis Courts | `courts/`     | 8081 | `/courts`       |

Each service follows the **Boundary-Control-Entity (BCE)** pattern internally, with the Control layer further split following **CQRS** (Command Query Responsibility Segregation) and a dedicated Repository:

- **Boundary** — the REST resource (e.g. `PlayerResource`) that handles HTTP requests/responses only, using request/response DTOs (e.g. `CreatePlayerRequest`, `PlayerResponse`) to decouple the API contract from the JPA entities. Requests are validated declaratively with [Jakarta Bean Validation](https://quarkus.io/guides/validation) (`@NotBlank`, `@NotNull`, `@Min`, `@Valid`).
- **Control** — split into:
  - a **Repository** (e.g. `PlayerRepository`) that owns data access only, built on [Hibernate ORM with Panache](https://quarkus.io/guides/hibernate-orm-panache);
  - a **Command service** (e.g. `PlayerCommandService`) that owns writes (create/update/delete);
  - a **Query service** (e.g. `PlayerQueryService`) that owns reads (list/find), with no side effects.
  - **Command objects** (e.g. `CreatePlayerCommand`, `UpdatePlayerCommand`, `DeletePlayerCommand`) — immutable records in `control/command/` that describe each write. The resource maps the request DTO to a command, and the command service takes that single object instead of a long parameter list.
- **Entity** — the JPA-mapped domain object (e.g. `PlayerEntity`), using [Lombok](https://projectlombok.org) (`@Getter`, per-field `@Setter`, `@NoArgsConstructor`) to remove boilerplate, and auditing fields (`createDate`, `updateDate`) auto-populated by Hibernate.

### Internal vs. external identifiers

Each entity has two identifiers:

- `id` (`Long`, auto-generated primary key) — internal only, used by Hibernate for indexing and relations. Never exposed by the API.
- `publicId` (`UUID`, generated on creation) — the identifier the API actually exposes (as `id` in request/response DTOs) and the one used in URLs (e.g. `/players/{publicId}`).

This avoids exposing a sequential, guessable identifier (and the information it leaks, like record count and creation rate) while keeping an efficient `Long` primary key for the database itself.

## Tech stack

- Java + Quarkus
- RESTEasy Reactive with Jackson (`quarkus-rest-jackson`)
- Hibernate ORM with Panache (`quarkus-hibernate-orm-panache`)
- Jakarta Bean Validation (`quarkus-hibernate-validator`)
- PostgreSQL, auto-provisioned via Quarkus Dev Services (requires Docker running locally — no manual database setup needed for development)
- [Lombok](https://projectlombok.org) for entity boilerplate (getters/setters/constructors)

## Prerequisites

- JDK 25+
- Docker (running), for Quarkus Dev Services to auto-provision a PostgreSQL container per service
- Maven wrapper is included, so a separate Maven install isn't required
- The [Lombok IntelliJ plugin](https://plugins.jetbrains.com/plugin/6317-lombok) (with "Enable annotation processing" turned on) if you're working in IntelliJ

## Running the services

Each service runs independently, in its own terminal:

```bash
# Terminal 1 — Players service (http://localhost:8080)
cd players
./mvnw quarkus:dev
```

```bash
# Terminal 2 — Tennis Courts service (http://localhost:8081)
cd courts
./mvnw quarkus:dev
```

On first startup, each service will pull and start its own throwaway PostgreSQL container automatically — no datasource configuration required.

To just compile/test both modules together from the root (without running dev mode):

```bash
./mvnw install
```

## API

### Players (`http://localhost:8080`)

| Method | Path            | Description              |
|--------|-----------------|---------------------------|
| GET    | `/players`      | List all players          |
| GET    | `/players/{id}` | Get a player by id (UUID) |
| POST   | `/players`      | Create a new player       |
| PUT    | `/players/{id}` | Update a player           |
| DELETE | `/players/{id}` | Delete a player           |

**Create a player**

```bash
curl -s -X POST localhost:8080/players \
  -H "Content-Type: application/json" \
  -d '{"email":"rafa@example.com","password":"secret","firstname":"Rafael","lastname":"Nadal","country":"Spain","age":37}'
```

```json
{
  "id": "ad7109a6-fe12-4b8b-a64b-dde959c1fbfb",
  "email": "rafa@example.com",
  "firstname": "Rafael",
  "lastname": "Nadal",
  "country": "Spain",
  "age": 37,
  "createDate": "2026-09-13T01:57:16.552958",
  "updateDate": "2026-09-13T01:57:16.552971"
}
```

Request body fields (`CreatePlayerRequest`): `email` (required, valid email, unique), `password`, `firstname`, `lastname`, `country` (required, non-blank strings), `age` (int, `>= 0`).

Request body fields (`UpdatePlayerRequest`, for `PUT`): same as create, without `password`. Returns `404` if the player doesn't exist and `409` if the email belongs to another player.

`DELETE` returns `204 No Content`, or `404` if the player doesn't exist.

Response body fields (`PlayerResponse`): `id` (UUID), `email`, `firstname`, `lastname`, `country`, `age`, `createDate`, `updateDate`.

### Tennis Courts (`http://localhost:8081`)

| Method | Path                 | Description                    |
|--------|----------------------|----------------------------------|
| GET    | `/courts`      | List all tennis courts          |
| GET    | `/courts/{id}` | Get a tennis court by id (UUID) |
| POST   | `/courts`      | Create a new tennis court       |
| PUT    | `/courts/{id}` | Update a tennis court           |
| DELETE | `/courts/{id}` | Delete a tennis court           |

**Create a tennis court**

```bash
curl -s -X POST localhost:8081/courts \
  -H "Content-Type: application/json" \
  -d '{"name":"Clube Tenis Porto","country":"Portugal","city":"Porto","surface":"CLAY"}'
```

Request body fields (`CreateCourtRequest`): `name`, `country`, `city` (required, non-blank strings), `surface` (required, one of `CLAY`, `GRASS`, `HARD`, `CARPET`).

Request body fields (`UpdateCourtRequest`, for `PUT`): same as create. Returns `404` if the court doesn't exist and `409` if another court already has that name in that city.

`DELETE` returns `204 No Content`, or `404` if the court doesn't exist.

Response body fields (`CourtResponse`): `id` (UUID), `name`, `country`, `city`, `surface`, `createDate`, `updateDate`.

## Project structure

```
tennis-microservices/
├── pom.xml                 → aggregator pom (build convenience only)
├── players/
│   └── src/main/java/players/
│       ├── boundary/
│       │   ├── PlayerResource.java        → REST endpoints
│       │   └── dto/                       → Create/UpdatePlayerRequest, PlayerResponse
│       ├── control/
│       │   ├── PlayerRepository.java      → data access (Panache)
│       │   ├── PlayerCommandService.java  → writes
│       │   ├── PlayerQueryService.java    → reads
│       │   └── command/                   → Create/Update/DeletePlayerCommand
│       └── entity/
│           └── PlayerEntity.java          → JPA entity (Lombok, internal id + public UUID)
└── courts/
    └── src/main/java/courts/
        ├── boundary/
        │   ├── CourtResource.java        → REST endpoints
        │   └── dto/                            → Create/UpdateCourtRequest, CourtResponse
        ├── control/
        │   ├── CourtRepository.java      → data access (Panache)
        │   ├── CourtCommandService.java  → writes
        │   ├── CourtQueryService.java    → reads
        │   └── command/                        → Create/Update/DeleteCourtCommand
        └── entity/
            ├── CourtEntity.java          → JPA entity (Lombok, internal id + public UUID)
            └── Surface.java                    → enum (CLAY, GRASS, HARD, CARPET)
```

## Known gaps / next steps

- No automated tests yet (`src/test` is currently empty in both modules).
- No OpenAPI/Swagger UI exposed.
