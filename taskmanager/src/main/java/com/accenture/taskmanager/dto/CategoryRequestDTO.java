package com.accenture.taskmanager.dto;

/**
 * DTO para la solicitud de creación o actualización de una categoría.
 * Contiene los campos necesarios para representar una categoría, incluyendo su nombre y descripción.
 * 
 * @author Javier Roldán
 * @version 1.0
 */
// Crear CategoryRequestDTO para recibir los datos de una categoría, con los campos name y description, sus getters y setters, y Javadoc de clase y métodos. Mantener el estilo de los DTOs existentes. — generado por IA
public class CategoryRequestDTO {

    private String name;
    private String description;

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
