
package com.accenture.taskmanager;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.accenture.taskmanager.dto.TaskRequestDTO;
import com.accenture.taskmanager.dto.TaskResponseDTO;
import com.accenture.taskmanager.enums.TaskPriority;
import com.accenture.taskmanager.enums.TaskStatus;
import com.accenture.taskmanager.model.Tag;
import com.accenture.taskmanager.repository.TagRepository;
import com.accenture.taskmanager.repository.TaskRepository;

// Genera el esqueleto de TaskIntegrationTest con @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT), @ActiveProfiles("test"), @AutoConfigureTestRestTemplate si es necesario en Spring Boot 4, la inyección de TestRestTemplate y TaskRepository, y un método @BeforeEach para limpiar la base de datos antes de cada test. No uses mocks. — generado por IA
// Crear una clase de prueba de integración para verificar el funcionamiento
// completo de las operaciones CRUD de tareas mediante peticiones HTTP.
// Utilizar @SpringBootTest con un servidor web aleatorio, activar el perfil
// test y configurar TestRestTemplate. Verificar la creación, consulta,
// actualización y eliminación de una tarea. — generado por IA
/**
 * Clase de prueba de integración para verificar el funcionamiento completo de
 * las operaciones CRUD de tareas mediante peticiones HTTP. Utiliza
 * @SpringBootTest con un servidor web aleatorio, activa el perfil "test" y
 * configura TestRestTemplate para realizar las solicitudes HTTP.
 * 
 * @author Javier Roldán
 * @version 1.0
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureTestRestTemplate
public class TaskIntegrationTest {

    /**
     * TestRestTemplate para realizar peticiones HTTP a la aplicación durante las pruebas de integración.
     */
    @Autowired
    private TestRestTemplate testRestTemplate;

    /**
     * TestRestTemplate para realizar peticiones HTTP a la aplicación durante las pruebas de integración.
     */
    @Autowired
    private TaskRepository taskRepository;

    // Inyectar TagRepository para crear una etiqueta válida durante la prueba
    // de actualización. Mantener la lógica de los servicios sin modificaciones.
    // — generado por IA
    /**
     * Repositorio de etiquetas para realizar operaciones CRUD sobre las etiquetas
     * asociadas a las tareas durante las pruebas de integración.
     */
    @Autowired
    private TagRepository tagRepository;

    // Limpiar los datos de prueba antes de cada ejecución y configurar las
    // peticiones HTTP para enviar contenido JSON. — generado por IA
    /**
     * Configuración previa a cada prueba de integración. Este método se ejecuta
     * antes de cada test y se encarga de limpiar la base de datos eliminando todas
     * las tareas y etiquetas existentes. Además, configura el TestRestTemplate para
     * que todas las peticiones HTTP envíen el encabezado "Content-Type" con el
     * valor "application/json", asegurando que las solicitudes sean interpretadas
     * correctamente por el servidor.
     */
    @BeforeEach
    public void setUp() {
        testRestTemplate.getRestTemplate().getInterceptors().add(
                (request, body, execution) -> {
                    request.getHeaders().add("Content-Type", "application/json");
                    return execution.execute(request, body);
                });

        taskRepository.deleteAll();
        tagRepository.deleteAll();
    }

    // Crear una prueba de integración que cree una tarea mediante POST,
    // consulte la tarea mediante GET, la actualice mediante PUT y compruebe
    // la actualización, y finalmente la elimine mediante DELETE verificando
    // que una consulta posterior devuelve HTTP 404. — generado por IA
    @Test
    /**
     * Test de integración para verificar el flujo completo de operaciones CRUD
     * sobre una tarea mediante peticiones HTTP. Se crea una tarea, se consulta,
     * se actualiza y finalmente se elimina, verificando los códigos de estado
     * HTTP y los datos devueltos en cada paso.
     */
    public void testTaskIntegration() {

        // Crear una tarea
        TaskRequestDTO taskRequest = new TaskRequestDTO(
                "Test Task",
                "Description",
                TaskStatus.PENDING,
                TaskPriority.HIGH,
                LocalDate.of(2027, 12, 31));

        ResponseEntity<TaskResponseDTO> createResponse = testRestTemplate.postForEntity(
                "/api/v1/tasks",
                taskRequest,
                TaskResponseDTO.class);

        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        assertNotNull(createResponse.getBody());

        Long taskId = createResponse.getBody().getId();

        // Consultar la tarea creada y comprobar que se devuelve correctamente
        // mediante una petición GET. — generado por IA
        ResponseEntity<TaskResponseDTO> getResponse = testRestTemplate.getForEntity(
                "/api/v1/tasks/" + taskId,
                TaskResponseDTO.class);

        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertNotNull(getResponse.getBody());
        assertEquals("Test Task", getResponse.getBody().getTitle());

        // Crear una etiqueta válida para utilizarla en la actualización
        // y evitar enviar una lista de etiquetas vacía. — generado por IA
        Tag tag = tagRepository.save(new Tag("Integración"));

        // Preparar los datos para actualizar la tarea
        TaskRequestDTO updateRequest = new TaskRequestDTO(
                "Updated Task",
                "Updated Description",
                TaskStatus.DONE,
                TaskPriority.MEDIUM,
                LocalDate.of(2027, 12, 31));

        // Asociar la etiqueta existente a la tarea que se va a actualizar
        // mediante la petición PUT. — generado por IA
        updateRequest.setTagIds(List.of(tag.getId()));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<TaskRequestDTO> request = new HttpEntity<>(updateRequest, headers);

        // Actualizar la tarea mediante una petición PUT y comprobar
        // el código HTTP devuelto por el servidor. — generado por IA
        ResponseEntity<String> putResponse = testRestTemplate.exchange(
                "/api/v1/tasks/" + taskId,
                HttpMethod.PUT,
                request,
                String.class);

        assertEquals(HttpStatus.OK, putResponse.getStatusCode());

        // Consultar de nuevo la tarea y verificar que los datos se han
        // actualizado correctamente. — generado por IA
        ResponseEntity<TaskResponseDTO> updateResponse = testRestTemplate.getForEntity(
                "/api/v1/tasks/" + taskId,
                TaskResponseDTO.class);

        assertEquals(HttpStatus.OK, updateResponse.getStatusCode());
        assertNotNull(updateResponse.getBody());
        assertEquals("Updated Task", updateResponse.getBody().getTitle());
        assertEquals("Updated Description",
                updateResponse.getBody().getDescription());
        assertEquals(TaskStatus.DONE, updateResponse.getBody().getStatus());
        assertEquals(TaskPriority.MEDIUM,
                updateResponse.getBody().getPriority());

        // Eliminar la tarea mediante una petición DELETE. — generado por IA
        testRestTemplate.delete("/api/v1/tasks/" + taskId);

        // Comprobar que la tarea eliminada ya no existe y que el servidor
        // devuelve HTTP 404 al intentar consultarla. — generado por IA
        ResponseEntity<String> finalResponse = testRestTemplate.getForEntity(
                "/api/v1/tasks/" + taskId,
                String.class);

        assertEquals(HttpStatus.NOT_FOUND, finalResponse.getStatusCode());
    }

    // Crear un test de integración que envíe una petición POST a /api/v1/tasks con
    // el título vacío y compruebe que el servidor devuelve HTTP 400 y un mensaje de
    // error en la respuesta. Mantener los componentes reales, sin mocks, y seguir
    // la estructura Given / When / Then. — generado por IA
    /**
     * Test de integración para verificar la respuesta del servidor al intentar
     * crear una tarea con el título vacío mediante POST /api/v1/tasks. Se comprueba
     * que el servidor devuelve HTTP 400 y un mensaje de error en la respuesta.
     */
    @Test
    public void testCreateTaskWithEmptyTitle() {
        // Given: Preparar una solicitud de creación de tarea con el título vacío
        TaskRequestDTO taskRequest = new TaskRequestDTO(
                "",
                "Description",
                TaskStatus.PENDING,
                TaskPriority.HIGH,
                LocalDate.of(2027, 12, 31));

        // When: Enviar la solicitud POST al endpoint de creación de tareas
        ResponseEntity<String> response = testRestTemplate.postForEntity(
                "/api/v1/tasks",
                taskRequest,
                String.class);

        // Then: Verificar que el servidor devuelve HTTP 400 y un mensaje de error
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    // Crear un test de integración que consulte mediante GET /api/v1/tasks/{id} una
    // tarea con un ID inexistente y compruebe que el servidor devuelve HTTP 404 y
    // un cuerpo con ErrorResponse. Utilizar componentes reales, sin mocks, y seguir
    // la estructura Given / When / Then. — generado por IA
    /**
     * Test de integración para verificar la respuesta del servidor al consultar una
     * tarea con un ID inexistente mediante GET /api/v1/tasks/{id}. Se comprueba que
     * el servidor devuelve HTTP 404 y un cuerpo con ErrorResponse.
     */
    @Test
    public void testGetNonExistentTask() {
        // Given: Un ID de tarea que no existe en la base de datos
        Long nonExistentTaskId = 999L;

        // When: Enviar una solicitud GET al endpoint de consulta de tareas con el ID
        // inexistente
        ResponseEntity<String> response = testRestTemplate.getForEntity(
                "/api/v1/tasks/" + nonExistentTaskId,
                String.class);

        // Then: Verificar que el servidor devuelve HTTP 404 y un cuerpo con
        // ErrorResponse
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    // Crear un test de integración que cree varias tareas con distintos estados y
    // prioridades y consulte el endpoint
    // /api/v1/tasks/by-status-priority?status=PENDING&priority=HIGH. Comprobar que
    // la respuesta HTTP es correcta y que solo aparecen las tareas cuyo estado sea
    // PENDING y cuya prioridad sea HIGH. Utilizar componentes reales, sin mocks, y
    // seguir la estructura Given / When / Then. — generado por IA
    /**
     * Test de integración para verificar la funcionalidad del endpoint
     * /api/v1/tasks/by-status-priority. Este test crea varias tareas con distintos
     * estados y prioridades, y luego consulta el endpoint filtrando por estado
     * PENDING y prioridad HIGH. Se comprueba que la respuesta HTTP es correcta y
     * que solo aparecen las tareas que cumplen con los criterios de filtrado.
     */
    @Test
    public void testGetTasksByStatusAndPriority() {
        // Given: Crear varias tareas con distintos estados y prioridades
        TaskRequestDTO task1 = new TaskRequestDTO(
                "Task 1",
                "Description 1",
                TaskStatus.PENDING,
                TaskPriority.HIGH,
                LocalDate.of(2027, 12, 31));

        TaskRequestDTO task2 = new TaskRequestDTO(
                "Task 2",
                "Description 2",
                TaskStatus.DONE,
                TaskPriority.MEDIUM,
                LocalDate.of(2027, 12, 31));

        TaskRequestDTO task3 = new TaskRequestDTO(
                "Task 3",
                "Description 3",
                TaskStatus.PENDING,
                TaskPriority.HIGH,
                LocalDate.of(2027, 12, 31));

        testRestTemplate.postForEntity("/api/v1/tasks", task1, TaskResponseDTO.class);
        testRestTemplate.postForEntity("/api/v1/tasks", task2, TaskResponseDTO.class);
        testRestTemplate.postForEntity("/api/v1/tasks", task3, TaskResponseDTO.class);

        // When: Consultar el endpoint /api/v1/tasks/by-status-priority con
        // status=PENDING y priority=HIGH
        ResponseEntity<String> response = testRestTemplate.getForEntity(
                "/api/v1/tasks/by-status-priority?status=PENDING&priority=HIGH",
                String.class);

        System.out.println("FILTRO STATUS: " + response.getStatusCode());
        System.out.println("FILTRO BODY: " + response.getBody());

        // Then: Verificar que la respuesta HTTP es correcta y que solo aparecen las
        // tareas cuyo estado sea PENDING y cuya prioridad sea HIGH
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        // Comprobar que la respuesta contiene las tareas esperadas
        assertTrue(response.getBody().contains("Task 1"));
        assertTrue(response.getBody().contains("Task 3"));
        assertFalse(response.getBody().contains("Task 2"));
    }
}
