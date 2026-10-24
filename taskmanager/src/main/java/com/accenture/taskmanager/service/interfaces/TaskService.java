package com.accenture.taskmanager.service.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.accenture.taskmanager.enums.TaskPriority;

import com.accenture.taskmanager.enums.TaskStatus;
import com.accenture.taskmanager.exceptions.TaskNotFoundException;
import com.accenture.taskmanager.model.Task;

/**
 * Interfaz que define las operaciones de servicio para gestionar las tareas.
 *
 * @author Javier Roldán
 * @version 1.0
 */
// Crear la interfaz TaskService que tenga los métodos findAll, findById,
// create, update y delete para la lógica de negocio de las tareas. ->generado
// con IA
// Añadir los métodos findByStatus y findByPriority para buscar tareas por
// estado y prioridad, respectivamente. -> generado por IA
// Adaptar los métodos de listado para aceptar Pageable, delegar la paginación
// al Repository y devolver Page<Task>, conservando la lógica existente. —>
// generado por IA
public interface TaskService {

    /**
     * Obtiene todas las tareas.
     *
     * @return iterable con todas las tareas.
     */
    Page<Task> findAll(Pageable pageable);

    /**
     * Busca una tarea por su identificador.
     *
     * @param id identificador de la tarea que se desea buscar.
     * @return la tarea encontrada o null si no existe.
     */
    Task findById(Long id);

    /**
     * Crea una nueva tarea.
     *
     * @param task tarea que se desea crear.
     * @return la tarea creada.
     */
    Task create(Task task);

    /**
     * Actualiza una tarea existente.
     *
     * @param id   identificador de la tarea que se desea actualizar.
     * @param task datos actualizados de la tarea.
     * @return la tarea actualizada o null si no existe.
     */
    Task update(Long id, Task task);

    /**
     * Elimina una tarea por su identificador.
     *
     * @param id identificador de la tarea que se desea eliminar.
     * @throws TaskNotFoundException si no existe una tarea con el identificador
     *                               indicado.
     */
    void delete(Long id);

    /**
     * Busca tareas por su estado.
     *
     * @param status estado de las tareas que se desean buscar.
     * @return tareas encontradas.
     */
    Iterable<Task> findByStatus(TaskStatus status);

    /**
     * Busca tareas por su prioridad.
     *
     * @param priority prioridad de las tareas que se desean buscar.
     * @return tareas encontradas.
     */
    Iterable<Task> findByPriority(TaskPriority priority);

    /**
     * Busca tareas por su estado y prioridad.
     *
     * @param status   estado de las tareas que se desean buscar.
     * @param priority prioridad de las tareas que se desean buscar.
     * @param pageable información de paginación.
     * @return iterable con las tareas encontradas.
     */
    Page<Task> findByStatusAndPriority(String status, String priority, Pageable pageable);

    // Añadir los métodos de servicio para las cinco consultas, delegando en
    // TaskRepository y devolviendo los resultados correspondientes, sin añadir
    // lógica de negocio innecesaria. — generado por IA
    /**
     * Busca tareas por su estado y prioridad, con paginación.
     *
     * @param status   estado de las tareas que se desean buscar.
     * @param priority prioridad de las tareas que se desean buscar.
     * @param pageable información de paginación y ordenación.
     * @return página de tareas que coinciden con el estado y la prioridad
     *         indicados.
     */
    Page<Task> findByCategoryId(Long categoryId, Pageable pageable);

    /**
     * Busca tareas por su etiqueta.
     *
     * @param tagId    identificador de la etiqueta que se desea buscar.
     * @param pageable información de paginación.
     * @return iterable con las tareas encontradas.
     */
    Page<Task> findByTagId(Long tagId, Pageable pageable);

    /**
     * Busca tareas cuya fecha de vencimiento sea anterior a la fecha indicada.
     *
     * @param date fecha límite para filtrar las tareas.
     * @return tareas cuya fecha de vencimiento es anterior a la fecha indicada.
     */
    Iterable<Task> findByDueDateBefore(java.time.LocalDate date);

    /**
     * Obtiene el número de tareas por estado.
     *
     * @param status estado de las tareas que se desea contar.
     * @return número de tareas con el estado indicado.
     */
    long getCountByStatus(TaskStatus status);
}