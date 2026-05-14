<div align="center">

<br/>

```
███╗   ███╗ ██████╗ ██████╗ ██╗██╗     ███████╗    ██╗  ██╗██╗   ██╗██████╗
████╗ ████║██╔═══██╗██╔══██╗██║██║     ██╔════╝    ██║  ██║██║   ██║██╔══██╗
██╔████╔██║██║   ██║██████╔╝██║██║     █████╗      ███████║██║   ██║██████╔╝
██║╚██╔╝██║██║   ██║██╔══██╗██║██║     ██╔══╝      ██╔══██║██║   ██║██╔══██╗
██║ ╚═╝ ██║╚██████╔╝██████╔╝██║███████╗███████╗    ██║  ██║╚██████╔╝██████╔╝
╚═╝     ╚═╝ ╚═════╝ ╚═════╝ ╚═╝╚══════╝╚══════╝    ╚═╝  ╚═╝ ╚═════╝ ╚═════╝
```

### 📱 Mobile Phone Shop — POS Backend REST API

*Secure by Design · Offline-Ready · Production Containerized*

<br/>

[![Java](https://img.shields.io/badge/Java_17+-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com/)
[![JWT](https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=JSON%20web%20tokens&logoColor=white)](https://jwt.io/)
[![Docker](https://img.shields.io/badge/Docker-2CA5E0?style=for-the-badge&logo=docker&logoColor=white)](https://www.docker.com/)
[![GitHub Actions](https://img.shields.io/badge/GitHub_Actions-2088FF?style=for-the-badge&logo=github-actions&logoColor=white)](https://github.com/features/actions)
[![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)](https://maven.apache.org/)

<br/>

[![API Status](https://img.shields.io/badge/API-🟢_Running_on_8080-2ea44f?style=for-the-badge)]()
[![License](https://img.shields.io/badge/License-MIT-yellow?style=for-the-badge)](LICENSE)

</div>

---

## 📖 Overview

A robust **REST API backend** for the **Mobile Phone Shop Point of Sale (POS)** system. Built with **Spring Boot**, it handles secure JWT authentication, role-based access control, real-time inventory management, offline data synchronization, and transactional sales processing — all containerized with **Docker** and automated via **GitHub Actions CI/CD**.

---

## ✨ Key Features

<table>
  <tr>
    <td>🔐 <strong>JWT Authentication</strong></td>
    <td>Stateless JSON Web Token authentication with Spring Security. Every protected endpoint requires a valid Bearer token.</td>
  </tr>
  <tr>
    <td>👥 <strong>Role-Based Access Control</strong></td>
    <td><code>ADMIN</code> — full system access. <code>CASHIER</code> — billing & checkout access only. Enforced at the API layer.</td>
  </tr>
  <tr>
    <td>🔄 <strong>Offline Sync Ready</strong></td>
    <td>Accepts batched offline sales from the frontend PWA and processes them synchronously — zero data loss guaranteed.</td>
  </tr>
  <tr>
    <td>📦 <strong>Inventory Management</strong></td>
    <td>Full CRUD for Products & Categories with automated stock deduction triggered on every confirmed sale.</td>
  </tr>
  <tr>
    <td>💾 <strong>Data Integrity</strong></td>
    <td>Spring Data JPA with <code>@Transactional</code> boundaries ensure database consistency during complex multi-item sales.</td>
  </tr>
  <tr>
    <td>🐳 <strong>Fully Containerized</strong></td>
    <td>Docker-based deployment. Runs identically across development, staging, and production environments.</td>
  </tr>
  <tr>
    <td>⚡ <strong>Automated CI/CD</strong></td>
    <td>GitHub Actions pipeline — build, test, dockerize, and deploy on every push to <code>main</code>.</td>
  </tr>
</table>

---

## 🛠️ Tech Stack

| Layer | Technology |
|-------|-----------|
| **Framework** | Java Spring Boot |
| **Security** | Spring Security & JWT |
| **Database** | postgresql |
| **ORM** | Spring Data JPA / Hibernate |
| **Build Tool** | Maven |
| **Containerization** | Docker & Docker Compose |
| **CI/CD** | GitHub Actions |

---

## 🏗️ CI/CD Architecture

A fully automated pipeline runs on every push to `main`:

```
┌─────────────┐    git push    ┌──────────────────┐   build+test   ┌──────────────┐
│             │───────────────▶│                  │───────────────▶│              │
│   GitHub    │                │  GitHub Actions  │                │  Docker Hub  │
│  (main)     │                │   (CI Pipeline)  │                │  (Registry)  │
└─────────────┘                └──────────────────┘                └──────┬───────┘
                                                                           │
                                                                      webhook pull
                                                                           │
                                                                  ┌────────▼────────┐
                                                                  │                 │
                                                                  │  Deployment     │
                                                                  │  Target         │
                                                                  │  (Azure / VPS)  │
                                                                  └────────┬────────┘
                                                                           │
                                                                  ┌────────▼───────────┐
                                                                  │ postgresql Database│
                                                                  └────────────────────┘
```

### Pipeline Jobs — `.github/workflows/ci-pipeline.yml`

```yaml
Trigger: push to main branch

  ① Checkout Code
  ② Set up JDK 17
  ③ Build with Maven  →  mvn clean install
  ④ Login to Docker Hub
  ⑤ Build Docker Image  →  prasindu1/mobilehub-backend:latest
  ⑥ Push Image to Docker Hub
  ⑦ Deploy to Target Environment
```

---

## 🔌 API Endpoints

### 🔑 Authentication
> Public endpoints — no token required

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/auth/login` | Authenticate user and receive JWT token |
| `POST` | `/api/auth/register` | Register a new user *(Admin only)* |

---

### 📦 Inventory — Products & Categories
> Requires: `Authorization: Bearer <token>`

| Method | Endpoint | Description | Role |
|--------|----------|-------------|------|
| `GET` | `/api/products` | Retrieve all products | All |
| `POST` | `/api/products` | Add a new product | Admin |
| `PUT` | `/api/products/{id}` | Update an existing product | Admin |
| `DELETE` | `/api/products/{id}` | Delete a product | Admin |

---

### 🧾 Sales & Billing
> Requires: `Authorization: Bearer <token>`

| Method | Endpoint | Description | Role |
|--------|----------|-------------|------|
| `POST` | `/api/sales` | Process a new sale *(auto stock deduction)* | All |
| `GET` | `/api/sales` | Retrieve sales history for analytics | Admin |

> **Note:** All endpoints except `/api/auth/login` require a valid **JWT Bearer Token** in the `Authorization` header.

---

## 🚀 Getting Started

### Prerequisites

- ☕ **JDK 17+**
- 🔧 **Maven**
- 🐬 **MySQL Server**
- 🐳 **Docker & Docker Compose** *(recommended)*

---

### ⚙️ Environment Configuration

Update `src/main/resources/application.properties`:

```properties
# ─── Database ────────────────────────────────────────────
spring.datasource.url=jdbc:mysql://localhost:3306/mobilehub_db
spring.datasource.username=root
spring.datasource.password=your_password

# ─── JPA / Hibernate ─────────────────────────────────────
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# ─── JWT Security ────────────────────────────────────────
app.jwt.secret=your_super_secret_key
app.jwt.expiration=86400000
```

> ⚠️ **Never commit real credentials.** Add `application.properties` to `.gitignore` and use environment variables in production.

---

### 🐳 Option 1 — Run with Docker *(Recommended)*

No Java or MySQL installation needed. Everything runs inside containers:

```bash
# Clone the repository
git clone https://github.com/prasindu/MobilePhoneshop-web-backend.git
cd MobilePhoneshop-web-backend

# Build and start the entire stack (API + MySQL)
docker-compose up --build
```

> ✅ API will be accessible at `http://localhost:8080`

**`docker-compose.yml`:**

```yaml
version: '3.8'
services:
  db:
    image: mysql:8
    environment:
      MYSQL_DATABASE: mobilehub_db
      MYSQL_ROOT_PASSWORD: your_password
    ports:
      - "3306:3306"

  api:
    build: .
    ports:
      - "8080:8080"
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://db:3306/mobilehub_db
      SPRING_DATASOURCE_USERNAME: root
      SPRING_DATASOURCE_PASSWORD: your_password
    depends_on:
      - db
```

---

### 🛠️ Option 2 — Run with Maven

```bash
# Clone the repository
git clone https://github.com/prasindu/MobilePhoneshop-web-backend.git
cd MobilePhoneshop-web-backend

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

> ✅ API will be accessible at `http://localhost:8080`

---

## 📁 Project Structure

```
MobilePhoneshop-web-backend/
├── .github/
│   └── workflows/
│       └── ci-pipeline.yml          # GitHub Actions CI/CD
├── src/
│   └── main/
│       ├── java/
│       │   └── com/prasindu/mobilehub/
│       │       ├── controller/       # REST API Controllers
│       │       ├── service/          # Business Logic Layer
│       │       ├── repository/       # Spring Data JPA Repos
│       │       ├── model/            # JPA Entity Classes
│       │       ├── dto/              # Request/Response DTOs
│       │       └── security/         # JWT & Spring Security
│       └── resources/
│           └── application.properties
├── Dockerfile
├── docker-compose.yml
└── pom.xml
```

---

## 🛡️ Security Summary

| Layer | Implementation |
|-------|---------------|
| **Authentication** | JWT Bearer Tokens (stateless) |
| **Authorization** | Role-based (`ADMIN` / `CASHIER`) via Spring Security |
| **Password Storage** | BCrypt hashing |
| **Database** | SSL-secured connection in production |
| **Secrets** | Environment variables — never hardcoded |

---

## 🤝 Contributing

Contributions, issues, and feature requests are welcome!

1. Fork the repository
2. Create your feature branch: `git checkout -b feature/amazing-feature`
3. Commit your changes: `git commit -m 'Add some amazing feature'`
4. Push to the branch: `git push origin feature/amazing-feature`
5. Open a Pull Request

---

## 📄 License

This project is licensed under the **MIT License** — see the [LICENSE](LICENSE) file for details.

---

<div align="center">

<br/>

**Developed with ❤️ by [Prasindu Deshan](https://github.com/prasindu)**

*Transforming ideas into scalable cloud solutions* ☁️

<br/>

[![GitHub](https://img.shields.io/badge/GitHub_Profile-181717?style=for-the-badge&logo=github&logoColor=white)](https://github.com/prasindu)
[![Docker Hub](https://img.shields.io/badge/Docker_Hub-2CA5E0?style=for-the-badge&logo=docker&logoColor=white)](https://hub.docker.com/r/prasindu1/mobilehub-backend)

<br/>

</div>