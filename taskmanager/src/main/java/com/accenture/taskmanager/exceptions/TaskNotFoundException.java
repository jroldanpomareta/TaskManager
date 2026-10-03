package com.accenture.taskmanager.exceptions;

/**
 * Excepción que indica que una tarea no ha sido encontrada.
 *
 * @author Javier Roldán
 * @version 1.0
 */
// Crear una excepción personalizada TaskNotFoundException para indicar que una
// tarea no existe -> generado con IA
public class TaskNotFoundException extends RuntimeException {

    /**
     * Crea una nueva excepción indicando que una tarea no existe.
     *
     * @param message mensaje descriptivo de la excepción.
     */
    public TaskNotFoundException(String message) {
        super(message);
    }

}