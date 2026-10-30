package com.accenture.taskmanager;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.accenture.taskmanager.model.Task;

import com.accenture.taskmanager.exceptions.TaskNotFoundException;

import org.springframework.data.domain.Pageable;

import com.accenture.taskmanager.dto.PagedResponseDTO;
import com.accenture.taskmanager.dto.TaskRequestDTO;
import com.accenture.taskmanager.dto.TaskResponseDTO;
import com.accenture.taskmanager.repository.CategoryRepository;
import com.accenture.taskmanager.repository.TagRepository;
import com.accenture.taskmanager.repository.TaskRepository;
import com.accenture.taskmanager.service.TaskServiceImpl;

// Crea el esqueleto de TaskServiceImplTest con JUnit 5 y Mockito. Usa @ExtendWith(MockitoExtension.class), declara @Mock para TaskRepository, CategoryRepository y TagRepository, e inyecta TaskServiceImpl con @InjectMocks. No uses @SpringBootTest ni ninguna anotación de Spring. No accedas a una base de datos real. Genera únicamente la estructura de la clase, sin métodos de test. — generado por IA
/**
 * Test class for TaskServiceImpl.
 * This class uses JUnit 5 and Mockito to test the TaskServiceImpl class.
 * It mocks the dependencies of TaskServiceImpl, including TaskRepository,
 * CategoryRepository, and TagRepository, and injects an instance of
 * TaskServiceImpl for testing.
 * 
 * @author Javier Roldán
 * version 1.0
 */
@ExtendWith(MockitoExtension.class)
public class TaskServiceImplTest {
    /**
     * Javadoc for the test class.
     */
    @Mock
    private TaskRepository taskRepository;

    /**
     * Javadoc for the taskRepository mock.
     */
    
    @Mock
    private CategoryRepository categoryRepository;
    /**
     * Javadoc for the categoryRepository mock.
     */
    
    @Mock
    private TagRepository tagRepository;

    /**
     * Javadoc for the tagRepository mock.
     */
    @InjectMocks
    private TaskServiceImpl taskServiceImpl;

    // Genera el test completo de findAll(Pageable): crea un Pageable, prepara una
    // Page<Task> con Mockito, configura taskRepository.findAll(pageable) con
    // when().thenReturn(), llama a taskServiceImpl.findAll(pageable) y verifica que
    // devuelve un PagedResponseDTO con los resultados esperados. Usa JUnit 5 y
    // Mockito, sin base de datos real. — generado por IA
    /**
     * Test method for findAll(Pageable).
     * This test verifies that the findAll method of TaskServiceImpl returns a
     * PagedResponseDTO containing the expected tasks when the repository returns a
     * Page of tasks.
     */
    @Test
    void testFindAll() {
        // Crea un Pageable
        Pageable pageable = PageRequest.of(0, 10);

        // Prepara una Page<Task> con Mockito
        List<Task> tasks = Arrays.asList(new Task(), new Task());
        Page<Task> taskPage = new PageImpl<>(tasks, pageable, tasks.size());

        // Configura taskRepository.findAll(pageable) con when().thenReturn()
        when(taskRepository.findAll(pageable)).thenReturn(taskPage);

        // Llama a taskServiceImpl.findAll(pageable)
        PagedResponseDTO<TaskResponseDTO> result = taskServiceImpl.findAll(pageable);

        // Verifica que devuelve un PagedResponseDTO con los resultados esperados
        assertEquals(tasks.size(), result.getContent().size());
    }

    // Genera un test unitario que compruebe que findById() devuelve la tarea
    // esperada cuando existe, utilizando Mockito para simular las dependencias —
    // generado por IA
    /**
     * Test method for findById().
     * This test verifies that the findById method of TaskServiceImpl returns the
     * expected task when it exists.
     */
    @Test
    void testFindById() {
        // Prepara un Task con un ID específico
        Long taskId = 1L;
        Task task = new Task();
        task.setId(taskId);

        // Configura taskRepository.findById(taskId) con when().thenReturn()
        when(taskRepository.findById(taskId)).thenReturn(java.util.Optional.of(task));

        // Llama a taskServiceImpl.findById(taskId)
        TaskResponseDTO result = taskServiceImpl.findById(taskId);

        // Verifica que devuelve la tarea esperada
        assertEquals(task.getId(), result.getId());
    }

