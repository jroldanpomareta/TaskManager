package com.accenture.taskmanager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.accenture.taskmanager.enums.TaskPriority;
import com.accenture.taskmanager.enums.TaskStatus;
import com.accenture.taskmanager.model.Task;

// Crear TaskRepository extendiendo JpaRepository y añadir los métodos findByStatus, findByPriority y findByStatusAndPriority para realizar búsquedas por estos campos -> generado por IA
// corrige los métodos para que utilicen los tipos correctos que son enums en status y priority. -> generado por IA
// Modificar los métodos findByCategoryId, findByTagId y findByStatusAndPriority para aceptar Pageable y devolver Page<Task>, manteniendo sus consultas actuales. — generado por IA
/**
 * Repository interface for managing Task entities.
 * Provides methods for querying tasks based on status, priority, category, and tags.
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    /**
     * Finds tasks by their status.
     *
     * @param status the status of the tasks to find
     * @return a list of tasks with the specified status
     */
    List<Task> findByStatus(TaskStatus status);
    
    /**
     * Finds tasks by their priority.
     *
     * @param priority the priority of the tasks to find
     * @return a list of tasks with the specified priority
     */
    List<Task> findByPriority(TaskPriority priority);
    /**
     * Finds tasks by their status and priority.
     *
     * @param status the status of the tasks to find
     * @param priority the priority of the tasks to find
     * @param pageable the page information
     * @return a page of tasks with the specified status and priority
     */
    Page<Task> findByStatusAndPriority(TaskStatus status, TaskPriority priority, Pageable pageable);
    // Buscar por categoría
    // Buscar tareas por categoría usando JPQL y @Query, filtrando por el ID de la categoría relacionada con Task. — generado por IA
    @Query("SELECT t FROM Task t WHERE t.category.id = :categoryId")
    /**
     * Finds tasks by their category ID.
     *
     * @param categoryId the ID of the category to filter tasks
     * @param pageable the page information
     * @return a page of tasks belonging to the specified category
     */
    Page<Task> findByCategoryId(@Param("categoryId") Long categoryId, Pageable pageable);
    // Buscar tareas por tag usando JPQL y @Query, realizando un JOIN con la relación tags de Task y filtrando por el ID del tag. — generado por IA
    @Query("SELECT t FROM Task t JOIN t.tags tag WHERE tag.id = :tagId")
    /**
     * Finds tasks by their tag ID.
     *
     * @param tagId the ID of the tag to filter tasks
     * @param pageable the page information
     * @return a page of tasks associated with the specified tag
     */
    Page<Task> findByTagId(@Param("tagId") Long tagId, Pageable pageable);
    // Buscar tareas cuya fecha límite dueDate sea anterior a una fecha recibida como parámetro, utilizando JPQL con @Query y @Param. — generado por IA
    @Query("SELECT t FROM Task t WHERE t.dueDate < :date")
    /**
     * Finds tasks by their due date, which is before a given date.
     *
     * @param date the date to compare with the due date
     * @return a list of tasks with a due date before the specified date
     */
    List<Task> findByDueDateBefore(@Param("date") java.time.LocalDate date );
    // Contar las tareas que tienen un estado concreto utilizando JPQL con COUNT y un parámetro status. — generado por IA
    @Query("SELECT COUNT(t) FROM Task t WHERE t.status = :status")
    /**
     * Counts the number of tasks with a specific status.
     *
     * @param status the status to filter tasks
     * @return the count of tasks with the specified status
     */
    long countByStatus(@Param("status") TaskStatus status);
}
