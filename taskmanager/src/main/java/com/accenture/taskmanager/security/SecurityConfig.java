package com.accenture.taskmanager.security;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.accenture.taskmanager.exceptions.ErrorResponse;
import com.accenture.taskmanager.repository.AppUserRepository;
import com.accenture.taskmanager.service.AppUserDetailsService;

import jakarta.servlet.http.HttpServletResponse;
import tools.jackson.databind.ObjectMapper;

//Implementa la estructura inicial de SecurityConfig con Javadoc a nivel de clase, las anotaciones @Configuration y @EnableWebSecurity, y un método con Javadoc que declare un bean SecurityFilterChain. El método debe recibir HttpSecurity como parámetro y devolver la cadena de filtros configurada, sin añadir todavía reglas de autorización. Incluye todos los imports necesarios. — generado por IA
/**
 * Configuración de seguridad para la aplicación.
 * Define las reglas de autorización y autenticación mediante Spring Security.
 * 
 * @author Javier Roldán
 * @version 1.0
 */
@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configura la cadena de filtros de seguridad para la aplicación.
     * 
     * @param http objeto HttpSecurity para configurar
     * @return SecurityFilterChain cadena de filtros configurada
     * @throws Exception si ocurre un error durante la configuración
     */
    // Modifica el método securityFilterChain para configurar las reglas de
    // autorización mediante authorizeHttpRequests con lambdas. Permite el acceso
    // público a POST /auth/login, GET /swagger-ui.html, GET /swagger-ui/** y GET
    // /v3/api-docs/** usando requestMatchers y permitAll(). Exige autenticación
    // para el resto de las rutas con anyRequest().authenticated(). Mantén los
    // imports necesarios, el Javadoc de todos los métodos y el comentario original
    // de autocompletado. Añade al final de este comentario: — generado por IA
    // Modifica el método securityFilterChain existente para configurar
    // SessionCreationPolicy.STATELESS mediante sessionManagement y desactivar CSRF
    // mediante csrf(csrf -> csrf.disable()). Mantén todas las reglas de
    // autorización ya configuradas, no crees otro bean ni otro método, y conserva
    // los Javadocs y comentarios de autocompletado anteriores. Incluye los imports
    // necesarios. — generado por IA
    // Modifica SecurityConfig para inyectar JwtAuthenticationFilter mediante un
    // constructor que reciba JwtAuthenticationFilter y lo asigne a un atributo
    // final de la clase. Elimina la creación manual de JwtAuthenticationFilter
    // dentro de securityFilterChain y utiliza el atributo inyectado en
    // addFilterBefore(jwtAuthenticationFilter,
    // UsernamePasswordAuthenticationFilter.class). Mantén todas las reglas de
    // autorización, la configuración STATELESS, la desactivación de CSRF y los
    // comentarios de autocompletado existentes. Añade los imports necesarios. —
    // generado por IA
    // Modifica SecurityConfig para configurar en el método securityFilterChain un
    // AuthenticationEntryPoint propio mediante exceptionHandling(). El entry point
    // debe responder con HTTP 401 Unauthorized y devolver un ErrorResponse en
    // formato JSON, utilizando tools.jackson.databind.ObjectMapper de Spring Boot 4
    // para escribir la respuesta. El ErrorResponse debe incluir timestamp, status,
    // error, message y path, siguiendo el formato utilizado por el resto de la API.
    // Debe aplicarse a las peticiones sin autenticar, incluidas las que no
    // presentan token o presentan un token inválido o expirado. Mantén la inyección
    // de JwtAuthenticationFilter, su registro antes de
    // UsernamePasswordAuthenticationFilter, todas las reglas de autorización,
    // STATELESS, la desactivación de CSRF y los comentarios existentes. Añade los
    // imports necesarios. — generado por IA

    private final ObjectMapper objectMapper;

    public SecurityConfig(
            ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/login").permitAll()
                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/error").permitAll()
                        .requestMatchers("/auth/register").permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(
                        exception -> exception.authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json");
                            ErrorResponse errorResponse = new ErrorResponse(
                                    java.time.LocalDateTime.now().toString(),
                                    HttpServletResponse.SC_UNAUTHORIZED,
                                    "Unauthorized",
                                    authException.getMessage(),
                                    request.getRequestURI());
                            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
                        }))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(csrf -> csrf.disable());
        return http.build();
    }

    // Añade los beans necesarios para la autenticación con usuarios almacenados en
    // base de datos: un bean PasswordEncoder que devuelva BCryptPasswordEncoder, un
    // bean UserDetailsService que devuelva la instancia de AppUserDetailsService y
    // un bean AuthenticationManager obtenido de AuthenticationConfiguration
    // mediante getAuthenticationManager(). Importa las clases necesarias y conserva
    // intactos el método securityFilterChain, el filtro JWT, las reglas de
    // autorización, el manejo de errores, los Javadocs y los comentarios
    // existentes. No dupliques beans ni métodos. — generado por IA
    // Crea un bean de PasswordEncoder que devuelva una instancia de BCryptPasswordEncoder.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Crea un bean de PasswordEncoder que devuelva una instancia de BCryptPasswordEncoder.
    @Bean
    public UserDetailsService userDetailsService(AppUserRepository appUserRepository) {
        return new AppUserDetailsService(appUserRepository);
    }

    // Crea un bean de UserDetailsService que devuelva una instancia de AppUserDetailsService.
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}