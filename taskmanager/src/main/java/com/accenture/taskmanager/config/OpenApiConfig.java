package com.accenture.taskmanager.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

// Crea la configuración general de OpenAPI con @OpenAPIDefinition. Define el título "Task Manager API", una descripción en español de la API de gestión de tareas, la versión "1.0", un contacto con el nombre "Javier Roldán" y la URL "http://localhost:8080" mediante @Server. Utiliza las anotaciones de io.swagger.v3.oas.annotations. No modifiques ninguna otra clase. — generado por IA
@OpenAPIDefinition(
    info = @Info(
        title = "Task Manager API",
        description = "API de gestión de tareas",
        version = "1.0",
        contact = @Contact(
            name = "Javier Roldán",
            url = "http://localhost:8080"
        )
    ),
    servers = {
        @Server(
            url = "http://localhost:8080",
            description = "Servidor local"
        )
    }
)
public class OpenApiConfig {

}
