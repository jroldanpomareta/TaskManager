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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.accenture.taskmanager.enums.TaskPriority;
import com.accenture.taskmanager.enums.TaskStatus;
import com.accenture.taskmanager.exceptions.TaskNotFoundException;
import com.accenture.taskmanager.model.Task;
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

    /**
     * Crea el controlador de tareas e inyecta el servicio.
     *
     * @param taskService servicio encargado de gestionar las tareas
     */
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * Obtiene todas las tareas con paginación y ordenación.
     *
     * @param page número de página (opcional, valor por defecto: 0).
     * @param size tamaño de página (opcional, valor por defecto: 10, máximo: 100).
     * @param sort criterio de ordenación (opcional, valor por defecto: "createdAt,desc").
     * @return respuesta HTTP con la página de tareas y estado 200 OK.
     */
    @GetMapping
    public ResponseEntity<Page<Task>> getAllTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort) {

        size = Math.min(size, 100);

        String[] sortParams = sort.split(",");
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.fromString(sortParams[1]), sortParams[0]));

        Page<Task> tasks = taskService.findAll(pageable);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    /**
     * Obtiene una tarea mediante su identificador.
     *
     * @param id identificador de la tarea.
     * @return respuesta HTTP con la tarea encontrada y estado 200 OK,
     *         o estado 404 NOT_FOUND si no existe.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
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
     * @param task objeto que contiene los datos de la nueva tarea.
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
     * @param task objeto que contiene los datos actualizados de la tarea.
     * @return respuesta HTTP con la tarea actualizada y estado 200 OK,
     *         o estado 404 NOT_FOUND si no existe.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task task) {
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
     * @return respuesta HTTP con estado 204 NO_CONTENT si se eliminó correctamente,
     *         o estado 404 NOT_FOUND si no existe.
     */
    // modifica el método deleteTask para que capture la excepción
    // TaskNotFoundException y devuelva un estado 404 NOT_FOUND en lugar de
    // IllegalArgumentException -> generado con IA
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        try {
            taskService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (TaskNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Añadir los cinco endpoints GET de las consultas personalizadas, usando
    // @PathVariable o @RequestParam según corresponda y delegando en TaskService,
    // sin modificar el endpoint GET /api/v1/tasks existente. — generado por IA
    // Traer los métodos findAll, findById, create, update y delete, del service
    // para implementar los endpoints del controlador -> generado con IA Adaptar los
    // endpoints GET de listado general, por categoría, por tag y por estado y
    // prioridad para aceptar page, size y sort, con valores por defecto page=0,
    // size=10 y sort=createdAt,desc, limitando size a un máximo de 100 -> generado
    // por IA
    /**
     * Obtiene las tareas filtradas por categoría.
     *
     * @param categoryId identificador de la categoría.
     * @param page       número de página (opcional, valor por defecto: 0).
     * @param size       tamaño de página (opcional, valor por defecto: 10, máximo: 100).
     * @param sort       criterio de ordenación (opcional, valor por defecto: "createdAt,desc").
     * @return respuesta HTTP con la página de tareas filtradas y estado 200 OK.
     */ 
    @GetMapping("/by-category/{categoryId}")
    public ResponseEntity<Page<Task>> getTasksByCategoryId(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort) {

        size = Math.min(size, 100);
        String[] sortParams = sort.split(",");
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.fromString(sortParams[1]), sortParams[0]));

        Page<Task> tasks = taskService.findByCategoryId(categoryId, pageable);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    // Traer los métodos findAll, findById, create, update y delete, del service
    // para implementar los endpoints del controlador -> generado con IA
    // Adaptar los
    // endpoints GET de listado general, por categoría, por tag y por estado y
    // prioridad para aceptar page, size y sort, con valores por defecto page=0,
    // size=10 y sort=createdAt,desc, limitando size a un máximo de 100 -> generado
    // por IA
    /**
     * Obtiene las tareas filtradas por etiqueta (tag).
     *
     * @param tagId identificador de la etiqueta.
     * @param page  número de página (opcional, valor por defecto: 0).
     * @param size  tamaño de página (opcional, valor por defecto: 10, máximo: 100).
     * @param sort  criterio de ordenación (opcional, valor por defecto: "createdAt,desc").
     * @return respuesta HTTP con la página de tareas filtradas y estado 200 OK.
     */ 
    @GetMapping("/by-tag/{tagId}")
    public ResponseEntity<Page<Task>> getTasksByTagId(
            @PathVariable Long tagId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort) {

        size = Math.min(size, 100);
        String[] sortParams = sort.split(",");
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.fromString(sortParams[1]), sortParams[0]));

        Page<Task> tasks = taskService.findByTagId(tagId, pageable);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    // Traer los métodos findAll, findById, create, update y delete, del service
    // para implementar los endpoints del controlador -> generado con IA Adaptar los
    // endpoints GET de listado general, por categoría, por tag y por estado y
    // prioridad para aceptar page, size y sort, con valores por defecto page=0,
    // size=10 y sort=createdAt,desc, limitando size a un máximo de 100 -> generado
    // por IA
    /**
     * Obtiene las tareas filtradas por estado y prioridad.
     *
     * @param status   estado de la tarea (TaskStatus).
     * @param priority prioridad de la tarea (TaskPriority).
     * @param page     número de página (opcional, valor por defecto: 0).
     * @param size     tamaño de página (opcional, valor por defecto: 10, máximo: 100).
     * @param sort     criterio de ordenación (opcional, valor por defecto: "createdAt,desc").
     * @return respuesta HTTP con la página de tareas filtradas y estado 200 OK.
     */
    @GetMapping("/by-status-priority")
    public ResponseEntity<Page<Task>> getTasksByStatusAndPriority(
            @RequestParam TaskStatus status,
            @RequestParam TaskPriority priority,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort) {

        size = Math.min(size, 100);
        String[] sortParams = sort.split(",");
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.fromString(sortParams[1]), sortParams[0]));

        Page<Task> tasks = taskService.findByStatusAndPriority(
                status.name(), priority.name(), pageable);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    /**
     * Obtiene las tareas cuya fecha de vencimiento es anterior a la fecha especificada.
     *
     * @param date fecha límite para filtrar las tareas.
     * @return respuesta HTTP con la lista de tareas filtradas y estado 200 OK.
     */ 
    @GetMapping("/before-date")
    public ResponseEntity<Iterable<Task>> getTasksByDueDateBefore(@RequestParam java.time.LocalDate date) {
        Iterable<Task> tasks = taskService.findByDueDateBefore(date);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    /**
     * Obtiene el conteo de tareas filtradas por estado.
     *
     * @param status estado de la tarea (TaskStatus).
     * @return respuesta HTTP con el conteo de tareas y estado 200 OK.
     */
    @GetMapping("/count-by-status")
    public ResponseEntity<Long> getTaskCountByStatus(@RequestParam TaskStatus status) {
        long count = taskService.getCountByStatus(status);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }
}
