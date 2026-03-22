# Eagle Bank API

## Overview

Spring Boot 4 (Java 25) REST API for a fictional bank. It allows to create, fetch, update & delete users, bank accounts and transactions.

## Stack

- Runtime: Java 25 JDK
- Language: Java
- Framework: Spring Boot 
- Database: H2 (in memory)
- Dependencies manager: gradle

## Project Structure

- `src/main/java/com/povilas/eagle_bank/` — application code
- `src/main/resources/application.properties` — Spring Boot application properties
- `src/main/resources/db/migration` - Flyway database schema migrations 

## Coding guidelines
- Use TDD approach (write a failing test first)
- Make small iterations
- Ask to validate and confirm before proceeding
- Prefer JDBC Template, manually written queries and row mapping over the Spring Data
- Ensure clear separation between Api, Domain and Persistence layers
- Business logic must live in domain layer
- Domain layer should be reliant on Api or Persistence layer classes
- Api and Persistence layers must have DTOs that are mapped to & from domain layer objects. Mappings should happen in dedicated 'Mapper' Spring Components
- Api Controllers must return POJOs instead of ResponseEntity
- Package by feature (feature packages can have nested 'layer' packages) so it is easier to separate this service into distributed smaller services
- SELECT SQL queries must explicitly list fields 

## Commands

- `./gradlew clean build` — Build the application
- `./gradlew clean test` — Run tests

## Verification

Always run tests after changes.

## Reference Docs

Read relevant docs before starting a task:
- `docs/openapi.yaml` — Open API spec that every endpoint in this API must follow.
- `docs/scenarios.md` - Scenarios that this API must implement.