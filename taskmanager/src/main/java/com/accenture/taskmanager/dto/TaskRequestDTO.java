package com.accenture.taskmanager.dto;

import java.time.LocalDate;
import java.util.List;

import com.accenture.taskmanager.enums.TaskPriority;
import com.accenture.taskmanager.enums.TaskStatus;
import com.accenture.taskmanager.validation.RequiereFechaLimite;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object for Task requests.
 * This class is used to receive data from the client when creating or updating
 * a task.
 * 
 * @author Javier Roldán
 * @version 1.0
 */
// Crea la clase TaskRequestDTO para recibir los datos enviados por el cliente
// al crear o actualizar una tarea. Incluye los campos title (String),
// description (String), status (TaskStatus), priority (TaskPriority) y dueDate
// (LocalDate), con sus getters y setters, constructor vacío y constructor con
// todos los campos. Añade Javadoc de clase y de todos los métodos. No añadas
// validaciones. — generado por IA
//pon la anotación @RequiereFechaLimite
@RequiereFechaLimite
public class TaskRequestDTO {

    // Añade las anotaciones de Bean Validation definidas en el diseño previo a los campos de TaskRequestDTO, con mensajes de error en español. Conserva los campos, tipos, métodos y lógica existentes. — generado por IA
    @NotBlank(message = "El título no puede estar vacío")
    @Size(max = 150, message = "El título no puede superar los 150 caracteres")
    private String title;

    @Size(max = 500, message = "La descripción no puede superar los 500 caracteres")
    private String description;

    @NotNull(message = "El estado es obligatorio")
    private TaskStatus status;

    @NotNull(message = "La prioridad es obligatoria")
    private TaskPriority priority;

    @Future(message = "La fecha límite debe ser futura")
    private LocalDate dueDate;

    /**
     * Default constructor.
     */
    public TaskRequestDTO() {
    }

    /**
     * Constructor with all fields.
     *
     * @param title       the title of the task
     * @param description the description of the task
     * @param status      the status of the task
     * @param priority    the priority of the task
     * @param dueDate     the due date of the task
     */
    public TaskRequestDTO(String title, String description, TaskStatus status, TaskPriority priority,
            LocalDate dueDate) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.dueDate = dueDate;
    }

    /**
     * Gets the title of the task.
     *
     * @return the title of the task
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the task.
     *
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the description of the task.
     *
     * @return the description of the task
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the task.
     *
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the status of the task.
     *
     * @return the status of the task
     */
    public TaskStatus getStatus() {
        return status;
    }

    /**
     * Sets the status of the task.
     *
     * @param status the status to set
     */
    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    /**
     * Gets the priority of the task.
     *
     * @return the priority of the task
     */
    public TaskPriority getPriority() {
        return priority;
    }

    /**
     * Sets the priority of the task.
     *
     * @param priority the priority to set
     */
    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }

    /**
     * Gets the due date of the task.
     *
     * @return the due date of the task
     */
    public LocalDate getDueDate() {
        return dueDate;
    }

    /**
     * Sets the due date of the task.
     *
     * @param dueDate the due date to set
     */
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    // Añadir el campo categoryId de tipo Long a TaskRequestDTO para indicar la categoría de la tarea. Debe ser opcional, permitiendo crear tareas sin categoría. Añadir su getter, setter y Javadoc, sin modificar los campos existentes. — generado por IA
    private Long categoryId;

    /**
     * Gets the category ID of the task.
     *
     * @return the category ID of the task
     */
    public Long getCategoryId() {
        return categoryId;
    }

    /**
     * Sets the category ID of the task.
     *
     * @param categoryId the category ID to set
     */
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    // Añadir el campo List<Long> tagIds a TaskRequestDTO, con su getter y setter y Javadoc para el nuevo campo y sus métodos. Importar java.util.List y mantener todos los campos y métodos existentes sin modificarlos. Mantener este comentario en el código. — generado por IA
    private List<Long> tagIds;

    /**
     * Gets the tag IDs of the task.
     *
     * @return the tag IDs of the task
     */
    public List<Long> getTagIds() {
        return tagIds;
    }

    /**
     * Sets the tag IDs of the task.
     *
     * @param tagIds the tag IDs to set
     */
    public void setTagIds(List<Long> tagIds) {
        this.tagIds = tagIds;
    }
}