package com.accenture.taskmanager.service.interfaces;

import com.accenture.taskmanager.exceptions.TaskNotFoundException;
import com.accenture.taskmanager.model.Task;

/**
 * Interfaz que define las operaciones de servicio para gestionar las tareas.
 *
 * @author Javier Roldán
 * @version 1.0
 */
// Crear la interfaz TaskService que tenga los métodos findAll, findById, create, update y delete para la lógica de negocio de las tareas. ->generado con IA
public interface TaskService {

    /**
     * Obtiene todas las tareas.
     *
     * @return iterable con todas las tareas.
     */
    Iterable<Task> findAll();

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
     * @param id identificador de la tarea que se desea actualizar.
     * @param task datos actualizados de la tarea.
     * @return la tarea actualizada o null si no existe.
     */
    Task update(Long id, Task task);

    /**
     * Elimina una tarea por su identificador.
     *
     * @param id identificador de la tarea que se desea eliminar.
     * @throws TaskNotFoundException si no existe una tarea con el identificador indicado.
     */
    void delete(Long id);
}
