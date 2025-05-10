# 🛒 ECOMMERCE SHOP

A Java 21 & Spring Boot 3.4.4 backend application for managing an e-commerce platform. Provides REST APIs for product, order, and customer management, integrates PostgreSQL, Kafka Streams, and Liquibase for migrations.

Built with Docker support for easy deployment.

---

## 📦 Features

✅ Product, order, and customer management  
✅ PostgreSQL database integration  
✅ Kafka Streams for event processing  
✅ Liquibase for database migrations  
✅ API documentation via OpenAPI (SpringDoc)  
✅ JWT-secured API endpoints  
✅ Exchange rate integration (NBP API + CCA)  
✅ Dockerized deployment with Spring Boot buildpacks

---

## 🚀 Getting Started

### 🖥️ Prerequisites

Make sure the following are installed:

- Java **21+**
- Maven **3.8+**
- Docker
- Docker Compose

---

## 🏗️ Building the Application

Build the application using Maven:

```bash
mvn clean install
```

---

## 🐳 Running the Application

Running the application requires the `ecommerce_mag` Docker environment.

The `docker-compose.yml` defines two services:

| Service  | Description                  | Ports    |
|----------|-----------------------------|----------|
| db       | PostgreSQL database          | 5432     |
| backend  | Spring Boot backend API      | 8082     |

### Start All Services

```bash
docker compose up -d
```