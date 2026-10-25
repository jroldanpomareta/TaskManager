package com.accenture.taskmanager.mapper;

import com.accenture.taskmanager.dto.CategoryRequestDTO;
import com.accenture.taskmanager.dto.CategoryResponseDTO;
import com.accenture.taskmanager.model.Category;

//javadoc
/**
 * Clase de mapeo para convertir entre la entidad Category y los DTOs CategoryRequestDTO y CategoryResponseDTO.
 * Proporciona métodos estáticos para realizar las conversiones necesarias.
 * 
 * @author Javier Roldán
 * @version 1.0
 */
// Crear CategoryMapper para convertir entre la entidad Category y CategoryRequestDTO/CategoryResponseDTO. Implementar métodos estáticos toResponseDTO(Category category) y toEntity(CategoryRequestDTO dto), copiando id, name y description cuando corresponda. Gestionar valores null y añadir Javadoc. Mantener el estilo de TaskMapper. — generado por IA
public class CategoryMapper {

    /**
     * Convierte una entidad Category a un DTO de respuesta CategoryResponseDTO.
     *
     * @param category la entidad Category a convertir
     * @return el DTO de respuesta CategoryResponseDTO correspondiente
     */
    public static CategoryResponseDTO toResponseDTO(Category category) {
        if (category == null) {
            return null;
        }
        CategoryResponseDTO dto = new CategoryResponseDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        return dto;
    }

    /**
     * Convierte un DTO de solicitud CategoryRequestDTO a una entidad Category.
     *
     * @param dto el DTO de solicitud CategoryRequestDTO a convertir
     * @return la entidad Category correspondiente
     */
    public static Category toEntity(CategoryRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        Category category = new Category();
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        return category;
    }
}
