package com.accenture.taskmanager.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.accenture.taskmanager.dto.CategoryRequestDTO;
import com.accenture.taskmanager.dto.CategoryResponseDTO;
import com.accenture.taskmanager.exceptions.ErrorResponse;
import com.accenture.taskmanager.service.interfaces.CategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Controlador REST para gestionar las categorías.
 */
// Crear un controlador REST para gestionar las categorías bajo /api/v1/categories. Inyectar CategoryService mediante constructor y definir los cinco endpoints CRUD: listar categorías (GET), buscar por ID (GET /{id}), crear (POST), actualizar (PUT /{id}) y eliminar (DELETE /{id}). Utilizar CategoryRequestDTO para las peticiones y CategoryResponseDTO para las respuestas, devolver los códigos HTTP adecuados y mantener el estilo del proyecto. — generado por IA
// Documenta este controller con @Tag, indicando en español que agrupa las operaciones de gestión de categorías de la API. No modifiques la lógica existente. — generado por IA
@Tag(name = "Categorías", description = "Operaciones de gestión de categorías")
@RestController 
@RequestMapping ("/api/v1/categories")
public class CategoryController {
    
    /**
     * Servicio de categorías inyectado mediante constructor.
     */
    private final CategoryService categoryService;

    /**
     * Constructor de la clase CategoryController.
     *
     * @param categoryService el servicio de categorías a inyectar
     */
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
    
    /**
     * Endpoint GET para listar todas las categorías.
     *
     * @return una lista de CategoryResponseDTO con HTTP 200
     */
    // Crear un endpoint GET para listar todas las categorías. Utilizar CategoryService.listCategories() y devolver una lista de CategoryResponseDTO con HTTP 200. Añadir los imports necesarios y Javadoc. — generado por IA
    // Documenta este endpoint con @Operation y @ApiResponse. Describe en español el listado de categorías y la respuesta HTTP 200 con una lista de CategoryResponseDTO. Documenta también los errores 500 cuando correspondan al comportamiento real, utilizando ErrorResponse mediante @Content(schema = @Schema(implementation = ErrorResponse.class)). No modifiques la lógica existente. — generado por IA
    @Operation(summary = "Listar categorías", description = "Obtiene una lista de todas las categorías disponibles.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de categorías obtenida correctamente", 
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = CategoryResponseDTO.class)))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor", 
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public List<CategoryResponseDTO> listCategories() {
        return categoryService.listCategories();
    }
    
    /**
     * Endpoint GET /{id} para buscar una categoría por su ID.
     *
     * @param id el ID de la categoría a buscar
     * @return el CategoryResponseDTO con la categoría encontrada o HTTP 404 si no se encuentra
     */
    // Crear un endpoint GET /{id} para buscar una categoría por su ID. Utilizar CategoryService.findCategoryById(id) y devolver un CategoryResponseDTO. Recibir el ID mediante @PathVariable y añadir los imports necesarios y Javadoc. — generado por IA
    // Documenta este endpoint con @Operation y @ApiResponse. Describe en español la búsqueda de una categoría por su ID, el parámetro id y la respuesta HTTP 200 con CategoryResponseDTO. Documenta también las respuestas 404 y 500 cuando correspondan al comportamiento real, utilizando ErrorResponse mediante @Content(schema = @Schema(implementation = ErrorResponse.class)) para los errores. No modifiques la lógica existente. — generado por IA
    @Operation(summary = "Buscar categoría por ID", description = "Obtiene una categoría específica mediante su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categoría encontrada correctamente", 
            content = @Content(schema = @Schema(implementation = CategoryResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Categoría no encontrada", 
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor", 
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public CategoryResponseDTO findCategoryById(@PathVariable Long id) {
        return categoryService.findCategoryById(id);
    }

    /**
     * Endpoint POST para crear una nueva categoría.
     *
     * @param categoryRequestDTO los datos de la categoría a crear
     * @return el CategoryResponseDTO con la categoría creada o HTTP 400 si los datos son inválidos
     */
    // Crear un endpoint POST para crear una categoría. Recibir CategoryRequestDTO mediante @RequestBody, llamar a CategoryService.createCategory() y devolver el CategoryResponseDTO creado con HTTP 201 Created. Añadir los imports necesarios y Javadoc. — generado por IA
    // Documenta este endpoint con @Operation y @ApiResponse. Describe en español la creación de una categoría, el cuerpo CategoryRequestDTO y la respuesta HTTP 201 con CategoryResponseDTO. Documenta también el error 500 cuando corresponda al comportamiento real. No añadas una respuesta 400 si el endpoint no valida los datos, y no modifiques la lógica existente. — generado por IA
    @Operation(summary = "Crear categoría", description = "Crea una nueva categoría con los datos proporcionados.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Categoría creada correctamente", 
            content = @Content(schema = @Schema(implementation = CategoryResponseDTO.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor", 
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CategoryResponseDTO createCategory(@RequestBody CategoryRequestDTO categoryRequestDTO) {
        return categoryService.createCategory(categoryRequestDTO);
    }

    /**
     * Endpoint PUT /{id} para actualizar una categoría existente.
     *
     * @param id el ID de la categoría a actualizar
     * @param categoryRequestDTO los datos actualizados de la categoría
     * @return el CategoryResponseDTO con la categoría actualizada o HTTP 404 si no se encuentra
     */
    // Crear un endpoint PUT /{id} para actualizar una categoría. Recibir el ID mediante @PathVariable y CategoryRequestDTO mediante @RequestBody. Llamar a CategoryService.updateCategory(id, categoryRequestDTO) y devolver el CategoryResponseDTO actualizado. Añadir los imports necesarios y Javadoc. — generado por IA
    // Documenta este endpoint con @Operation y @ApiResponse. Describe en español la actualización de una categoría por ID, los parámetros id y CategoryRequestDTO, y la respuesta HTTP 200 con CategoryResponseDTO. Documenta también el error 500 cuando corresponda al comportamiento real. No añadas una respuesta 400 si el endpoint no valida los datos, y no modifiques la lógica existente. — generado por IA
    @Operation(summary = "Actualizar categoría", description = "Actualiza una categoría existente mediante su ID con los datos proporcionados.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categoría actualizada correctamente", 
            content = @Content(schema = @Schema(implementation = CategoryResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Categoría no encontrada", 
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor", 
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{id}")
    public CategoryResponseDTO updateCategory(@PathVariable Long id, @RequestBody CategoryRequestDTO categoryRequestDTO) {
        return categoryService.updateCategory(id, categoryRequestDTO);
    }

    /**
     * Endpoint DELETE /{id} para eliminar una categoría.
     *
     * @param id el ID de la categoría a eliminar
     */
    // Crear un endpoint DELETE /{id} para eliminar una categoría. Recibir el ID mediante @PathVariable, llamar a CategoryService.deleteCategory(id) y devolver HTTP 204 No Content. Añadir los imports necesarios y Javadoc. — generado por IA
    // Documenta este endpoint con @Operation y @ApiResponse. Describe en español la eliminación de una categoría por ID, el parámetro id y la respuesta HTTP 204 sin cuerpo. Documenta también las respuestas 404 y 500 cuando correspondan al comportamiento real, utilizando ErrorResponse mediante @Content(schema = @Schema(implementation = ErrorResponse.class)) para los errores. No modifiques la lógica existente. — generado por IA
    @Operation(summary = "Eliminar categoría", description = "Elimina una categoría existente mediante su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Categoría eliminada correctamente"),
        @ApiResponse(responseCode = "404", description = "Categoría no encontrada", 
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor", 
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
    }
}
