package com.accenture.taskmanager.service;

import org.springframework.stereotype.Service;

import java.util.List;

import com.accenture.taskmanager.model.Tag;

import com.accenture.taskmanager.model.Category;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.accenture.taskmanager.dto.PagedResponseDTO;
import com.accenture.taskmanager.dto.TaskRequestDTO;
import com.accenture.taskmanager.dto.TaskResponseDTO;
import com.accenture.taskmanager.enums.TaskPriority;
import com.accenture.taskmanager.enums.TaskStatus;
import com.accenture.taskmanager.exceptions.CategoryNotFoundException;
import com.accenture.taskmanager.exceptions.TaskNotFoundException;
import com.accenture.taskmanager.mapper.TaskMapper;
import com.accenture.taskmanager.model.Task;
import com.accenture.taskmanager.repository.TaskRepository;
import com.accenture.taskmanager.service.interfaces.TaskService;

import com.accenture.taskmanager.repository.CategoryRepository;
import com.accenture.taskmanager.repository.TagRepository;

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
    /**
     * Repositorio de categorías utilizado para acceder a los datos de categorías
     * almacenados en la base de datos.
     */ 
    private final TagRepository tagRepository;

    // Añadir TagRepository como dependencia mediante inyección por constructor,
    // manteniendo TaskRepository y CategoryRepository. Actualizar el constructor
    // para recibir e inicializar los tres repositorios. Añadir los imports
    // necesarios y conservar el código existente. — generado por IA
    /**
     * Crea un nuevo servicio de tareas con los repositorios proporcionados.
     * 
     * @param taskRepository     repositorio de tareas a utilizar.
     * @param categoryRepository repositorio de categorías a utilizar.
     * @param tagRepository      repositorio de etiquetas a utilizar.
     */
    public TaskServiceImpl(TaskRepository taskRepository, CategoryRepository categoryRepository,
            TagRepository tagRepository) {
        this.taskRepository = taskRepository;
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
    }

    // Crear el constructor que reciba TaskRepository y lo asigne al atributo
    // taskRepository. -> generado por IA
    /**
     * Crea un nuevo servicio de tareas con el repositorio proporcionado.
     * 
     * @param taskRepository repositorio de tareas a utilizar.
     */
    // Actualizar el constructor para recibir TaskRepository y CategoryRepository e
    // inicializar ambos atributos con this. Mantener la inyección por constructor y
    // añadir el parámetro de cada repositorio. — generado por IA

    // Añadir CategoryRepository como dependencia mediante inyección por
    // constructor, manteniendo TaskRepository. Actualizar el constructor para
    // recibir e inicializar ambos repositorios. Añadir los imports necesarios y
    // conservar el estilo existente. — generado por IA
    private final CategoryRepository categoryRepository;

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

    /**
     * Obtiene todas las tareas con paginación.
     *
     * @param pageable información de paginación y ordenación.
     * @return página de tareas.
     */
    // Adapta findAll para devolver PagedResponseDTO<TaskResponseDTO>, transformando
    // manualmente cada Task de la página con TaskMapper.toResponseDTO y conservando
    // los metadatos de paginación. — generado por IA
    @Override
    public PagedResponseDTO<TaskResponseDTO> findAll(Pageable pageable) {
        Page<Task> tasks = taskRepository.findAll(pageable);
        return new PagedResponseDTO<>(
                tasks.getContent().stream()
                        .map(TaskMapper::toResponseDTO)
                        .toList(),
                tasks.getNumber(),
                tasks.getSize(),
                tasks.getTotalElements(),
                tasks.getTotalPages(),
                tasks.isLast());
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
    // Adapta findById para devolver TaskResponseDTO, buscando la tarea por id y
    // transformándola con TaskMapper.toResponseDTO. Conserva TaskNotFoundException
    // si no existe. — generado por IA
    // Actualizar el mensaje de TaskNotFoundException para que siga exactamente el formato "Tarea con id X no encontrada", manteniendo la lógica existente. — generado por IA
    @Override
    public TaskResponseDTO findById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(
                        "Tarea con id " + id + " no encontrada."));
        return TaskMapper.toResponseDTO(task);
    }

    // Añadir @Transactional a este método de escritura del Service para garantizar
    // que sus operaciones sobre la base de datos se ejecuten dentro de una
    // transacción. — generado por IA
    // actualiza el javadoc
    /**
     * Crea una nueva tarea con los datos proporcionados.
     *
     * @param dto objeto TaskRequestDTO con los datos de la tarea que se desea
     *            crear.
     * @return la tarea creada.
     */
    // Adapta create para recibir TaskRequestDTO, convertirlo a Task con
    // TaskMapper.toEntity, guardarlo con taskRepository y devolver el resultado
    // como TaskResponseDTO. — generado por IA
    // Modificar create para resolver dto.getCategoryId() mediante
    // CategoryRepository. Si categoryId es null, crear la tarea sin categoría. Si
    // contiene un ID, buscar la categoría y asignarla a la tarea. Si no existe,
    // lanzar CategoryNotFoundException indicando el ID. Mantener el guardado y la
    // respuesta mediante TaskMapper. — generado por IA
    // Resolver dto.getTagIds() mediante TagRepository y asignar las etiquetas
    // encontradas a task. Si tagIds es null o está vacío, la tarea no tendrá
    // etiquetas. Si algún ID no existe, lanzar una excepción que indique el ID
    // ausente. Mantener intactos el tratamiento de la categoría, el guardado y la
    // respuesta mediante TaskMapper. Añadir los imports necesarios. — generado por
    // IA
    @Transactional
    @Override
    public TaskResponseDTO create(TaskRequestDTO dto) {
        Task task = TaskMapper.toEntity(dto);
        // Si el categoryId recibido en el DTO no existe, lanzar IllegalArgumentException con el mensaje "Categoría con id X no encontrada". Mantener el resto de la lógica existente. — generado por IA
        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Categoría con id " + dto.getCategoryId() + " no encontrada."));
            task.setCategory(category);
        }
        // Cambiar el mensaje de IllegalArgumentException para etiquetas inexistentes al formato "Etiqueta con id X no encontrada", conservando la comprobación existente. — generado por IA
        if (dto.getTagIds() != null && !dto.getTagIds().isEmpty()) {
            List<Tag> tags = tagRepository.findAllById(dto.getTagIds());
            for (Long tagId : dto.getTagIds()) {
                if (tags.stream().noneMatch(tag -> tag.getId().equals(tagId))) {
                    throw new IllegalArgumentException(
                            "Etiqueta con id " + tagId + " no encontrada.");
                }
            }
            task.setTags(tags);
        }
        Task savedTask = taskRepository.save(task);
        return TaskMapper.toResponseDTO(savedTask);
    }

    // Añadir @Transactional a este método de escritura del Service para garantizar
    // que sus operaciones sobre la base de datos se ejecuten dentro de una
    // transacción. — generado por IA
    // actualiza el javadoc
    /**
     * Actualiza una tarea existente con los datos proporcionados.
     *
     * @param id  identificador de la tarea que se desea actualizar.
     * @param dto objeto TaskRequestDTO con los datos de la tarea que se desea
     *            actualizar.
     * @return la tarea actualizada.
     * @throws TaskNotFoundException si no existe una tarea con el identificador
     *                               indicado.
     */
    // Lógica para actualizar una tarea existente
    // Buscar la tarea en la lista de tareas por su ID y actualizar sus campos con
    // los valores del objeto task -> generado con IA
    // Actualizar una tarea existente utilizando taskRepository: buscarla por ID y,
    // si existe, actualizar title, description, status, priority, dueDate y
    // categoryId con los valores recibidos en task, conservando createdAt. Guardar
    // y devolver la tarea actualizada. Si no existe, lanzar TaskNotFoundException.
    // -> generado por IA
    // Adapta update para recibir el id y TaskRequestDTO, buscar la tarea existente,
    // actualizar title, description, status, priority y dueDate, conservar
    // createdAt, guardar y devolver TaskResponseDTO mediante TaskMapper. — generado
    // por IA
    // Actualizar la categoría de la tarea usando dto.getCategoryId() y
    // CategoryRepository. Si categoryId es null, dejar la tarea sin categoría. Si
    // contiene un ID, buscar la categoría y asignarla a existingTask. Si no existe,
    // lanzar CategoryNotFoundException indicando el ID. Mantener el resto de campos
    // y el guardado existentes. — generado por IA
    // Resolver dto.getTagIds() mediante TagRepository y reemplazar la lista de
    // etiquetas de existingTask. Si tagIds es null o está vacío, dejar la tarea sin
    // etiquetas. Si algún ID no existe, lanzar una excepción indicando el ID
    // ausente. Mantener el resto de la lógica de actualización, el guardado y la
    // respuesta mediante TaskMapper. Añadir los imports necesarios y conservar el
    // código existente. — generado por IA
    // Actualizar el mensaje de TaskNotFoundException para que siga exactamente el formato "Tarea con id X no encontrada", manteniendo la lógica existente. — generado por IA
    @Transactional
    @Override
    public TaskResponseDTO update(Long id, TaskRequestDTO dto) {
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(
                        "Tarea con id " + id + " no encontrada."));

        existingTask.setTitle(dto.getTitle());
        existingTask.setDescription(dto.getDescription());
        existingTask.setStatus(dto.getStatus());
        existingTask.setPriority(dto.getPriority());
        existingTask.setDueDate(dto.getDueDate());

        // Si el categoryId recibido en el DTO no existe, lanzar IllegalArgumentException con el mensaje "Categoría con id X no encontrada". Mantener el resto de la lógica existente. — generado por IA
        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Categoría con id " + dto.getCategoryId() + " no encontrada."));
            existingTask.setCategory(category);
        } else {
            existingTask.setCategory(null);
        }

        if (dto.getTagIds() != null && !dto.getTagIds().isEmpty()) {
            List<Tag> tags = tagRepository.findAllById(dto.getTagIds());
            // Cambiar el mensaje de IllegalArgumentException para etiquetas inexistentes al formato "Etiqueta con id X no encontrada", conservando la comprobación existente. — generado por IA
            for (Long tagId : dto.getTagIds()) {
                if (tags.stream().noneMatch(tag -> tag.getId().equals(tagId))) {
                    throw new IllegalArgumentException(
                            "Etiqueta con id " + tagId + " no encontrada.");
                }
            }

            existingTask.setTags(tags);
        } else {
            existingTask.setTags(List.of());
        }

        Task updatedTask = taskRepository.save(existingTask);
        return TaskMapper.toResponseDTO(updatedTask);
    }

    // Añadir @Transactional a este método de escritura del Service para garantizar
    // que sus operaciones sobre la base de datos se ejecuten dentro de una
    // transacción. — generado por IA
    // Lógica para eliminar una tarea por su ID
    // Eliminar una tarea de la lista de tareas por su ID, comprobar antes si la
    // tarea existe y si no existe lanzar una excepción -> generado con IA
    // Cambia la excepción lanzada a TaskNotFoundException para indicar que la tarea
    // no existe
    // Eliminar la tarea utilizando taskRepository: buscarla por ID y comprobar que
    // existe mediante findById. Si no existe, lanzar TaskNotFoundException. Si
    // existe, eliminarla utilizando taskRepository.delete(taskToDelete). ->
    // generado por IA
    // actualiza el javadoc
    // Adapta delete para que busque la entidad Task directamente en taskRepository
    // mediante su id, lance TaskNotFoundException si no existe y elimine la entidad
    // encontrada. No utilices findById del servicio porque ahora devuelve
    // TaskResponseDTO. Mantén @Transactional y la firma void delete(Long id). —
    // generado por IA
    // Actualizar el mensaje de TaskNotFoundException para que siga exactamente el formato "Tarea con id X no encontrada", manteniendo la lógica existente. — generado por IA
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
        Task taskToDelete = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(
                        "Tarea con id " + id + " no encontrada."));
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
     * @return respuesta paginada con los DTO de las tareas de la categoría.
     */
    // Adapta findByCategoryId para devolver PagedResponseDTO<TaskResponseDTO>,
    // usando TaskMapper para convertir las tareas y conservando los metadatos
    // de paginación. — generado por IA
    @Override
    public PagedResponseDTO<TaskResponseDTO> findByCategoryId(Long categoryId, Pageable pageable) {
        Page<Task> tasks = taskRepository.findByCategoryId(categoryId, pageable);

        List<TaskResponseDTO> content = tasks.getContent()
                .stream()
                .map(TaskMapper::toResponseDTO)
                .toList();

        return new PagedResponseDTO<>(
                content,
                tasks.getNumber(),
                tasks.getSize(),
                tasks.getTotalElements(),
                tasks.getTotalPages(),
                tasks.isLast());
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