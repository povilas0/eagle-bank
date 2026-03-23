# Eagle Bank API

A REST API for a fictional bank built with Spring Boot 4 and Java 25. Supports full lifecycle management of users, bank accounts and transactions, secured with JWT authentication.

## Stack

| Layer | Technology |
|---|---|
| Language | Java 25 |
| Framework | Spring Boot 4 |
| Security | Spring Security + JWT (JJWT 0.12) |
| Database | H2 (in-memory) |
| Migrations | Flyway |
| Build tool | Gradle |
| Testing | JUnit 5, MockMvc, Spring Security Test |

## Getting Started

**Prerequisites:** Java 25 JDK

```bash
# Build
./gradlew clean build

# Run tests
./gradlew clean test

# Start the application
./gradlew bootRun
```

The API is available at `http://localhost:8080`.
The H2 console is available at `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:eagle_bank`).

## Authentication

All endpoints except `POST /v1/users` and `POST /v1/auth/login` require a Bearer JWT token.

```
Authorization: Bearer <token>
```

Obtain a token by registering a user and then logging in:

```bash
# 1. Register
curl -X POST http://localhost:8080/v1/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Jane Doe",
    "address": { "line1": "1 High Street", "town": "London", "county": "Greater London", "postcode": "EC1A 1BB" },
    "phoneNumber": "+447911123456",
    "email": "jane@example.com",
    "password": "Password123!"
  }'

# 2. Login
curl -X POST http://localhost:8080/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{ "email": "jane@example.com", "password": "Password123!" }'
# → { "token": "<jwt>" }
```

The JWT includes a `userId` claim and expires after 24 hours.

## API Reference

Full spec: [`docs/openapi.yaml`](docs/openapi.yaml)

### Auth

| Method | Path | Description | Auth |
|---|---|---|---|
| `POST` | `/v1/auth/login` | Login and receive a JWT | No |

### Users

| Method | Path | Description | Auth |
|---|---|---|---|
| `POST` | `/v1/users` | Register a new user | No |
| `GET` | `/v1/users/{userId}` | Fetch own profile | Yes — own user only |
| `PATCH` | `/v1/users/{userId}` | Update own profile | Yes — own user only |
| `DELETE` | `/v1/users/{userId}` | Delete own account | Yes — own user only, no active accounts |

### Bank Accounts

| Method | Path | Description | Auth |
|---|---|---|---|
| `POST` | `/v1/accounts` | Create a new bank account | Yes |
| `GET` | `/v1/accounts` | List own bank accounts | Yes |
| `GET` | `/v1/accounts/{accountNumber}` | Fetch a bank account | Yes — own account only |
| `PATCH` | `/v1/accounts/{accountNumber}` | Update a bank account | Yes — own account only |
| `DELETE` | `/v1/accounts/{accountNumber}` | Delete a bank account | Yes — own account only |

### Transactions

| Method | Path | Description | Auth |
|---|---|---|---|
| `POST` | `/v1/accounts/{accountNumber}/transactions` | Deposit or withdraw | Yes — own account only |
| `GET` | `/v1/accounts/{accountNumber}/transactions` | List transactions | Yes — own account only |
| `GET` | `/v1/accounts/{accountNumber}/transactions/{transactionId}` | Fetch a transaction | Yes — own account only |

## Project Structure

The project is organised **by feature**, each with nested `api`, `domain` and `persistence` layers. This makes it straightforward to extract individual features into separate services.

```
src/main/java/com/povilas/eagle_bank/
├── auth/
│   ├── api/          # AuthController, LoginRequest/Response, AuthApiMapper
│   └── domain/       # AuthService, LoginCommand, InvalidCredentialsException
├── account/
│   ├── api/          # AccountController, request/response DTOs, AccountApiMapper
│   ├── domain/       # Account, AccountService, AccountRepository, commands, exceptions
│   └── persistence/  # JdbcAccountRepository, AccountEntity, AccountPersistenceMapper
├── transaction/
│   ├── api/          # TransactionController, request/response DTOs, TransactionApiMapper
│   ├── domain/       # Transaction, TransactionService, TransactionRepository, commands, exceptions
│   └── persistence/  # JdbcTransactionRepository, TransactionEntity, TransactionPersistenceMapper
├── user/
│   ├── api/          # UserController, request/response DTOs, UserApiMapper
│   ├── domain/       # User, UserService, UserRepository, commands, exceptions
│   └── persistence/  # JdbcUserRepository, UserEntity, UserPersistenceMapper
└── common/
    ├── api/          # GlobalExceptionHandler
    ├── domain/       # ForbiddenException
    └── security/     # SecurityConfig, JwtService, JwtAuthenticationFilter
```

### Architectural principles

- **Domain layer** owns all business logic — domain models (records) carry factory constructors and instance methods; services orchestrate
- **API layer** uses String-typed request DTOs; mapping and validation into domain types happens in dedicated `*ApiMapper` components
- **Persistence layer** uses `NamedParameterJdbcTemplate` with hand-written SQL; `SELECT` queries always list columns explicitly
- **Controllers** return plain POJOs (never `ResponseEntity`)
- **Cross-cutting concerns** — `GlobalExceptionHandler` translates domain exceptions into HTTP responses; `JwtAuthenticationFilter` validates tokens on every request

## Database Schema

Managed by Flyway. Migrations run automatically on startup.

| Migration | Description |
|---|---|
| `V1` | `users` table |
| `V2` | `accounts` table (FK → users) |
| `V3` | `transactions` table (FK → accounts) |
| `V4` | Add `password_hash` column and unique email constraint to `users` |

## Configuration

| Property | Default | Description |
|---|---|---|
| `jwt.secret` | (see `application.properties`) | HMAC-SHA256 signing secret |
| `jwt.expiration-ms` | `86400000` | Token lifetime in milliseconds (24 h) |
| `spring.h2.console.enabled` | `true` | Enable H2 web console |
