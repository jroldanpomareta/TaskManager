package com.accenture.taskmanager.dto;

/**
 * DTO para la solicitud de creación o actualización de una etiqueta (Tag).
 * Contiene los campos necesarios para representar una etiqueta.
 * 
 * @author Javier Roldán
 * @version 1.0
 */
// Crear TagRequestDTO con el campo name, su getter y setter, y Javadoc a nivel de clase y de todos los métodos. Mantener el estilo de los DTOs existentes del proyecto. — generado por IA
public class TagRequestDTO {

    // Documenta el campo name con @Schema, indicando en español que es el nombre de la etiqueta e incluyendo un ejemplo representativo. Utiliza únicamente los atributos description y example. No modifiques la lógica existente. — generado por IA
    @io.swagger.v3.oas.annotations.media.Schema(description = "Nombre de la etiqueta", example = "Urgente")
    private String name;

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