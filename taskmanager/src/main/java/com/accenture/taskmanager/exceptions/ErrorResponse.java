package com.accenture.taskmanager.exceptions;

/**
 * Clase que representa la respuesta de error de la API.
 * Contiene información sobre el error ocurrido, incluyendo la fecha y hora,
 * el código HTTP, la descripción del error, el mensaje en español y la ruta solicitada.
 */
// Crear una clase ErrorResponse para estandarizar las respuestas de error de la API con los campos timestamp (fecha y hora), status (código HTTP), error (descripción HTTP), message (mensaje en español) y path (ruta solicitada). Añadir Javadoc a nivel de clase y a todos sus métodos. — generado por IA
public class ErrorResponse {

    private String timestamp;
    private int status;
    private String error;
    private String message;
    private String path;

    /**
     * Constructor de la clase ErrorResponse.
     *
     * @param timestamp Fecha y hora del error.
     * @param status Código HTTP del error.
     * @param error Descripción del error HTTP.
     * @param message Mensaje de error en español.
     * @param path Ruta solicitada que causó el error.
     */
    public ErrorResponse(String timestamp, int status, String error, String message, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    /**
     * Obtiene la fecha y hora del error.
     *
     * @return Fecha y hora del error.
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     * Establece la fecha y hora del error.
     *
     * @param timestamp Fecha y hora del error.
     */
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Obtiene el código HTTP del error.
     *
     * @return Código HTTP del error.
     */
    public int getStatus() {
        return status;
    }

    /**
     * Establece el código HTTP del error.
     *
     * @param status Código HTTP del error.
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * Obtiene la descripción del error HTTP.
     *
     * @return Descripción del error HTTP.
     */
    public String getError() {
        return error;
    }

    /**
     * Establece la descripción del error HTTP.
     *
     * @param error Descripción del error HTTP.
     */
    public void setError(String error) {
        this.error = error;
    }

    /**
     * Obtiene el mensaje de error en español.
     *
     * @return Mensaje de error en español.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Establece el mensaje de error en español.
     *
     * @param message Mensaje de error en español.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Obtiene la ruta solicitada que causó el error.
     *
     * @return Ruta solicitada que causó el error.
     */
    public String getPath() {
        return path;
    }

    /**
     * Establece la ruta solicitada que causó el error.
     *
     * @param path Ruta solicitada que causó el error.
     */
    public void setPath(String path) {
        this.path = path;
    }
}
