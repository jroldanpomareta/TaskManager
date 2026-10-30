package com.accenture.taskmanager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.jdbc.Sql;

import com.accenture.taskmanager.enums.TaskStatus;

import com.accenture.taskmanager.repository.TaskRepository;

// Genera el esqueleto de TaskRepositoryTest con @DataJpaTest, @ActiveProfiles("test"), @Sql("/test-data.sql") y @Autowired para TaskRepository. No generes todavía los métodos de test. — generado por IA
/**
 * Test class for TaskRepository.
 * This class contains unit tests for the TaskRepository interface.
 * It uses an in-memory database and loads test data from test-data.sql.
 * 
 * @author Javier Roldán
 * @version 1.0
 */
@DataJpaTest
@ActiveProfiles("test")
@Sql("/test-data.sql")
public class TaskRepositoryTest {

    // Configuración de pruebas con un PasswordEncoder real para permitir la creación del usuario administrador durante la inicialización del contexto — generado por IA.
    /**
     * Test configuration class.
     * This class provides the necessary beans for the test context.
     */
    @TestConfiguration
    static class TestConfig {

        /**
         * Bean for the PasswordEncoder.
         * This bean provides a BCryptPasswordEncoder for the test context.
         */
        @Bean
        PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }

    /**
     * The TaskRepository instance used for testing.
     * This repository is automatically injected by Spring's dependency injection.
     */
    @Autowired
    private TaskRepository taskRepository;

    // Genera un test findAll_shouldReturnAllTasks que compruebe que
    // taskRepository.findAll() devuelve las 3 tareas de test-data.sql. Sigue
    // Given/When/Then y usa una aserción principal. — generado por IA
    /**
     * Test method for findAll() in TaskRepository.
     * This test verifies that the findAll() method returns all tasks present in the
     * database.
     * It assumes that 3 tasks have been inserted into the database via
     * test-data.sql.
     */
    @org.junit.jupiter.api.Test
    public void findAll_shouldReturnAllTasks() {
        // Given: Se han insertado 3 tareas en la base de datos a través de
        // test-data.sql

        // When: Se llama al método findAll() del repositorio
        var tasks = taskRepository.findAll();

        // Then: Se comprueba que se devuelven exactamente 3 tareas
        org.junit.jupiter.api.Assertions.assertEquals(3, tasks.size(), "Debería devolver 3 tareas");
    }

    // Genera un test findById_shouldReturnTask_whenTaskExists que busque la tarea
    // con id 1 y compruebe que existe. Sigue Given/When/Then y usa una aserción
    // principal. — generado por IA
    /**
     * Test method for findById() in TaskRepository.
     * This test verifies that the findById() method returns the task with the
     * specified ID if it exists.
     * It assumes that a task with ID 1 has been inserted into the database via
     * test-data.sql.
     */
    @org.junit.jupiter.api.Test
    public void findById_shouldReturnTask_whenTaskExists() {
        // Given: Se ha insertado una tarea con id 1 en la base de datos a través de
        // test-data.sql

        // When: Se llama al método findById(1) del repositorio
        var taskOptional = taskRepository.findById(1L);

        // Then: Se comprueba que la tarea existe
        org.junit.jupiter.api.Assertions.assertTrue(taskOptional.isPresent(), "La tarea con id 1 debería existir");
    }

    // Genera un test findById_shouldReturnEmpty_whenTaskDoesNotExist que busque la
    // tarea con id 999 y compruebe que el Optional está vacío. Sigue
    // Given/When/Then y usa una aserción principal. — generado por IA
    /**
     * Test method for findById() in TaskRepository.
     * This test verifies that the findById() method returns an empty Optional when
     * the task with the specified ID does not exist.
     * It assumes that no task with ID 999 has been inserted into the database.
     */
    @org.junit.jupiter.api.Test
    public void findById_shouldReturnEmpty_whenTaskDoesNotExist() {
        // Given: No se ha insertado ninguna tarea con id 999 en la base de datos

        // When: Se llama al método findById(999) del repositorio
        var taskOptional = taskRepository.findById(999L);

        // Then: Se comprueba que el Optional está vacío
        org.junit.jupiter.api.Assertions.assertFalse(taskOptional.isPresent(),
                "La tarea con id 999 no debería existir");
    }

