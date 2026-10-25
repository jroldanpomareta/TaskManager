package com.accenture.taskmanager.dto;

/**
 * DTO para la respuesta de una categoría.
 * Contiene los campos necesarios para representar una categoría, incluyendo su ID, nombre y descripción.
 * 
 * @author Javier Roldán
 * @version 1.0
 */
// Crear CategoryResponseDTO para devolver los datos de una categoría, con los campos id (Long), name y description (String), sus getters y setters, y Javadoc de clase y métodos. Mantener el estilo de los DTOs existentes. — generado por IA
public class CategoryResponseDTO {

    private Long id;
    private String name;
    private String description;

    /**
     * Obtiene el ID de la categoría.
     *
     * @return el ID de la categoría
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece el ID de la categoría.
     *
     * @param id el ID de la categoría
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre de la categoría.
     *
     * @return el nombre de la categoría
     */
    public String getName() {
        return name;
    }

    /**
     * Establece el nombre de la categoría.
     *
     * @param name el nombre de la categoría
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Obtiene la descripción de la categoría.
     *
     * @return la descripción de la categoría
     */
    public String getDescription() {
        return description;
    }

    /**
     * Establece la descripción de la categoría.
     *
     * @param description la descripción de la categoría
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
