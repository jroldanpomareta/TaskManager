package com.accenture.taskmanager.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.accenture.taskmanager.repository.AppUserRepository;

// Crea la clase AppUserDetailsService en el paquete com.accenture.taskmanager.service que implemente UserDetailsService de Spring Security e inyecte AppUserRepository mediante constructor. Implementa loadUserByUsername(String username) para buscar el usuario con findByUsername(username) y devolver la entidad AppUser encontrada; si no existe, lanza UsernameNotFoundException con un mensaje descriptivo. Añade los imports necesarios, constructor, Javadoc de clase y de todos los métodos, y conserva el estilo del proyecto. No codifiques contraseñas ni añadas lógica de registro o generación de tokens. — generado por IA
public class AppUserDetailsService implements UserDetailsService {
    private final AppUserRepository appUserRepository;

    public AppUserDetailsService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return appUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
    }
}
