package com.accenture.taskmanager.service;

import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.accenture.taskmanager.enums.TaskPriority;
import com.accenture.taskmanager.enums.TaskStatus;
import com.accenture.taskmanager.exceptions.TaskNotFoundException;
import com.accenture.taskmanager.model.Task;
import com.accenture.taskmanager.repository.TaskRepository;
import com.accenture.taskmanager.service.interfaces.TaskService;

import jakarta.transaction.Transactional;

/**
 * Implementación del servicio encargado de gestionar la lógica de negocio
 * relacionada con las tareas.
 *
 * <p>
 * Esta clase implementa las operaciones definidas en la interfaz
 * {@link TaskService} y utiliza {@link TaskRepository} para acceder
 * a las tareas almacenadas en la base de datos.
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
    // —> generado con IA (sería el comentario genérico para que actualizara pero
    // con los null en campos nuevos ya no aplica)
    // Sustituir la lista estática por una dependencia de TaskRepository inyectada
    // mediante constructor. -> generado por IA
    /**
     * Repositorio de tareas utilizado para acceder a los datos almacenados en la
     * base de datos.
     */
    private final TaskRepository taskRepository;
    // private static final List<Task> tasks = new ArrayList<>();

    // Crear el constructor que reciba TaskRepository y lo asigne al atributo
    // taskRepository. -> generado por IA
    /**
     * Crea una nueva instancia de TaskServiceImpl con el repositorio de tareas
     * proporcionado.
     *
     * @param taskRepository repositorio de tareas utilizado para acceder a los
     *                       datos almacenados en la base de datos.
     */
    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    // static {
    // tasks.add(new Task(1L, "Tarea 1", "Descripción de la tarea 1",
    // // esto no lo he sacado a constantes porque has dicho que no toquemos nada de
    // aquí pero sí que me acuerdo eh?
    // TaskStatus.PENDING, TaskPriority.LOW,
    // java.time.LocalDate.of(2024, 6, 30), null, null));

    // tasks.add(new Task(2L, "Tarea 2", "Descripción de la tarea 2",
    // TaskStatus.IN_PROGRESS, TaskPriority.HIGH,
    // java.time.LocalDate.of(2024, 7, 15), null, null));
    // }

    // actualiza el javadoc
    /**
     * Devuelve una página de tareas.
     *
     * @param pageable información de paginación y ordenación.
     * @return página de tareas con sus metadatos.
     */
    @Override
    public Page<Task> findAll(Pageable pageable) {
        return taskRepository.findAll(pageable);
    }

    // actualiza el javadoc
    /**
     * Busca una tarea por su identificador.
     *
     * @param id identificador de la tarea que se desea buscar.
     * @return la tarea encontrada.
     * @throws TaskNotFoundException si no existe una tarea con el identificador
     *                               indicado.
     */
    @Override
    public Task findById(Long id) {
        // Lógica para encontrar una tarea por su ID
        // Buscar la tarea en la lista de tareas por su ID y devolverla -> generado con
        // IA
        // Buscar la tarea por ID usando taskRepository y lanzar TaskNotFoundException
        // si no existe. -> generado por IA
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("La tarea con ID " + id + " no existe."));
    }

    // Añadir @Transactional a este método de escritura del Service para garantizar
    // que sus operaciones sobre la base de datos se ejecuten dentro de una
    // transacción. — generado por IA
    // actualiza el javadoc
    /**
     * Crea una nueva tarea.
     *
     * @param task objeto Task que se desea crear.
     * @return la tarea creada.
     */
    @Transactional
    @Override
    public Task create(Task task) {
        // Lógica para crear una nueva tarea
        // Añadir la tarea nueva a la lista de tareas y devolverla en el return ->
        // generado con IA
        // Guardar la tarea utilizando taskRepository y devolver la tarea guardada. ->
        // generado por IA
        return taskRepository.save(task);
    }

    // Añadir @Transactional a este método de escritura del Service para garantizar
    // que sus operaciones sobre la base de datos se ejecuten dentro de una
    // transacción. — generado por IA
    // actualiza el javadoc
    /**
     * Actualiza una tarea existente con los datos proporcionados.
     *
     * @param id   identificador de la tarea que se desea actualizar.
     * @param task objeto Task con los nuevos datos para actualizar la tarea.
     * @return la tarea actualizada.
     * @throws TaskNotFoundException si no existe una tarea con el identificador
     *                               indicado.
     */
    @Transactional
    @Override
    public Task update(Long id, Task task) {
        // Lógica para actualizar una tarea existente
        // Buscar la tarea en la lista de tareas por su ID y actualizar sus campos con
        // los valores del objeto task -> generado con IA
        // Actualizar una tarea existente utilizando taskRepository: buscarla por ID y,
        // si existe, actualizar title, description, status, priority, dueDate y
        // categoryId con los valores recibidos en task, conservando createdAt. Guardar
        // y devolver la tarea actualizada. Si no existe, lanzar TaskNotFoundException.
        // -> generado por IA
        Task existingTask = findById(id);
        existingTask.setTitle(task.getTitle());
        existingTask.setDescription(task.getDescription());
        existingTask.setStatus(task.getStatus());
        existingTask.setPriority(task.getPriority());
        existingTask.setDueDate(task.getDueDate());
        existingTask.setCategory(task.getCategory());
        existingTask.setTags(task.getTags());
        return taskRepository.save(existingTask);
    }

    // Añadir @Transactional a este método de escritura del Service para garantizar
    // que sus operaciones sobre la base de datos se ejecuten dentro de una
    // transacción. — generado por IA
    // actualiza el javadoc
    /**
     * Elimina una tarea por su identificador.
     *
     * @param id identificador de la tarea que se desea eliminar.
     * @throws TaskNotFoundException si no existe una tarea con el identificador
     *                               indicado.
     */
    @Transactional
    @Override
    public void delete(Long id) {
        // Lógica para eliminar una tarea por su ID
        // Eliminar una tarea de la lista de tareas por su ID, comprobar antes si la
        // tarea existe y si no existe lanzar una excepción -> generado con IA
        // Cambia la excepción lanzada a TaskNotFoundException para indicar que la tarea
        // no existe
        // Eliminar la tarea utilizando taskRepository: buscarla por ID y comprobar que
        // existe mediante findById. Si no existe, lanzar TaskNotFoundException. Si
        // existe, eliminarla utilizando taskRepository.delete(taskToDelete). ->
        // generado por IA
        Task taskToDelete = findById(id);
        taskRepository.delete(taskToDelete);

    }

    // pon los métodos que faltan de la interfaz TaskService, que son findByStatus y
    // findByPriority, implementados con taskRepository. -> generado por IA
    // Corregir la implementación para que coincida con la interfaz TaskService: el
    // método findByStatus debe recibir un String status, devolver Iterable<Task> y
    // utilizar taskRepository.findByStatus(status) para obtener las tareas. ->
    // generado por IA
    // Corregir la implementación para que coincida con la interfaz TaskService: el
    // método findByPriority debe recibir un String priority, devolver
    // Iterable<Task> y utilizar taskRepository.findByPriority(priority) para
    // obtener las tareas. -> generado por IA
    // Corregir findByPriority para que coincida con la firma real de
    // TaskRepository.findByPriority. Revisar el tipo de parámetro que espera el
    // repositorio y adaptar la implementación manteniendo la firma String priority
    // exigida por TaskService. -> generado por IA
    // corrige los métodos para que utilicen los tipos correctos que son enums en
    // status y priority como en la interfaz. -> generado por IA
    // Adaptar los métodos de listado para aceptar Pageable, delegar la paginación
    // al Repository y devolver Page<Task>, conservando la lógica existente. —>
    // generado por IA
    /**
     * Busca tareas por su estado.
     *
     * @param status estado por el que se desea filtrar.
     * @return tareas que coinciden con el estado indicado.
     */
    @Override
    public Iterable<Task> findByStatus(TaskStatus status) {
        return taskRepository.findByStatus(status);
    }

    /**
     * Busca tareas por su prioridad.
     *
     * @param priority prioridad por la que se desea filtrar.
     * @return tareas que coinciden con la prioridad indicada.
     */
    @Override
    public Iterable<Task> findByPriority(TaskPriority priority) {
        return taskRepository.findByPriority(priority);
    }

    /**
     * Busca tareas por el identificador de su categoría, con paginación.
     *
     * @param categoryId identificador de la categoría.
     * @param pageable   información de paginación y ordenación.
     * @return página de tareas pertenecientes a la categoría indicada.
     */
    @Override
    public Page<Task> findByCategoryId(Long categoryId, Pageable pageable) {
        return taskRepository.findByCategoryId(categoryId, pageable);
    }

    /**
     * Busca tareas por el identificador de su etiqueta, con paginación.
     *
     * @param tagId    identificador de la etiqueta.
     * @param pageable información de paginación y ordenación.
     * @return página de tareas que contienen la etiqueta indicada.
     */
    @Override
    public Page<Task> findByTagId(Long tagId, Pageable pageable) {
        return taskRepository.findByTagId(tagId, pageable);
    }

    // Implementar findByStatusAndPriority convirtiendo los parámetros String a sus
    // enums TaskStatus y TaskPriority y delegando la búsqueda en TaskRepository. —
    // generado por IA
    // Añadir los métodos de servicio para las cinco consultas, delegando en
    // TaskRepository y devolviendo los resultados correspondientes, sin añadir
    // lógica de negocio innecesaria. — generado por IA
    /**
     * Busca tareas por su estado y prioridad, con paginación.
     *
     * @param status   estado por el que se desea filtrar.
     * @param priority prioridad por el que se desea filtrar.
     * @param pageable información de paginación y ordenación.
     * @return página de tareas que coinciden con el estado y prioridad indicados.
     */
    @Override
    public Page<Task> findByStatusAndPriority(
            String status, String priority, Pageable pageable) {
        TaskStatus taskStatus = TaskStatus.valueOf(status);
        TaskPriority taskPriority = TaskPriority.valueOf(priority);
        return taskRepository.findByStatusAndPriority(
                taskStatus, taskPriority, pageable);
    }

    /**
     * Busca tareas cuya fecha de vencimiento sea anterior a la fecha indicada.
     *
     * @param date fecha límite para filtrar las tareas.
     * @return tareas cuya fecha de vencimiento es anterior a la fecha indicada.
     */
    @Override
    public Iterable<Task> findByDueDateBefore(java.time.LocalDate date) {
        return taskRepository.findByDueDateBefore(date);
    }

    /**
     * Obtiene el conteo de tareas por su estado.
     *
     * @param status estado por el que se desea contar las tareas.
     * @return número de tareas que coinciden con el estado indicado.
     */
    @Override
    public long getCountByStatus(TaskStatus status) {
        return taskRepository.countByStatus(status);
    }

}