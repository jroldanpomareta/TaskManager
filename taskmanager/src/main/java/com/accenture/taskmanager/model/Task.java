package com.accenture.taskmanager.model;

import com.accenture.taskmanager.enums.TaskPriority;
import com.accenture.taskmanager.enums.TaskStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.EnumType;

// Crear el modelo Task con los atributos: id, title, description, status, priority, dueDate, incluir sus tipos de datos más adecuados, añadir constructor vacío y por parámetros, añadir getters y setters -> generado por IA
// Añadir las anotaciones JPA necesarias en la clase Task para mapearla con la tabla task, incluyendo @Column en los campos cuyo nombre no coincide con la columna SQL (due_date, created_at, category_id) y añadir los imports necesarios, sin sustituir ningún código existente. —> generado por IA
// Añadir las anotaciones JPA necesarias en la clase Task para mapearla con la tabla task, incluyendo @Entity, @Table, @Id y @GeneratedValue, y añadir los imports necesarios, sin sustituir ningún código existente. —> generado por IA
// Añade @Enumerated(EnumType.STRING) a los campos status y priority y conviértelos en enums con los nombres TaskStatus y TaskPriority -> generado por IA
/**
 * Modelo que representa una tarea de la aplicación.
 *
 * @author Javier Roldán
 * @version 1.0
 */
@Entity
@Table(name = "task")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    @Enumerated(EnumType.STRING)
    private TaskStatus status;
    @Enumerated(EnumType.STRING)
    private TaskPriority priority;
    @Column(name = "due_date")
    private java.time.LocalDate dueDate;
    @Column(name = "created_at")
    private java.time.LocalDateTime createdAt;
    @Column(name = "category_id")
    private Long categoryId;

    /**
     * Constructor vacío de la clase Task.
     */
    public Task() {
    }

    // cambia el constructor para que los atributos status y priority sean de tipo TaskStatus y TaskPriority, respectivamente, y añade los imports necesarios, sin sustituir ningún código existente. —> generado por IA
    /**
     * Constructor de la clase Task con todos sus atributos.
     *
     * @param id          identificador de la tarea
     * @param title       título de la tarea
     * @param description descripción de la tarea
     * @param status      estado de la tarea
     * @param priority    prioridad de la tarea
     * @param dueDate     fecha límite de la tarea
     * @param createdAt   fecha de creación de la tarea
     * @param categoryId  identificador de la categoría a la que pertenece la tarea
     */
    public Task(Long id, String title, String description, TaskStatus status, TaskPriority priority,
            java.time.LocalDate dueDate, java.time.LocalDateTime createdAt, Long categoryId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.dueDate = dueDate;
        this.createdAt = createdAt;
        this.categoryId = categoryId;
    }

    // ajusta los getters y setters de los atributos status y priority para que devuelvan y acepten valores de tipo TaskStatus y TaskPriority, respectivamente, y añade los imports necesarios, sin sustituir ningún código existente. —> generado por IA
    /**
     * Obtiene el identificador de la tarea.
     *
     * @return identificador de la tarea
     */
    public Long getId() {
        return id;
    }

    /**
     * Modifica el identificador de la tarea.
     *
     * @param id nuevo identificador de la tarea
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtiene el título de la tarea.
     *
     * @return título de la tarea
     */
    public String getTitle() {
        return title;
    }

    /**
     * Modifica el título de la tarea.
     *
     * @param title nuevo título de la tarea
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Obtiene la descripción de la tarea.
     *
     * @return descripción de la tarea
     */
    public String getDescription() {
        return description;
    }

    /**
     * Modifica la descripción de la tarea.
     *
     * @param description nueva descripción de la tarea
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Obtiene el estado de la tarea.
     *
     * @return estado de la tarea
     */
    public TaskStatus getStatus() {
        return status;
    }

    /**
     * Modifica el estado de la tarea.
     *
     * @param status nuevo estado de la tarea
     */
    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    /**
     * Obtiene la prioridad de la tarea.
     *
     * @return prioridad de la tarea
     */
    public TaskPriority getPriority() {
        return priority;
    }

    /**
     * Modifica la prioridad de la tarea.
     *
     * @param priority nueva prioridad de la tarea
     */
    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }

    /**
     * Obtiene la fecha límite de la tarea.
     *
     * @return fecha límite de la tarea
     */
    public java.time.LocalDate getDueDate() {
        return dueDate;
    }

    /**
     * Modifica la fecha límite de la tarea.
     *
     * @param dueDate nueva fecha límite de la tarea
     */
    public void setDueDate(java.time.LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    //pon los getters y setters de los atributos createdAt y categoryId, y añade los imports necesarios, sin sustituir ningún código existente. —> generado por IA
    /**
     * Obtiene la fecha de creación de la tarea.
     *
     * @return fecha de creación de la tarea
     */
    public java.time.LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Modifica la fecha de creación de la tarea.
     *
     * @param createdAt nueva fecha de creación de la tarea
     */
    public void setCreatedAt(java.time.LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Obtiene la categoría de la tarea.
     *
     * @return categoría de la tarea
     */
    public Long getCategoryId() {
        return categoryId;
    }

    /**
     * Modifica la categoría de la tarea.
     *
     * @param categoryId nueva categoría de la tarea
     */
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}