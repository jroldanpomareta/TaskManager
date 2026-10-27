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

    // Documenta el campo name con @Schema, indicando en español que es el nombre de la categoría e incluyendo un ejemplo representativo. Utiliza únicamente los atributos description y example. No modifiques la lógica existente. — generado por IA
    @io.swagger.v3.oas.annotations.media.Schema(description = "Nombre de la categoría", example = "Trabajo")
    private String name;
    // Documenta el campo description con @Schema, indicando en español que es la descripción de la categoría e incluyendo un ejemplo representativo. Utiliza únicamente los atributos description y example. No modifiques la lógica existente. — generado por IA
    @io.swagger.v3.oas.annotations.media.Schema(description = "Descripción de la categoría", example = "Tareas relacionadas con el trabajo")
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
