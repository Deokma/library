# 📚 Library — Web Application for PDF Document Storage

> A pet project — a web application for storing, reading, and downloading PDF documents, with role-based user management.

---

## 📖 Description

**Library** is a full-stack web application built with **Spring Boot** that acts as a personal digital library. Users can browse the catalog, read PDF books directly in the browser, and download them. Administrators have extended access to manage the book catalog and user accounts.

---

## ✨ Features

### For Users
- Browse the library catalog
- Read PDF documents in-browser
- Download PDF files

### For Administrators
- Add, edit, and delete books from the catalog
- Manage user accounts (create, block, assign roles)

### Planned
- 💬 Book reviews and ratings

---

## 🗂️ Project Structure

```
library/
├── src/
│   ├── main/
│   │   ├── java/         # Backend — controllers, services, models, repositories
│   │   └── resources/
│   │       ├── templates/    # Thymeleaf HTML templates
│   │       ├── static/       # CSS, JavaScript
│   │       └── application.properties
│   └── test/             # Unit and integration tests
├── .mvn/wrapper/         # Maven wrapper
├── pom.xml               # Dependencies and build config
└── mvnw / mvnw.cmd       # Maven wrapper scripts
```

---

## 🛠️ Tech Stack

| Component        | Technology                        |
|------------------|-----------------------------------|
| Language         | Java                              |
| Framework        | Spring Boot                       |
| Template Engine  | Thymeleaf                         |
| Security         | Spring Security                   |
| Build Tool       | Maven                             |
| Frontend         | HTML, CSS, JavaScript             |
| Database         | SQL (MySQL / PostgreSQL)          |

---

## 🚀 Getting Started

### Prerequisites

- **JDK 17+**
- **MySQL** or **PostgreSQL** running locally
- **Maven** (or use the included `mvnw` wrapper)

### 1. Configure the database

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/library
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 2. Run the application

```bash
# Linux / macOS
./mvnw spring-boot:run

# Windows
mvnw.cmd spring-boot:run
```

The app will be available at `http://localhost:8080`.

---

## 👤 User Roles

| Role          | Permissions                                      |
|---------------|--------------------------------------------------|
| `USER`        | Browse catalog, read and download PDFs           |
| `ADMIN`       | All of the above + manage books and users        |

---
