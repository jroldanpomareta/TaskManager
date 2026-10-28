package com.accenture.taskmanager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.accenture.taskmanager.exceptions.TaskNotFoundException;

import com.accenture.taskmanager.controller.TaskController;
import com.accenture.taskmanager.dto.PagedResponseDTO;
import com.accenture.taskmanager.dto.TaskResponseDTO;
import com.accenture.taskmanager.enums.TaskPriority;
import com.accenture.taskmanager.enums.TaskStatus;
import com.accenture.taskmanager.service.interfaces.TaskService;

// Comprueba que GET /api/v1/tasks devuelve la lista paginada de tareas con HTTP 200 y una respuesta JSON correcta. — generado por IA
/**
 * Test class for TaskController.
 * This class contains unit tests for the TaskController endpoints.
 * @author Javier Roldán
 * @version 1.0
 */
@WebMvcTest(TaskController.class)
public class TaskControllerTest {

        /**
         * MockMvc instance for performing HTTP requests in the tests.
         */
    @Autowired
    private MockMvc mockMvc;

    /**
     * MockitoBean for the TaskService.
     */
    @MockitoBean
    private TaskService taskService;

    // Comprueba que GET /api/v1/tasks devuelve la lista paginada de tareas con HTTP
    // 200 y una respuesta JSON correcta. — generado por IA
    /**
     * Test method for GET /api/v1/tasks endpoint.
     * This test verifies that the endpoint returns a paginated list of tasks with HTTP 200 status.
     * It mocks the TaskService to return an empty paginated response and checks the JSON structure of the response.
     * @throws Exception if an error occurs during the request
     */
    @Test
    public void getTasks_shouldReturnPagedTasks() throws Exception {
        // Configura la respuesta paginada simulada del servicio
        when(taskService.findAll(any())).thenReturn(
                new PagedResponseDTO<>(List.of(), 0, 10, 0L, 0, true));

        // Realiza la solicitud GET y verifica la respuesta
        mockMvc.perform(get("/api/v1/tasks")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    // Comprueba que GET /api/v1/tasks/{id} devuelve una tarea existente con HTTP
    // 200. Usa TaskResponseDTO, que es el DTO real del proyecto, y construye la
    // respuesta respetando su constructor. No uses TaskDTO ni inventes campos como
    // completed. — generado por IA
        /**
         * Test method for GET /api/v1/tasks/{id} endpoint.
         * This test verifies that the endpoint returns a task with HTTP 200 status when the task exists.
         * It mocks the TaskService to return a TaskResponseDTO and checks the JSON structure of the response.
         * @throws Exception if an error occurs during the request
         */
    @Test
    public void getTaskById_shouldReturnTask_whenTaskExists() throws Exception {
        // Configura la respuesta simulada del servicio
        TaskResponseDTO task = new TaskResponseDTO(
                1L,
                "Test Task",
                "Description",
                TaskStatus.PENDING,
                TaskPriority.HIGH,
                LocalDate.of(2027, 12, 31),
                LocalDateTime.of(2027, 10, 1, 10, 0));

        when(taskService.findById(1L)).thenReturn(task);

        // Realiza la solicitud GET y verifica la respuesta
        mockMvc.perform(get("/api/v1/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Task"))
                .andExpect(jsonPath("$.description").value("Description"))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.priority").value("HIGH"));
    }

    // Comprueba que GET /api/v1/tasks/{id} devuelve HTTP 404 cuando la tarea no
    // existe, verificando la respuesta de error JSON. — generado por IA
        /**
         * Test method for GET /api/v1/tasks/{id} endpoint.
         * This test verifies that the endpoint returns HTTP 404 status when the task does not exist.
         * It mocks the TaskService to throw a TaskNotFoundException and checks the JSON structure of the error response.
         * @throws Exception if an error occurs during the request
         */
    @Test
    public void getTaskById_shouldReturn404_whenTaskDoesNotExist() throws Exception {
        // Configura la respuesta simulada del servicio para lanzar una excepción
        when(taskService.findById(999L))
                .thenThrow(new TaskNotFoundException("Task not found"));

        // Realiza la solicitud GET y verifica la respuesta
        mockMvc.perform(get("/api/v1/tasks/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Task not found"));
    }

    // Comprueba que POST /api/v1/tasks crea una tarea con datos válidos y devuelve
    // HTTP 201 con los datos de la tarea creada. — generado por IA
    /**
     * Test method for POST /api/v1/tasks endpoint.
     * This test verifies that the endpoint creates a task with valid data and returns HTTP 201 status with the created task data.
     * It mocks the TaskService to return a TaskResponseDTO and checks the JSON structure of the response.
     * @throws Exception if an error occurs during the request
     */
    @Test
    public void createTask_shouldReturnCreatedTask_whenValidDataIsProvided() throws Exception {
        // Configura la respuesta simulada del servicio
        TaskResponseDTO createdTask = new TaskResponseDTO(
                1L,
                "Test Task",
                "Description",
                TaskStatus.PENDING,
                TaskPriority.HIGH,
                LocalDate.of(2027, 12, 31),
                LocalDateTime.of(2027, 10, 1, 10, 0));

        when(taskService.create(any())).thenReturn(createdTask);

        // Realiza la solicitud POST y verifica la respuesta
        mockMvc.perform(post("/api/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        "{ \"title\": \"Test Task\", \"description\": \"Description\", \"status\": \"PENDING\", \"priority\": \"HIGH\", \"dueDate\": \"2027-12-31\", \"createdAt\": \"2027-10-01T10:00:00\" }"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Task"))
                .andExpect(jsonPath("$.description").value("Description"))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.priority").value("HIGH"));
    }

    // Comprueba que POST /api/v1/tasks devuelve HTTP 400 cuando se envía una tarea
    // con el título vacío y verifica el mensaje de error en el JSON. — generado por
    // IA
    /**
     * Test method for POST /api/v1/tasks endpoint.
     * This test verifies that the endpoint returns HTTP 400 status when a task with an empty title is provided.
     * It checks the JSON structure of the error response to ensure the correct error message is returned.
     * @throws Exception if an error occurs during the request
     */
    @Test
    public void createTask_shouldReturn400_whenTitleIsEmpty() throws Exception {
        // Realiza la solicitud POST con título vacío y verifica la respuesta
        mockMvc.perform(post("/api/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        "{ \"title\": \"\", \"description\": \"Description\", \"status\": \"PENDING\", \"priority\": \"HIGH\", \"dueDate\": \"2027-12-31\", \"createdAt\": \"2027-10-01T10:00:00\" }"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("El título no puede estar vacío"));
    }

    // Comprueba que POST /api/v1/tasks devuelve HTTP 400 cuando se envía un status
    // inválido y verifica la respuesta de error JSON. — generado por IA
    /**
     * Test method for POST /api/v1/tasks endpoint.
     * This test verifies that the endpoint returns HTTP 400 status when a task with an invalid status is provided.
     * It checks the JSON structure of the error response to ensure the correct error message is returned.
     * @throws Exception if an error occurs during the request
     */
    @Test
    public void createTask_shouldReturn400_whenStatusIsInvalid() throws Exception {
        // Realiza la solicitud POST con status inválido y verifica la respuesta
        mockMvc.perform(post("/api/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        "{ \"title\": \"Test Task\", \"description\": \"Description\", \"status\": \"INVALID_STATUS\", \"priority\": \"HIGH\", \"dueDate\": \"2027-12-31\", \"createdAt\": \"2027-10-01T10:00:00\" }"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("El campo 'status' contiene un valor no válido."));
    }

    // Comprueba que PUT /api/v1/tasks/{id} actualiza una tarea existente y devuelve
    // HTTP 200 con los datos actualizados en el JSON. — generado por IA
        /**
         * Test method for PUT /api/v1/tasks/{id} endpoint.
         * This test verifies that the endpoint updates an existing task and returns HTTP 200 status with the updated task data.
         * It mocks the TaskService to return a TaskResponseDTO and checks the JSON structure of the response.
         * @throws Exception if an error occurs during the request
         */
    @Test
    public void updateTask_shouldReturnUpdatedTask_whenTaskExists() throws Exception {
        // Configura la respuesta simulada del servicio
        TaskResponseDTO updatedTask = new TaskResponseDTO(
                1L,
                "Updated Task",
                "Updated Description",
                TaskStatus.DONE,
                TaskPriority.MEDIUM,
                LocalDate.of(2027, 12, 31),
                LocalDateTime.of(2027, 10, 1, 10, 0));

        when(taskService.update(any(), any())).thenReturn(updatedTask);

        // Realiza la solicitud PUT y verifica la respuesta
        mockMvc.perform(put("/api/v1/tasks/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        "{ \"title\": \"Updated Task\", \"description\": \"Updated Description\", \"status\": \"DONE\", \"priority\": \"MEDIUM\", \"dueDate\": \"2027-12-31\", \"createdAt\": \"2027-10-01T10:00:00\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Updated Task"))
                .andExpect(jsonPath("$.description").value("Updated Description"))
                .andExpect(jsonPath("$.status").value("DONE"))
                .andExpect(jsonPath("$.priority").value("MEDIUM"));
    }

    // Comprueba que PUT /api/v1/tasks/{id} devuelve HTTP 404 cuando se intenta
    // actualizar una tarea inexistente y verifica la respuesta de error JSON. —
    // generado por IA
    /** 
     * Test method for PUT /api/v1/tasks/{id} endpoint.
     * This test verifies that the endpoint returns HTTP 404 status when attempting to update a non-existent task.
     * It mocks the TaskService to throw a TaskNotFoundException and checks the JSON structure of the error response.
     * @throws Exception if an error occurs during the request
     */
    @Test
    public void updateTask_shouldReturn404_whenTaskDoesNotExist() throws Exception {
        // Configura la respuesta simulada del servicio para lanzar una excepción
        when(taskService.update(any(), any()))
                .thenThrow(new TaskNotFoundException("Task not found"));

        // Realiza la solicitud PUT y verifica la respuesta
        mockMvc.perform(put("/api/v1/tasks/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        "{ \"title\": \"Updated Task\", \"description\": \"Updated Description\", \"status\": \"DONE\", \"priority\": \"MEDIUM\", \"dueDate\": \"2027-12-31\", \"createdAt\": \"2027-10-01T10:00:00\" }"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Task not found"));
    }

    // Comprueba que DELETE /api/v1/tasks/{id} elimina una tarea existente y
    // devuelve HTTP 204 No Content. — generado por IA
    /**
     * Test method for DELETE /api/v1/tasks/{id} endpoint.
     * This test verifies that the endpoint deletes an existing task and returns HTTP 204 No Content status.
     * It mocks the TaskService to do nothing when deleting a task and checks the response status.
     * @throws Exception if an error occurs during the request
     */
    @Test
    public void deleteTask_shouldReturn204_whenTaskExists() throws Exception {
        // Configura el comportamiento simulado del servicio
        doNothing().when(taskService).delete(1L);

        // Realiza la solicitud DELETE y verifica la respuesta
        mockMvc.perform(delete("/api/v1/tasks/1"))
                .andExpect(status().isNoContent());
    }

    
    // Comprueba que DELETE /api/v1/tasks/{id} devuelve HTTP 404 cuando se intenta eliminar una tarea inexistente y verifica la respuesta de error JSON. — generado por IA
    /**
     * Test method for DELETE /api/v1/tasks/{id} endpoint.
     * This test verifies that the endpoint returns HTTP 404 status when attempting to delete a non-existent task.
     * It mocks the TaskService to throw a TaskNotFoundException and checks the JSON structure of the error response.
     * @throws Exception if an error occurs during the request
     */
    @Test
    public void deleteTask_shouldReturn404_whenTaskDoesNotExist() throws Exception {
        // Configura el comportamiento simulado del servicio para lanzar una excepción
        doThrow(new TaskNotFoundException("Task not found"))
                .when(taskService).delete(999L);

        // Realiza la solicitud DELETE y verifica la respuesta
        mockMvc.perform(delete("/api/v1/tasks/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Task not found"));
    }

}