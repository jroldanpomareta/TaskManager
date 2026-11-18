# Proyecto incremental de evaluación continua realizado por partes

# 🚀 TaskManager API

<p align="center">

**REST API para la gestión de tareas, categorías y etiquetas**

Desarrollada con Java y Spring Boot.

</p>

---

## 📌 Descripción

TaskManager es una API REST diseñada para gestionar tareas de forma organizada, permitiendo crear, consultar, actualizar y eliminar tareas, así como administrar categorías y etiquetas.

El proyecto implementa una arquitectura por capas, autenticación mediante JWT, validación de datos, gestión de errores y documentación de la API.

## 🛠️ Tecnologías utilizadas

- ☕ Java 17
- 🌱 Spring Boot 4.1.1
- 🌐 Spring Web
- 🔐 Spring Security
- 🔑 JWT
- 🗄️ Spring Data JPA
- 🐬 MySQL
- ✅ Bean Validation
- 📖 OpenAPI / Swagger
- 🧪 JUnit y Mockito
- 💾 H2 para pruebas
- 📊 JaCoCo
- 🏗️ Maven

## ✨ Funcionalidades

- 📋 Gestión completa de tareas (CRUD).
- 🗂️ Gestión de categorías.
- 🏷️ Gestión de etiquetas.
- 🔎 Consulta de tareas por identificador.
- 📄 Paginación de resultados.
- ↕️ Ordenación de resultados.
- 🔐 Autenticación mediante JWT.
- 👤 Registro e inicio de sesión de usuarios.
- 🛡️ Protección de endpoints mediante Spring Security.
- ✅ Validación de datos de entrada.
- ⚠️ Manejo centralizado de excepciones.
- 📚 Documentación de endpoints mediante Swagger.
- 🧪 Pruebas automatizadas.

## 🏗️ Arquitectura

El proyecto sigue una arquitectura por capas para separar responsabilidades:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
Database
```

Además, utiliza DTOs y Mappers para separar los datos de entrada y salida de las entidades.

```text
📦 taskmanager
 ┣ 📂 config
 ┣ 📂 controller
 ┣ 📂 dto
 ┣ 📂 enums
 ┣ 📂 exceptions
 ┣ 📂 mapper
 ┣ 📂 model
 ┣ 📂 repository
 ┣ 📂 security
 ┣ 📂 service
 ┣ 📂 validation
 ┗ 📂 test
```

## 🔐 Seguridad

La API utiliza Spring Security y JWT para proteger los endpoints.

Incluye:

- Registro de usuarios.
- Autenticación.
- Generación de tokens JWT.
- Protección de recursos.
- Control de acceso.

## 📖 Documentación de la API

Una vez iniciada la aplicación, la documentación interactiva está disponible mediante Swagger UI:

```text
http://localhost:8080/swagger-ui/index.html
```

Documentación OpenAPI:

```text
http://localhost:8080/v3/api-docs
```

## ⚙️ Instalación y ejecución

### 1. Clonar el repositorio

```bash
git clone https://github.com/jroldanpomareta/TaskManager.git
```

### 2. Acceder al proyecto

```bash
cd TaskManager
```

### 3. Configurar la base de datos

Configura la conexión a MySQL en el archivo `application.properties`.

### 4. Ejecutar la aplicación

```bash
mvn spring-boot:run
```

### 5. Ejecutar los tests

```bash
mvn clean test
```

## 🧪 Testing

El proyecto incluye pruebas automatizadas para comprobar el correcto funcionamiento de sus componentes.

Para ejecutarlas:

```bash
mvn clean test
```

También se utiliza **JaCoCo** para generar informes de cobertura de código.

## 📸 Capturas

En la carpeta `capturas/` se incluyen evidencias del funcionamiento de la API, pruebas, validaciones, paginación y operaciones realizadas.

## 🎯 Objetivos del proyecto

Este proyecto forma parte de mi formación en desarrollo Backend con Java y Spring Boot, y me ha permitido trabajar con:

- Desarrollo de APIs REST.
- Arquitectura por capas.
- Persistencia de datos.
- Seguridad y autenticación.
- Testing.
- Validación y gestión de errores.
- Documentación de APIs.
- Buenas prácticas de desarrollo.

---

## 👨‍💻 Autor

**Javier Roldán**

Proyecto desarrollado como parte de mi formación en Java Backend y Spring Boot.

---

⭐ Si te resulta interesante este proyecto, puedes explorar el repositorio y sus funcionalidades.
