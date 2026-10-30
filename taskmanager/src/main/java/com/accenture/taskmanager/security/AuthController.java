package com.accenture.taskmanager.security;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.accenture.taskmanager.dto.LoginRequestDTO;
import com.accenture.taskmanager.dto.LoginResponseDTO;
import com.accenture.taskmanager.exceptions.ErrorResponse;

import org.springframework.http.HttpStatus;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import org.springframework.security.crypto.password.PasswordEncoder;
import com.accenture.taskmanager.enums.Role;
import com.accenture.taskmanager.model.AppUser;
import com.accenture.taskmanager.repository.AppUserRepository;

import org.springframework.web.bind.annotation.RequestBody;

//Implementa AuthController con @RestController y @RequestMapping("/auth"), incluyendo Javadoc a nivel de clase y de todos sus métodos. Inyecta JwtService mediante constructor. Crea el endpoint POST /auth/login que reciba un LoginRequestDTO con username y password. Comprueba las credenciales hardcodeadas admin y admin123, marcándolas con el comentario // TODO: en producción esto debe venir de base de datos. Si son correctas, genera un JWT mediante jwtService.generateToken(username) y devuelve HTTP 200 con un LoginResponseDTO que contenga el token. Si son incorrectas, devuelve HTTP 401 con un ErrorResponse compatible con la estructura existente del proyecto. No implementes UserDetailsService ni acceso a base de datos de usuarios. Incluye todos los imports necesarios y utiliza los constructores y métodos reales de las clases existentes. — generado por IA
/**
 * Controlador para la autenticación de usuarios.
 * 
 * @author Javier Roldán
 * @version 1.0
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    /**
     * Servicio para la generación y validación de tokens JWT.
     */
    private final JwtService jwtService;

    /**
     * Gestor de autenticación de Spring Security.
     */
    private final AuthenticationManager authenticationManager;

    /**
     * Repositorio para la gestión de usuarios.
     */
    private final AppUserRepository appUserRepository;

    /**
     * Codificador de contraseñas.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructor para inyectar las dependencias necesarias.
     *
     * @param jwtService            servicio de JWT
     * @param authenticationManager gestor de autenticación
     * @param appUserRepository     repositorio de usuarios
     * @param passwordEncoder       codificador de contraseñas
     */
    public AuthController(
            JwtService jwtService,
            AuthenticationManager authenticationManager,
            AppUserRepository appUserRepository,
            PasswordEncoder passwordEncoder) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Sustituye el login con credenciales hardcodeadas por AuthenticationManager y
    // UsernamePasswordAuthenticationToken. Autentica usando username y password del
    // DTO y, si son correctos, genera el JWT con jwtService.generateToken(username)
    // y devuelve HTTP 200 con LoginResponseDTO. Mantén la estructura existente,
    // añade los imports necesarios y Javadoc. — generado por IA
    /**
     * Endpoint para el inicio de sesión de usuarios.
     * Valida las credenciales y genera un token JWT si son correctas.
     *
     * @param loginRequest DTO con el nombre de usuario y la contraseña
     * @return ResponseEntity con el token JWT o un mensaje de error
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest) {
        try {
            AppUser admin = appUserRepository.findByUsername("admin").orElseThrow();

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()));

            String token = jwtService.generateToken(authentication.getName());
            return ResponseEntity.ok(new LoginResponseDTO(token));

        } catch (AuthenticationException e) {
            ErrorResponse error = new ErrorResponse(
                    java.time.Instant.now().toString(),
                    HttpStatus.UNAUTHORIZED.value(),
                    HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                    "Credenciales inválidas",
                    "/auth/login");

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }

    }

    // Implementa POST /auth/register para registrar usuarios nuevos. Comprueba si
    // existe el username con appUserRepository.existsByUsername; si existe,
    // devuelve HTTP 400 con ErrorResponse. Si no existe, crea AppUser con username,
    // contraseña codificada mediante passwordEncoder.encode y Role.USER, guarda el
    // usuario y devuelve HTTP 201 con LoginResponseDTO que contenga el JWT generado
    // por jwtService.generateToken(username). Nunca aceptes el rol desde el DTO ni
    // devuelvas la contraseña. Añade Javadoc, utiliza los constructores existentes
    // y no modifiques el login. — generado por IA
    /**
     * Endpoint para registrar nuevos usuarios.
     * Valida si el nombre de usuario ya existe y, si no, crea un nuevo usuario con
     * rol USER.
     *
     * @param loginRequest DTO con el nombre de usuario y la contraseña
     * @return ResponseEntity con el token JWT o un mensaje de error
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody LoginRequestDTO loginRequest) {
        if (appUserRepository.existsByUsername(loginRequest.getUsername())) {
            ErrorResponse error = new ErrorResponse(
                    java.time.Instant.now().toString(),
                    HttpStatus.BAD_REQUEST.value(),
                    HttpStatus.BAD_REQUEST.getReasonPhrase(),
                    "El nombre de usuario ya existe",
                    "/auth/register");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

        AppUser user = new AppUser(
                loginRequest.getUsername(),
                passwordEncoder.encode(loginRequest.getPassword()),
                Role.USER);

        appUserRepository.save(user);

        String token = jwtService.generateToken(user.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(new LoginResponseDTO(token));
    }

}