
INSERT INTO category (id, name, description) VALUES
    (1, 'Backend', 'Tareas de backend'),
    (2, 'Frontend', 'Tareas de frontend');

INSERT INTO task (id, title, description, status, priority, due_date, created_at, category_id) VALUES
    (1, 'Tarea PENDING alta', 'Descripcion', 'PENDING', 'HIGH', '2026-06-01', NOW(), 1),
    (2, 'Tarea IN_PROGRESS media', 'Descripcion', 'IN_PROGRESS', 'MEDIUM', '2026-06-15', NOW(), 1),
    (3, 'Tarea DONE baja', 'Descripcion', 'DONE', 'LOW', '2026-01-01', NOW(), 2);

INSERT INTO tag (id, name) VALUES
    (1, 'urgente'),
    (2, 'bug');

INSERT INTO task_tag (task_id, tag_id) VALUES
    (1, 1),
    (1, 2),
    (2, 1);

-- Reinicia el contador de IDs para evitar conflictos con los IDs explícitos
ALTER TABLE task ALTER COLUMN id RESTART WITH 100;
