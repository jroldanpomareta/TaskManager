package com.accenture.taskmanager.dto;

//Implementa LoginResponseDTO como DTO para devolver el token JWT tras un login correcto. Incluye un campo privado token de tipo String, un constructor vacío, un constructor que reciba el token, un getter y un setter. Añade Javadoc a nivel de clase y a todos sus métodos, incluidos ambos constructores, getter y setter. Incluye los imports necesarios si los hubiera. — generado por IA

/**
 * DTO para devolver el token JWT tras un login correcto.
 * 
 * @author Javier Roldán
 * @version 1.0
 */
public class LoginResponseDTO {

    private String token;

    /**
     * Constructor vacío.
     */
    public LoginResponseDTO() {
    }

    /**
     * Constructor que recibe el token JWT.
     *
     * @param token token JWT generado tras un login exitoso
     */
    public LoginResponseDTO(String token) {
        this.token = token;
    }

    /**
     * Obtiene el token JWT.
     *
     * @return token JWT
     */
    public String getToken() {
        return token;
    }

    /**
     * Establece el token JWT.
     *
     * @param token token JWT
     */
    public void setToken(String token) {
        this.token = token;
    }
}