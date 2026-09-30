package com.accenture.taskmanager;

// Crear el modelo Task con los atributos: id, title, description, status, priority, dueDate, incluir sus tipos de datos más adecuados, añadir constructor vacío y por parámetros, añadir getters y setters
/**
 * Modelo que representa una tarea de la aplicación.
 *
 * @author Javier Roldán
 * @version 1.0
 */
public class Task {
    private int id;
    private String title;
    private String description;
    private String status;
    private String priority;
    private java.time.LocalDate dueDate;

    /**
     * Constructor vacío de la clase Task.
     */
    public Task() {
    }

    /**
     * Constructor de la clase Task con todos sus atributos.
     *
     * @param id          identificador de la tarea
     * @param title       título de la tarea
     * @param description descripción de la tarea
     * @param status      estado de la tarea
     * @param priority    prioridad de la tarea
     * @param dueDate     fecha límite de la tarea
     */
    public Task(int id, String title, String description, String status, String priority, java.time.LocalDate dueDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.dueDate = dueDate;
    }

    /**
     * Obtiene el identificador de la tarea.
     *
     * @return identificador de la tarea
     */
    public int getId() {
        return id;
    }

    /**
     * Modifica el identificador de la tarea.
     *
     * @param id nuevo identificador de la tarea
     */
    public void setId(int id) {
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
    public String getStatus() {
        return status;
    }

    /**
     * Modifica el estado de la tarea.
     *
     * @param status nuevo estado de la tarea
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Obtiene la prioridad de la tarea.
     *
     * @return prioridad de la tarea
     */
    public String getPriority() {
        return priority;
    }

    /**
     * Modifica la prioridad de la tarea.
     *
     * @param priority nueva prioridad de la tarea
     */
    public void setPriority(String priority) {
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
}