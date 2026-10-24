package com.accenture.taskmanager.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//añade la anotación @RestControllerAdvice para que sea un manejador global de excepciones -> generado por IA
/**
 * Global exception handler for the Task Manager application.
 * This class is annotated with @RestControllerAdvice to handle exceptions
 * globally across all controllers.
 * It captures specific exceptions and returns appropriate HTTP responses.
 * 
 * @author Javier Roldán
 * 
 * @version 1.0
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    // Crear un manejador global de excepciones con @RestControllerAdvice que
    // capture TaskNotFoundException y devuelva HTTP 404 NOT_FOUND. -> generado por
    // IA
    /**
     * Handles TaskNotFoundException and returns a 404 NOT_FOUND response.
     *
     * @param e the TaskNotFoundException thrown when a task is not found
     * @return ResponseEntity with a message indicating the task was not found and
     *         HTTP status 404
     */
    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<String> handleTaskNotFound(TaskNotFoundException e) {
        return new ResponseEntity<>("Task not found", HttpStatus.NOT_FOUND);
    }
}
