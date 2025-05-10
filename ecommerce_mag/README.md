# 🏬 ECOMMERCE_MAG

A **Java 21 & Spring Boot 3.4.4** microservice responsible for **supplying product data** to other services in the e-commerce ecosystem.

This service exposes REST endpoints to fetch product information and publishes product updates via **Kafka Streams**.

---

## 📦 Features

✅ REST API to access product data  
✅ Kafka Streams integration for product event streaming  
✅ Built with Spring Boot for rapid development  
✅ Java 21 compatibility  
✅ Docker-ready with Spring Boot Maven plugin

---

## 🚀 Getting Started

### 🖥️ Prerequisites

Ensure the following are installed:

- Java **21+**
- Maven **3.8+**
- Docker
- Docker Compose (optional)

---

## 🏗️ Building the Application

Build the application using Maven:

```bash
mvn clean install
```

---

## 🐳 Running the Application

The `docker-compose.yml` defines service

### Start All Services

```bash
docker compose up -d
```

and then start the app to produce data by run button