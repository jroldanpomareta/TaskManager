package com.accenture.taskmanager.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.accenture.taskmanager.enums.TaskPriority;
import com.accenture.taskmanager.enums.TaskStatus;
import com.accenture.taskmanager.exceptions.TaskNotFoundException;
import com.accenture.taskmanager.model.Task;
import com.accenture.taskmanager.service.interfaces.TaskService;

/**
 * Implementación del servicio encargado de gestionar la lógica de negocio
 * relacionada con las tareas.
 *
 * <p>
 * Esta clase implementa las operaciones definidas en la interfaz
 * {@link TaskService} y mantiene temporalmente las tareas en una lista
 * en memoria.
 * </p>
 *
 * @author Javier Roldán
 * @version 1.0
 */
@Service
public class TaskServiceImpl implements TaskService {

    // Crea una lista igual que en el controlador para simular la persistencia de
    // datos y añade 2 tareas de ejemplo
    // al ArrayList usando el constructor de la clase Task para poder probar los
    // endpoints -> generado con IA
    // Actualiza las tareas de ejemplo para adaptarlas al nuevo constructor de Task,
    // Para los valores de status, consulta directamente el archivo
    // src/main/java/com/accenture/taskmanager/enums/TaskStatus.java y utiliza
    // exactamente
    // los nombres de los enums declarados allí, sin traducirlos ni inventarlos.
    // Para los valores de priority, consulta directamente el archivo
    // src/main/java/com/accenture/taskmanager/enums/TaskPriority.java y utiliza
    // exactamente
    // los nombres de los enums declarados allí, sin traducirlos ni inventarlos.
    // Añade también los valores correspondientes a createdAt y categoryId, no
    // pongas PENDIENTE, ni ALTA, ni EN_PROGRESO ni MEDIA.
    // —> generado con IA (sería el comentario genérico para que actualizara pero con los null en campos nuevos ya no aplica)
    private static final List<Task> tasks = new ArrayList<>();

    static {
        tasks.add(new Task(1L, "Tarea 1", "Descripción de la tarea 1",
                // esto no lo he sacado a constantes porque has dicho que no toquemos nada de aquí pero sí que me acuerdo eh?
                TaskStatus.PENDING, TaskPriority.LOW,
                java.time.LocalDate.of(2024, 6, 30), null, null));

        tasks.add(new Task(2L, "Tarea 2", "Descripción de la tarea 2",
                TaskStatus.IN_PROGRESS, TaskPriority.HIGH,
                java.time.LocalDate.of(2024, 7, 15), null, null));
    }

    /**
     * Obtiene todas las tareas disponibles.
     *
     * @return un Iterable que contiene todas las tareas.
     */
    // Implementación de los métodos de la interfaz TaskService
    @Override
    public Iterable<Task> findAll() {
        // Lógica para encontrar una tarea por su ID
        // Obtener todas las tareas y devolverlas como un Iterable<Task> -> generado con
        // IA
        return tasks; // Reemplazar con la implementación real
    }

    /**
     * Busca una tarea por su identificador.
     *
     * @param id identificador de la tarea que se desea buscar.
     * @return la tarea encontrada o null si no existe.
     */
    @Override
    public Task findById(Long id) {
        // Lógica para encontrar una tarea por su ID
        // Buscar la tarea en la lista de tareas por su ID y devolverla -> generado con
        // IA
        for (Task task : tasks) {
            if (task.getId().equals(id)) {
                return task; // Reemplazar con la implementación real
            }
        }
        return null; // Reemplazar con la implementación real
    }

    /**
     * Crea una nueva tarea y la añade a la lista de tareas.
     *
     * @param task tarea que se desea crear.
     * @return la tarea creada.
     */
    @Override
    public Task create(Task task) {
        // Lógica para crear una nueva tarea
        // Añadir la tarea nueva a la lista de tareas y devolverla en el return ->
        // generado con IA
        tasks.add(task);
        return task; // Reemplazar con la implementación real
    }

    /**
     * Actualiza los datos de una tarea existente.
     *
     * @param id   identificador de la tarea que se desea actualizar.
     * @param task objeto que contiene los nuevos datos de la tarea.
     * @return la tarea actualizada o null si no existe.
     */
    @Override
    public Task update(Long id, Task task) {
        // Lógica para actualizar una tarea existente
        // Buscar la tarea en la lista de tareas por su ID y actualizar sus campos con
        // los valores del objeto task -> generado con IA
        for (Task existingTask : tasks) {
            if (existingTask.getId().equals(id)) {
                existingTask.setTitle(task.getTitle());
                existingTask.setDescription(task.getDescription());
                existingTask.setStatus(task.getStatus());
                existingTask.setPriority(task.getPriority());
                existingTask.setDueDate(task.getDueDate());
                return existingTask; // Reemplazar con la implementación real
            }
        }
        return null; // Reemplazar con la implementación real
    }

    /**
     * Elimina una tarea por su identificador.
     *
     * @param id identificador de la tarea que se desea eliminar.
     * @throws TaskNotFoundException si no existe una tarea con el identificador
     *                               indicado.
     */
    @Override
    public void delete(Long id) {
        // Lógica para eliminar una tarea por su ID
        // Eliminar una tarea de la lista de tareas por su ID, comprobar antes si la
        // tarea existe y si no existe lanzar una excepción -> generado con IA
        // Cambia la excepción lanzada a TaskNotFoundException para indicar que la tarea
        // no existe
        Task taskToDelete = findById(id);
        if (taskToDelete != null) {
            tasks.remove(taskToDelete);
        } else {
            throw new TaskNotFoundException("La tarea con ID " + id + " no existe.");
        }

    }

}