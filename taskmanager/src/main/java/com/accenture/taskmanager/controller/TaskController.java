package com.accenture.taskmanager.controller;

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

import com.accenture.taskmanager.Task;
import com.accenture.taskmanager.exceptions.TaskNotFoundException;
import com.accenture.taskmanager.service.interfaces.TaskService;

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

    // Crea una inyección de dependencias para el service, usa la interfaz
    // TaskService y la clase TaskServiceImpl, y utiliza el service para implementar
    // los endpoints del controlador
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * Obtiene todas las tareas.
     *
     * @return respuesta HTTP con la lista de tareas y estado 200 OK.
     */
    // Traer los métodos findAll, findById, create, update y delete, del service
    // para implementar los endpoints del controlador -> generado con IA
    @GetMapping
    public ResponseEntity<Iterable<Task>> getAllTasks() {
        Iterable<Task> tasks = taskService.findAll();
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    /**
     * Obtiene una tarea mediante su identificador.
     *
     * @param id identificador de la tarea.
     * @return respuesta HTTP con la tarea y estado 200 OK,
     *         o estado 404 NOT_FOUND si no existe.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable int id) {
        Task task = taskService.findById(id);
        if (task != null) {
            return new ResponseEntity<>(task, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Crea una nueva tarea.
     *
     * @param task tarea que se desea crear.
     * @return respuesta HTTP con la tarea creada y estado 201 CREATED.
     */
    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        Task createdTask = taskService.create(task);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    /**
     * Actualiza una tarea existente.
     *
     * @param id   identificador de la tarea que se desea actualizar.
     * @param task objeto que contiene los nuevos datos.
     * @return respuesta HTTP con la tarea actualizada y estado 200 OK,
     *         o estado 404 NOT_FOUND si no existe.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable int id, @RequestBody Task task) {
        Task updatedTask = taskService.update(id, task);
        if (updatedTask != null) {
            return new ResponseEntity<>(updatedTask, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Elimina una tarea mediante su identificador.
     *
     * @param id identificador de la tarea que se desea eliminar.
     * @throws TaskNotFoundException si no existe una tarea con el identificador
     *                               indicado.
     * @return respuesta HTTP con estado 204 NO_CONTENT si se elimina correctamente,
     *         o estado 404 NOT_FOUND si la tarea no existe.
     */
    // modifica el método deleteTask para que capture la excepción TaskNotFoundException y devuelva un estado 404 NOT_FOUND en lugar de IllegalArgumentException -> generado con IA
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable int id) {
        try {
            taskService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (TaskNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
