package com.accenture.taskmanager.service;

import java.util.List;

import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.accenture.taskmanager.exceptions.CategoryNotFoundException;

import com.accenture.taskmanager.model.Category;
import com.accenture.taskmanager.mapper.CategoryMapper;

import com.accenture.taskmanager.dto.CategoryRequestDTO;
import com.accenture.taskmanager.dto.CategoryResponseDTO;
import com.accenture.taskmanager.repository.CategoryRepository;
import com.accenture.taskmanager.service.interfaces.CategoryService;

// Crear CategoryServiceImpl en el paquete com.accenture.taskmanager.service.impl e implementar CategoryService. Inyectar CategoryRepository, utilizar los métodos estáticos de CategoryMapper y completar las cinco operaciones CRUD definidas en CategoryService. Mapear las entidades y DTOs correctamente, actualizar name y description, y gestionar las categorías inexistentes con una excepción coherente con el proyecto. Añadir los imports necesarios y Javadoc, manteniendo el estilo existente. — generado por IA
/**
 * Implementation of the CategoryService interface.
 * Provides CRUD operations for managing categories.
 * 
 * @author Javier Roldán
 * @version 1.0
 */
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    /**
     * Constructor for CategoryServiceImpl.
     *
     * @param categoryRepository the repository for managing categories
     */
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * Retrieves a list of all categories.
     *
     * @return a list of CategoryResponseDTO representing all categories
     */
    @Override
    public List<CategoryResponseDTO> listCategories() {
        return categoryRepository.findAll().stream()
                .map(CategoryMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Finds a category by its ID.
     *
     * @param id the ID of the category to find
     * @return a CategoryResponseDTO representing the found category
     * @throws CategoryNotFoundException if the category is not found
     */
    @Override
    public CategoryResponseDTO findCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + id));
        return CategoryMapper.toResponseDTO(category);
    }

    /**
     * Creates a new category.
     *
     * @param categoryRequestDTO the DTO containing the details of the category to create
     * @return a CategoryResponseDTO representing the created category
     */
    @Override
    public CategoryResponseDTO createCategory(CategoryRequestDTO categoryRequestDTO) {
        Category category = CategoryMapper.toEntity(categoryRequestDTO);
        category = categoryRepository.save(category);
        return CategoryMapper.toResponseDTO(category);
    }

    /**
     * Updates an existing category.
     *
     * @param id                   the ID of the category to update
     * @param categoryRequestDTO   the DTO containing the updated details of the category
     * @return a CategoryResponseDTO representing the updated category
     * @throws CategoryNotFoundException if the category is not found
     */
    @Override
    public CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO categoryRequestDTO) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + id));
        existingCategory.setName(categoryRequestDTO.getName());
        existingCategory.setDescription(categoryRequestDTO.getDescription());
        categoryRepository.save(existingCategory);
        return CategoryMapper.toResponseDTO(existingCategory);
    }

    // Comprueba que la categoría existe antes de eliminarla; si no, lanza una
    // excepción. — generado por IA
    //usa la excepción personalizada de categoría no encontrada en lugar de RuntimeException. — generado por IA
    /**
     * Deletes a category by its ID.
     *
     * @param id the ID of the category to delete
     * @throws CategoryNotFoundException if the category is not found
     */
    @Override
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new CategoryNotFoundException("Category not found with id: " + id);
        }
        categoryRepository.deleteById(id);
    }
}