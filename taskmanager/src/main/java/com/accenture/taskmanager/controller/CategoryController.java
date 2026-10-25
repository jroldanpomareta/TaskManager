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
import com.accenture.taskmanager.service.interfaces.CategoryService;

/**
 * Controlador REST para gestionar las categorías.
 */
// Crear un controlador REST para gestionar las categorías bajo /api/v1/categories. Inyectar CategoryService mediante constructor y definir los cinco endpoints CRUD: listar categorías (GET), buscar por ID (GET /{id}), crear (POST), actualizar (PUT /{id}) y eliminar (DELETE /{id}). Utilizar CategoryRequestDTO para las peticiones y CategoryResponseDTO para las respuestas, devolver los códigos HTTP adecuados y mantener el estilo del proyecto. — generado por IA
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
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
    }
}
