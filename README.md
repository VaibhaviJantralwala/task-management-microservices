# Task Management System (Microservices)

A task management platform built with a **microservices architecture** using Spring Boot and Spring Cloud. It is split into independent User, Project and Task services that register with a Eureka server, sit behind a Spring Cloud API Gateway, and are secured with **JWT**. A React app provides the UI.

## Architecture

```
                      ┌────────────────────┐
   React (Vite)  ───► │  API Gateway :9090 │  validates JWT, routes requests
                      └─────────┬──────────┘
                                │  lb:// (service discovery)
        ┌───────────────────────┼───────────────────────┐
        ▼                       ▼                       ▼
 ┌──────────────┐       ┌───────────────┐       ┌──────────────┐
 │ micro-user   │ ◄───► │ micro-task    │ ◄───► │ micro-project│
 │    :8081     │ Feign │    :8083      │ Feign │    :8082     │
 └──────┬───────┘       └──────┬────────┘       └──────┬───────┘
        └──────────────────────┴───────────────────────┘
                               │
                          MySQL (:3307)

        All services register with ──►  Eureka Server :8761
```

## Services

| Service | Port | Responsibility |
|---|---|---|
| `EurekaServer` | 8761 | Service registry and discovery |
| `micro-gateway` | 9090 | Single entry point: JWT validation and routing |
| `micro-user` | 8081 | Registration, login (issues JWT), user management |
| `micro-project` | 8082 | CRUD for projects |
| `micro-task` | 8083 | CRUD for tasks, status updates, queries by user or project |
| `task-management` | 5173 (Vite default) | React frontend |

## Tech Stack

- **Backend:** Java 21, Spring Boot 4.1, Spring MVC, Spring Data JPA / Hibernate, Spring Security
- **Microservices:** Spring Cloud (Netflix Eureka, Spring Cloud Gateway, OpenFeign)
- **Security:** JWT (JJWT, HS512), BCrypt password hashing, stateless sessions
- **Database:** MySQL
- **Frontend:** React 19, Vite, React Router, Axios, Tailwind CSS
- **Tools:** Maven, Docker, Git, Postman

## Key Features

- **Service discovery:** every service registers with Eureka; the gateway routes by service name (`lb://MICRO-USER`, etc.)
- **Centralised authentication:** a global gateway filter rejects requests without a valid, unexpired Bearer token. Only `/auth/login` and `/auth/register` are public.
- **Inter-service communication** with OpenFeign:
  - `micro-task` calls `micro-user` and `micro-project` to look up the assigned user and the project
  - deleting a project calls `micro-task` to delete that project's tasks
  - deleting a user calls `micro-task` to unassign that user's tasks
- **Consistent error handling:** `@RestControllerAdvice` with custom exceptions, and a common `ApiResponse` wrapper for all responses
- **Request validation** with Bean Validation (`@Valid`)
- **Frontend:** login, dashboard, projects and tasks pages. The JWT is attached to every request by an Axios interceptor, and a 401 response logs the user out.

## API Overview

All requests go through the gateway at `http://localhost:9090`. Everything except `/auth/*` needs the header `Authorization: Bearer <token>`.

**Auth and users**

| Method | Endpoint | Description |
|---|---|---|
| POST | `/auth/register` | Register a user (public) |
| POST | `/auth/login` | Log in and receive a JWT (public) |
| GET | `/user` | List all users |
| GET | `/user/{id}` | Get a user by id |
| DELETE | `/user/{id}` | Delete a user (and unassign their tasks) |

**Projects**

| Method | Endpoint | Description |
|---|---|---|
| POST | `/project` | Create a project |
| GET | `/project` | List projects |
| GET | `/project/{id}` | Get a project |
| PUT | `/project/{id}` | Update a project |
| DELETE | `/project/{id}` | Delete a project (and its tasks) |

**Tasks**

| Method | Endpoint | Description |
|---|---|---|
| POST | `/task` | Create a task |
| GET | `/task` | List tasks |
| GET | `/task/{id}` | Get a task |
| GET | `/task/user/{id}` | Tasks assigned to a user |
| GET | `/task/project/{id}` | Tasks of a project |
| PUT | `/task/{id}` | Update a task |
| PATCH | `/task/{id}/status` | Update task status |
| DELETE | `/task/{id}` | Delete a task |

**Example**

```bash
# register
curl -X POST http://localhost:9090/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"vaibhavi","password":"Pass@123","email":"v@example.com","mobile":"9999999999","department":"Engineering","role":"manager"}'

# login (returns the JWT as plain text)
curl -X POST http://localhost:9090/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"vaibhavi","password":"Pass@123"}'

# call a protected endpoint
curl http://localhost:9090/project -H "Authorization: Bearer <token>"
```

## Running Locally

**Prerequisites:** Java 21, Maven, MySQL, Node.js 18+

1. **Database:** start MySQL (the services expect port `3307`, user `root`, empty password; change these in each service's `application.properties` if yours differ) and create the schema. Tables are created automatically (`ddl-auto=update`).
   ```sql
   CREATE DATABASE microservices;
   ```
2. **JWT secret:** the secret is not stored in the repo. Set the same `JWT_KEY` environment variable for both `micro-gateway` and `micro-user`. It must be a Base64-encoded key of at least 64 bytes (HS512). To generate one:
   ```bash
   openssl rand -base64 64 | tr -d '\n'
   ```
3. **Start the services in this order**, each with `./mvnw spring-boot:run` (or run the main class from your IDE):
   1. `EurekaServer`
   2. `micro-user`, `micro-project`, `micro-task`
   3. `micro-gateway`

   Check `http://localhost:8761`: all services should show as registered.
4. **Frontend:**
   ```bash
   cd task-management
   npm install
   npm run dev
   ```

## Docker

- [x] Docker images built for the services
- [ ] `docker-compose` setup with MySQL (in progress)

## Project Structure

```
├── EurekaServer/       # service registry
├── micro-gateway/      # API gateway + JWT filter
├── micro-user/         # auth and user service
├── micro-project/      # project service
├── micro-task/         # task service
└── task-management/    # React frontend
```

## Known Limitations and Next Steps

- Docker Compose with MySQL to run the whole stack with one command
- Each service currently uses the same MySQL schema; moving to a database per service would give full data isolation
- Role-based authorisation on endpoints (roles are already included in the JWT claims)
- User update endpoint, registration page in the UI, and automated tests

## Author

**Vaibhavi Jantralwala**
Java Backend Developer | [LinkedIn](https://linkedin.com/in/vaibhavijantralwala) | [GitHub](https://github.com/VaibhaviJantralwala)
