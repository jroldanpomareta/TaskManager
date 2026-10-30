package com.accenture.taskmanager.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.accenture.taskmanager.model.AppUser;

// Crea la interfaz AppUserRepository en el paquete com.accenture.taskmanager.repository, extendiendo JpaRepository<AppUser, Long> e importando AppUser desde com.accenture.taskmanager.model. Añade el método Optional<AppUser> findByUsername(String username) para buscar un usuario por su nombre de usuario y el método boolean existsByUsername(String username) para comprobar si ya existe. Incluye los imports necesarios y Javadoc para la interfaz y ambos métodos. No añadas implementaciones manuales ni métodos adicionales. — generado por IA
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    /**
     * Busca un usuario por su nombre de usuario.
     *
     * @param username el nombre de usuario del usuario
     * @return el usuario encontrado, o Optional.empty() si no se encuentra
     */
    Optional<AppUser> findByUsername(String username);

    /**
     * Comprueba si ya existe un usuario con el nombre de usuario especificado.
     *
     * @param username el nombre de usuario del usuario
     * @return true si el usuario existe, false en caso contrario
     */
    boolean existsByUsername(String username);
}
