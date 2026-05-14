# 🛒 MobilePhoneshop Web Backend (Spring Boot)

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![MySQL](https://img.shields.io/badge/mysql-%2300f.svg?style=for-the-badge&logo=mysql&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens)

This is the robust backend REST API for the **Mobile Phone Shop Point of Sale (POS)** system. Built with Spring Boot, it handles secure authentication, complex inventory management, offline data synchronization, and transactional sales processing.

## ✨ Key Features

* **🔐 Secure Authentication:** Implements stateless JWT (JSON Web Token) authentication with Spring Security for secure API access.
* **👥 Role-Based Access Control (RBAC):** Differentiates permissions between `ADMIN` (full access) and `CASHIER` (billing access only).
* **🔄 Offline Sync Ready:** Designed to accept batched offline sales from the frontend PWA and process them synchronously without data loss.
* **📦 Inventory Management:** Full CRUD operations for Products and Categories with automated stock deduction upon sales.
* **💾 Data Integrity:** Uses Spring Data JPA with transactional boundaries (`@Transactional`) to ensure database consistency during complex multi-item sales.

## 🛠️ Tech Stack

* **Framework:** Java Spring Boot
* **Security:** Spring Security & JWT
* **Database:** MySQL
* **ORM:** Spring Data JPA / Hibernate
* **Build Tool:** Maven

## 🚀 Getting Started

### Prerequisites

* Java Development Kit (JDK) 17 or higher
* Maven installed
* MySQL Server running locally

### Installation & Setup

1. **Clone the repository:**
   ```bash
   git clone [https://github.com/prasindu/MobilePhoneshop-web-backend.git](https://github.com/prasindu/MobilePhoneshop-web-backend.git)
   cd MobilePhoneshop-web-backend
   ```
2. **Database Configuration:**
Open src/main/resources/application.properties and update your database credentials:

Properties
```bash
spring.datasource.url=jdbc:mysql://localhost:3306/mobilehub_db
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```
3. **Build the project:**

```Bash
mvn clean install
```
4. **Run the Application:**
```Bash
mvn spring-boot:run
```
The API will start running on http://localhost:8080.

##🔌 Core API Endpoints
Authentication
* POST /api/auth/login - Authenticate user and get JWT token

* POST /api/auth/register - Register a new user (Admin only)

Inventory (Products & Categories)
* GET /api/products - Retrieve all products

* POST /api/products - Add a new product

* PUT /api/products/{id} - Update existing product

* DELETE /api/products/{id} - Delete a product

Sales & Billing
* POST /api/sales - Process a new sale (deducts stock automatically)

* GET /api/sales - Retrieve sales history for analytics

Note: All endpoints except /api/auth/login require a valid JWT Bearer Token in the Authorization header.

## 🤝 Contributing
Contributions, issues, and feature requests are welcome! Feel free to check the issues page.

## 📄 License
This project is licensed under the MIT License.
