package com.accenture.taskmanager.security;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

//Implementa JwtAuthenticationFilter extendiendo OncePerRequestFilter. Inyecta JwtService y añade Javadoc a la clase y a todos sus métodos. En doFilterInternal, extrae el token de la cabecera Authorization si empieza por Bearer, obtiene el username con JwtService y valida el token. Si es válido y aún no existe autenticación en SecurityContextHolder, crea un UsernamePasswordAuthenticationToken con el username, sin credenciales ni authorities, y establece la autenticación en SecurityContextHolder. Si no hay token o es inválido, continúa la cadena sin autenticar. Incluye todos los imports necesarios. — generado por IA

/**
 * Filtro que extrae y valida el token JWT de cada solicitud HTTP.
 * Si el token es válido, establece la autenticación en el contexto de
 * seguridad.
 * 
 * @author Javier Roldán
 * @version 1.0
 */ 
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // Modifica JwtAuthenticationFilter para inyectar también UserDetailsService
    // mediante el constructor, conservando JwtService. En doFilterInternal, mantén
    // la extracción y validación del JWT, pero carga el usuario con
    // userDetailsService.loadUserByUsername(username) y crea
    // UsernamePasswordAuthenticationToken con el objeto UserDetails, null como
    // credenciales y userDetails.getAuthorities() como authorities. Establece la
    // autenticación en SecurityContextHolder solo si el token es válido y no existe
    // autenticación previa. Elimina el uso de Collections.emptyList() y su import
    // si deja de ser necesario. Conserva el resto del comportamiento, añade los
    // imports y Javadoc necesarios y no dupliques métodos ni constructores. —
    // generado por IA
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    /**
     * Constructor para inyectar JwtService y UserDetailsService.
     *
     * @param jwtService         servicio de JWT
     * @param userDetailsService servicio de detalles de usuario
     */
    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Procesa la solicitud y establece la autenticación cuando el token es válido.
     *
     * @param request     solicitud HTTP
     * @param response    respuesta HTTP
     * @param filterChain cadena de filtros
     * @throws ServletException si ocurre un error de servlet
     * @throws IOException      si ocurre un error de entrada o salida
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String username = jwtService.extractUsername(token);

            if (username != null
                    && !username.isEmpty()
                    && SecurityContextHolder.getContext().getAuthentication() == null
                    && jwtService.isTokenValid(token, username)) {

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }
}