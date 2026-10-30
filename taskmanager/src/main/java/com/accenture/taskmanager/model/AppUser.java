package com.accenture.taskmanager.model;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.accenture.taskmanager.enums.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// Crea desde cero la entidad JPA AppUser en el paquete com.accenture.taskmanager.model, implementando org.springframework.security.core.userdetails.UserDetails y mapeándola a la tabla app_user mediante @Entity y @Table(name = "app_user"). Incluye los campos id de tipo Long con @Id y @GeneratedValue(strategy = GenerationType.IDENTITY), username de tipo String con @Column(unique = true, nullable = false, length = 50), password de tipo String con @Column(nullable = false, length = 100) y role de tipo Role del paquete com.accenture.taskmanager.enums, almacenado mediante @Enumerated(EnumType.STRING) y obligatorio. Añade un constructor vacío y otro con username, password y role, además de getters y setters para todos los campos. Implementa todos los métodos de UserDetails: getAuthorities() debe devolver Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.name())); getUsername() debe devolver username; getPassword() debe devolver password; isAccountNonExpired(), isAccountNonLocked(), isCredentialsNonExpired() e isEnabled() deben devolver true. Utiliza java.util.Collections, no io.jsonwebtoken.lang.Collections. Añade todos los imports necesarios, Javadoc para la clase, ambos constructores y todos los métodos, y conserva el estilo y la documentación del proyecto. No añadas clases anidadas, no dupliques métodos ni campos y no codifiques la contraseña en esta entidad, ya que se hará con BCrypt en el servicio de autenticación. — generado por IA
/**
 * Entidad JPA que representa un usuario de la aplicación.
 * Implementa la interfaz UserDetails de Spring Security para la autenticación y autorización.
 * Mapeada a la tabla "app_user" en la base de datos.
 * 
 * @author Javier Roldán
 * @version 1.0
 */
@Entity
@Table(name = "app_user")
public class AppUser implements org.springframework.security.core.userdetails.UserDetails {

    /**
     * Serial version UID para la serialización.
     */
    private static final long serialVersionUID = 1L;

    /**
     * El ID del usuario.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * El nombre de usuario del usuario.
     */
    @Column(unique = true, nullable = false, length = 50)
    private String username;

    /**
     * La contraseña del usuario.
     */
    @Column(nullable = false, length = 100)
    private String password;

    /**
     * El rol del usuario.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    /**
     * Constructor vacío requerido por JPA.
     */
    public AppUser() {
        // Constructor vacío
    }

    /**
     * Constructor con parámetros para inicializar un usuario con nombre de usuario, contraseña y rol.
     *
     * @param username el nombre de usuario del usuario
     * @param password la contraseña del usuario
     * @param role     el rol del usuario
     */
    public AppUser(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Getters y Setters

    /**
     * Obtiene el ID del usuario.
     *
     * @return el ID del usuario
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece el ID del usuario.
     *
     * @param id el ID del usuario
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre de usuario del usuario.
     *
     * @return el nombre de usuario del usuario
     */
    public String getUsername() {
        return username;
    }

    /**
     * Establece el nombre de usuario del usuario.
     *
     * @param username el nombre de usuario del usuario
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Obtiene la contraseña del usuario.
     *
     * @return la contraseña del usuario
     */
    public String getPassword() {
        return password;
    }

    /**
     * Establece la contraseña del usuario.
     *
     * @param password la contraseña del usuario
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Obtiene el rol del usuario.
     *
     * @return el rol del usuario
     */
    public Role getRole() {
        return role;
    }

    /**
     * Establece el rol del usuario.
     *
     * @param role el rol del usuario
     */
    public void setRole(Role role) {
        this.role = role;
    }

    /**
     * Obtiene las autoridades del usuario.
     *
     * @return las autoridades del usuario
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    /**
     * Verifica si la cuenta del usuario no ha expirado.
     *
     * @return true si la cuenta no ha expirado, false en caso contrario
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Verifica si la cuenta del usuario no está bloqueada.
     *
     * @return true si la cuenta no está bloqueada, false en caso contrario
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Verifica si las credenciales del usuario no han expirado.
     *
     * @return true si las credenciales no han expirado, false en caso contrario
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Verifica si el usuario está habilitado.
     *
     * @return true si el usuario está habilitado, false en caso contrario
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}