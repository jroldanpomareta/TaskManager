package com.accenture.taskmanager.mapper;

import java.util.Collections;
import java.util.stream.Collectors;

import com.accenture.taskmanager.dto.TaskRequestDTO;
import com.accenture.taskmanager.dto.TaskResponseDTO;
import com.accenture.taskmanager.model.Task;

/**
 * Mapper class for Task entities and DTOs.
 * This class is used to convert between Task entities and their corresponding
 * Data Transfer Objects (DTOs).
 * 
 * @author Javier Roldán
 * @version 1.0
 */
public class TaskMapper {

    // Implementa el método toResponseDTO(Task task) para transformar una entidad
    // Task en un TaskResponseDTO, copiando id, title, description, status,
    // priority, dueDate y createdAt. Incluye Javadoc del método. — generado por IA

    // Completa el método toResponseDTO(Task task) creando un único TaskResponseDTO con los campos id, title, description, status, priority, dueDate y createdAt. Después, asigna categoryName usando el nombre de la categoría asociada a task, comprobando que task.getCategory() no sea null. Devuelve el mismo DTO al que has asignado categoryName. No expongas la entidad Category completa. Mantén el estilo y el Javadoc existentes. — generado por IA
    /**
     * Converts a Task entity to a TaskResponseDTO.
     *
     * @param task the Task entity to convert
     * @return the corresponding TaskResponseDTO
     */
    public static TaskResponseDTO toResponseDTO(Task task) {
        TaskResponseDTO dto = new TaskResponseDTO();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setStatus(task.getStatus());
        dto.setPriority(task.getPriority());
        dto.setDueDate(task.getDueDate());
        dto.setCreatedAt(task.getCreatedAt());

        if (task.getCategory() != null) {
            dto.setCategoryName(task.getCategory().getName());
        }
        // Asignar al campo tags del DTO la lista de etiquetas asociadas a task, convirtiendo cada entidad Tag en un TagResponseDTO mediante TagMapper. Si task o su lista de etiquetas es null, evitar errores y mantener una lista vacía. Añadir los imports necesarios y conservar el código existente. — generado por IA
        if (task.getTags() != null) {
            dto.setTags(task.getTags().stream()
                    .map(TagMapper::toTagResponseDTO)
                    .collect(Collectors.toList()));
        } else {
            dto.setTags(Collections.emptyList());
        }
        return dto;
    }

    // Implementa el método toEntity(TaskRequestDTO dto) para transformar un
    // TaskRequestDTO en una entidad Task, copiando title, description, status,
    // priority y dueDate. Incluye Javadoc del método. — generado por IA
    /**
     * Converts a TaskRequestDTO to a Task entity.
     *
     * @param dto the TaskRequestDTO to convert
     * @return the corresponding Task entity
     */
    public static Task toEntity(TaskRequestDTO dto) {
        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setStatus(dto.getStatus());
        task.setPriority(dto.getPriority());
        task.setDueDate(dto.getDueDate());
        return task;
    }
}
