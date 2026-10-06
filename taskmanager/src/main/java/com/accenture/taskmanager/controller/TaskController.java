package com.accenture.taskmanager.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.accenture.taskmanager.model.Task;

/**
 * Controlador REST encargado de gestionar las peticiones HTTP relacionadas
 * con las tareas de la aplicación.
 *
 * @author Javier Roldán
 * @version 1.0
 */
@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {

    private static final List<Task> tasks = new ArrayList<>(); // -> medio generado con IA porque ya había escrito hasta List

    // Añadir 2 tareas de ejemplo al ArrayList para poder probar los endpoints -> generado con IA
    static {
        tasks.add(new Task(1L, "Tarea 1", "Descripción de la tarea 1", "Pendiente", "Alta", java.time.LocalDate.of(2024, 6, 30)));
        tasks.add(new Task(2L, "Tarea 2", "Descripción de la tarea 2", "En progreso", "Media", java.time.LocalDate.of(2024, 7, 15)));
    }

    // El Rest Controller debe implementar el endpoint GET para obtener todas las
    // tareas, utilizar ResponseEntity ->generado con IA
    /**
     * Obtiene todas las tareas disponibles.
     *
     * @return respuesta HTTP con la lista de tareas y código 200 OK
     */
    @GetMapping
    public ResponseEntity<Iterable<Task>> getAllTasks() {
        // Implementar la lógica para obtener todas las tareas
        return ResponseEntity.ok(tasks);
    }

    // El Rest Controller debe implementar el endpoint Get para obtener una tarea
    // por ID, utilizar ResponseEntity -> generado con IA
    /**
     * Obtiene una tarea a partir de su identificador.
     *
     * @param id identificador de la tarea que se desea obtener
     * @return respuesta HTTP con la tarea y código 200 OK, o código 404
     *         si la tarea no existe
     */
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        // Implementar la lógica para obtener una tarea por ID -> generado con IA
        for (Task task : tasks) {
            if (task.getId().equals(id)) {
                return ResponseEntity.ok(task);
            }
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Crea una nueva tarea.
     *
     * @param task tarea recibida en el cuerpo de la petición
     * @return respuesta HTTP con la tarea creada y código 201 CREATED
     */
    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        // Implementar la lógica para crear una tarea -> generado con IA
        tasks.add(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    // El Rest Controller debe implementar el endpoint PUT para actualizar una tarea
    // por ID, utilizar ResponseEntity -> generado con IA
    /**
     * Actualiza una tarea existente a partir de su identificador.
     *
     * @param id   identificador de la tarea que se desea actualizar
     * @param task datos de la tarea que se utilizarán para realizar la
     *             actualización
     * @return respuesta HTTP con la tarea actualizada y código 200 OK, o código 404
     *         si la tarea no existe
     */
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task task) {
        // Implementar la lógica para actualizar una tarea por ID -> generado con IA
        for (Task existingTask : tasks) {
            if (existingTask.getId().equals(id)) {
                existingTask.setTitle(task.getTitle());
                existingTask.setDescription(task.getDescription());
                existingTask.setStatus(task.getStatus());
                existingTask.setPriority(task.getPriority());
                existingTask.setDueDate(task.getDueDate());
                return ResponseEntity.ok(existingTask);
            }
        }
        return ResponseEntity.notFound().build();
    }

    // El Rest Controller debe implementar el endpoint DELETE para eliminar una
    // tarea por ID, utilizar ResponseEntity -> generado con IA
    /**
     * Elimina una tarea a partir de su identificador.
     *
     * @param id identificador de la tarea que se desea eliminar
     * @return respuesta HTTP con código 204 NO CONTENT si se elimina correctamente,
     *         o código 404 si la tarea no existe
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        // Implementar la lógica para eliminar una tarea por ID -> medio generado con IA
        // porque lo que me dió no valía porque era un 200
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getId().equals(id)) {
                tasks.remove(i);
                return ResponseEntity.noContent().build();
            }
        }

        return ResponseEntity.notFound().build();
    }
}
