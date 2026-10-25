package com.accenture.taskmanager.exceptions;

/**
 * Excepción personalizada para indicar que una categoría no existe.
 * 
 * @author Javier Roldán
 * @version 1.0
 */
// Crear la excepción personalizada CategoryNotFoundException para indicar que una categoría no existe. Debe extender RuntimeException, incluir un constructor que reciba un mensaje y llamar a super(message). Añadir Javadoc siguiendo el estilo de TaskNotFoundException. — generado por IA
public class CategoryNotFoundException extends RuntimeException {

    /**
     * Constructor de la excepción CategoryNotFoundException.
     *
     * @param message el mensaje de error que describe la excepción
     */
    public CategoryNotFoundException(String message) {
        super(message);
    }
}
