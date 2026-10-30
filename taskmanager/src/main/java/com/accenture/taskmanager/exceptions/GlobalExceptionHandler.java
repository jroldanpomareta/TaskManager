package com.accenture.taskmanager.exceptions;

import org.springframework.security.access.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

import javax.naming.AuthenticationException;

import org.springframework.data.core.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import jakarta.servlet.http.HttpServletRequest;
import tools.jackson.databind.exc.InvalidFormatException;

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
    //actualizar javadoc
    /**
     * Handles TaskNotFoundException and returns a standardized error response.
     * This method captures the TaskNotFoundException, constructs an ErrorResponse
     * with the current timestamp, HTTP status 404, error description, exception message,
     * and the requested URI.
     *
     * @param e the TaskNotFoundException thrown when a task is not found
     * @param request the HttpServletRequest containing the details of the request
     * @return ResponseEntity with an ErrorResponse and HTTP status 404
     */
    // Adaptar el manejador de TaskNotFoundException para devolver ErrorResponse con la fecha y hora actual, el código HTTP 404, la descripción del error, el mensaje de la excepción y la ruta real de la petición obtenida mediante HttpServletRequest. Mantener el Javadoc. — generado por IA
    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTaskNotFound(TaskNotFoundException e, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
            LocalDateTime.now().toString(),
            HttpStatus.NOT_FOUND.value(),
            HttpStatus.NOT_FOUND.getReasonPhrase(),
            e.getMessage(),
            request.getRequestURI()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    // Crear un manejador para NoSuchElementException que devuelva HTTP 404 y un ErrorResponse con timestamp, código HTTP, descripción, mensaje de la excepción y ruta solicitada. Añadir Javadoc. — generado por IA
    /**
     * Handles NoSuchElementException and returns a standardized error response.
     * This method captures the NoSuchElementException, constructs an ErrorResponse
     * with the current timestamp, HTTP status 404, error description, exception message,
     * and the requested URI.
     *
     * @param e the NoSuchElementException thrown when an element is not found
     * @param request the HttpServletRequest containing the details of the request
     * @return ResponseEntity with an ErrorResponse and HTTP status 404
     */
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> handleNoSuchElementException(NoSuchElementException e, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
            LocalDateTime.now().toString(),
            HttpStatus.NOT_FOUND.value(),
            HttpStatus.NOT_FOUND.getReasonPhrase(),
            e.getMessage(),
            request.getRequestURI()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles MethodArgumentNotValidException and returns a standardized error response.
     * This method captures the MethodArgumentNotValidException, extracts all validation error messages,
     * constructs an ErrorResponse with the current timestamp, HTTP status 400, error description,
     * concatenated validation messages, and the requested URI.
     *
     * @param e the MethodArgumentNotValidException thrown when validation fails
     * @param request the HttpServletRequest containing the details of the request
     * @return ResponseEntity with an ErrorResponse and HTTP status 400
     */
    // Crear un manejador para MethodArgumentNotValidException que devuelva HTTP 400 y un ErrorResponse. Extraer todos los mensajes de error de validación, incluidos los errores de campo y los errores globales del DTO, y construir un mensaje en español. Incluir timestamp, código HTTP, descripción, mensaje y ruta solicitada. Añadir Javadoc. — generado por IA
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        StringBuilder message = new StringBuilder();
        for (ObjectError error : e.getBindingResult().getAllErrors()) {
            message.append(error.getDefaultMessage()).append(" ");
        }
        ErrorResponse errorResponse = new ErrorResponse(
            LocalDateTime.now().toString(),
            HttpStatus.BAD_REQUEST.value(),
            HttpStatus.BAD_REQUEST.getReasonPhrase(),
            message.toString().trim(),
            request.getRequestURI()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Manejador para excepciones de tipo HttpMessageNotReadableException.
     * Devuelve una respuesta HTTP 400 con un ErrorResponse que contiene
     * el timestamp, código HTTP, descripción del error, mensaje en español
     * y la ruta solicitada.
     *
     * @param e La excepción de tipo HttpMessageNotReadableException.
     * @param request La solicitud HTTP.
     * @return Una ResponseEntity con el ErrorResponse y el código HTTP 400.
     */
    // Crear un manejador para HttpMessageNotReadableException que devuelva HTTP 400 y un ErrorResponse con timestamp, código HTTP, descripción del error, mensaje en español y ruta solicitada. Si la excepción contiene una InvalidFormatException de tools.jackson.databind.exc, identificar el campo que contiene el valor incorrecto y devolver un mensaje indicando qué campo tiene un valor no válido. Para el resto de errores de lectura, devolver un mensaje genérico en español. Utilizar LocalDateTime para la fecha y hora, obtener la ruta mediante HttpServletRequest y mantener el formato ErrorResponse. Añadir Javadoc al método. — generado por IA
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException e, HttpServletRequest request) {

        String message = "El cuerpo de la solicitud no se puede leer. Revise el formato del JSON.";
        Throwable cause = e;

        while (cause != null) {
            if (cause instanceof InvalidFormatException invalidFormat) {
                if (!invalidFormat.getPath().isEmpty()) {
                    String fieldName = invalidFormat.getPath()
                            .get(invalidFormat.getPath().size() - 1)
                            .getPropertyName();
                    message = "El campo '" + fieldName + "' contiene un valor no válido.";
                }
                break;
            }
            cause = cause.getCause();
        }

        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now().toString(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                message,
                request.getRequestURI()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // Crear un manejador para IllegalArgumentException que devuelva HTTP 400 y un ErrorResponse con timestamp, código HTTP, descripción, mensaje de la excepción en español y ruta solicitada. Añadir Javadoc completo. — generado por IA
    /**
     * Manejador para excepciones de tipo IllegalArgumentException.
     * Devuelve una respuesta HTTP 400 con un ErrorResponse que contiene
     * el timestamp, código HTTP, descripción del error, mensaje en español
     * y la ruta solicitada.
     *
     * @param e La excepción de tipo IllegalArgumentException.
     * @param request La solicitud HTTP.
     * @return Una ResponseEntity con el ErrorResponse y el código HTTP 400.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
            IllegalArgumentException e, HttpServletRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now().toString(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                e.getMessage(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // Crear un manejador para MethodArgumentTypeMismatchException que devuelva HTTP 400 y un ErrorResponse con timestamp, código HTTP, descripción del error y un mensaje en español que identifique el parámetro y el valor incorrectos, además de la ruta solicitada. Añadir Javadoc completo. — generado por IA
    /**
     * Manejador para excepciones de tipo MethodArgumentTypeMismatchException.
     * Devuelve una respuesta HTTP 400 con un ErrorResponse que contiene
     * el timestamp, código HTTP, descripción del error, mensaje en español
     * y la ruta solicitada.
     *
     * @param e La excepción de tipo MethodArgumentTypeMismatchException.
     * @param request La solicitud HTTP.
     * @return Una ResponseEntity con el ErrorResponse y el código HTTP 400.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException e, HttpServletRequest request) {

        String message = "El parámetro '" + e.getName() + "' tiene un valor no válido: '" + e.getValue() + "'.";

        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now().toString(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                message,
                request.getRequestURI()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // Crear un manejador para MissingServletRequestParameterException que devuelva HTTP 400 y un ErrorResponse con timestamp, código HTTP, descripción del error, un mensaje en español que identifique el parámetro obligatorio ausente y la ruta solicitada. Añadir Javadoc completo. — generado por IA
    /**
     * Manejador para excepciones de tipo MissingServletRequestParameterException.
     * Devuelve una respuesta HTTP 400 con un ErrorResponse que contiene
     * el timestamp, código HTTP, descripción del error, mensaje en español
     * y la ruta solicitada.
     *
     * @param e La excepción de tipo MissingServletRequestParameterException.
     * @param request La solicitud HTTP.
     * @return Una ResponseEntity con el ErrorResponse y el código HTTP 400.
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(
            MissingServletRequestParameterException e, HttpServletRequest request) {

        String message = "El parámetro obligatorio '" + e.getParameterName() + "' está ausente.";

        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now().toString(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                message,
                request.getRequestURI()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // Crear un manejador para PropertyReferenceException que devuelva HTTP 400 y un ErrorResponse con timestamp, código HTTP, descripción del error, un mensaje en español que indique que el campo de ordenación no es válido y la ruta solicitada. Añadir Javadoc completo. — generado por IA
    /**
     * Manejador para excepciones de tipo PropertyReferenceException.
     * Devuelve una respuesta HTTP 400 con un ErrorResponse que contiene
     * el timestamp, código HTTP, descripción del error, mensaje en español
     * y la ruta solicitada.
     *
     * @param e La excepción de tipo PropertyReferenceException.
     * @param request La solicitud HTTP.
     * @return Una ResponseEntity con el ErrorResponse y el código HTTP 400.
     */
    @ExceptionHandler(PropertyReferenceException.class)
    public ResponseEntity<ErrorResponse> handlePropertyReferenceException(
            PropertyReferenceException e, HttpServletRequest request) {

        String message = "El campo de ordenación '" + e.getPropertyName() + "' no es válido.";

        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now().toString(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                message,
                request.getRequestURI()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // Crear un manejador para NoResourceFoundException que devuelva HTTP 404 y un ErrorResponse con timestamp, código HTTP, descripción del error, un mensaje en español indicando que la ruta solicitada no existe y la ruta de la petición. Añadir Javadoc completo. — generado por IA
    /**
     * Manejador para excepciones de tipo NoResourceFoundException.
     * Devuelve una respuesta HTTP 404 con un ErrorResponse que contiene
     * el timestamp, código HTTP, descripción del error, mensaje en español
     * y la ruta solicitada.
     *
     * @param e La excepción de tipo NoResourceFoundException.
     * @param request La solicitud HTTP.
     * @return Una ResponseEntity con el ErrorResponse y el código HTTP 404.
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFoundException(
            NoResourceFoundException e, HttpServletRequest request) {

        String message = "La ruta solicitada no existe.";

        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now().toString(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                message,
                request.getRequestURI()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    // Crear un manejador genérico para Exception que devuelva HTTP 500 y un ErrorResponse con timestamp, código HTTP, descripción del error, un mensaje genérico en español que no exponga detalles internos y la ruta solicitada. Añadir Javadoc completo. — generado por IA
    /**
     * Manejador genérico para excepciones de tipo Exception.
     * Devuelve una respuesta HTTP 500 con un ErrorResponse que contiene
     * el timestamp, código HTTP, descripción del error, mensaje genérico en español
     * y la ruta solicitada.
     *
     * @param e La excepción de tipo Exception.
     * @param request La solicitud HTTP.
     * @return Una ResponseEntity con el ErrorResponse y el código HTTP 500.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(
            Exception e, HttpServletRequest request) {

        String message = "Ocurrió un error interno en el servidor.";

        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now().toString(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                message,
                request.getRequestURI()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //Añade dos métodos al GlobalExceptionHandler existente, sin modificar ni duplicar los manejadores actuales. El primero debe manejar AuthenticationException y devolver HTTP 401 UNAUTHORIZED con un ErrorResponse que incluya timestamp usando LocalDateTime.now().toString(), código HTTP, descripción, mensaje genérico en español "Credenciales inválidas" y la ruta real obtenida de HttpServletRequest. El segundo debe manejar AccessDeniedException y devolver HTTP 403 FORBIDDEN con un ErrorResponse que incluya timestamp, código HTTP, descripción, mensaje en español "No tienes permisos para realizar esta operación" y la ruta real de la petición. Añade los imports necesarios y Javadoc completo para ambos métodos. Conserva el formato ErrorResponse y el resto del código. — generado por IA
    /**
     * Manejador para excepciones de tipo AuthenticationException.
     * Devuelve una respuesta HTTP 401 UNAUTHORIZED con un ErrorResponse que contiene
     * el timestamp, código HTTP, descripción del error, mensaje genérico en español
     * y la ruta solicitada.
     *
     * @param e La excepción de tipo AuthenticationException.
     * @param request La solicitud HTTP.
     * @return Una ResponseEntity con el ErrorResponse y el código HTTP 401.
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(
            AuthenticationException e, HttpServletRequest request) {

        String message = "Credenciales inválidas.";

        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now().toString(),
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                message,
                request.getRequestURI()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Manejador para excepciones de tipo AccessDeniedException.
     * Devuelve una respuesta HTTP 403 FORBIDDEN con un ErrorResponse que contiene
     * el timestamp, código HTTP, descripción del error, mensaje genérico en español
     * y la ruta solicitada.
     *
     * @param e La excepción de tipo AccessDeniedException.
     * @param request La solicitud HTTP.
     * @return Una ResponseEntity con el ErrorResponse y el código HTTP 403.
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(
            AccessDeniedException e, HttpServletRequest request) {

        String message = "No tienes permisos para realizar esta operación.";

        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now().toString(),
                HttpStatus.FORBIDDEN.value(),
                HttpStatus.FORBIDDEN.getReasonPhrase(),
                message,
                request.getRequestURI()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

}