    // Genera un test unitario que compruebe que findById() lanza
    // TaskNotFoundException cuando la tarea solicitada no existe — generado por IA
    /**
     * Test method for findById().
     * This test verifies that the findById method of TaskServiceImpl throws a
     * TaskNotFoundException when the requested task does not exist.
     */
    @Test
    void testFindByIdNotFound() {
        // Prepara un ID de tarea que no existe
        Long taskId = 1L;

        // Configura taskRepository.findById(taskId) con when().thenReturn()
        when(taskRepository.findById(taskId)).thenReturn(java.util.Optional.empty());

        // Llama a taskServiceImpl.findById(taskId) y verifica que lanza
        // TaskNotFoundException
        assertThrows(TaskNotFoundException.class, () -> {
            taskServiceImpl.findById(taskId);
        });
    }

    // Genera un test unitario que compruebe que create() crea y devuelve una tarea, utilizando Mockito para simular las dependencias — generado por IA
    /**
     * Test method for create().
     * This test verifies that the create method of TaskServiceImpl creates and returns a
     * new task when the request is valid.
     */
    @Test
    void testCreate() {
        TaskRequestDTO request = new TaskRequestDTO();
        request.setTitle("New Task");

        Task task = new Task();
        task.setId(1L);
        task.setTitle("New Task");

        when(taskRepository.save(org.mockito.ArgumentMatchers.any(Task.class)))
                .thenReturn(task);

        TaskResponseDTO result = taskServiceImpl.create(request);

        assertEquals(task.getId(), result.getId());
        assertEquals(task.getTitle(), result.getTitle());
    }

    // Genera un test unitario que compruebe que update() actualiza y devuelve una tarea cuando existe, utilizando Mockito para simular las dependencias — generado por IA
    /**
     * Test method for update().
     * This test verifies that the update method of TaskServiceImpl updates and returns a
     * task when it exists.
     */
    @Test
    void testUpdate() {
        Long taskId = 1L;
        TaskRequestDTO request = new TaskRequestDTO();
        request.setTitle("Updated Task");

        Task existingTask = new Task();
        existingTask.setId(taskId);
        existingTask.setTitle("Old Task");

        Task updatedTask = new Task();
        updatedTask.setId(taskId);
        updatedTask.setTitle("Updated Task");

        when(taskRepository.findById(taskId)).thenReturn(java.util.Optional.of(existingTask));
        when(taskRepository.save(org.mockito.ArgumentMatchers.any(Task.class))).thenReturn(updatedTask);

        TaskResponseDTO result = taskServiceImpl.update(taskId, request);

        assertEquals(updatedTask.getId(), result.getId());
        assertEquals(updatedTask.getTitle(), result.getTitle());
    }

    // Genera un test unitario que compruebe que update() lanza TaskNotFoundException cuando la tarea no existe, utilizando Mockito y assertThrows — generado por IA
    /**
     * Test method for update().
     * This test verifies that the update method of TaskServiceImpl throws a
     * TaskNotFoundException when the requested task does not exist.
     */
    @Test
    void testUpdateNotFound() {
        Long taskId = 1L;
        TaskRequestDTO request = new TaskRequestDTO();
        request.setTitle("Updated Task");

        when(taskRepository.findById(taskId)).thenReturn(java.util.Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> {
            taskServiceImpl.update(taskId, request);
        });
    }

    // Genera un test unitario que compruebe que delete() elimina una tarea cuando existe, utilizando Mockito para simular las dependencias — generado por IA
    /**
     * Test method for delete().
     * This test verifies that the delete method of TaskServiceImpl deletes a
     * task when it exists.
     */
    @Test
    void testDelete() {
        Long taskId = 1L;

        Task existingTask = new Task();
        existingTask.setId(taskId);

        when(taskRepository.findById(taskId)).thenReturn(java.util.Optional.of(existingTask));

        taskServiceImpl.delete(taskId);

        // Verifica que taskRepository.delete() fue llamado con la tarea existente
        org.mockito.Mockito.verify(taskRepository).delete(existingTask);
    }

    // Genera un test unitario que compruebe que delete() lanza TaskNotFoundException cuando la tarea no existe, utilizando Mockito y assertThrows — generado por IA
    /**
     * Test method for delete().
     * This test verifies that the delete method of TaskServiceImpl throws a
     * TaskNotFoundException when the requested task does not exist.
     */
    @Test
    void testDeleteNotFound() {
        Long taskId = 1L;

        when(taskRepository.findById(taskId)).thenReturn(java.util.Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> {
            taskServiceImpl.delete(taskId);
        });
    }

}
