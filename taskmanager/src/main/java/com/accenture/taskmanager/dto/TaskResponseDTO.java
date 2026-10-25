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

    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private LocalDate dueDate;
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