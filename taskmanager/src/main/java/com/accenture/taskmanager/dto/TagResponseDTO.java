package com.accenture.taskmanager.dto;

/**
 * DTO para la respuesta de una etiqueta (Tag).
 * Contiene el identificador y el nombre de la etiqueta.
 * 
 * @author Javier Roldán
 * @version 1.0
 */
// Crear TagResponseDTO con los campos id (Long) y name (String), sus getters y setters, y Javadoc a nivel de clase y de todos los métodos. Mantener el estilo de los DTOs existentes del proyecto. — generado por IA
public class TagResponseDTO {

    // Documenta el campo id con @Schema, indicando en español que es el identificador único de la etiqueta e incluyendo un ejemplo numérico representativo. Utiliza únicamente los atributos description y example. No modifiques la lógica existente. — generado por IA
    @io.swagger.v3.oas.annotations.media.Schema(description = "Identificador único de la etiqueta", example = "1")
    private Long id;
    // Documenta el campo name con @Schema, indicando en español que es el nombre de la etiqueta e incluyendo un ejemplo representativo. Utiliza únicamente los atributos description y example. No modifiques la lógica existente. — generado por IA
    @io.swagger.v3.oas.annotations.media.Schema(description = "Nombre de la etiqueta", example = "Urgente")
    private String name;

    /**
     * Obtiene el ID de la etiqueta.
     *
     * @return el ID de la etiqueta
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece el ID de la etiqueta.
     *
     * @param id el ID de la etiqueta
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre de la etiqueta.
     *
     * @return el nombre de la etiqueta
     */
    public String getName() {
        return name;
    }

    /**
     * Establece el nombre de la etiqueta.
     *
     * @param name el nombre de la etiqueta
     */
    public void setName(String name) {
        this.name = name;
    }
}