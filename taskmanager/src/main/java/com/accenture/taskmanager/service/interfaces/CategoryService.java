package com.accenture.taskmanager.service.interfaces;

import java.util.List;

import com.accenture.taskmanager.dto.CategoryRequestDTO;
import com.accenture.taskmanager.dto.CategoryResponseDTO;

/**
 * Interfaz que define los métodos de servicio para la entidad Category.
 * Proporciona operaciones CRUD (Create, Read, Update, Delete) para las categorías.
 * Utiliza CategoryRequestDTO para recibir datos de entrada y CategoryResponseDTO para devolver datos de salida.
 * 
 * @author Javier Roldán
 * @version 1.0
 */
// Crear la interfaz CategoryService con los métodos CRUD básicos para categorías: listar todas, buscar por id, crear, actualizar y eliminar. Utilizar CategoryRequestDTO y CategoryResponseDTO como objetos de entrada y salida, siguiendo el estilo de TaskService. Añadir Javadoc. — generado por IA
public interface CategoryService {
    /**
     * Lista todas las categorías disponibles.
     *
     * @return una lista de CategoryResponseDTO que representa todas las categorías
     */
    List<CategoryResponseDTO> listCategories();

    /**
     * Busca una categoría por su ID.
     *
     * @param id el ID de la categoría a buscar
     * @return un CategoryResponseDTO que representa la categoría encontrada, o null si no se encuentra
     */
    CategoryResponseDTO findCategoryById(Long id);

    /**
     * Crea una nueva categoría.
     *
     * @param categoryRequestDTO los datos de la nueva categoría
     * @return un CategoryResponseDTO que representa la categoría creada
     */
    CategoryResponseDTO createCategory(CategoryRequestDTO categoryRequestDTO);

    /**
     * Actualiza una categoría existente.
     *
     * @param id el ID de la categoría a actualizar
     * @param categoryRequestDTO los nuevos datos de la categoría
     * @return un CategoryResponseDTO que representa la categoría actualizada
     */
    CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO categoryRequestDTO);

    /**
     * Elimina una categoría por su ID.
     *
     * @param id el ID de la categoría a eliminar
     */
    void deleteCategory(Long id);

}
