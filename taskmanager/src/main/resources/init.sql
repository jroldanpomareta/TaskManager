-- Active: 1780738611428@@127.0.0.1@3306@mysql
-- =====================================================================
-- init.sql — Task Manager
-- =====================================================================

CREATE DATABASE IF NOT EXISTS taskmanager
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE taskmanager;

DROP TABLE IF EXISTS task_tag;
DROP TABLE IF EXISTS task;
DROP TABLE IF EXISTS tag;
DROP TABLE IF EXISTS category;


CREATE TABLE category (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    description VARCHAR(255)
) ENGINE = InnoDB;


CREATE TABLE tag (
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
) ENGINE = InnoDB;


CREATE TABLE task (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    title       VARCHAR(150)  NOT NULL,
    description VARCHAR(1000),
    status      VARCHAR(20)   NOT NULL,
    priority    VARCHAR(20)   NOT NULL,
    due_date    DATE,
    created_at  DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    category_id BIGINT,
    CONSTRAINT fk_task_category FOREIGN KEY (category_id) REFERENCES category (id)
) ENGINE = InnoDB;


CREATE TABLE task_tag (
    task_id BIGINT NOT NULL,
    tag_id  BIGINT NOT NULL,
    PRIMARY KEY (task_id, tag_id),
    CONSTRAINT fk_tasktag_task FOREIGN KEY (task_id) REFERENCES task (id),
    CONSTRAINT fk_tasktag_tag  FOREIGN KEY (tag_id)  REFERENCES tag (id)
) ENGINE = InnoDB;


INSERT INTO category (name, description) VALUES
    ('Backend',       'Tareas de desarrollo del lado del servidor'),
    ('Frontend',      'Tareas de interfaz de usuario'),
    ('DevOps',        'Infraestructura, CI/CD y despliegues'),
    ('Documentacion', 'Documentacion tecnica y de usuario');

INSERT INTO tag (name) VALUES
    ('urgente'),
    ('bug'),
    ('mejora'),
    ('refactor'),
    ('investigacion');

INSERT INTO task (title, description, status, priority, due_date, created_at, category_id) VALUES
    ('Configurar Spring Data JPA',            'Añadir el starter y configurar la conexión a MySQL', 'DONE',        'HIGH',   '2026-01-10', '2026-01-02 09:00:00', 1),
    ('Crear entidad Task',                    'Mapear la clase Task con anotaciones JPA',            'DONE',        'HIGH',   '2026-01-12', '2026-01-02 09:05:00', 1),
    ('Implementar TaskRepository',            'Extender JpaRepository y añadir query methods',       'IN_PROGRESS', 'HIGH',   '2026-01-20', '2026-01-03 10:00:00', 1),
    ('Añadir validaciones a TaskRequestDTO',  'Bean Validation en los campos de entrada',            'PENDING',     'MEDIUM', '2026-02-01', '2026-01-05 11:30:00', 1),
    ('Manejo global de errores',              'GlobalExceptionHandler con @RestControllerAdvice',    'PENDING',     'MEDIUM', '2026-02-05', '2026-01-06 08:15:00', 1),
    ('Diseñar pantalla de login',             'Mockup de la pantalla de acceso',                     'IN_PROGRESS', 'MEDIUM', '2026-01-25', '2026-01-04 12:00:00', 2),
    ('Maquetar listado de tareas',            'Tabla con filtros por estado y prioridad',            'PENDING',     'LOW',    '2026-02-10', '2026-01-07 09:45:00', 2),
    ('Integrar consumo de la API REST',       'Conectar el frontend con los endpoints del backend',  'PENDING',     'MEDIUM', '2026-02-15', '2026-01-08 14:20:00', 2),
    ('Configurar pipeline de CI',             'Build y tests automáticos en cada push',              'PENDING',     'HIGH',   '2026-01-30', '2026-01-05 16:00:00', 3),
    ('Dockerizar la aplicación',              'Dockerfile multi-stage para el backend',              'PENDING',     'MEDIUM', '2026-02-20', '2026-01-09 10:10:00', 3),
    ('Configurar perfil de producción',       'application-prod.properties con variables de entorno','PENDING',     'LOW',    '2026-02-25', '2026-01-10 11:00:00', 3),
    ('Documentar la API con Swagger',         'springdoc-openapi para la documentación interactiva', 'PENDING',     'LOW',    '2026-03-01', '2026-01-11 09:30:00', 4),
    ('Escribir README del proyecto',          'Instrucciones de instalación y arranque',             'DONE',        'LOW',    '2026-01-15', '2026-01-02 15:00:00', 4),
    ('Revisar seguridad con JWT',             'Filtro de autenticación y generación de tokens',      'PENDING',     'HIGH',   NULL,         '2026-01-12 13:00:00', NULL),
    ('Tarea sin categoría asignar',           'Pendiente de triage',                                 'PENDING',     'LOW',    NULL,         '2026-01-13 17:45:00', NULL);

INSERT INTO task_tag (task_id, tag_id) VALUES
    (1, 3), (2, 3), (3, 1), (3, 3), (4, 2), (5, 2), (5, 1),
    (6, 3), (8, 4), (9, 1), (10, 4), (14, 1), (14, 5);

CREATE TABLE app_user (
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role     VARCHAR(20) NOT NULL
) ENGINE = InnoDB;