    // Genera un test findByStatus_shouldReturnOnlyTasksWithRequestedStatus que
    // compruebe que findByStatus(TaskStatus.PENDING) devuelve únicamente tareas con
    // estado PENDING. Usa el enum TaskStatus del paquete
    // com.accenture.taskmanager.enums, sigue Given/When/Then y utiliza una aserción
    // principal. — generado por IA
    /**
     * Test method for findByStatus() in TaskRepository.
     * This test verifies that the findByStatus() method returns only tasks with the
     * requested status.
     * It assumes that tasks with different statuses have been inserted into the
     * database via test-data.sql.
     */
    @org.junit.jupiter.api.Test
    public void findByStatus_shouldReturnOnlyTasksWithRequestedStatus() {
        // Given: Se han insertado tareas con diferentes estados en la base de datos a
        // través de test-data.sql

        // When: Se llama al método findByStatus(TaskStatus.PENDING) del repositorio
        var pendingTasks = taskRepository.findByStatus(TaskStatus.PENDING);

        // Then: Se comprueba que todas las tareas devueltas tienen estado PENDING
        org.junit.jupiter.api.Assertions.assertTrue(
                pendingTasks.stream().allMatch(task -> task.getStatus() == TaskStatus.PENDING),
                "Todas las tareas devueltas deberían tener estado PENDING");
    }

    // Genera un test findByCategoryId_shouldReturnTasksForCategory que compruebe
    // que findByCategoryId(1L, PageRequest.of(0, 10)) devuelve únicamente tareas de
    // la categoría con id 1. Sigue Given/When/Then y utiliza una aserción
    // principal. — generado por IA
    /**
     * Test method for findByCategoryId() in TaskRepository.
     * This test verifies that the findByCategoryId() method returns only tasks for
     * the specified category.
     * It assumes that tasks with different categories have been inserted into the
     * database via test-data.sql.
     */
    @org.junit.jupiter.api.Test
    public void findByCategoryId_shouldReturnTasksForCategory() {
        // Given: Se han insertado tareas con diferentes categorías en la base de datos
        // a través de test-data.sql

        // When: Se llama al método findByCategoryId(1L, PageRequest.of(0, 10)) del
        // repositorio
        var tasksForCategory = taskRepository.findByCategoryId(1L,
                org.springframework.data.domain.PageRequest.of(0, 10));

        // Then: Se comprueba que todas las tareas devueltas pertenecen a la categoría
        // con id 1
        org.junit.jupiter.api.Assertions.assertTrue(
                tasksForCategory.stream().allMatch(task -> task.getCategory().getId() == 1L),
                "Todas las tareas devueltas deberían pertenecer a la categoría con id 1");
    }

    // Genera un test findByTagId_shouldReturnTasksWithTag que compruebe que
    // findByTagId(1L, PageRequest.of(0, 10)) devuelve únicamente tareas que
    // contienen la etiqueta con id 1. Sigue Given/When/Then y utiliza una aserción
    // principal. — generado por IA
    /**
     * Test method for findByTagId() in TaskRepository.
     * This test verifies that the findByTagId() method returns only tasks that
     * contain the specified tag.
     * It assumes that tasks with different tags have been inserted into the
     * database via test-data.sql.
     */
    @org.junit.jupiter.api.Test
    public void findByTagId_shouldReturnTasksWithTag() {
        // Given: Se han insertado tareas con diferentes etiquetas en la base de datos a
        // través de test-data.sql

        // When: Se llama al método findByTagId(1L, PageRequest.of(0, 10)) del
        // repositorio
        var tasksWithTag = taskRepository.findByTagId(1L, org.springframework.data.domain.PageRequest.of(0, 10));

        // Then: Se comprueba que todas las tareas devueltas contienen la etiqueta con
        // id 1
        org.junit.jupiter.api.Assertions.assertTrue(
                tasksWithTag.stream().allMatch(task -> task.getTags().stream().anyMatch(tag -> tag.getId() == 1L)),
                "Todas las tareas devueltas deberían contener la etiqueta con id 1");
    }

