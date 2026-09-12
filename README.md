# Tennis Microservices

A small microservices-based REST API for managing tennis players and tennis courts, built with [Quarkus](https://quarkus.io). This started as a single monolithic Quarkus app and was split into two independent services as a learning exercise in microservices architecture and the Boundary-Control-Entity (BCE) design pattern.

## Architecture

The project is made up of two completely independent Quarkus applications, each with its own `pom.xml`, its own database, and its own HTTP port. A root `pom.xml` aggregates both as Maven modules purely for build convenience (`./mvnw install` from the root builds both) — it does **not** create a shared runtime dependency between them; each service is still built, versioned, and deployed independently:

| Service       | Path           | Port | Resource        |
|---------------|----------------|------|------------------|
| Players       | `players/`     | 8080 | `/players`       |
| Tennis Courts | `tennisCourts/`| 8081 | `/tennisCourts`  |

Each service follows the **Boundary-Control-Entity (BCE)** pattern internally, with the Control layer further split following **CQRS** (Command Query Responsibility Segregation) and a dedicated Repository:

- **Boundary** — the REST resource (e.g. `PlayerResource`) that handles HTTP requests/responses only, using request/response DTOs (e.g. `CreatePlayerRequest`, `PlayerResponse`) to decouple the API contract from the JPA entities.
- **Control** — split into:
  - a **Repository** (e.g. `PlayerRepository`) that owns data access only, built on [Hibernate ORM with Panache](https://quarkus.io/guides/hibernate-orm-panache);
  - a **Command service** (e.g. `PlayerCommandService`) that owns writes (create/update/delete);
  - a **Query service** (e.g. `PlayerQueryService`) that owns reads (list/find), with no side effects.
- **Entity** — the JPA-mapped domain object (e.g. `PlayerEntity`), using [Lombok](https://projectlombok.org) (`@Getter`, `@Setter`, `@NoArgsConstructor`) to remove boilerplate, and auditing fields (`createDate`, `updateDate`) auto-populated by Hibernate.

## Tech stack

- Java + Quarkus
- RESTEasy Reactive with Jackson (`quarkus-rest-jackson`)
- Hibernate ORM with Panache (`quarkus-hibernate-orm-panache`)
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
cd tennisCourts
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
| GET    | `/players/{id}` | Get a player by id        |
| POST   | `/players`      | Create a new player       |

**Create a player**

```bash
curl -s -X POST localhost:8080/players \
  -H "Content-Type: application/json" \
  -d '{"firstname":"Rafael","lastname":"Nadal","country":"Spain","age":37}'
```

Request body fields (`CreatePlayerRequest`): `firstname`, `lastname`, `country` (all required strings), `age` (int).

Response body fields (`PlayerResponse`): `id`, `firstname`, `lastname`, `country`, `age`, `createDate`, `updateDate`.

### Tennis Courts (`http://localhost:8081`)

| Method | Path                 | Description                |
|--------|----------------------|------------------------------|
| GET    | `/tennisCourts`      | List all tennis courts       |
| GET    | `/tennisCourts/{id}` | Get a tennis court by id     |
| POST   | `/tennisCourts`      | Create a new tennis court    |

**Create a tennis court**

```bash
curl -s -X POST localhost:8081/tennisCourts \
  -H "Content-Type: application/json" \
  -d '{"name":"Clube Tenis Porto","country":"Portugal","city":"Porto","surface":"CLAY"}'
```

Request body fields (`CreateTennisCourtRequest`): `name`, `country`, `city` (all required strings), `surface` (required, one of `CLAY`, `GRASS`, `HARD`, `CARPET`).

Response body fields (`TennisCourtResponse`): `id`, `name`, `country`, `city`, `surface`, `createDate`, `updateDate`.

## Project structure

```
tennis-microservices/
├── pom.xml                 → aggregator pom (build convenience only)
├── players/
│   └── src/main/java/players/
│       ├── boundary/
│       │   ├── PlayerResource.java        → REST endpoints
│       │   └── dto/                       → CreatePlayerRequest, PlayerResponse
│       ├── control/
│       │   ├── PlayerRepository.java      → data access (Panache)
│       │   ├── PlayerCommandService.java  → writes
│       │   └── PlayerQueryService.java    → reads
│       └── entity/
│           └── PlayerEntity.java          → JPA entity (Lombok)
└── tennisCourts/
    └── src/main/java/tennisCourts/
        ├── boundary/
        │   ├── TennisCourtResource.java        → REST endpoints
        │   └── dto/                            → CreateTennisCourtRequest, TennisCourtResponse
        ├── control/
        │   ├── TennisCourtRepository.java      → data access (Panache)
        │   ├── TennisCourtCommandService.java  → writes
        │   └── TennisCourtQueryService.java    → reads
        └── entity/
            ├── TennisCourtEntity.java          → JPA entity (Lombok)
            └── Surface.java                    → enum (CLAY, GRASS, HARD, CARPET)
```
