<div align="center">

<br/>

```
██████╗  ██████╗ ███████╗    ███████╗██╗   ██╗███████╗████████╗███████╗███╗   ███╗
██╔══██╗██╔═══██╗██╔════╝    ██╔════╝╚██╗ ██╔╝██╔════╝╚══██╔══╝██╔════╝████╗ ████║
██████╔╝██║   ██║███████╗    ███████╗ ╚████╔╝ ███████╗   ██║   █████╗  ██╔████╔██║
██╔═══╝ ██║   ██║╚════██║    ╚════██║  ╚██╔╝  ╚════██║   ██║   ██╔══╝  ██║╚██╔╝██║
██║     ╚██████╔╝███████║    ███████║   ██║   ███████║   ██║   ███████╗██║ ╚═╝ ██║
╚═╝      ╚═════╝ ╚══════╝    ╚══════╝   ╚═╝   ╚══════╝   ╚═╝   ╚══════╝╚═╝     ╚═╝
```

### ☁️ Cloud-Native Point of Sale Backend API

*Engineered for Scale · Automated by Design · Deployed to the Cloud*

<br/>

[![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Docker](https://img.shields.io/badge/Docker-2CA5E0?style=for-the-badge&logo=docker&logoColor=white)](https://www.docker.com/)
[![GitHub Actions](https://img.shields.io/badge/GitHub_Actions-2088FF?style=for-the-badge&logo=github-actions&logoColor=white)](https://github.com/features/actions)
[![Microsoft Azure](https://img.shields.io/badge/Microsoft_Azure-0078D4?style=for-the-badge&logo=microsoft-azure&logoColor=white)](https://azure.microsoft.com/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)](https://www.postgresql.org/)

<br/>

[![API Status](https://img.shields.io/badge/API_Status-🟢_Online_%26_Healthy-2ea44f?style=for-the-badge)](https://prasindu-pos-api-e5bbfnb3b0ezbehh.southeastasia-01.azurewebsites.net/api)
[![Java](https://img.shields.io/badge/Java-17+-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://openjdk.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow?style=for-the-badge)](LICENSE)

</div>

---

## 🌐 Live Environment

> The API is live and production-ready on **Microsoft Azure Southeast Asia** region.

| Property | Value |
|----------|-------|
| 🔗 **Base URL** | `https://prasindu-pos-api-e5bbfnb3b0ezbehh.southeastasia-01.azurewebsites.net/api` |
| 🐳 **Docker Image** | `prasindu1/pos-backend:latest` |
| 🌍 **Region** | Southeast Asia (Azure) |
| 📦 **Database** | NeonDB Serverless PostgreSQL |

---

## 🏗️ Architecture & DevOps Pipeline

This project implements a fully automated **CI/CD pipeline** following a Cloud-Native architecture pattern.

```
┌─────────────┐     push      ┌──────────────────┐    build     ┌──────────────┐
│             │──────────────▶│                  │─────────────▶│              │
│   GitHub    │               │  GitHub Actions   │              │  Docker Hub  │
│             │               │   (CI Pipeline)   │              │  (Registry)  │
└─────────────┘               └──────────────────┘              └──────┬───────┘
                                                                        │
                                                                   webhook pull
                                                                        │
                                                               ┌────────▼────────┐
                                                               │                 │
                                                               │  Azure Web App  │
                                                               │  for Containers │
                                                               │                 │
                                                               └────────┬────────┘
                                                                        │
                                                               ┌────────▼────────┐
                                                               │  NeonDB (Cloud  │
                                                               │   PostgreSQL)   │
                                                               └─────────────────┘
```

### Pipeline Steps

| Step | Tool | Description |
|------|------|-------------|
| **1. Version Control** | GitHub | Code is pushed to `main` branch |
| **2. CI Trigger** | GitHub Actions | Workflow automatically triggered on push |
| **3. Build** | Maven | Spring Boot `.jar` is compiled and packaged |
| **4. Containerize** | Docker | A Docker image is built from the `.jar` |
| **5. Publish** | Docker Hub | Image pushed as `prasindu1/pos-backend:latest` |
| **6. Deploy** | Azure Webhook | Azure pulls the latest image and redeploys |

---

## 🚀 Features

<table>
  <tr>
    <td>🔐 <strong>JWT Authentication</strong></td>
    <td>Secure user login and authorization powered by Spring Security with stateless JWT tokens</td>
  </tr>
  <tr>
    <td>📦 <strong>Inventory Management</strong></td>
    <td>Real-time stock tracking, product CRUD operations, and category management</td>
  </tr>
  <tr>
    <td>🧾 <strong>Billing & Checkout</strong></td>
    <td>Full cart lifecycle — item calculation, discount application, and sale finalization</td>
  </tr>
  <tr>
    <td>🐳 <strong>Fully Containerized</strong></td>
    <td>Docker-based deployment guarantees consistent behavior across all environments</td>
  </tr>
  <tr>
    <td>☁️ <strong>Cloud Database</strong></td>
    <td>Persistent storage on NeonDB Serverless PostgreSQL with SSL-secured connections</td>
  </tr>
  <tr>
    <td>⚡ <strong>CI/CD Automation</strong></td>
    <td>Zero-touch deployments on every push to <code>main</code> via GitHub Actions</td>
  </tr>
</table>

---

## 💻 Local Development Setup

### Prerequisites

- ☕ **Java 17+**
- 🔧 **Maven**
- 🐳 **Docker & Docker Compose** *(recommended)*

---

### ⚙️ Environment Configuration

Create an `application.properties` file (or set as environment variables):

```properties
# ─── Database ────────────────────────────────────────────
spring.datasource.url=jdbc:postgresql://<NEON_DB_URL>:5432/neondb?sslmode=require
spring.datasource.username=<YOUR_DB_USER>
spring.datasource.password=<YOUR_DB_PASSWORD>

# ─── JWT Security ────────────────────────────────────────
app.jwt.secret=<YOUR_JWT_SECRET>
```

> ⚠️ **Warning:** Never commit real credentials to version control. Use `.gitignore` and secrets management in production.

---

### 🐳 Run with Docker *(Recommended)*

No need to install Java or PostgreSQL locally. Just run:

```bash
# Build and spin up the entire stack (Backend + Database)
docker-compose up --build
```

> ✅ API will be accessible at: `http://localhost:8080/api`

---

### 🛠️ Run with Maven

```bash
# Install dependencies and package
mvn clean install

# Start the application
mvn spring-boot:run
```

---

## 🔄 CI/CD Workflow

**File:** `.github/workflows/ci-pipeline.yml`  
**Trigger:** Every push to the `main` branch

```yaml
Jobs executed in order:
  ① Checkout Code
  ② Login to Docker Hub
  ③ Build Spring Boot JAR  →  Dockerize  →  Push to Docker Hub
  ④ Authenticate with Azure Publishing Profile
  ⑤ Redeploy Azure Web App Container
```

---

## 🛡️ Security

| Layer | Implementation |
|-------|---------------|
| **Authentication** | JWT (JSON Web Tokens) via Spring Security |
| **CORS Policy** | Configured for `localhost`, Vercel, and Netlify frontends |
| **Database** | SSL-enforced connection (`sslmode=require`) |
| **Secrets Management** | Azure App Settings (Environment Variables) in production |

---

## 📁 Project Structure

```
pos-backend/
├── .github/
│   └── workflows/
│       └── ci-pipeline.yml       # GitHub Actions CI/CD
├── src/
│   └── main/
│       ├── java/
│       │   └── com/prasindu/pos/
│       │       ├── controller/   # REST API Controllers
│       │       ├── service/      # Business Logic
│       │       ├── repository/   # Data Access Layer
│       │       ├── model/        # JPA Entities
│       │       ├── dto/          # Data Transfer Objects
│       │       └── security/     # JWT & Spring Security
│       └── resources/
│           └── application.properties
├── Dockerfile
├── docker-compose.yml
└── pom.xml
```

---

<div align="center">

<br/>

**Developed with ❤️ by [Prasindu Deshan](https://github.com/prasindu)**

*Transforming ideas into scalable cloud solutions* ☁️

<br/>

[![GitHub](https://img.shields.io/badge/Follow_on_GitHub-181717?style=for-the-badge&logo=github&logoColor=white)](https://github.com/prasindu)
[![Docker Hub](https://img.shields.io/badge/Docker_Hub-2CA5E0?style=for-the-badge&logo=docker&logoColor=white)](https://hub.docker.com/r/prasindu1/pos-backend)

<br/>

</div>