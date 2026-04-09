# TaskFlow

A full-stack task management application built with Spring Boot and Angular.

> **Product information** — purpose, feature plans, roadmap, and business requirements are maintained in the [`product-information/`](product-information/) directory.

---

## Table of Contents

- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Environment Variables](#environment-variables)
  - [Running Locally (Docker)](#running-locally-docker)
  - [Running for QA (Docker)](#running-for-qa-docker)
  - [Running for Production (Docker)](#running-for-production-docker)
- [Backend](#backend)
  - [Architecture](#architecture)
  - [REST API](#rest-api)
  - [Security](#security)
  - [Database](#database)
  - [Configuration Profiles](#configuration-profiles)
- [Frontend](#frontend)
  - [Architecture](#architecture-1)
  - [Services](#services)
  - [Routing & Guards](#routing--guards)
  - [Build Configurations](#build-configurations)
- [Infrastructure](#infrastructure)
  - [Docker Services](#docker-services)
  - [Nginx](#nginx)

---

## Tech Stack

| Layer | Technology |
|---|---|
| **Backend** | Java 25, Spring Boot 4.0.5, Spring Security (stateless JWT), MyBatis, Liquibase |
| **Database** | PostgreSQL 17 |
| **Frontend** | Angular 21.2 (standalone, zoneless), TypeScript ~5.9, SCSS, RxJS |
| **Build** | Gradle (backend), npm 11 (frontend) |
| **Containerisation** | Docker, Docker Compose |
| **Reverse proxy** | Nginx (production/QA) |

---

## Project Structure

```
TaskFlow/
├── backend/                  # Spring Boot application
│   └── src/main/java/com/killeen/taskflow/
│       ├── components/
│       │   ├── user/         # Auth controller, service, repository, models
│       │   └── email/        # Email & token service, repository, models
│       ├── config/           # Security, JWT, CORS, retry configuration
│       ├── db/               # MyBatis mappers & generated models
│       └── exception/        # Global exception handler & error response
├── frontend/                 # Angular application
│   └── src/app/
│       ├── components/       # Auth forms (login, register, forgot-password), header
│       ├── pages/            # Page-level route components
│       ├── services/         # Auth, theme, platform services
│       ├── guards/           # Auth route guard
│       ├── interceptors/     # URL prefix & Bearer token interceptors
│       ├── interfaces/       # Shared TypeScript interfaces
│       └── shared/           # Reusable UI components and styles
├── product-information/      # Product docs: purpose, plans, roadmap, requirements
├── docker-compose.yml        # Base / production Compose file
├── docker-compose.local.yml  # Local development override
├── docker-compose.qa.yml     # QA environment override
└── env.example               # Required & optional environment variables
```

---

## Getting Started

### Prerequisites

- [Docker](https://www.docker.com/) and Docker Compose v2

### Environment Variables

Copy `env.example` to `.env` and fill in the required values:

```bash
cp env.example .env
```

| Variable | Required | Default | Description |
|---|---|---|---|
| `POSTGRES_PASSWORD` | Yes | — | PostgreSQL password |
| `JWT_SECRET` | Yes | — | HS256 signing secret (min 32 chars) |
| `MAIL_USERNAME` | QA/Prod | — | SMTP username |
| `MAIL_PASSWORD` | QA/Prod | — | SMTP password |
| `POSTGRES_DB` | No | `taskflow` | Database name |
| `POSTGRES_USER` | No | `taskflow` | Database user |
| `MAIL_HOST` | No | `smtp.gmail.com` | SMTP host |
| `MAIL_PORT` | No | `587` | SMTP port |
| `MAIL_FROM` | No | `noreply@taskflow.com` | Sender address |
| `APP_BASE_URL` | No | Profile-dependent | Base URL used in emails |

### Running Locally (Docker)

Starts the backend with Spring DevTools and the Angular dev server with live reload. Source directories are volume-mounted so changes are reflected without rebuilding.

```bash
docker compose -f docker-compose.yml -f docker-compose.local.yml up --build
```

| Service | URL |
|---|---|
| Frontend (Angular dev server) | http://localhost:4200 |
| Backend API | http://localhost:8080 |

### Running for QA (Docker)

Builds an optimised Angular bundle with the `qa` configuration and starts the backend with the `qa` Spring profile.

```bash
docker compose -f docker-compose.yml -f docker-compose.qa.yml up --build
```

### Running for Production (Docker)

```bash
docker compose up --build
```

---

## Backend

### Architecture

The backend follows a component-based layered architecture under `com.killeen.taskflow`:

```
components/
  user/   → AuthController, UserService, UserRepository
  email/  → EmailService (with @Retryable), EmailTokenService, EmailTokenRepository
config/   → SecurityConfig, JwtService, JwtAuthenticationFilter, WebConfig, RetryConfig
db/       → MyBatis-generated mappers and DB models
exception/→ GlobalExceptionHandler (@RestControllerAdvice), ErrorResponse
```

**MyBatis** handles all SQL interactions. Generated mappers are produced by the `mybatisGenerator` Gradle task from `mybatis-generator-config.xml`. Custom mapper XML files live alongside the generated ones under `src/main/resources/db/mapper/`.

**Liquibase** manages schema migrations via `src/main/resources/db/liquibase.xml`.

### REST API

All endpoints are under `/auth`. Every endpoint not listed as public requires a valid JWT Bearer token.

| Method | Path | Auth | Description |
|---|---|---|---|
| `POST` | `/auth/register` | Public | Register a new user; sends a verification email |
| `POST` | `/auth/login` | Public | Authenticate and receive a JWT |
| `GET` | `/auth/me` | JWT | Return the authenticated user's profile |
| `POST` | `/auth/verify-email?token=` | Public | Verify email address via token |
| `POST` | `/auth/resend-verification` | Public | Resend the email verification link |
| `POST` | `/auth/forgot-password` | Public | Send a password-reset email |
| `POST` | `/auth/reset-password` | Public | Reset password via token |

### Security

- **Stateless JWT** — no server-side sessions. Tokens use HS256 with a 24-hour expiry.
- **Password hashing** — BCrypt.
- **Email tokens** — raw UUID issued to the user; SHA-256 hash stored in the database. Verification tokens expire after 24 hours; password-reset tokens after 1 hour.
- **Email sending** — annotated with `@Retryable` (up to 3 attempts, exponential backoff starting at 2 s) to handle transient SMTP failures.
- **CORS** — configured via `app.cors.allowed-origins` in the application YAML (profile-specific).

#### Exception → HTTP Status Mapping

| Exception | Status |
|---|---|
| `UserAlreadyExistsException` | 409 Conflict |
| `InvalidCredentialsException` | 401 Unauthorized |
| `UserNotFoundException` | 404 Not Found |
| `EmailNotVerifiedException` | 403 Forbidden |
| `InvalidTokenException` | 400 Bad Request |
| Validation failure | 400 Bad Request |

### Database

#### Schema (managed by Liquibase)

**`users`**
| Column | Type | Notes |
|---|---|---|
| `id` | `BIGSERIAL` | Primary key |
| `email` | `VARCHAR` | Unique, not null |
| `password_hash` | `VARCHAR` | BCrypt hash |
| `display_name` | `VARCHAR` | |
| `email_verified` | `BOOLEAN` | Default `false` |
| `created_at` / `updated_at` | `TIMESTAMP` | |

**`email_tokens`**
| Column | Type | Notes |
|---|---|---|
| `id` | `BIGSERIAL` | Primary key |
| `user_id` | `BIGINT` | FK → `users(id)` ON DELETE CASCADE |
| `token` | `VARCHAR(64)` | SHA-256 hash, unique |
| `token_type` | `email_token_type` | `VERIFY_EMAIL` or `RESET_PASSWORD` |
| `created_at` / `expires_at` / `used_at` | `TIMESTAMP` | |

Indexes: `idx_email_tokens_token`, `idx_email_tokens_user_type`.

### Configuration Profiles

| Profile | Activated by | Notable behaviour |
|---|---|---|
| *(default)* | Always | Base config; datasource points to `localhost:5432` |
| `local` | `docker-compose.local.yml` | Datasource uses Docker service name `db`; Spring DevTools enabled |
| `qa` | `docker-compose.qa.yml` | Actuator exposes `health` + `info`; `com.killeen` logged at INFO |
| `prod` | `docker-compose.yml` | Actuator exposes `health` only; root logged at WARN |

---

## Frontend

### Architecture

Built with Angular 21.2 using **standalone components** and **zoneless change detection** (`provideZonelessChangeDetection()`). State is managed with Angular signals.

On startup, `provideAppInitializer` calls `AuthService.loadCurrentUser()` to hydrate authentication state from the stored JWT before the first route is rendered.

HTTP requests are processed by two functional interceptors (applied in order):
1. **`UrlInterceptor`** — prepends `environment.api_url` to every request.
2. **`AuthInterceptor`** — attaches `Authorization: Bearer <token>`; calls `authService.logout()` on a 401 response.

### Services

| Service | Responsibility |
|---|---|
| `AuthService` | JWT storage (`localStorage`), all auth API calls, `isAuthenticated` computed signal, `logout$` observable |
| `ThemeService` | `light`/`dark` theme preference persisted in `localStorage`; respects `prefers-color-scheme`; applies `data-theme` attribute via Angular `effect()` |
| `PlatformService` | Responsive breakpoint signals (`phone`, `tablet`, `laptop`, `desktop`) and computed helpers (`isMobile`, `isLargeScreen`) via `matchMedia` |

### Routing & Guards

| Path | Component | Notes |
|---|---|---|
| `/` | — | Redirects to `/login` |
| `/login` | `AuthComponent` | Lazy-loaded; hosts LOGIN / REGISTER / FORGOT_PASSWORD modes |
| `/verify-email` | `VerifyEmailComponent` | Lazy-loaded |
| `/reset-password` | `ResetPasswordComponent` | Lazy-loaded |

`authGuard` (`CanActivateFn`) protects authenticated routes and redirects unauthenticated users to `/login`.

### Build Configurations

| Configuration | Environment | Optimisation |
|---|---|---|
| `production` | `environment.prod.ts` | Full optimisation, output hashing, bundle budgets |
| `qa` | `environment.qa.ts` | Optimised, source maps enabled |
| `development` | `environment.ts` | No optimisation, source maps enabled |

API base URLs per environment:

| Environment | `api_url` |
|---|---|
| Development | `http://localhost:8080` |
| QA | `http://qa.taskflow.com` |
| Production | `https://api.taskflow.com` |

---

## Infrastructure

### Docker Services

| Service | Image | Port | Notes |
|---|---|---|---|
| `db` | `postgres:17-alpine` | 5432 | Named volume `postgres_data`; healthcheck via `pg_isready` |
| `backend` | Built from `./backend` | 8080 | Depends on `db` (healthy) |
| `frontend` | Built from `./frontend` | 80 (prod/QA) / 4200 (local) | Depends on `backend` |

Both the backend and frontend Dockerfiles use **multi-stage builds**:

- **`dev` stage** — runs the dev server (Spring DevTools / `ng serve`) with source volume-mounted.
- **`builder` stage** — compiles the production artefact.
- **`runtime` stage** — minimal image; backend uses `eclipse-temurin:25-jre-alpine` as a non-root user, frontend uses `nginx:alpine`.

### Nginx

In production/QA the frontend container serves Angular via Nginx with the following configuration:

- `try_files` fallback to `index.html` for client-side SPA routing.
- `/api/` requests are proxied to `http://backend:8080/api/`.
- Security headers: `X-Frame-Options: SAMEORIGIN`, `X-Content-Type-Options: nosniff`, `X-XSS-Protection`.
