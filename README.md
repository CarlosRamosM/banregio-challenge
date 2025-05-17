# 🏦 Challenge Técnico Banregio - API REST de Gestión de Pago de Prestamos

![Java](https://img.shields.io/badge/Java-23-orange?style=flat-square&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.5-brightgreen?style=flat-square&logo=springboot)
![Maven](https://img.shields.io/badge/Maven-3.8.4-C71A36?style=flat-square&logo=apache-maven)

## 📝 Descripción del Proyecto
Este proyecto es una solución al Challenge Técnico propuesto por Banregio como parte de su proceso de reclutamiento. Consiste en una API REST desarrollada con Spring Boot que permite la gestión de empleados y sus datos laborales.

## ⭐ Características Principales
* 🔄 Gestión de pago de prestamos cuando una cuenta cumple con los requisitos necesarios
* ✅ Validación de datos
* 💾 Persistencia de datos utilizando JPA
* 🔄 Manejo de migraciones de base de datos con Flyway
* 📚 Documentación de API con Swagger/OpenAPI
* 🏆 Implementación de buenas prácticas de desarrollo

## 🛠️ Tecnologías Utilizadas
* ☕ Java 23
* 🍃 Spring Boot 3.4.5
* 📊 Spring Data JPA
* 🌐 Spring MVC
* 🔧 Jakarta EE 10
* 🎯 Lombok
* 📦 Maven 3.8.4
* 🔄 Flyway

## 🏗️ Estructura del Proyecto
El proyecto sigue una arquitectura en capas:
* 🎮 `Controllers`: Manejo de endpoints REST
* ⚙️ `Services`: Lógica de negocio
* 💽 `Repositories`: Acceso a datos
* 📋 `Models/Entities`: Entidades y modelos de datos
* 🔄 `DTOs`: Objetos de transferencia de datos
* ✔️ `Validators`: Validaciones personalizadas

## 🚀 Instalación y Ejecución

### 📋 Prerequisitos
* ☕ Java 23
* 📦 Maven 3.8.4+
* 🗄️ PostgreSQL

### ⚡ Pasos de Instalación

1. Clonar el repositorio:
   git clone https://github.com/carlosramosm/banregio-challenge.git

2. Configurar base de datos en application.yaml:
    - URL: jdbc:mysql://localhost:3306/banregio_db
    - Usuario: [TU_USUARIO]
    - Contraseña: [TU_CONTRASEÑA]

3. Para ejecutar:
    - `mvn spring-boot:run`

🌐 URL local: http://localhost:8080

## 📚 Documentación API

URLs importantes:
* Swagger UI: http://localhost:8080/swagger-ui.html
* API Docs: http://localhost:8080/v3/api-docs

## 🧪 Pruebas

Ejecutar pruebas:

`mvn test`