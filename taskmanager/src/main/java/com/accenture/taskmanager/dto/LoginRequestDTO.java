package com.accenture.taskmanager.dto;

//Implementa LoginRequestDTO como DTO para recibir las credenciales del login, con los campos privados username y password de tipo String, constructor vacío, constructor con ambos campos, getters y setters. Añade Javadoc a nivel de clase y a todos sus métodos, incluidos constructores, getters y setters. Incluye los imports necesarios si los hubiera. — generado por IA

/**
 * DTO para recibir las credenciales del login.
 * Contiene el nombre de usuario y la contraseña.
 * 
 * @author Javier Roldán
 * @version 1.0
 */
public class LoginRequestDTO {

    private String username;
    private String password;

    /**
     * Constructor vacío.
     */
    public LoginRequestDTO() {
    }

    /**
     * Constructor con ambos campos.
     *
     * @param username nombre de usuario
     * @param password contraseña
     */
    public LoginRequestDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Obtiene el nombre de usuario.
     *
     * @return nombre de usuario
     */
    public String getUsername() {
        return username;
    }

    /**
     * Establece el nombre de usuario.
     *
     * @param username nombre de usuario
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Obtiene la contraseña.
     *
     * @return contraseña
     */
    public String getPassword() {
        return password;
    }

    /**
     * Establece la contraseña.
     *
     * @param password contraseña
     */
    public void setPassword(String password) {
        this.password = password;
    }
}