    // Genera un test findByDueDateBefore_shouldReturnTasksWithEarlierDueDate que
    // compruebe que findByDueDateBefore(LocalDate.of(2026, 6, 10)) devuelve
    // únicamente tareas con fecha límite anterior al 10 de junio de 2026. Sigue
    // Given/When/Then y utiliza una aserción principal. — generado por IA
    /**
     * Test method for findByDueDateBefore() in TaskRepository.
     * This test verifies that the findByDueDateBefore() method returns only tasks
     * with a due date before the specified date.
     * It assumes that tasks with different due dates have been inserted into the
     * database via test-data.sql.
     */
    @org.junit.jupiter.api.Test
    public void findByDueDateBefore_shouldReturnTasksWithEarlierDueDate() {
        // Given: Se han insertado tareas con diferentes fechas límite en la base de
        // datos a través de test-data.sql

        // When: Se llama al método findByDueDateBefore(LocalDate.of(2026, 6, 10)) del
        // repositorio
        var tasksWithEarlierDueDate = taskRepository.findByDueDateBefore(java.time.LocalDate.of(2026, 6, 10));

        // Then: Se comprueba que todas las tareas devueltas tienen fecha límite
        // anterior al 10 de junio de 2026
        org.junit.jupiter.api.Assertions.assertTrue(
                tasksWithEarlierDueDate.stream()
                        .allMatch(task -> task.getDueDate().isBefore(java.time.LocalDate.of(2026, 6, 10))),
                "Todas las tareas devueltas deberían tener fecha límite anterior al 10 de junio de 2026");
    }

    // Genera un test save_shouldPersistNewTask que cree y guarde una tarea nueva
    // con los campos obligatorios, compruebe que se persiste y que tiene un id
    // asignado. Sigue Given/When/Then y utiliza una aserción principal. — generado
    // por IA
    /**
     * Test method for save() in TaskRepository.
     * This test verifies that the save() method persists a new task and assigns it
     * an ID.
     * It assumes that the database is properly configured and the task is valid.
     */
    @org.junit.jupiter.api.Test
    public void save_shouldPersistNewTask() {
        // Given: Se crea una nueva tarea con los campos obligatorios
        com.accenture.taskmanager.model.Task newTask = new com.accenture.taskmanager.model.Task();
        newTask.setTitle("Nueva Tarea");
        newTask.setStatus(TaskStatus.PENDING);
        newTask.setDueDate(java.time.LocalDate.of(2026, 6, 15));

        // When: Se guarda la nueva tarea en el repositorio
        com.accenture.taskmanager.model.Task savedTask = taskRepository.save(newTask);

        // Then: Se comprueba que la tarea se ha persistido y tiene un id asignado
        org.junit.jupiter.api.Assertions.assertNotNull(savedTask.getId(),
                "La tarea guardada debería tener un id asignado");
    }

    // Genera un test deleteById_shouldDeleteTask que elimine la tarea con id 1 y
    // compruebe que ya no existe en el repositorio. Sigue Given/When/Then y utiliza
    // una aserción principal. — generado por IA
    /**
     * Test method for deleteById() in TaskRepository.
     * This test verifies that the deleteById() method removes a task by its ID.
     * It assumes that a task with ID 1 exists in the database.
     */
    @org.junit.jupiter.api.Test
    public void deleteById_shouldDeleteTask() {
        // Given: Se ha insertado una tarea con id 1 en la base de datos a través de
        // test-data.sql

        // When: Se elimina la tarea con id 1 del repositorio
        taskRepository.deleteById(1L);

        // Then: Se comprueba que la tarea con id 1 ya no existe en el repositorio
        var taskOptional = taskRepository.findById(1L);
        org.junit.jupiter.api.Assertions.assertFalse(taskOptional.isPresent(),
                "La tarea con id 1 debería haber sido eliminada");
    }
}
