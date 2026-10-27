package com.accenture.taskmanager.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.accenture.taskmanager.enums.TaskPriority;
import com.accenture.taskmanager.enums.TaskStatus;

/**
 * Data Transfer Object for Task responses.
 * This class is used to send data to the client when retrieving a task.
 * 
 * @author Javier Roldán
 * @version 1.0
 */
//Crea la clase TaskResponseDTO para devolver los datos de una tarea al cliente. Incluye los campos id (Long), title (String), description (String), status (TaskStatus), priority (TaskPriority), dueDate (LocalDate) y createdAt (LocalDateTime). Añade getters, setters, constructor vacío y constructor con todos los campos. Incluye Javadoc de clase y todos los métodos. No añadas validaciones. — generado por IA
public class TaskResponseDTO {

    // Documenta el campo id con @Schema, indicando en español que es el identificador único de la tarea e incluyendo un ejemplo numérico representativo. Utiliza únicamente los atributos description y example. No modifiques la lógica existente. — generado por IA
    @io.swagger.v3.oas.annotations.media.Schema(description = "Identificador único de la tarea", example = "1")
    private Long id;
    // Documenta el campo title con @Schema, indicando en español que es el título de la tarea e incluyendo un ejemplo representativo. Utiliza únicamente los atributos description y example. No modifiques la lógica existente. — generado por IA
    @io.swagger.v3.oas.annotations.media.Schema(description = "Título de la tarea", example = "Comprar leche")
    private String title;
    // Documenta el campo description con @Schema, indicando en español que es la descripción de la tarea e incluyendo un ejemplo representativo. Utiliza únicamente los atributos description y example. No modifiques la lógica existente. — generado por IA
    @io.swagger.v3.oas.annotations.media.Schema(description = "Descripción de la tarea", example = "Comprar leche en el supermercado")
    private String description;
    // Documenta el campo status con @Schema, indicando en español que es el estado de la tarea e incluyendo un ejemplo representativo. Utiliza únicamente los atributos description y example. No modifiques la lógica existente. — generado por IA
    @io.swagger.v3.oas.annotations.media.Schema(description = "Estado de la tarea", example = "PENDING")
    private TaskStatus status;
    // Documenta el campo priority con @Schema, indicando en español que es la prioridad de la tarea e incluyendo un ejemplo representativo. Utiliza únicamente los atributos description y example. No modifiques la lógica existente. — generado por IA
    @io.swagger.v3.oas.annotations.media.Schema(description = "Prioridad de la tarea", example = "HIGH")
    private TaskPriority priority;
    // Documenta el campo dueDate con @Schema, indicando en español que es la fecha de vencimiento de la tarea e incluyendo un ejemplo representativo. Utiliza únicamente los atributos description y example. No modifiques la lógica existente. — generado por IA
    @io.swagger.v3.oas.annotations.media.Schema(description = "Fecha de vencimiento de la tarea", example = "2027-12-31")
    private LocalDate dueDate;
    // Documenta el campo createdAt con @Schema, indicando en español que es la fecha y hora de creación de la tarea e incluyendo un ejemplo representativo. Utiliza únicamente los atributos description y example. No modifiques la lógica existente. — generado por IA
    @io.swagger.v3.oas.annotations.media.Schema(description = "Fecha y hora de creación de la tarea", example = "2027-10-01T10:00:00")
    private LocalDateTime createdAt;

    /**
     * Default constructor.
     */
    public TaskResponseDTO() {
    }

    /**
     * Constructor with all fields.
     *
     * @param id          the ID of the task
     * @param title       the title of the task
     * @param description the description of the task
     * @param status      the status of the task
     * @param priority    the priority of the task
     * @param dueDate     the due date of the task
     * @param createdAt   the creation date and time of the task
     */
    public TaskResponseDTO(Long id, String title, String description, TaskStatus status, TaskPriority priority,
            LocalDate dueDate, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.dueDate = dueDate;
        this.createdAt = createdAt;
    }

    /**
     * Gets the ID of the task.
     *
     * @return the ID of the task
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the ID of the task.
     *
     * @param id the ID to set
     */
    public void setId(Long id) {
        this.id = id;
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

    /**
     * Gets the creation date and time of the task.
     *
     * @return the creation date and time of the task
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the creation date and time of the task.
     *
     * @param createdAt the creation date and time to set
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Añadir el campo categoryName de tipo String a TaskResponseDTO para devolver el nombre de la categoría asociada a la tarea, sin exponer la entidad Category completa. La categoría puede ser null. Añadir getter, setter y Javadoc, sin modificar los campos existentes. — generado por IA
    // Documenta el campo categoryName con @Schema, indicando en español que es el nombre de la categoría asociada a la tarea y que puede ser null si no tiene categoría. Incluye un ejemplo representativo. Utiliza únicamente los atributos description y example. No modifiques la lógica existente. — generado por IA
    @io.swagger.v3.oas.annotations.media.Schema(description = "Nombre de la categoría asociada a la tarea (puede ser null si no tiene categoría)", example = "Trabajo")
    private String categoryName;

    /**
     * Gets the name of the category associated with the task.
     *
     * @return the name of the category associated with the task
     */
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * Sets the name of the category associated with the task.
     *
     * @param categoryName the name of the category to set
     */
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    // Añadir el campo List<TagResponseDTO> tags a TaskResponseDTO, con su getter, setter y Javadoc para el campo y ambos métodos. Importar java.util.List y com.accenture.taskmanager.dto.TagResponseDTO. Mantener todos los campos y métodos existentes sin modificarlos y conservar este comentario en el código. — generado por IA
    // Documenta el campo tags con @Schema, indicando en español que es la lista de etiquetas asociadas a la tarea e incluyendo un ejemplo representativo. Utiliza únicamente los atributos description y example. No modifiques la lógica existente. — generado por IA
    @io.swagger.v3.oas.annotations.media.Schema(description = "Lista de etiquetas asociadas a la tarea", example = "[{\"id\":1,\"name\":\"Urgente\"},{\"id\":2,\"name\":\"Importante\"}]")
    private List<TagResponseDTO> tags;

    /**
     * Gets the tags associated with the task.
     *
     * @return the tags associated with the task
     */
    public List<TagResponseDTO> getTags() {
        return tags;
    }

    /**
     * Sets the tags associated with the task.
     *
     * @param tags the tags to set
     */
    public void setTags(List<TagResponseDTO> tags) {
        this.tags = tags;
    }
}