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

import com.accenture.taskmanager.dto.PagedResponseDTO;
import com.accenture.taskmanager.dto.TaskRequestDTO;
import com.accenture.taskmanager.dto.TaskResponseDTO;
import com.accenture.taskmanager.enums.TaskPriority;
import com.accenture.taskmanager.enums.TaskStatus;
import com.accenture.taskmanager.exceptions.ErrorResponse;
import com.accenture.taskmanager.model.Task;
import com.accenture.taskmanager.service.interfaces.TaskService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;

/**
 * Controlador REST encargado de gestionar las peticiones HTTP relacionadas
 * con las tareas de la aplicación.
 *
 * @author Javier Roldán
 * @version 1.0
 */
// Documenta este controller con @Tag, indicando que agrupa las operaciones relacionadas con las tareas de la API. Utiliza un nombre y una descripción en español. No modifiques la lógica existente. — generado por IA
@Tag(name = "Tareas", description = "Operaciones relacionadas con las tareas de la API")
@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {

    /**
     * Servicio de gestión de tareas.
     */
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
    // Adapta getAllTasks para devolver
    // ResponseEntity<PagedResponseDTO<TaskResponseDTO>> usando
    // taskService.findAll(pageable), conservando los parámetros de paginación,
    // ordenación y el estado HTTP 200 OK. — generado por IA
    // Añade tres anotaciones @ApiResponse: una para el código 200 con la respuesta paginada, otra para el 400 y otra para el 500. En las respuestas de error utiliza @Content(schema = @Schema(implementation = ErrorResponse.class)). No modifiques el método. — generado por IA
    @Operation(summary = "Obtiene todas las tareas con paginación y ordenación", 
               description = "Devuelve una lista paginada de tareas, con opciones de ordenación y tamaño de página.")
    @ApiResponse(responseCode = "200", description = "Tareas encontradas", content = @Content(schema = @Schema(implementation = PagedResponseDTO.class)))
    @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping
    public ResponseEntity<PagedResponseDTO<TaskResponseDTO>> getAllTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort) {

        size = Math.min(size, 100);
        String[] sortParams = sort.split(",");
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.fromString(sortParams[1]), sortParams[0]));

        PagedResponseDTO<TaskResponseDTO> tasks = taskService.findAll(pageable);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    /**
     * Obtiene una tarea mediante su identificador.
     * 
     * @param id identificador de la tarea que se desea obtener
     * @return respuesta HTTP con la tarea encontrada y estado 200 OK, o estado 404
     *         NOT_FOUND si no existe
     */
    // Adapta getTaskById para devolver ResponseEntity<TaskResponseDTO> usando
    // taskService.findById(id). Conserva la ruta y el estado HTTP 200 OK cuando se
    // encuentra la tarea. — generado por IA
    // Añade @Operation y @ApiResponse para documentar en español la consulta de una tarea por ID, incluyendo las respuestas 200, 404 y 500 que correspondan al comportamiento real; en los errores 404 y 500 utiliza ErrorResponse mediante @Content y @Schema(implementation = ErrorResponse.class), sin modificar la lógica existente. — generado por IA
    @Operation(
            summary = "Obtiene una tarea por ID",
            description = "Devuelve los detalles de una tarea específica identificada por su ID."
    )
    @ApiResponse(responseCode = "200", description = "Tarea encontrada", content = @Content(schema = @Schema(implementation = TaskResponseDTO.class)))
    @ApiResponse(responseCode = "404", description = "Tarea no encontrada", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = ErrorResponse.class))) 
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> getTaskById(@PathVariable Long id) {
        TaskResponseDTO task = taskService.findById(id);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    /**
     * Crea una nueva tarea.
     * 
     * @param task objeto que contiene los datos de la tarea a crear
     * @return respuesta HTTP con la tarea creada y estado 201 CREATED
     */
    // Adapta createTask para recibir TaskRequestDTO, llamar a taskService.create y
    // devolver ResponseEntity<TaskResponseDTO> con estado HTTP 201 CREATED. —
    // generado por IA
    // Añade @Valid al parámetro TaskRequestDTO del método POST para activar las validaciones antes de ejecutar el servicio. No modifiques la lógica del método ni el endpoint existente. — generado por IA
    // Añade @Operation y @ApiResponse para documentar en español la creación de una tarea, incluyendo las respuestas HTTP 201 y los errores que realmente pueda producir; para los errores 400 y 500 utiliza ErrorResponse mediante @Content y @Schema(implementation = ErrorResponse.class), sin modificar la lógica existente. — generado por IA
    
    @Operation(
            summary = "Crea una nueva tarea",
            description = "Permite crear una nueva tarea con los datos proporcionados."
    )
    @ApiResponse(responseCode = "201", description = "Tarea creada correctamente", content = @Content(schema = @Schema(implementation = TaskResponseDTO.class)))
    @ApiResponse(responseCode = "400", description = "Datos de la tarea inválidos", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PostMapping
    public ResponseEntity<TaskResponseDTO> createTask(@RequestBody @Valid TaskRequestDTO task) {
        TaskResponseDTO createdTask = taskService.create(task);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    /**
     * Actualiza una tarea existente.
     *
     * @param id   identificador de la tarea que se desea actualizar
     * @param task objeto que contiene los datos actualizados de la tarea
     * @return respuesta HTTP con la tarea actualizada y estado 200 OK
     */
    // Adapta updateTask para recibir TaskRequestDTO, llamar a taskService.update y
    // devolver ResponseEntity<TaskResponseDTO> con estado HTTP 200 OK. — generado
    // por IA
    // Añade @Valid al parámetro TaskRequestDTO del método PUT para activar las validaciones antes de ejecutar el servicio. No modifiques la lógica del método ni el endpoint existente. — generado por IA
    // Documenta este endpoint de actualización de tareas con @Operation y @ApiResponse. Describe en español su propósito, el parámetro id, el cuerpo TaskRequestDTO y la respuesta HTTP 200 con TaskResponseDTO. Documenta también las respuestas HTTP 400, 404 y 500 cuando correspondan al comportamiento real, utilizando ErrorResponse mediante @Content(schema = @Schema(implementation = ErrorResponse.class)) para los errores. No modifiques la lógica existente ni las validaciones. — generado por IA
    @Operation(
            summary = "Actualiza una tarea existente",
            description = "Permite actualizar los datos de una tarea específica identificada por su ID."
    )
    @ApiResponse(responseCode = "200", description = "Tarea actualizada correctamente", content = @Content(schema = @Schema(implementation = TaskResponseDTO.class)))
    @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Tarea no encontrada", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> updateTask(
            @PathVariable Long id,
            @RequestBody @Valid TaskRequestDTO task) {

        TaskResponseDTO updatedTask = taskService.update(id, task);
        return new ResponseEntity<>(updatedTask, HttpStatus.OK);
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
    // Eliminar el try/catch de deleteTask y dejar que TaskNotFoundException sea gestionada por GlobalExceptionHandler. Mantener la respuesta HTTP 204 cuando la eliminación tenga éxito y conservar el Javadoc. — generado por IA
    // Documenta este endpoint de eliminación de tareas con @Operation y @ApiResponse. Describe en español su propósito, el parámetro id y la respuesta HTTP 204 sin cuerpo. Documenta también las respuestas HTTP 404 y 500 cuando correspondan al comportamiento real, utilizando ErrorResponse mediante @Content(schema = @Schema(implementation = ErrorResponse.class)) para los errores. No modifiques la lógica existente. — generado por IA
    @Operation(
            summary = "Elimina una tarea por ID",
            description = "Permite eliminar una tarea específica identificada por su ID."
    )
    @ApiResponse(responseCode = "204", description = "Tarea eliminada correctamente", content = @Content)
    @ApiResponse(responseCode = "404", description = "Tarea no encontrada", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
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
     * @param sort       criterio de ordenación (opcional, valor por defecto:
     *                   "createdAt,desc").
     * @return respuesta HTTP con la página de tareas filtradas y estado 200 OK.
     */
    // Documenta este endpoint con @Operation y @ApiResponse. Describe en español la consulta paginada de tareas por categoría, el parámetro categoryId y los parámetros page, size y sort, incluyendo la respuesta HTTP 200 y los errores 400, 404 y 500 cuando correspondan al comportamiento real; utiliza ErrorResponse mediante @Content(schema = @Schema(implementation = ErrorResponse.class)) para los errores. No modifiques la lógica existente. — generado por IA
    @Operation(
            summary = "Obtiene tareas por categoría",
            description = "Devuelve una lista paginada de tareas filtradas por la categoría especificada."
    )
    @ApiResponse(responseCode = "200", description = "Tareas obtenidas correctamente", content = @Content(schema = @Schema(implementation = PagedResponseDTO.class)))
    @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Categoría no encontrada", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/by-category/{categoryId}")
    public ResponseEntity<PagedResponseDTO<TaskResponseDTO>> getTasksByCategoryId(
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

        PagedResponseDTO<TaskResponseDTO> tasks =
        taskService.findByCategoryId(categoryId, pageable);
        return ResponseEntity.ok(tasks);
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
     * @param sort  criterio de ordenación (opcional, valor por defecto:
     *              "createdAt,desc").
     * @return respuesta HTTP con la página de tareas filtradas y estado 200 OK.
     */
    // Documenta este endpoint con @Operation y @ApiResponse. Describe en español la consulta paginada de tareas por tag, el parámetro tagId y los parámetros page, size y sort, incluyendo la respuesta HTTP 200 y los errores 400, 404 y 500 cuando correspondan al comportamiento real; utiliza ErrorResponse mediante @Content(schema = @Schema(implementation = ErrorResponse.class)) para los errores. No modifiques la lógica existente. — generado por IA
    @Operation(
            summary = "Obtiene tareas por etiqueta (tag)",
            description = "Devuelve una lista paginada de tareas filtradas por la etiqueta especificada."
    )
    @ApiResponse(responseCode = "200", description = "Tareas obtenidas correctamente", content = @Content(schema = @Schema(implementation = Page.class)))
    @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Etiqueta no encontrada", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
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
     * @param size     tamaño de página (opcional, valor por defecto: 10, máximo:
     *                 100).
     * @param sort     criterio de ordenación (opcional, valor por defecto:
     *                 "createdAt,desc").
     * @return respuesta HTTP con la página de tareas filtradas y estado 200 OK.
     */
    // Documenta este endpoint con @Operation y @ApiResponse. Describe en español la consulta paginada de tareas por estado y prioridad, los parámetros status, priority, page, size y sort, la respuesta HTTP 200 y los errores 400 y 500 cuando correspondan al comportamiento real; utiliza ErrorResponse mediante @Content(schema = @Schema(implementation = ErrorResponse.class)) para los errores. No modifiques la lógica existente. — generado por IA
    @Operation(
            summary = "Obtiene tareas por estado y prioridad",
            description = "Devuelve una lista paginada de tareas filtradas por el estado y la prioridad especificados."
    )
    @ApiResponse(responseCode = "200", description = "Tareas obtenidas correctamente", content = @Content(schema = @Schema(implementation = Page.class)))
    @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
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
     * Obtiene las tareas cuya fecha de vencimiento es anterior a la fecha
     * especificada.
     *
     * @param date fecha límite para filtrar las tareas.
     * @return respuesta HTTP con la lista de tareas filtradas y estado 200 OK.
     */
    // Documenta este endpoint con @Operation y @ApiResponse. Describe en español la consulta de tareas cuya fecha de vencimiento es anterior a la fecha indicada, el parámetro date y la respuesta HTTP 200; documenta también los errores 400 y 500 cuando correspondan al comportamiento real, utilizando ErrorResponse mediante @Content(schema = @Schema(implementation = ErrorResponse.class)) para los errores. No modifiques la lógica existente. — generado por IA
    @Operation(
            summary = "Obtiene tareas con fecha de vencimiento anterior a la indicada",
            description = "Devuelve una lista de tareas cuya fecha de vencimiento es anterior a la fecha especificada."
    )
    @ApiResponse(responseCode = "200", description = "Tareas obtenidas correctamente", content = @Content(schema = @Schema(implementation = Iterable.class)))
    @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
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
    // Documenta este endpoint con @Operation y @ApiResponse. Describe en español el recuento de tareas por estado, el parámetro status y la respuesta HTTP 200 con el número obtenido; documenta también los errores 400 y 500 cuando correspondan al comportamiento real, utilizando ErrorResponse mediante @Content(schema = @Schema(implementation = ErrorResponse.class)) para los errores. No modifiques la lógica existente. — generado por IA
    @Operation(
            summary = "Obtiene el conteo de tareas por estado",
            description = "Devuelve el número total de tareas que tienen el estado especificado."
    )
    @ApiResponse(responseCode = "200", description = "Conteo de tareas obtenido correctamente", content = @Content(schema = @Schema(implementation = Long.class)))
    @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @GetMapping("/count-by-status")
    public ResponseEntity<Long> getTaskCountByStatus(@RequestParam TaskStatus status) {
        long count = taskService.getCountByStatus(status);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }
}
