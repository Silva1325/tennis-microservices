# Tennis Microservices

A small microservices-based REST API for managing tennis players and tennis courts, built with [Quarkus](https://quarkus.io). This started as a single monolithic Quarkus app and was split into two independent services as a learning exercise in microservices architecture and the Boundary-Control-Entity (BCE) design pattern.

## Architecture

The project is made up of two completely independent Quarkus applications, each with its own `pom.xml`, its own database, and its own HTTP port:

| Service       | Path           | Port | Resource        |
|---------------|----------------|------|------------------|
| Players       | `players/`     | 8080 | `/players`       |
| Tennis Courts | `tennisCourts/`| 8081 | `/tennisCourts`  |

Each service follows the **Boundary-Control-Entity (BCE)** pattern internally:

- **Boundary** — the REST resource (e.g. `PlayerResource`) that handles HTTP requests/responses only.
- **Control** — the service layer (e.g. `PlayerService`) that owns the business logic and data access, built on [Hibernate ORM with Panache](https://quarkus.io/guides/hibernate-orm-panache).
- **Entity** — the JPA-mapped domain object (e.g. `PlayerEntity`) representing the persisted data.

## Tech stack

- Java + Quarkus
- RESTEasy Reactive with Jackson (`quarkus-rest-jackson`)
- Hibernate ORM with Panache (`quarkus-hibernate-orm-panache`)
- PostgreSQL, auto-provisioned via Quarkus Dev Services (requires Docker running locally — no manual database setup needed for development)

## Prerequisites

- JDK 17+
- Docker (running), for Quarkus Dev Services to auto-provision a PostgreSQL container per service
- Maven wrapper is included, so a separate Maven install isn't required

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

Request body fields: `firstname`, `lastname`, `country` (all required strings), `age` (int).

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

Request body fields: `name`, `country`, `city` (all required strings), `surface` (required, one of `CLAY`, `GRASS`, `HARD`, `CARPET`).

## Project structure

```
tennis-microservices/
├── players/
│   └── src/main/java/players/
│       ├── boundary/   → PlayerResource (REST endpoints)
│       ├── control/    → PlayerService (business logic + persistence)
│       └── entity/     → PlayerEntity (JPA entity)
└── tennisCourts/
    └── src/main/java/tennisCourts/
        ├── boundary/   → TennisCourtResource (REST endpoints)
        ├── control/    → TennisCourtService (business logic + persistence)
        └── entity/     → TennisCourtEntity, Surface (JPA entity + enum)
```
