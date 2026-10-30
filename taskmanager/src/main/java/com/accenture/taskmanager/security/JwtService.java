package com.accenture.taskmanager.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

// Implementa JwtService como servicio Spring usando JJWT 0.13.0 y las propiedades jwt.secret y jwt.expiration. Genera los métodos generateToken(String username), extractUsername(String token) e isTokenValid(String token, String username), incluyendo los claims sub, iat y exp. Usa Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)) y la API moderna Jwts.parser().verifyWith(key).build().parseSignedClaims(token). Añade @Service, los imports necesarios y Javadoc a la clase y a todos sus métodos; gestiona tokens inválidos o expirados devolviendo false en isTokenValid. — generado por IA.
/**
 * Servicio para la gestión de tokens JWT.
 * Proporciona métodos para generar, extraer información y validar tokens JWT.
 * 
 * @author Javier Roldán
 * @version 1.0
 */
@Service
public class JwtService {

    /**
     * Clave secreta para firmar los tokens JWT.
     */
    @Value("${jwt.secret}")
    private String secret;

    /**
     * Tiempo de expiración de los tokens JWT en milisegundos.
     */
    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * Obtiene la clave secreta para firmar los tokens JWT.
     *
     * @return La clave secreta.
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
    /**
     * Genera un token JWT para el usuario especificado.
     *
     * @param username El nombre de usuario para el que se genera el token.
     * @return El token JWT generado.
     */
    public String generateToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .subject(username)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Extrae el nombre de usuario de un token JWT.
     *
     * @param token El token JWT del que se extrae el nombre de usuario.
     * @return El nombre de usuario extraído, o null si no se puede extraer.
     */
    public String extractUsername(String token) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return claims.getPayload().getSubject();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Verifica si un token JWT es válido para un usuario específico.
     *
     * @param token El token JWT a verificar.
     * @param username El nombre de usuario contra el que se verifica el token.
     * @return true si el token es válido y corresponde al usuario, false en caso contrario.
     */
    public boolean isTokenValid(String token, String username) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return username.equals(claims.getPayload().getSubject());
        } catch (Exception e) {
            return false;
        }
    }
}