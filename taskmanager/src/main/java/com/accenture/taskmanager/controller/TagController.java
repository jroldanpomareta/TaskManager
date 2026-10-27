package com.accenture.taskmanager.controller;

import java.util.List;

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

import com.accenture.taskmanager.dto.TagRequestDTO;
import com.accenture.taskmanager.dto.TagResponseDTO;
import com.accenture.taskmanager.exceptions.ErrorResponse;
import com.accenture.taskmanager.service.interfaces.TagService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Controlador para la gestión de etiquetas.
 * Proporciona endpoints para realizar operaciones CRUD sobre las etiquetas.
 *
 * @author Javier Roldán
 * @version 1.0
 */
// Crear TagController con @RestController y @RequestMapping("/api/v1/tags"), inyectando TagService por constructor. Implementar los endpoints GET para listar todas las etiquetas y obtener una por ID, POST para crear, PUT para actualizar por ID y DELETE para eliminar por ID. Utilizar los DTOs correspondientes, devolver códigos HTTP adecuados y añadir Javadoc a la clase y a todos los métodos. Mantener el estilo de los controladores existentes. — generado por IA
// Documenta este controller con @Tag, indicando en español que agrupa las operaciones de gestión de etiquetas (tags) de la API. No modifiques la lógica existente. — generado por IA
@Tag(name = "Etiquetas", description = "Operaciones de gestión de etiquetas")
@RestController
@RequestMapping("/api/v1/tags")
public class TagController {

    /**
     * Servicio de gestión de etiquetas.
     */
    private final TagService tagService;

    /**
     * Constructor del controlador.
     *
     * @param tagService servicio de gestión de etiquetas
     */
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    /**
     * Lista todas las etiquetas.
     *
     * @return lista de etiquetas con estado HTTP 200
     */
    // Documenta este endpoint con @Operation y @ApiResponse. Describe en español el listado de etiquetas y la respuesta HTTP 200 con una lista de TagResponseDTO. Documenta también el error 500 cuando corresponda al comportamiento real, utilizando ErrorResponse mediante @Content(schema = @Schema(implementation = ErrorResponse.class)). No modifiques la lógica existente. — generado por IA
    @Operation(summary = "Listar etiquetas", description = "Obtiene una lista de todas las etiquetas.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de etiquetas obtenida correctamente",
            content = @Content(schema = @Schema(implementation = TagResponseDTO.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<List<TagResponseDTO>> getAllTags() {
        return ResponseEntity.ok(tagService.getAllTags());
    }

    /**
     * Obtiene una etiqueta por su identificador.
     *
     * @param id identificador de la etiqueta
     * @return etiqueta encontrada con estado HTTP 200
     */
    // Documenta este endpoint con @Operation y @ApiResponse. Describe en español la consulta de una etiqueta por ID, la respuesta HTTP 200 con TagResponseDTO y los errores HTTP 404 y 500 con ErrorResponse cuando correspondan. No modifiques la lógica existente. — generado por IA
    @Operation(summary = "Obtener etiqueta por ID", description = "Obtiene una etiqueta específica por su identificador.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Etiqueta obtenida correctamente",
            content = @Content(schema = @Schema(implementation = TagResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Etiqueta no encontrada",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<TagResponseDTO> getTagById(@PathVariable Long id) {
        return ResponseEntity.ok(tagService.getTagById(id));
    }

    /**
     * Crea una nueva etiqueta.
     *
     * @param tagRequestDTO datos de la etiqueta
     * @return etiqueta creada con estado HTTP 201
     */
    // Documenta este endpoint con @Operation y @ApiResponse. Describe en español la creación de una etiqueta y la respuesta HTTP 201 con TagResponseDTO. Incluye el error 500 con ErrorResponse cuando corresponda. No documentes un 400 de validación porque el parámetro no utiliza @Valid. No modifiques la lógica existente. — generado por IA
    @Operation(summary = "Crear etiqueta", description = "Crea una nueva etiqueta con los datos proporcionados.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Etiqueta creada correctamente",
            content = @Content(schema = @Schema(implementation = TagResponseDTO.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<TagResponseDTO> createTag(@RequestBody TagRequestDTO tagRequestDTO) {
        TagResponseDTO createdTag = tagService.createTag(tagRequestDTO);
        return new ResponseEntity<>(createdTag, HttpStatus.CREATED);
    }

    /**
     * Actualiza una etiqueta existente.
     *
     * @param id identificador de la etiqueta
     * @param tagRequestDTO datos actualizados
     * @return etiqueta actualizada con estado HTTP 200
     */
    // Documenta este endpoint con @Operation y @ApiResponse. Describe en español la actualización de una etiqueta por ID, la respuesta HTTP 200 con TagResponseDTO y los errores HTTP 404 y 500 con ErrorResponse cuando correspondan. No documentes un 400 de validación porque el parámetro no utiliza @Valid. No modifiques la lógica existente. — generado por IA
    @Operation(summary = "Actualizar etiqueta", description = "Actualiza una etiqueta existente con los datos proporcionados.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Etiqueta actualizada correctamente",
            content = @Content(schema = @Schema(implementation = TagResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Etiqueta no encontrada",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<TagResponseDTO> updateTag(
            @PathVariable Long id,
            @RequestBody TagRequestDTO tagRequestDTO) {
        return ResponseEntity.ok(tagService.updateTag(id, tagRequestDTO));
    }

    /**
     * Elimina una etiqueta por su identificador.
     *
     * @param id identificador de la etiqueta
     * @return respuesta vacía con estado HTTP 204
     */
    // Documenta este endpoint con @Operation y @ApiResponse. Describe en español la eliminación de una etiqueta por ID, la respuesta HTTP 204 sin contenido y los errores HTTP 404 y 500 con ErrorResponse cuando correspondan. No modifiques la lógica existente. — generado por IA
    @Operation(summary = "Eliminar etiqueta", description = "Elimina una etiqueta específica por su identificador.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Etiqueta eliminada correctamente"),
        @ApiResponse(responseCode = "404", description = "Etiqueta no encontrada",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
        tagService.deleteTag(id);
        return ResponseEntity.noContent().build();
    }
}