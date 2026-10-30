Parte 1<br>
<br>
Paso 1 - Preparación (Ask)<br>
<br>
Preguntas:<br>
<br>
¿Qué es Spring Security y qué problemas de seguridad permite resolver en una aplicación Spring Boot?<br>
<br>
Respuesta:<br>
<br>

<br>
Pregunta:<br>
<br>
¿Cuál es la diferencia entre autenticación y autorización en Spring Security?<br>
<br>
Respuesta:<br>
<br>
Voy a consultar las notas de seguridad para adaptar la explicación al contenido y al nivel del proyecto.<br>
<br>
Spring Security es el framework de Spring encargado de proteger las aplicaciones frente a accesos no autorizados y otros problemas de seguridad.<br>
<br>
En una aplicación Spring Boot permite gestionar principalmente:<br>
<br>
Autenticación: comprobar quién es el usuario mediante usuario y contraseña, JWT, OAuth2, etc.<br>
Autorización: decidir qué recursos puede utilizar cada usuario según sus roles o permisos.<br>
Protección de endpoints y rutas HTTP.<br>
Gestión de sesiones y cierre de sesión.<br>
Cifrado seguro de contraseñas mediante PasswordEncoder.<br>
Protección frente a CSRF.<br>
Control de acceso por roles, por ejemplo ADMIN o USER.<br>
Integración con bases de datos, LDAP, OAuth2 o proveedores externos.<br>
Aplicación de cabeceras de seguridad y políticas HTTP.<br>
Por ejemplo, puede configurarse que cualquiera pueda consultar tareas, pero solo un usuario autenticado pueda crearlas:

```
http.authorizeHttpRequests(auth -> auth
        .requestMatchers(HttpMethod.GET, "/api/v1/tasks/**")
        .permitAll()
        .requestMatchers(HttpMethod.POST, "/api/v1/tasks")
        .authenticated()
        .anyRequest()
        .denyAll()
);
```

Sin Spring Security, los endpoints podrían quedar accesibles sin comprobar la identidad ni los permisos del usuario. Spring Security centraliza estas comprobaciones mediante filtros y reglas de configuración antes de que la petición llegue al controlador.<br>
<br>
En resumen:<br>
<br>
Autenticación: “¿Quién eres?”<br>
Autorización: “¿Qué puedes hacer?”<br>
Spring Security proporciona la infraestructura para responder a ambas preguntas y proteger los recursos de la aplicación.<br>
<br>
Pregunta:<br>
<br>
¿Qué función cumple `SecurityFilterChain` y cómo permite configurar las reglas de acceso a los endpoints?<br>
<br>
Respuesta:<br>
<br>
SecurityFilterChain define la cadena de filtros de Spring Security que procesa cada petición HTTP antes de que llegue al controlador.<br>
<br>
En ella se configuran aspectos como:<br>
<br>
Qué endpoints son públicos.<br>
Qué endpoints requieren autenticación.<br>
Qué roles pueden acceder.<br>
Cómo se autentican los usuarios.<br>
Qué ocurre ante errores 401 o 403.<br>
Protección CSRF, sesiones y logout.<br>
Ejemplo:

```
@Bean
SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(HttpMethod.GET, "/api/v1/tasks/**")
                .permitAll()
            .requestMatchers(HttpMethod.POST, "/api/v1/tasks/**")
                .hasRole("USER")
            .requestMatchers("/admin/**")
                .hasRole("ADMIN")
            .anyRequest()
                .authenticated()
        );

    return http.build();
}
```

Las reglas se evalúan normalmente de arriba abajo. Por eso las reglas específicas deben colocarse antes de:

```
.anyRequest().authenticated()
```

En el ejemplo:<br>
<br>
Las consultas GET de tareas son públicas.<br>
Crear tareas requiere el rol USER.<br>
Las rutas /admin/** requieren el rol ADMIN.<br>
Cualquier otra petición necesita un usuario autenticado.<br>
La cadena de filtros intercepta la petición, comprueba sus credenciales y permisos y, si todo es correcto, permite que continúe hasta el controlador. Si no, devuelve una respuesta como 401 Unauthorized o 403 Forbidden.<br>
<br>
Pregunta:<br>
<br>
¿Cuál es la diferencia entre los códigos HTTP 401 (Unauthorized) y 403 (Forbidden) en el contexto de Spring Security?<br>
<br>
Respuesta:<br>
<br>
En Spring Security, la diferencia es:<br>
<br>
401 Unauthorized: el usuario no está autenticado.<br>
403 Forbidden: el usuario está autenticado, pero no tiene permisos suficientes.<br>
401 Unauthorized<br>
Aparece cuando se intenta acceder a un recurso protegido sin credenciales válidas:

```
GET /api/v1/tasks
```

sin enviar sesión, token o credenciales.<br>
<br>
Significa:<br>
<br>
“No sé quién eres.”<br>
<br>
El cliente debe autenticarse, por ejemplo enviando un token JWT válido.<br>
<br>
403 Forbidden<br>
Aparece cuando el usuario sí está autenticado, pero intenta acceder a un recurso reservado para otro rol:

```
DELETE /api/v1/tasks/1
```

si el usuario tiene ROLE_USER, pero la operación requiere ROLE_ADMIN.<br>
<br>
Significa:<br>
<br>
“Sé quién eres, pero no tienes permiso.”<br>
<br>
Ejemplo

```
http.authorizeHttpRequests(auth -> auth
        .requestMatchers("/admin/**").hasRole("ADMIN")
        .anyRequest().authenticated()
);
```

Petición sin autenticación a /admin/** → 401.<br>
Usuario autenticado sin ROLE_ADMIN → 403.<br>
Usuario autenticado con ROLE_ADMIN → acceso permitido.<br>
Una excepción habitual es la protección CSRF: una petición autenticada que no incluya un token CSRF válido puede recibir 403, porque el usuario está identificado, pero la petición no cumple la protección requerida.<br>

---
<br>
Paso 2 - Añadir dependencia:<br>
<br>
Comentario:

```
<!-- dependencia spring boot starter security ->generado con IA -->
```

Resultado:
```
<!-- dependencia spring boot starter security ->generado con IA -->
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-security</artifactId>
		</dependency>
```

---
<br>
Paso 3 - Diseño previo<br>
<br>
Rutas públicas y protegidas<br>
<br>
| Ruta | Acceso |<br>
|---|---|<br>
| `POST /auth/login` | Público (aquí se obtendrá el token en la Parte 2) |<br>
| `GET /swagger-ui.html` | Público |<br>
| `GET /swagger-ui/**` | Público |<br>
| `GET /v3/api-docs/**` | Público |<br>
| `GET /api/v1/tasks` | Requiere autenticación |<br>
| `POST /api/v1/tasks` | Requiere autenticación |<br>
| Resto de endpoints | Requiere autenticación |<br>
<br>
Comentario:<br>
<br>
//Implementa SecurityConfig.java con @Configuration y @EnableWebSecurity, creando un bean SecurityFilterChain. Configura las rutas públicas con permitAll() y exige autenticación para el resto con authenticated(). Utiliza la configuración moderna de Spring Security mediante lambdas, sin WebSecurityConfigurerAdapter. No añadas usuarios en memoria ni autenticación básica HTTP, no cambies ningún Controller ni endpoint existente. Añade Javadoc a nivel de clase y a todos sus métodos — generado por IA<br>
<br>

---
Paso 4 - Implementación (autocompletado)<br>
<br>
SecurityConfig<br>
<br>
Primer comentario:<br>
<br>
Implementa la estructura inicial de SecurityConfig con Javadoc a nivel de clase, las anotaciones @Configuration y @EnableWebSecurity, y un método con Javadoc que declare un bean SecurityFilterChain. El método debe recibir HttpSecurity como parámetro y devolver la cadena de filtros configurada, sin añadir todavía reglas de autorización. Incluye todos los imports necesarios. — generado por IA<br>
<br>
Resultado:

```
package com.accenture.taskmanager.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

//Implementa la estructura inicial de SecurityConfig con Javadoc a nivel de clase, las anotaciones @Configuration y @EnableWebSecurity, y un método con Javadoc que declare un bean SecurityFilterChain. El método debe recibir HttpSecurity como parámetro y devolver la cadena de filtros configurada, sin añadir todavía reglas de autorización. Incluye todos los imports necesarios. — generado por IA
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configura la cadena de filtros de seguridad.
     *
     * @param http objeto HttpSecurity para configurar
     * @return SecurityFilterChain cadena de filtros configurada
     * @throws Exception si ocurre un error durante la configuración
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Configuración inicial de seguridad (sin reglas específicas)
        return http.build();
    }
}
```

Segundo comentario:<br>
<br>
//Modifica el método securityFilterChain para configurar las reglas de autorización mediante authorizeHttpRequests con lambdas. Permite el acceso público a POST /auth/login, GET /swagger-ui.html, GET /swagger-ui/** y GET /v3/api-docs/** usando requestMatchers y permitAll(). Exige autenticación para el resto de las rutas con anyRequest().authenticated(). Mantén los imports necesarios, el Javadoc de todos los métodos y el comentario original de autocompletado. Añade al final de este comentario: — generado por IA<br>
<br>
Resultado:

```
    /**
     * Configura la cadena de filtros de seguridad para la aplicación.
     * 
     * @param http objeto HttpSecurity para configurar
     * @return SecurityFilterChain cadena de filtros configurada
     * @throws Exception si ocurre un error durante la configuración
     */
    //Modifica el método securityFilterChain para configurar las reglas de autorización mediante authorizeHttpRequests con lambdas. Permite el acceso público a POST /auth/login, GET /swagger-ui.html, GET /swagger-ui/** y GET /v3/api-docs/** usando requestMatchers y permitAll(). Exige autenticación para el resto de las rutas con anyRequest().authenticated(). Mantén los imports necesarios, el Javadoc de todos los métodos y el comentario original de autocompletado. Añade al final de este comentario: — generado por IA
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/login").permitAll()
                .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .anyRequest().authenticated()
            );
        return http.build();
    }
```

Tercer comentario:<br>
<br>
//Modifica el método securityFilterChain existente para configurar SessionCreationPolicy.STATELESS mediante sessionManagement y desactivar CSRF mediante csrf(csrf -> csrf.disable()). Mantén todas las reglas de autorización ya configuradas, no crees otro bean ni otro método, y conserva los Javadocs y comentarios de autocompletado anteriores. Incluye los imports necesarios. — generado por IA<br>
<br>
Resultado:

```
    /**
     * Configura la cadena de filtros de seguridad para la aplicación.
     * 
     * @param http objeto HttpSecurity para configurar
     * @return SecurityFilterChain cadena de filtros configurada
     * @throws Exception si ocurre un error durante la configuración
     */
    //Modifica el método securityFilterChain para configurar las reglas de autorización mediante authorizeHttpRequests con lambdas. Permite el acceso público a POST /auth/login, GET /swagger-ui.html, GET /swagger-ui/** y GET /v3/api-docs/** usando requestMatchers y permitAll(). Exige autenticación para el resto de las rutas con anyRequest().authenticated(). Mantén los imports necesarios, el Javadoc de todos los métodos y el comentario original de autocompletado. Añade al final de este comentario: — generado por IA
    //Modifica el método securityFilterChain existente para configurar SessionCreationPolicy.STATELESS mediante sessionManagement y desactivar CSRF mediante csrf(csrf -> csrf.disable()). Mantén todas las reglas de autorización ya configuradas, no crees otro bean ni otro método, y conserva los Javadocs y comentarios de autocompletado anteriores. Incluye los imports necesarios. — generado por IA
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/login").permitAll()
                .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .csrf(csrf -> csrf.disable());
        return http.build();
    }
```

Paso 5 - Comprensión del código (Ask)<br>
<br>
Pregunta:<br>
<br>
¿Qué hace SessionCreationPolicy.STATELESS y por qué es importante en una API REST?<br>
<br>
Respuesta:<br>
<br>
SessionCreationPolicy.STATELESS indica a Spring Security que la aplicación no debe crear ni utilizar sesiones HTTP para guardar el estado de autenticación.<br>
<br>
En una API REST configurada como stateless:<br>
<br>
El cliente envía sus credenciales o un token —por ejemplo, un JWT— en cada petición:

```
Authorization: Bearer <token>
```

El servidor valida ese token en cada solicitud.<br>
No se guarda la identidad del usuario en una sesión del servidor.<br>
Cada petición es independiente de las anteriores.<br>
Ejemplo habitual:

```
http
    .sessionManagement(session ->
        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    );
```

Es importante en una API REST porque:<br>
<br>
Facilita la escalabilidad horizontal: cualquier instancia del servidor puede atender cualquier petición sin consultar la sesión almacenada en otra instancia.<br>
Reduce el consumo de memoria del servidor.<br>
Evita depender de cookies de sesión y del estado almacenado en el servidor.<br>
Hace que la API sea más sencilla de usar desde aplicaciones móviles, SPAs y otros clientes.<br>
Encaja con la autenticación basada en tokens, especialmente JWT.<br>
La contrapartida es que el servidor no mantiene automáticamente el estado de autenticación ni la revocación inmediata de los tokens. Por eso hay que gestionar correctamente la expiración, los refresh tokens y, si es necesario, una lista de revocación.<br>
<br>
En resumen: STATELESS significa que cada petición debe contener toda la información necesaria para autenticarse y el servidor no guarda la autenticación en una sesión HTTP.<br>
<br>
Pregunta:<br>
<br>
¿Por qué se desactiva CSRF en una API REST stateless?<br>
<br>
Respuesta:<br>
<br>
En una API REST stateless suele desactivarse CSRF porque la autenticación normalmente se realiza mediante un token enviado explícitamente en la cabecera Authorization:

```
Authorization: Bearer <token>
```

El navegador no añade automáticamente esa cabecera a peticiones realizadas desde otra web. Por tanto, un sitio malicioso no puede aprovechar fácilmente las credenciales del usuario para ejecutar una petición autenticada.<br>
<br>
CSRF (Cross-Site Request Forgery) afecta principalmente a aplicaciones que autentican mediante cookies de sesión, porque el navegador sí envía esas cookies automáticamente:

```
Cookie: JSESSIONID=...
```

Una configuración habitual para una API basada en JWT es:

```
http
    .csrf(csrf -> csrf.disable())
    .sessionManagement(session ->
        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    );
```

Sin embargo, STATELESS por sí solo no significa que CSRF sea innecesario. Si la API utiliza cookies para autenticar, o si el token se almacena y se envía mediante cookies, CSRF sigue siendo relevante y debe protegerse, por ejemplo usando tokens CSRF y una política adecuada de cookies.<br>
<br>
Además:<br>
<br>
CORS no sustituye a CSRF; son mecanismos de seguridad distintos.<br>
Desactivar CSRF no protege frente a XSS.<br>
Solo debe desactivarse cuando la API no depende de credenciales que el navegador envíe automáticamente, como las cookies.<br>
En resumen: se desactiva habitualmente porque una API REST con tokens en Authorization ya exige que el cliente incluya explícitamente las credenciales en cada petición, eliminando el escenario típico de un ataque CSRF.<br>
<br>
Pregunta:<br>
<br>
¿Qué hace requestMatchers("/auth/login").permitAll()? ¿Qué pasaría si lo eliminas?<br>
<br>
Respuesta:<br>
<br>
requestMatchers("/auth/login").permitAll() indica que las peticiones dirigidas a /auth/login pueden realizarse sin estar autenticado.<br>
<br>
Por ejemplo:

```
http
    .authorizeHttpRequests(auth -> auth
        .requestMatchers("/auth/login").permitAll()
        .anyRequest().authenticated()
    );
```

Esto es necesario porque el inicio de sesión es precisamente el endpoint que recibe las credenciales —usuario y contraseña— y genera el token o autenticación correspondiente. El cliente todavía no dispone de un token válido cuando realiza esa petición.<br>
<br>
Si se elimina esa regla, /auth/login quedaría sometido a:

```
.anyRequest().authenticated()
```

Por tanto, Spring Security exigiría autenticación previa para acceder al propio endpoint de login. El resultado habitual sería:<br>
<br>
respuesta 401 Unauthorized si no se proporcionan credenciales válidas;<br>
el método del controlador de login no llegaría a ejecutarse;<br>
se produciría una situación circular: para obtener el token habría que autenticarse previamente.<br>
permitAll() no significa que el endpoint quede sin ninguna protección. El endpoint sigue pudiendo validar las credenciales, aplicar límites de intentos, comprobar datos de entrada y devolver errores apropiados. Solo indica que no se requiere una autenticación previa para entrar en él.<br>
<br>
Si se desea limitarlo únicamente al método POST, puede configurarse así:<br>
<br>
Pregunta:<br>
<br>
¿Qué ocurriría si noc onfiguras ningún SecurityFilterChain pero tienes Spring Security en el classpath?<br>
<br>
Respuesta:<br>
<br>
Si tienes Spring Security en el classpath pero no defines ningún SecurityFilterChain, Spring Boot aplica su configuración de seguridad automática.<br>
<br>
Habitualmente ocurriría lo siguiente:<br>
<br>
Todos los endpoints quedan protegidos por defecto.<br>
Se exige autenticación para acceder a ellos.<br>
Se habilita autenticación mediante formulario y/o HTTP Basic.<br>
Spring Boot crea un usuario user automáticamente.<br>
Se genera una contraseña aleatoria y se muestra en los logs al arrancar la aplicación:<br>

```
Using generated security password: ...
```

Para acceder habría que utilizar, por ejemplo:

```
Usuario: user
Contraseña: la generada en los logs
```

Si intentas acceder a un endpoint sin autenticarte, normalmente recibirás:<br>
<br>
una redirección a /login desde un navegador;<br>
una respuesta 401 Unauthorized si utilizas un cliente REST como Postman o curl.<br>
Por tanto, una API que antes permitía acceder libremente a sus endpoints podría empezar a rechazar las peticiones.<br>
<br>
Al definir un SecurityFilterChain, sustituyes esa configuración automática por las reglas que tú indiques:

```
@Bean
SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/auth/login").permitAll()
            .anyRequest().authenticated()
        );

    return http.build();
}
```

En resumen: tener Spring Security como dependencia activa la seguridad automática; no configurar un SecurityFilterChain no deja la aplicación sin seguridad, sino que aplica una configuración predeterminada bastante restrictiva.

---
<br>
Paso 6 - Validación en Postman<br>
<br>
Prueba 1 — GET tareas

![Captura-09-parte-01-Paso-06](..\capturas\Captura-09-parte-01-Paso-06.png)
<br>
Prueba 2 — POST tareas

![Captura-09-parte-01-Paso-06b](..\capturas\Captura-09-parte-01-Paso-06b.png)
<br>
Prueba 3 — Swagger UI

![Captura-09-parte-01-Paso-06c](..\capturas\Captura-09-parte-01-Paso-06c.png)

---
<br>
Reflexión<br>
<br>
Pregunta:<br>
<br>
¿QuétestsdelmódulodetestinghandejadodepasaralañadirSpringSecurityycuálessiguenpasando?¿Porquéunossíyotrosno(piensaenquécargacadatipodetest)?<br>
<br>
Respuesta:<br>
<br>
Los tests de Controller y de integración pueden dejar de pasar porque las peticiones ahora atraviesan los filtros de seguridad y necesitan autenticación. Los tests de Service y Repository deberían seguir pasando porque prueban la lógica de negocio y el acceso a datos sin depender directamente de la cadena de filtros HTTP.<br>
<br>
Pregunta:<br>
<br>
¿Quédiferenciahayentreun401Unauthorizedyun403Forbidden?¿Cuándodevolveríacadauno?<br>
<br>
Respuesta:<br>
<br>
Un 401 indica que la petición no está autenticada correctamente. Un 403 indica que el acceso está prohibido. En este ejercicio, los endpoints protegidos devuelven 403 porque todavía no se ha configurado un mecanismo de autenticación propio que responda con 401.<br>
<br>
Pregunta:<br>
<br>
¿Porquéesimportanteque/auth/loginseaunarutapública?<br>
<br>
Respuesta:<br>
<br>
Porque permite que un usuario envíe sus credenciales y obtenga un token sin estar autenticado previamente. Si el endpoint estuviera protegido, el usuario necesitaría autenticarse para poder iniciar sesión.<br>

---
<br>
Parte 2 - Ejercicio para la Autenticación con JWT<br>
<br>
Paso 1 - Preparación (Ask)<br>
<br>
Pregunta:<br>
<br>
¿Qué es un JWT, qué partes lo componen (header, payload y signature) y qué información debería incluir cada una? ¿El payload está cifrado o solo codificado?<br>
<br>
Respuesta:<br>
<br>
Un JWT (JSON Web Token) es un token compacto utilizado para transmitir información entre partes de forma verificable. En una API REST suele emplearse para identificar y autorizar a un usuario después del login.<br>
<br>
Un JWT normalmente tiene tres partes separadas por puntos:

```
header.payload.signature
```

Por ejemplo:

```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9
.
eyJzdWIiOiIxMjMiLCJleHAiOjE3MDAwMDAwMDB9
.
firma-generada
```

1. Header<br>
Indica cómo se debe interpretar y verificar el token. Suele incluir:

```
{
  "alg": "HS256",
  "typ": "JWT"
}
```

alg: algoritmo utilizado para firmar, como HS256 o RS256.<br>
typ: tipo de token, normalmente JWT.<br>
kid: identificador de la clave, opcional y útil cuando se rotan claves.<br>
2. Payload<br>
Contiene los claims, es decir, los datos del token:

```
{
  "sub": "123",
  "iss": "mi-api",
  "aud": "mi-aplicacion",
  "roles": ["USER"],
  "iat": 1700000000,
  "exp": 1700003600,
  "jti": "abc-123"
}
```

Claims habituales:<br>
<br>
sub: sujeto del token, normalmente el identificador del usuario.<br>
iss: emisor del token.<br>
aud: audiencia prevista.<br>
iat: fecha de emisión.<br>
exp: fecha de expiración.<br>
nbf: fecha a partir de la cual es válido.<br>
jti: identificador único del token.<br>
roles o scope: permisos o roles del usuario.<br>
El payload debería contener solo la información mínima necesaria para identificar y autorizar al usuario. No se deben incluir contraseñas, secretos, datos bancarios ni información sensible.<br>
<br>
3. Signature<br>
La firma permite comprobar que:<br>
<br>
El token fue emitido por quien posee la clave correspondiente.<br>
El header o el payload no han sido modificados.<br>
Conceptualmente, se firma:

```
Base64Url(header) + "." + Base64Url(payload)
```

Con:<br>
<br>
una clave secreta compartida, por ejemplo con HS256; o<br>
una clave privada, cuya firma se verifica con la clave pública, por ejemplo con RS256.<br>
Si alguien modifica el payload, la firma deja de ser válida.<br>
<br>
¿El payload está cifrado?<br>
No. En un JWT firmado convencional —un JWS— el header y el payload están únicamente codificados en Base64URL, no cifrados.<br>
<br>
Por tanto, cualquiera que obtenga el token puede decodificar y leer su contenido. La firma garantiza integridad y autenticidad, pero no confidencialidad.<br>
<br>
Para proteger el contenido mediante cifrado se utilizaría un JWE (JSON Web Encryption), que es un mecanismo diferente.<br>
<br>
En resumen:<br>
<br>
Header: cómo se firma el token.<br>
Payload: qué información contiene.<br>
Signature: demuestra que no ha sido alterado y que procede de un emisor autorizado.<br>
Payload: legible por quien tenga el token; no debe contener secretos.<br>
<br>
Pregunta:<br>
<br>
¿Cómo funciona el flujo completo de autenticación con JWT en una API REST, desde que el usuario envía sus credenciales a /auth/login hasta que accede a un endpoint protegido utilizando la cabecera Authorization: Bearer?<br>
<br>
Respuesta:<br>
<br>
El flujo completo suele ser el siguiente:<br>
<br>
1. El cliente envía sus credenciales<br>
El cliente realiza una petición pública a:

```
POST /auth/login
Content-Type: application/json
```

Con un cuerpo similar a:

```
{
  "username": "javier",
  "password": "secreto"
}
```

El endpoint /auth/login debe estar configurado con permitAll() porque el usuario todavía no tiene un token:

```
.requestMatchers("/auth/login").permitAll()
```

2. La aplicación autentica al usuario<br>
El controlador recibe las credenciales y delega la autenticación en AuthenticationManager:

```
Authentication authentication =
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.username(),
            request.password()
        )
    );
```

Internamente:<br>
<br>
Spring busca al usuario mediante UserDetailsService.<br>
Obtiene sus datos y su contraseña almacenada.<br>
Compara la contraseña recibida con la contraseña cifrada usando, por ejemplo, PasswordEncoder.<br>
Si las credenciales son correctas, devuelve un objeto Authentication autenticado con los roles o permisos del usuario.<br>
Si son incorrectas, lanza una excepción de autenticación y se devuelve normalmente un 401 Unauthorized.<br>
La contraseña no debe guardarse ni incluirse dentro del JWT.<br>
<br>
3. El servidor genera el JWT<br>
Después de autenticar correctamente al usuario, el servidor crea un token firmado:

```
{
  "sub": "javier",
  "roles": ["USER"],
  "iat": 1700000000,
  "exp": 1700003600
}
```

El JWT se firma utilizando una clave secreta o una clave privada. La respuesta puede ser:

```
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

También puede incluir información como el tipo de token y su tiempo de expiración:

```
{
  "token": "eyJ...",
  "tokenType": "Bearer",
  "expiresIn": 3600
}
```

El servidor no necesita crear una sesión HTTP si utiliza:

```
.sessionManagement(session ->
    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
)
```

4. El cliente almacena el token<br>
El cliente conserva el JWT y lo envía en las peticiones posteriores a recursos protegidos.<br>
<br>
Por ejemplo:

```
GET /tasks
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

La palabra Bearer indica que quien posee el token puede presentarlo como prueba de autenticación.<br>
<br>
5. Un filtro intercepta la petición protegida<br>
Antes de llegar al controlador, un filtro de Spring Security examina la cabecera Authorization.<br>
<br>
El filtro:<br>
<br>
Comprueba que la cabecera existe.<br>
Comprueba que empieza por Bearer .<br>
Extrae el JWT.<br>
Valida su firma.<br>
Comprueba que no está expirado.<br>
Valida claims relevantes como iss, aud o nbf, si se utilizan.<br>
Extrae del token el usuario y sus roles.<br>
Carga información adicional del usuario si la aplicación lo requiere.<br>
Crea un objeto Authentication.<br>
Lo coloca en el SecurityContext.<br>
Conceptualmente:

```
SecurityContextHolder.getContext()
    .setAuthentication(authentication);
```

El filtro debe ejecutarse antes de los filtros que necesitan conocer la identidad del usuario, normalmente antes de UsernamePasswordAuthenticationFilter.<br>
<br>
6. Spring comprueba la autorización<br>
Una vez que el usuario está autenticado, Spring comprueba si tiene permiso para acceder al endpoint:

```
.authorizeHttpRequests(auth -> auth
    .requestMatchers("/auth/login").permitAll()
    .requestMatchers("/admin/**").hasRole("ADMIN")
    .anyRequest().authenticated()
)
```

Hay dos conceptos distintos:<br>
<br>
Autenticación: comprobar quién es el usuario.<br>
Autorización: comprobar si puede realizar esa operación.<br>
Por ejemplo, un usuario autenticado con rol USER podría acceder a /tasks, pero recibiría un 403 Forbidden al intentar acceder a /admin/users si no tiene el rol ADMIN.<br>
<br>
7. El controlador procesa la petición<br>
Si el token es válido y el usuario tiene permisos suficientes, la petición llega al controlador:

```
@GetMapping("/tasks")
public List<Task> getTasks(Authentication authentication) {
    String username = authentication.getName();
    // Procesar la petición para ese usuario
}
```

También se puede obtener el usuario mediante @AuthenticationPrincipal.<br>
<br>
Respuestas habituales ante errores

![Captura-09-parte-02-Paso-01](..\capturas\Captura-09-parte-02-Paso-01.png)

Resumen del flujo

```
Cliente
  |
  | POST /auth/login
  | username + password
  v
Servidor
  |
  | Busca usuario y verifica contraseña
  | Genera y firma JWT
  v
Cliente
  |
  | GET /endpoint-protegido
  | Authorization: Bearer <JWT>
  v
Filtro JWT
  |
  | Valida firma, expiración y claims
  | Crea Authentication
  v
Spring Security
  |
  | Comprueba roles y permisos
  v
Controlador protegido
```

La característica esencial es que cada petición protegida lleva su propio token. El servidor no depende de una sesión anterior para saber quién es el usuario, por lo que el flujo es compatible con una API REST stateless.<br>
<br>
Pregunta:<br>
<br>
¿Qué responsabilidades tienen JwtService, JwtAuthenticationFilter y AuthController, y cómo colaboran para generar, validar y utilizar el token?<br>
<br>
Respuesta:<br>
<br>
En este proyecto, las tres clases tienen responsabilidades separadas:<br>
<br>
AuthController.java: recibe las credenciales y genera el token tras un login correcto.<br>
JwtService.java: encapsula la lógica de creación, lectura y validación de JWT.<br>
JwtAuthenticationFilter.java: examina cada petición y establece la autenticación en Spring Security cuando encuentra un token válido.<br>
1. AuthController: inicio de sesión y generación del token<br>
Expone:

```
POST /auth/login
```

Recibe un LoginRequestDTO con:

```
{
  "username": "admin",
  "password": "admin123"
}
```

El controlador:<br>
<br>
Recibe el nombre de usuario y la contraseña.<br>
<br>
Comprueba las credenciales.<br>
<br>
Si son correctas, llama a:

```
jwtService.generateToken(loginRequest.getUsername());
```

Devuelve el JWT dentro de un LoginResponseDTO.<br>
<br>
Si son incorrectas, devuelve 401 Unauthorized.<br>
<br>
En la implementación actual, las credenciales están codificadas directamente:

```
"admin".equals(loginRequest.getUsername())
    && "admin123".equals(loginRequest.getPassword())
```

El comentario TODO indica correctamente que, en producción, deberían verificarse mediante un servicio de usuarios y una contraseña almacenada de forma segura.<br>
<br>
El endpoint puede ejecutarse sin token porque SecurityConfig.java contiene:

```
.requestMatchers("/auth/login").permitAll()
```

2. JwtService: generación y validación del JWT<br>
Este servicio contiene la lógica específica de JWT.<br>
<br>
Generación<br>
generateToken(String username) crea un token con:<br>
<br>
sub: nombre de usuario.<br>
iat: fecha de emisión.<br>
exp: fecha de expiración.<br>
Después firma el token con la clave configurada en jwt.secret:

```
return Jwts.builder()
        .subject(username)
        .issuedAt(now)
        .expiration(expiryDate)
        .signWith(getSigningKey())
        .compact();
```

La respuesta del login contiene ese token, que el cliente debe conservar.<br>
<br>
Extracción del usuario<br>
extractUsername(String token):<br>
<br>
Comprueba la firma usando la clave configurada.<br>
Analiza el JWT.<br>
Extrae el subject, que contiene el nombre de usuario.<br>
Si el token está mal formado, manipulado o no puede validarse, devuelve null.<br>
<br>
Validación<br>
isTokenValid(String token, String username) comprueba que:<br>
<br>
el token tenga una firma válida;<br>
no esté expirado;<br>
el usuario del token coincida con el usuario extraído.<br>
Si cualquiera de esas comprobaciones falla, devuelve false.<br>
<br>
Por tanto, JwtService no decide si una ruta está permitida. Solo genera y comprueba tokens.<br>
<br>
3. JwtAuthenticationFilter: autenticación de cada petición<br>
El filtro extiende OncePerRequestFilter, por lo que se ejecuta una vez por cada petición HTTP.<br>
<br>
Busca la cabecera:

```
Authorization: Bearer <token>
```

Su flujo es:

```
String authHeader = request.getHeader("Authorization");
```

Si la cabecera empieza por Bearer :<br>
<br>
Extrae el token eliminando ese prefijo.<br>
<br>
Obtiene el usuario mediante jwtService.extractUsername(token).<br>
<br>
Comprueba que todavía no haya una autenticación en el SecurityContext.<br>
<br>
Valida el token con jwtService.isTokenValid(...).<br>
<br>
Crea un UsernamePasswordAuthenticationToken.<br>
<br>
Lo guarda en:

```
SecurityContextHolder.getContext()
    .setAuthentication(authentication);
```

A partir de ese momento, Spring Security considera autenticada la petición y puede aplicar las reglas de autorización.<br>
<br>
Si no hay token o el token es inválido, el filtro continúa la cadena sin establecer autenticación:

```
filterChain.doFilter(request, response);
```

Por eso el filtro permite identificar al usuario, pero no proporciona roles como ROLE_ADMIN. Si posteriormente se necesitan reglas como hasRole("ADMIN"), el filtro deberá cargar y asignar las autoridades correspondientes.<br>
<br>
Flujo completo

```
1. Cliente
   POST /auth/login
   username + password
          |
          v
2. AuthController
   Comprueba las credenciales
          |
          v
3. JwtService
   Genera y firma el JWT
          |
          v
4. AuthController
   Devuelve { "token": "..." }
          |
          v
5. Cliente
   GET /tasks
   Authorization: Bearer <token>
          |
          v
6. JwtAuthenticationFilter
   Extrae y valida el token
   Crea Authentication
   Lo guarda en SecurityContext
          |
          v
7. Spring Security
   Comprueba .authenticated()
          |
          v
8. Controlador protegido
   Procesa la petición
```

En resumen:<br>
<br>
Clase	Responsabilidad<br>
AuthController	Recibir credenciales, iniciar sesión y devolver el JWT<br>
JwtService	Generar, firmar, leer y validar el JWT<br>
JwtAuthenticationFilter	Leer el JWT de cada petición y convertirlo en una autenticación de Spring Security<br>
AuthController se utiliza principalmente al iniciar sesión; JwtAuthenticationFilter se utiliza en las peticiones posteriores; y JwtService es el componente común que realiza las operaciones criptográficas y de validación.<br>
<br>
Pregunta:<br>
<br>
¿Cómo se valida la firma y la fecha de expiración de un JWT? ¿Qué riesgos existen si la clave secreta es corta, predecible o se guarda en el repositorio?<br>
<br>
Respuesta:<br>
<br>
La firma y la expiración de un JWT se validan normalmente al analizarlo con la clave secreta —en algoritmos HMAC como HS256— o con la clave pública correspondiente —en algoritmos asimétricos como RS256—.<br>
<br>
En este proyecto, JwtService.java utiliza JJWT:

```
Jws<Claims> claims = Jwts.parser()
        .verifyWith(getSigningKey())
        .build()
        .parseSignedClaims(token);
```

Validación de la firma<br>
Al crear el token, el servidor firma:

```
Base64Url(header) + "." + Base64Url(payload)
```

usando la clave secreta configurada. Al recibirlo, el servidor:<br>
<br>
Lee el header y el payload.<br>
Calcula la firma esperada utilizando la clave configurada.<br>
Compara esa firma con la firma incluida en el JWT.<br>
Rechaza el token si no coinciden.<br>
Si alguien modifica el payload, por ejemplo cambia:

```
{
  "sub": "admin"
}
```

por:

```
{
  "sub": "otro-usuario"
}
```

la firma original deja de ser válida.<br>
<br>
En el proyecto, la clave se obtiene mediante:

```
Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8))
```

Es importante que el algoritmo esperado esté controlado por la aplicación y no se acepte ciegamente cualquier algoritmo indicado por el token.<br>
<br>
Validación de la expiración<br>
El JWT contiene normalmente el claim estándar exp:

```
{
  "sub": "admin",
  "iat": 1700000000,
  "exp": 1700003600
}
```

exp indica el instante límite hasta el que el token es válido. Al ejecutar:

```
.parseSignedClaims(token)
```

JJWT valida los claims temporales, incluida la expiración. Si la fecha actual es posterior a exp, lanza una excepción y el token se considera inválido.<br>
<br>
En este proyecto, isTokenValid captura esa situación y devuelve false:

```
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
```

El filtro JwtAuthenticationFilter.java no establece la autenticación si esa validación falla.<br>
<br>
Además de exp, una validación completa puede comprobar:<br>
<br>
nbf: el token aún no debe utilizarse antes de ese instante.<br>
iat: fecha de emisión razonable.<br>
iss: emisor esperado.<br>
aud: audiencia esperada.<br>
sub: sujeto existente y válido.<br>
jti: identificador del token, si se utiliza revocación.<br>
Riesgos de una clave corta o predecible<br>
Una clave HMAC debe ser suficientemente larga, aleatoria y secreta. Si es corta o predecible, un atacante que obtenga un JWT podría intentar adivinarla mediante fuerza bruta o diccionarios.<br>
<br>
Si consigue la clave, podría:<br>
<br>
crear tokens completamente válidos;<br>
hacerse pasar por cualquier usuario;<br>
modificar el sub, roles u otros claims;<br>
asignarse privilegios de administrador;<br>
prolongar la expiración de un token;<br>
firmar tokens incluso después de que los usuarios legítimos cierren sesión.<br>
La firma solo protege el token mientras la clave permanezca secreta. Un token no se vuelve seguro por estar firmado si la clave utilizada para firmarlo puede adivinarse.<br>
<br>
Debe evitarse usar valores como:

```
secret
password
123456
mi-clave-jwt
```

La clave debería generarse aleatoriamente, tener una longitud adecuada para el algoritmo utilizado y rotarse periódicamente.<br>
<br>
Riesgos de guardar la clave en el repositorio<br>
Si la clave secreta se guarda en un archivo versionado, por ejemplo:

```
jwt.secret=mi-clave-secreta
```

puede quedar expuesta a:<br>
<br>
colaboradores actuales o antiguos;<br>
repositorios públicos;<br>
clones locales;<br>
historial de Git, incluso aunque posteriormente se borre el archivo;<br>
copias de seguridad;<br>
registros de CI/CD;<br>
artefactos de despliegue.<br>
Aunque se elimine la clave del último commit, puede seguir recuperándose desde el historial. Si se sospecha que ha sido expuesta, no basta con ocultarla: hay que revocarla y generar una nueva, además de invalidar o dejar caducar los tokens firmados con ella.<br>
<br>
Una configuración más segura es obtenerla desde una variable de entorno o un gestor de secretos:

```
jwt.secret=${JWT_SECRET}
jwt.expiration=${JWT_EXPIRATION:3600000}
```

En producción, la clave debería gestionarse mediante mecanismos como un gestor de secretos del proveedor cloud, Vault o el sistema seguro de configuración de la infraestructura.<br>
<br>
En resumen:<br>
<br>
La firma se valida recalculándola con la clave esperada.<br>
exp se valida al analizar el JWT y provoca el rechazo de tokens caducados.<br>
Una clave débil permite falsificar tokens.<br>
Una clave expuesta en el repositorio compromete toda la autenticación.<br>
Si la clave se filtra, debe rotarse y deben considerarse comprometidos todos los tokens firmados con ella.<br>

---
<br>
Paso 2 - Añadir dependencia<br>
<br>
Añadir la dependencia de la librería JWT:<br>
<br>
Comentario:

```
<!-- dependencia jwt ->generado con IA -->
```

Resultado:

```
		<!-- dependencia jwt ->generado con IA -->
		<dependency>
			<groupId>io.jsonwebtoken</groupId>
			<artifactId>jjwt-api</artifactId>
			<version>0.13.0</version>
		</dependency>
		<dependency>
			<groupId>io.jsonwebtoken</groupId>
			<artifactId>jjwt-impl</artifactId>
			<version>0.13.0</version>
			<scope>runtime</scope>
		</dependency>
		<dependency>
			<groupId>io.jsonwebtoken</groupId>
			<artifactId>jjwt-jackson</artifactId>
			<version>0.13.0</version>
			<scope>runtime</scope>
		</dependency>
```

añadir clave secreta en application.properties<br>
<br>
# Configuración JWT — generado por IA<br>
jwt.expiration=86400000<br>
jwt.secret=clave-secreta-de-32-caracteres!!<br>
<br>

---
<br>
Paso 3 - Diseño previo<br>
<br>
Pregunta:<br>
<br>
¿Qué responsabilidad tiene JwtService? ¿Qué métodos necesita?<br>
<br>
Respuesta:<br>
<br>
Se encarga de generar y validar tokens JWT. Necesita estos métodos:<br>
- generateToken(): genera un token con el usuario y su fecha de expiración.<br>
- extractUsername(): extrae el nombre de usuario del token.<br>
- isTokenValid(): comprueba que el token es válido y corresponde al usuario indicado.<br>
<br>
Pregunta:<br>
<br>
¿Qué responsabilidad tiene JwtAuthenticationFilter? ¿Cuándo se ejecuta?<br>
<br>
Respuesta:<br>
<br>
Intercepta las peticiones HTTP, extrae el token JWT de la cabecera Authorization, lo valida y establece la autenticación en SecurityContextHolder. Se ejecuta en cada petición que atraviesa la cadena de filtros de Spring Security.<br>
<br>
Pregunta:<br>
<br>
¿Qué debe devolver el endpoint /auth/login si las credenciales son correctas? ¿Y si son incorrectas?<br>
<br>
Respuesta:<br>
<br>
Una respuesta HTTP 200 OK con el token JWT generado en el cuerpo de la respuesta.<br>
<br>
Pregunta:<br>
<br>
¿Qué comentario exacto escribirás para que el autocompletado lo genere?<br>
<br>
Respuesta:<br>
<br>
// Implementa la autenticación JWT: crea JwtService con métodos para generar tokens, extraer el nombre de usuario y validar tokens; crea JwtAuthenticationFilter para extraer, validar y establecer la autenticación; y crea AuthController con POST /auth/login, que compruebe las credenciales y devuelva el JWT o un ErrorResponse con HTTP 401. Añade Javadoc a la clase y a todos sus métodos. — generado por IA<br>
<br>

---
<br>
Paso 4 - Implementación (autocompletado)<br>
<br>
JwtService<br>
<br>
Comentario:<br>
<br>
// Implementa JwtService como servicio Spring usando JJWT 0.13.0 y las propiedades jwt.secret y jwt.expiration. Genera los métodos generateToken(String username), extractUsername(String token) e isTokenValid(String token, String username), incluyendo los claims sub, iat y exp. Usa Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)) y la API moderna Jwts.parser().verifyWith(key).build().parseSignedClaims(token). Añade @Service, los imports necesarios y Javadoc a la clase y a todos sus métodos; gestiona tokens inválidos o expirados devolviendo false en isTokenValid. — generado por IA.<br>
<br>
Resultado:

```
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
```

JwtAuthenticationFilter<br>
<br>
Comentario:<br>
<br>
//Implementa JwtAuthenticationFilter extendiendo OncePerRequestFilter. Inyecta JwtService y añade Javadoc a la clase y a todos sus métodos. En doFilterInternal, extrae el token de la cabecera Authorization si empieza por Bearer, obtiene el username con JwtService y valida el token. Si es válido y aún no existe autenticación en SecurityContextHolder, crea un UsernamePasswordAuthenticationToken con el username, sin credenciales ni authorities, y establece la autenticación en SecurityContextHolder. Si no hay token o es inválido, continúa la cadena sin autenticar. Incluye todos los imports necesarios. — generado por IA<br>
<br>
Resulatdo:

```
package com.accenture.taskmanager.security;

import java.io.IOException;
import java.util.Collections;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

//Implementa JwtAuthenticationFilter extendiendo OncePerRequestFilter. Inyecta JwtService y añade Javadoc a la clase y a todos sus métodos. En doFilterInternal, extrae el token de la cabecera Authorization si empieza por Bearer, obtiene el username con JwtService y valida el token. Si es válido y aún no existe autenticación en SecurityContextHolder, crea un UsernamePasswordAuthenticationToken con el username, sin credenciales ni authorities, y establece la autenticación en SecurityContextHolder. Si no hay token o es inválido, continúa la cadena sin autenticar. Incluye todos los imports necesarios. — generado por IA

/**
 * Filtro que extrae y valida el token JWT de cada solicitud HTTP.
 * Si el token es válido, establece la autenticación en el contexto de seguridad.
 * 
 * @author Javier Roldán
 * @version 1.0
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    /**
     * Crea el filtro e inyecta el servicio JWT.
     *
     * @param jwtService servicio encargado de gestionar los tokens JWT
     */
    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    /**
     * Procesa la solicitud y establece la autenticación cuando el token es válido.
     *
     * @param request solicitud HTTP
     * @param response respuesta HTTP
     * @param filterChain cadena de filtros
     * @throws ServletException si ocurre un error de servlet
     * @throws IOException si ocurre un error de entrada o salida
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

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                username, null, Collections.emptyList());

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }
}
```

AuthController<br>
<br>
Comentario:<br>
<br>
//Implementa AuthController con @RestController y @RequestMapping("/auth"), incluyendo Javadoc a nivel de clase y de todos sus métodos. Inyecta JwtService mediante constructor. Crea el endpoint POST /auth/login que reciba un LoginRequestDTO con username y password. Comprueba las credenciales hardcodeadas admin y admin123, marcándolas con el comentario // TODO: en producción esto debe venir de base de datos. Si son correctas, genera un JWT mediante jwtService.generateToken(username) y devuelve HTTP 200 con un LoginResponseDTO que contenga el token. Si son incorrectas, devuelve HTTP 401 con un ErrorResponse compatible con la estructura existente del proyecto. No implementes UserDetailsService ni acceso a base de datos de usuarios. Incluye todos los imports necesarios y utiliza los constructores y métodos reales de las clases existentes. — generado por IA<br>
<br>
Resultado:

```
package com.accenture.taskmanager.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.accenture.taskmanager.dto.LoginRequestDTO;
import com.accenture.taskmanager.dto.LoginResponseDTO;
import com.accenture.taskmanager.exceptions.ErrorResponse;

import org.springframework.http.HttpStatus;

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
     * Crea el controlador e inyecta el servicio JWT.
     *
     * @param jwtService servicio encargado de gestionar los tokens JWT
     */
    public AuthController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    /**
     * Endpoint para el inicio de sesión de usuarios.
     * Valida las credenciales y genera un token JWT si son correctas.
     *
     * @param loginRequest DTO con el nombre de usuario y la contraseña
     * @return ResponseEntity con el token JWT o un mensaje de error
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest) {
        // TODO: en producción esto debe venir de base de datos
        if ("admin".equals(loginRequest.getUsername()) && "admin123".equals(loginRequest.getPassword())) {
            String token = jwtService.generateToken(loginRequest.getUsername());
            return ResponseEntity.ok(new LoginResponseDTO(token));
        } else {
            ErrorResponse error = new ErrorResponse(
                    java.time.Instant.now().toString(),
                    HttpStatus.UNAUTHORIZED.value(),
                    HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                    "Credenciales inválidas",
                    "/auth/login");

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }
}
```

LoginRequestDTO<br>
<br>
Comentario:<br>
<br>
//Implementa LoginRequestDTO como DTO para recibir las credenciales del login, con los campos privados username y password de tipo String, constructor vacío, constructor con ambos campos, getters y setters. Añade Javadoc a nivel de clase y a todos sus métodos, incluidos constructores, getters y setters. Incluye los imports necesarios si los hubiera. — generado por IA<br>
<br>
Resultado:

```
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
```

LoginResponseDTO<br>
<br>
Comentario:
<br>
//Implementa LoginResponseDTO como DTO para devolver el token JWT tras un login correcto. Incluye un campo privado token de tipo String, un constructor vacío, un constructor que reciba el token, un getter y un setter. Añade Javadoc a nivel de clase y a todos sus métodos, incluidos ambos constructores, getter y setter. Incluye los imports necesarios si los hubiera. — generado por IA<br>
<br>
Resultado:

```
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
```

Integrar el filtro JWT en SecurityConfig<br>
<br>
Comentario:<br>
<br>
//Modifica SecurityConfig para inyectar JwtAuthenticationFilter mediante un constructor que reciba JwtAuthenticationFilter y lo asigne a un atributo final de la clase. Elimina la creación manual de JwtAuthenticationFilter dentro de securityFilterChain y utiliza el atributo inyectado en addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class). Mantén todas las reglas de autorización, la configuración STATELESS, la desactivación de CSRF y los comentarios de autocompletado existentes. Añade los imports necesarios. — generado por IA<br>
<br>
Resultado:

```
package com.accenture.taskmanager.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//Implementa la estructura inicial de SecurityConfig con Javadoc a nivel de clase, las anotaciones @Configuration y @EnableWebSecurity, y un método con Javadoc que declare un bean SecurityFilterChain. El método debe recibir HttpSecurity como parámetro y devolver la cadena de filtros configurada, sin añadir todavía reglas de autorización. Incluye todos los imports necesarios. — generado por IA
/**
 * Configuración de seguridad para la aplicación.
 * Define las reglas de autorización y autenticación mediante Spring Security.
 * 
 * @author Javier Roldán
 * @version 1.0
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configura la cadena de filtros de seguridad para la aplicación.
     * 
     * @param http objeto HttpSecurity para configurar
     * @return SecurityFilterChain cadena de filtros configurada
     * @throws Exception si ocurre un error durante la configuración
     */
    //Modifica el método securityFilterChain para configurar las reglas de autorización mediante authorizeHttpRequests con lambdas. Permite el acceso público a POST /auth/login, GET /swagger-ui.html, GET /swagger-ui/** y GET /v3/api-docs/** usando requestMatchers y permitAll(). Exige autenticación para el resto de las rutas con anyRequest().authenticated(). Mantén los imports necesarios, el Javadoc de todos los métodos y el comentario original de autocompletado. Añade al final de este comentario: — generado por IA
    //Modifica el método securityFilterChain existente para configurar SessionCreationPolicy.STATELESS mediante sessionManagement y desactivar CSRF mediante csrf(csrf -> csrf.disable()). Mantén todas las reglas de autorización ya configuradas, no crees otro bean ni otro método, y conserva los Javadocs y comentarios de autocompletado anteriores. Incluye los imports necesarios. — generado por IA
    //Modifica SecurityConfig para inyectar JwtAuthenticationFilter mediante un constructor que reciba JwtAuthenticationFilter y lo asigne a un atributo final de la clase. Elimina la creación manual de JwtAuthenticationFilter dentro de securityFilterChain y utiliza el atributo inyectado en addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class). Mantén todas las reglas de autorización, la configuración STATELESS, la desactivación de CSRF y los comentarios de autocompletado existentes. Añade los imports necesarios. — generado por IA
    
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/login").permitAll()
                .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .csrf(csrf -> csrf.disable());
        return http.build();
    }
}
```

AuthenticationEntryPoint<br>
<br>
Comentario:<br>
<br>
Modifica SecurityConfig para configurar en el método securityFilterChain un AuthenticationEntryPoint propio mediante exceptionHandling(). El entry point debe responder con HTTP 401 Unauthorized y devolver un ErrorResponse en formato JSON, utilizando tools.jackson.databind.ObjectMapper de Spring Boot 4 para escribir la respuesta. El ErrorResponse debe incluir timestamp, status, error, message y path, siguiendo el formato utilizado por el resto de la API. Debe aplicarse a las peticiones sin autenticar, incluidas las que no presentan token o presentan un token inválido o expirado. Mantén la inyección de JwtAuthenticationFilter, su registro antes de UsernamePasswordAuthenticationFilter, todas las reglas de autorización, STATELESS, la desactivación de CSRF y los comentarios existentes. Añade los imports necesarios. — generado por IA<br>
<br>
Resultado:

```
package com.accenture.taskmanager.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.accenture.taskmanager.exceptions.ErrorResponse;

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

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final ObjectMapper objectMapper;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            ObjectMapper objectMapper) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.objectMapper = objectMapper;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/login").permitAll()
                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/error").permitAll()
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
}
```

---
<br>
Paso 5 - Comprensión del código (Ask)<br>
<br>
Pregunta:<br>
<br>
¿Qué hace OncePerRequestFilter y por qué es importante que el filtro se ejecute solo una vez por request?<br>
<br>
OncePerRequestFilter es una clase base de Spring que permite crear un filtro HTTP garantizando que su lógica se ejecute una vez por petición dentro de un mismo ciclo de procesamiento.<br>
<br>
En este proyecto, JwtAuthenticationFilter.java extiende OncePerRequestFilter y sobrescribe:

```
@Override
protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain)
        throws ServletException, IOException {
    // Validación del JWT
}
```

¿Qué hace?<br>
El filtro:<br>
<br>
Se ejecuta cuando llega una petición HTTP.<br>
<br>
Lee la cabecera Authorization.<br>
<br>
Extrae el token después de Bearer .<br>
<br>
Valida el JWT.<br>
<br>
Crea una autenticación si el token es válido.<br>
<br>
La guarda en el SecurityContext.<br>
<br>
Continúa la cadena con:

```
filterChain.doFilter(request, response);
```

OncePerRequestFilter utiliza un atributo interno de la petición para evitar que la misma lógica se vuelva a ejecutar accidentalmente dentro del mismo procesamiento.<br>
<br>
¿Por qué es importante en un filtro JWT?<br>
Validar el token una sola vez evita:<br>
<br>
realizar varias veces el análisis criptográfico del JWT;<br>
crear repetidamente objetos Authentication;<br>
sobrescribir innecesariamente el SecurityContext;<br>
ejecutar varias veces lógica que puede tener efectos secundarios;<br>
consumir recursos adicionales;<br>
generar comportamientos inconsistentes en peticiones con forward, include o ciertos despachos internos.<br>
Por ejemplo, sin esa garantía, un filtro podría procesar varias veces la misma petición y repetir:

```
jwtService.extractUsername(token);
jwtService.isTokenValid(token, username);
SecurityContextHolder.getContext().setAuthentication(authentication);
```

Esto es especialmente relevante porque la validación de la firma puede ser una operación costosa y el contexto de seguridad debería establecerse de forma controlada.<br>
<br>
¿Significa que nunca se ejecuta en ningún otro despacho?<br>
No exactamente. La garantía se refiere al procesamiento de una petición, pero una petición puede tener distintos tipos de dispatch, como:<br>
<br>
REQUEST;<br>
ASYNC;<br>
ERROR;<br>
FORWARD;<br>
INCLUDE.<br>
Por defecto, OncePerRequestFilter evita repeticiones dentro del mismo ciclo, aunque las aplicaciones pueden configurar si el filtro debe participar también en despachos asíncronos o de error sobrescribiendo métodos como:

```
shouldNotFilterAsyncDispatch()
shouldNotFilterErrorDispatch()
```

Para un filtro JWT normal, lo habitual es validar la autenticación durante el despacho principal de la petición y evitar trabajo duplicado en despachos secundarios.<br>
<br>
En resumen, OncePerRequestFilter proporciona una base segura y controlada para filtros como JwtAuthenticationFilter: lee y valida el token una sola vez por petición, establece la autenticación y permite que el resto de Spring Security continúe el procesamiento.<br>
<br>
Respuesta:<br>
<br>
Pregunta:<br>
<br>
¿Qué hace SecurityContextHolder.getContext().setAuthentication() y por qué es necesario?<br>
<br>
SecurityContextHolder.getContext().setAuthentication(authentication) guarda el objeto de autenticación de la petición actual en el contexto de seguridad de Spring Security.<br>
<br>
En el filtro JWT del proyecto:

```
UsernamePasswordAuthenticationToken authentication =
        new UsernamePasswordAuthenticationToken(
                username, null, Collections.emptyList());

SecurityContextHolder.getContext().setAuthentication(authentication);
```

¿Qué contiene authentication?<br>
Representa la identidad ya verificada del usuario e incluye normalmente:<br>
<br>
principal: quién es el usuario, en este caso el username;<br>
credentials: credenciales, que en este caso son null porque no deben conservarse;<br>
authorities: roles o permisos del usuario;<br>
authenticated: indica que la autenticación se considera válida.<br>
¿Por qué es necesario?<br>
Validar el JWT solo demuestra que el token es correcto, pero Spring Security necesita que ese resultado se registre en su contexto. Al ejecutar:

```
SecurityContextHolder.getContext()
        .setAuthentication(authentication);
```

se informa al resto de la cadena de filtros y a la aplicación de que la petición pertenece a ese usuario.<br>
<br>
Así, Spring puede:<br>
<br>
considerar autenticada la petición;<br>
<br>
aplicar reglas como:

```
.anyRequest().authenticated()
```

comprobar roles mediante hasRole(...) o hasAuthority(...);<br>
<br>
permitir que el controlador obtenga el usuario autenticado;<br>
<br>
exponer la identidad mediante Authentication o @AuthenticationPrincipal.<br>
<br>
Por ejemplo:

```
@GetMapping("/tasks")
public List<Task> getTasks(Authentication authentication) {
    String username = authentication.getName();
    // Usuario identificado a partir del JWT
}
```

También se puede consultar:

```
Authentication authentication =
        SecurityContextHolder.getContext().getAuthentication();
```

¿Qué ocurriría si no se ejecuta?<br>
El filtro podría validar correctamente el token, pero Spring Security no sabría que la petición está autenticada. En consecuencia:<br>
<br>
las rutas protegidas con .authenticated() devolverían normalmente 401 Unauthorized;<br>
el controlador no tendría disponible la identidad autenticada;<br>
no se podrían aplicar correctamente las autorizaciones basadas en roles;<br>
getAuthentication() podría devolver null o una autenticación anónima.<br>
El flujo sería incompleto:

```
JWT válido
   |
   X
No se registra en SecurityContext
   |
Petición tratada como no autenticada
```

Relación con SecurityContextHolder<br>
SecurityContextHolder proporciona acceso al contexto de seguridad asociado al procesamiento actual. En el modelo tradicional de Spring Security, ese contexto se mantiene ligado al hilo que procesa la petición y se limpia al finalizarla para no mezclar usuarios entre peticiones.<br>
<br>
En una aplicación stateless, el contexto no se conserva como una sesión entre peticiones. Se reconstruye en cada solicitud a partir del JWT:

```
Petición 1 -> JWT -> SecurityContext de la petición 1
Petición 2 -> JWT -> SecurityContext de la petición 2
```

Por tanto, setAuthentication(...) es el paso que conecta la validación del JWT con el mecanismo de autorización de Spring Security: el filtro valida el token y esta llamada registra oficialmente al usuario autenticado para esa petición.<br>
<br>
Respuesta:<br>
<br>
Pregunta:<br>
<br>
¿Qué ocurre si el token ha expirado? ¿Qué excepción lanza jjwt y dónde deberí acapturarse?<br>
<br>
Cuando el JWT contiene un claim exp anterior al momento actual, JJWT considera que el token ha caducado y rechaza su análisis. La excepción específica es:

```
io.jsonwebtoken.ExpiredJwtException
```

Es una excepción de tipo JwtException.<br>
<br>
En este proyecto, la excepción se produce al ejecutar parseSignedClaims(token) dentro de JwtService.java:

```
Jwts.parser()
    .verifyWith(getSigningKey())
    .build()
    .parseSignedClaims(token);
```

JJWT valida automáticamente el claim exp. Si el token está expirado, no devuelve los Claims como si fueran válidos, sino que lanza ExpiredJwtException.<br>
<br>
¿Dónde debe capturarse?<br>
Debe capturarse en la frontera donde se procesa el JWT: normalmente en JwtService o en JwtAuthenticationFilter.java, no en el controlador de recursos protegido.<br>
<br>
En la implementación actual, JwtService.java captura la excepción dentro de sus métodos:

```
catch (Exception e) {
    return false;
}
```

En extractUsername, devuelve null, y en isTokenValid, devuelve false. Como consecuencia, JwtAuthenticationFilter.java no establece ninguna autenticación en el SecurityContext:

```
if (username != null
        && jwtService.isTokenValid(token, username)) {
    // Establecer autenticación
}
```

Después, Spring Security trata la petición como no autenticada y normalmente responde:

```
401 Unauthorized
```

Captura explícita recomendada<br>
Es preferible capturar primero la excepción específica y no ocultar todos los errores con un catch (Exception) demasiado amplio:

```
try {
    Jws<Claims> claims = Jwts.parser()
            .verifyWith(getSigningKey())
            .build()
            .parseSignedClaims(token);

    return username.equals(claims.getPayload().getSubject());
} catch (ExpiredJwtException ex) {
    return false;
} catch (JwtException ex) {
    return false;
}
```

Los imports serían:

```
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
```

El orden es importante: ExpiredJwtException debe capturarse antes que JwtException, porque es una subclase de esta.<br>
<br>
En una aplicación más elaborada, el filtro podría distinguir los casos y devolver directamente un 401 con un mensaje como Token expirado, aunque muchas APIs prefieren responder de forma genérica para no revelar detalles innecesarios:

```
401 Unauthorized
```

El cliente puede entonces solicitar un nuevo token mediante un mecanismo de refresh token o pedir al usuario que vuelva a iniciar sesión.<br>
<br>
En resumen:

```
JWT expirado
   |
JJWT lanza ExpiredJwtException
   |
JwtService o JwtAuthenticationFilter la captura
   |
No se establece Authentication
   |
Spring Security responde 401 Unauthorized
```

No debe aceptarse un token expirado ni ignorarse la excepción para continuar la petición como si el usuario estuviera autenticado.<br>
<br>
Respuesta:<br>
<br>
Pregunta:<br>
<br>
¿Por qué el filtro JWT debe añadirse antes de UsernamePasswordAuthenticationFilter en la cadena?<br>
<br>
El filtro JWT suele añadirse antes de UsernamePasswordAuthenticationFilter para que pueda autenticar la petición al principio de la cadena, antes de que Spring Security llegue a las comprobaciones de autorización.<br>
<br>
Configuración habitual:

```
@Bean
SecurityFilterChain securityFilterChain(
        HttpSecurity http,
        JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {

    http
        .csrf(csrf -> csrf.disable())
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/auth/login").permitAll()
            .anyRequest().authenticated())
        .addFilterBefore(
            jwtAuthenticationFilter,
            UsernamePasswordAuthenticationFilter.class);

    return http.build();
}
```

Flujo<br>
Llega una petición con:

```
Authorization: Bearer <jwt>
```

JwtAuthenticationFilter extrae y valida el token.<br>
<br>
Si es válido, guarda un Authentication en SecurityContextHolder.<br>
<br>
Los filtros posteriores encuentran al usuario ya autenticado.<br>
<br>
Spring Security puede aplicar correctamente .authenticated(), hasRole(...), etc.<br>
<br>
La petición llega al controlador.

```
Petición
   ↓
JwtAuthenticationFilter
   ↓
UsernamePasswordAuthenticationFilter
   ↓
Filtros de autorización
   ↓
Controlador
```

UsernamePasswordAuthenticationFilter se utiliza principalmente para procesar formularios de login basados en usuario y contraseña. En una API JWT, el login puede estar implementado mediante un controlador propio, por lo que dicho filtro no suele autenticar las peticiones protegidas. Se utiliza como punto de referencia de orden dentro de la cadena.<br>
<br>
Si el filtro JWT se coloca demasiado tarde, la autorización podría evaluarse antes de que el SecurityContext contenga la autenticación. La petición se trataría entonces como anónima y podría terminar en:

```
401 Unauthorized
```

En el proyecto, JwtAuthenticationFilter.java contiene la lógica correcta para leer el token, pero SecurityConfig.java debe registrar explícitamente el filtro en la cadena con addFilterBefore(...). La anotación @Component permite que Spring lo cree como bean, pero no sustituye necesariamente la configuración explícita de su posición en la cadena de Spring Security.<br>
<br>
En resumen, se coloca antes para que la identidad obtenida del JWT esté disponible antes de que Spring decida si la petición puede acceder al recurso protegido.<br>
<br>
Respuesta:<br>
<br>
Pregunta:<br>
<br>
¿Qué problema de seguridad tendría usar una clave secreta corta o predecible para firmar el JWT?<br>
<br>
Respuesta:<br>
<br>
Usar una clave secreta corta o predecible debilita la firma del JWT porque un atacante podría intentar descubrirla mediante fuerza bruta o ataques de diccionario.<br>
<br>
Si consigue la clave, podrá generar firmas válidas y crear tokens falsos, por ejemplo:

```
{
  "sub": "admin",
  "roles": ["ADMIN"],
  "exp": 1999999999
}
```

Con ese token podría:<br>
<br>
suplantar a cualquier usuario;<br>
asignarse roles o privilegios de administrador;<br>
modificar el usuario del claim sub;<br>
ampliar la fecha de expiración;<br>
acceder a recursos protegidos sin conocer ninguna contraseña.<br>
La firma solo garantiza la integridad del JWT mientras la clave sea suficientemente fuerte y permanezca secreta. No basta con que el token tenga una firma: si la clave puede adivinarse, el atacante puede producir sus propias firmas válidas.<br>
<br>
Deben evitarse valores como:

```
secret
password
123456
mi-clave-jwt
```

La clave debe ser:<br>
<br>
generada aleatoriamente;<br>
suficientemente larga para el algoritmo utilizado;<br>
almacenada fuera del código y del repositorio;<br>
gestionada mediante variables de entorno o un gestor de secretos.<br>
Para HMAC, como HS256, se recomienda una clave de al menos 256 bits de entropía. Además, si la clave se filtra, debe sustituirse inmediatamente y deben considerarse comprometidos los tokens firmados con ella.<br>
<br>
En resumen: una clave débil permite falsificar JWT y puede convertir toda la autenticación de la API en insegura.

---
<br>
Paso 6 - Validación en Postman<br>
<br>
Login correcto<br>

![Captura-09-P2-P6-01-Login-correcto](..\capturas\Captura-09-P2-P6-01-Login-correcto.png)
<br>
Login con contraseña incorrecta

![Captura-09-P2-P6-02-Login-incorrecto](..\capturas\Captura-09-P2-P6-02-Login-incorrecto.png)
<br>
GET de tareas sin token

![Captura-09-P2-P6-03-Tareas-sin-token](..\capturas\Captura-09-P2-P6-03-Tareas-sin-token.png)
<br>
Obtener tareas con token válido

![Captura-09-P2-P6-04-Tareas-con-token](..\capturas\Captura-09-P2-P6-04-Tareas-con-token.png)
<br>
Crear una tarea con token válido

![Captura-09-P2-P6-05-Crear-tarea-con-token](..\capturas\Captura-09-P2-P6-05-Crear-tarea-con-token.png)
<br>

Token inválido

![Captura-09-P2-P6-06-Token-invalido](..\capturas\Captura-09-P2-P6-06-Token-invalido.png)
<br>

---
<br>
Paso 7 - Adaptar los test a la seguridad<br>
<br>
MVN TEST fallan:

![Captura-09-test-Fallan](..\capturas\Captura-09-test-Fallan.png)

adaptar TaskControllerTest y TaskServiceImplTest:<br>
<br>
se añade:

```
@WebMvcTest(TaskController.class)
@AutoConfigureMockMvc(addFilters = false)
public class TaskControllerTest {
```

Comentario:<br>
<br>
//Añade un campo JwtService simulado con @MockitoBean en TaskControllerTest para que JwtAuthenticationFilter pueda cargarse en @WebMvcTest. Incluye el import de com.accenture.taskmanager.security.JwtService y no modifiques los tests existentes. — generado por IA<br>
<br>
Resultado:

```
    //Añade un campo JwtService simulado con @MockitoBean en TaskControllerTest para que JwtAuthenticationFilter pueda cargarse en @WebMvcTest. Incluye el import de com.accenture.taskmanager.security.JwtService y no modifiques los tests existentes. — generado por IA
    /**
     * MockitoBean for the JwtService.
     */
    @MockitoBean
    private JwtService jwtService;
```

Comentario:<br>
<br>
//Modifica el método setUp de TaskIntegrationTest para realizar un login real mediante POST /auth/login con las credenciales admin y admin123, extraer el token JWT de la respuesta LoginResponseDTO y configurar el TestRestTemplate para enviar Authorization: Bearer con ese token en las peticiones posteriores. Mantén la limpieza de los repositorios y el encabezado Content-Type existentes. Añade los imports necesarios y no modifiques los demás tests. — generado por IA
@<br>
<br>
Resultado:

```
    @BeforeEach
    public void setUp() {
        // Limpiar la base de datos eliminando todas las tareas y etiquetas
        taskRepository.deleteAll();
        tagRepository.deleteAll();

        // Realizar login para obtener el token JWT
        LoginRequestDTO loginRequest = new LoginRequestDTO("admin", "admin123");
        ResponseEntity<LoginResponseDTO> loginResponse = testRestTemplate.postForEntity(
                "/auth/login",
                loginRequest,
                LoginResponseDTO.class);

        assertEquals(HttpStatus.OK, loginResponse.getStatusCode());
        assertNotNull(loginResponse.getBody());
        String token = loginResponse.getBody().getToken();

        // Configurar TestRestTemplate para enviar el encabezado Authorization con el token JWT
        testRestTemplate.getRestTemplate().getInterceptors().add((request, body, execution) -> {
            request.getHeaders().setBearerAuth(token);
            request.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            return execution.execute(request, body);
        });
    }
```

Comentario:<br>
<br>
// Añade el campo localServerPort anotado con @LocalServerPort para obtener el puerto aleatorio del servidor de pruebas. Incluye el import org.springframework.boot.test.web.server.LocalServerPort. No modifiques ningún otro campo ni método. — generado por IA<br>
<br>
Resultado:

```
// Añade el campo localServerPort anotado con @LocalServerPort para obtener el puerto aleatorio del servidor de pruebas. Incluye el import org.springframework.boot.test.web.server.LocalServerPort. No modifiques ningún otro campo ni método. — generado por IA
        @LocalServerPort
        private int localServerPort;
```

Comentario:<br>
<br>
// Añade un test de integración llamado
        // getTasksWithoutToken_shouldReturnUnauthorized que realice una petición GET a
        // /api/v1/tasks sin enviar el token JWT y compruebe que devuelve HTTP 401
        // Unauthorized y un cuerpo JSON con los campos timestamp, status, error,
        // message y path de ErrorResponse. Como el TestRestTemplate tiene un
        // interceptor global que añade el token, utiliza un RestTemplate independiente
        // sin interceptores para esta petición. Obtén la URL completa usando el puerto
        // aleatorio del servidor con @LocalServerPort. Añade los imports y campos
        // necesarios. No modifiques el setUp ni los demás tests. — generado por IA
        // Corrige únicamente el método getTasksWithoutToken_shouldReturnUnauthorized
        // para que RestTemplate pueda leer la respuesta HTTP 401 sin lanzar
        // HttpClientErrorException. Configura el RestTemplate independiente con un
        // ResponseErrorHandler que no trate las respuestas 4xx como excepciones. Mantén
        // la petición sin Authorization y las comprobaciones del estado 401 y los
        // campos timestamp, status, error, message y path. No modifiques otros métodos.
        // — generado por IA<br>
<br>
Resultado:

```
        // Añade un test de integración llamado
        // getTasksWithoutToken_shouldReturnUnauthorized que realice una petición GET a
        // /api/v1/tasks sin enviar el token JWT y compruebe que devuelve HTTP 401
        // Unauthorized y un cuerpo JSON con los campos timestamp, status, error,
        // message y path de ErrorResponse. Como el TestRestTemplate tiene un
        // interceptor global que añade el token, utiliza un RestTemplate independiente
        // sin interceptores para esta petición. Obtén la URL completa usando el puerto
        // aleatorio del servidor con @LocalServerPort. Añade los imports y campos
        // necesarios. No modifiques el setUp ni los demás tests. — generado por IA
        // Corrige únicamente el método getTasksWithoutToken_shouldReturnUnauthorized
        // para que RestTemplate pueda leer la respuesta HTTP 401 sin lanzar
        // HttpClientErrorException. Configura el RestTemplate independiente con un
        // ResponseErrorHandler que no trate las respuestas 4xx como excepciones. Mantén
        // la petición sin Authorization y las comprobaciones del estado 401 y los
        // campos timestamp, status, error, message y path. No modifiques otros métodos.
        // — generado por IA
        @Test
        public void getTasksWithoutToken_shouldReturnUnauthorized() {
                String url = "http://localhost:" + localServerPort + "/api/v1/tasks";

                ResponseEntity<String> response = testRestTemplate
                                .withBasicAuth("", "")
                                .getForEntity(url, String.class);

                assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
                assertNotNull(response.getBody());

                String responseBody = response.getBody();
                assertTrue(responseBody.contains("\"timestamp\""));
                assertTrue(responseBody.contains("\"status\""));
                assertTrue(responseBody.contains("\"error\""));
                assertTrue(responseBody.contains("\"message\""));
                assertTrue(responseBody.contains("\"path\""));
        }
```

Resultado de los ajustes:

![Captura-09-resultado-Ajustes](..\capturas\Captura-09-resultado-Ajustes.png)

---
<br>
Reflexión<br>
<br>
Pregunta:<br>
<br>
¿Por qué las credenciales hardcodeadas son un problema grave en producción? ¿Qué alternativa usarías?<br>
<br>
Respuesta:<br>
<br>
Las credenciales hardcodeadas en el código fuente pueden quedar expuestas al compartir el repositorio o publicarlo en Git. Además, dificultan su rotación y mantenimiento. En producción utilizaría variables de entorno o un gestor de secretos, evitando guardar contraseñas directamente en el código.<br>
<br>
Pregunta:<br>
<br>
¿Qué información incluirías o no incluirías en el payload del JWT? ¿Por qué no deberías incluir contraseñas o datos sensibles?<br>
<br>
Respuesta:<br>
<br>
Incluiría información como el nombre de usuario (sub), la fecha de emisión (iat), la fecha de expiración (exp) y, si es necesario, los roles del usuario. No incluiría contraseñas ni datos sensibles porque el payload de un JWT firmado no está cifrado por defecto y puede leerse si se obtiene el token.<br>
<br>
Pregunta:<br>
<br>
¿Qué ocurriría si un atacante intercepta un JWT válido? ¿Cómo se puede mitigar ese riesgo?<br>
<br>
Respuesta:<br>
<br>
El atacante podría utilizar el token para suplantar al usuario y acceder a los recursos autorizados hasta que el token expire o sea invalidado. Para reducir el riesgo utilizaría HTTPS, tokens de corta duración, almacenamiento seguro y mecanismos de revocación o renovación controlada cuando sean necesarios.<br>
<br>
Pregunta:<br>
<br>
¿Cómo permitirías que solo un administrador pudiera borrar tareas? ¿Qué tendrías que añadir al usuario, al token y a la configuración (roles, @EnableMethodSecurity, @PreAuthorize)?<br>
<br>
Respuesta:<br>
<br>
Añadiría roles a los usuarios, por ejemplo ROLE_ADMIN y ROLE_USER. Incluiría los roles correspondientes en el JWT y los convertiría en autoridades de Spring Security durante la autenticación. Después habilitaría la seguridad a nivel de método con @EnableMethodSecurity y protegería la operación de borrado con @PreAuthorize("hasRole('ADMIN')"), de modo que solo los administradores pudieran eliminar tareas.<br>

---
<br>
Parte 3 (opcional)<br>
<br>
Ejercicio opcional: usuarios, contraseñas y roles<br>
<br>
Paso 1 - Preparación (Ask)<br>
<br>
Pregunta:<br>
<br>
¿Qué es BCrypt, cómo funciona el hash de contraseñas y por qué debemos guardar el hash en lugar de la contraseña en texto plano? ¿Por qué dos usuarios con la misma contraseña pueden tener hashes diferentes?<br>
<br>
Respuesta:<br>
<br>
BCrypt es un algoritmo diseñado específicamente para almacenar contraseñas de forma segura. No es un cifrado reversible, sino una función de hash de una sola dirección.<br>
<br>
¿Cómo funciona?<br>
Cuando el usuario crea o cambia su contraseña:<br>
<br>
Se genera una sal aleatoria (salt).<br>
BCrypt combina la contraseña con esa sal.<br>
Ejecuta un proceso de cálculo costoso y repetitivo.<br>
Guarda el resultado, que incluye normalmente:<br>
el algoritmo;<br>
el factor de coste;<br>
la sal;<br>
el hash final.<br>
Un hash BCrypt puede tener un formato parecido a:

```
$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy
```

El valor 10 representa el factor de coste utilizado en ese ejemplo.<br>
<br>
Al iniciar sesión, la aplicación no descifra el hash. En su lugar:<br>
<br>
Obtiene la contraseña introducida.<br>
Lee la sal y el coste incluidos en el hash almacenado.<br>
Recalcula el hash con esos mismos parámetros.<br>
Compara el resultado con el hash guardado.<br>
En Spring Security se utiliza normalmente:

```
@Bean
PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
```

Para guardar:

```
String hash = passwordEncoder.encode(password);
```

Para comprobar:

```
boolean correcta =
    passwordEncoder.matches(passwordIntroducida, hashGuardado);
```

¿Por qué guardar el hash y no la contraseña?<br>
Si se almacenan contraseñas en texto plano y la base de datos se filtra, el atacante obtiene inmediatamente todas las contraseñas.<br>
<br>
Con hashes BCrypt:<br>
<br>
la contraseña original no está almacenada;<br>
el hash no se puede revertir directamente;<br>
cada contraseña debe atacarse mediante intentos;<br>
el proceso de prueba es deliberadamente costoso;<br>
se dificulta el uso de tablas precalculadas y ataques masivos.<br>
Aun así, un hash no garantiza seguridad absoluta. Si la contraseña es débil, un atacante puede probar candidatos hasta encontrar una que produzca el mismo hash. Por eso también son importantes las contraseñas robustas, los límites de intentos y la autenticación multifactor.<br>
<br>
¿Por qué dos usuarios con la misma contraseña tienen hashes diferentes?<br>
Porque BCrypt genera una sal aleatoria diferente cada vez que se codifica una contraseña.<br>
<br>
Por ejemplo, aunque ambos usuarios utilicen:

```
MiPassword123
```

podrían obtener:

```
$2a$10$sal-aleatoria-1...
$2a$10$sal-aleatoria-2...
```

Esto es intencionado. La sal evita que:<br>
<br>
dos usuarios con la misma contraseña tengan el mismo hash;<br>
un atacante identifique contraseñas repetidas;<br>
una misma tabla precalculada sirva para atacar todos los hashes;<br>
todas las cuentas con una contraseña común queden agrupadas visualmente.<br>
La sal no tiene que mantenerse secreta: BCrypt la almacena dentro del propio resultado para poder verificar posteriormente la contraseña. Lo que debe mantenerse secreto es la contraseña original y cualquier información que permita comprometer el sistema.<br>
<br>
En resumen:<br>
<br>
BCrypt genera hashes lentos y con sal.<br>
El hash se almacena, nunca la contraseña en texto plano.<br>
La contraseña se verifica recalculando el hash, no descifrándolo.<br>
Dos hashes distintos pueden corresponder a la misma contraseña porque cada codificación utiliza una sal aleatoria diferente.<br>
<br>
Pregunta:<br>
<br>
¿Qué funciones cumplen UserDetails y UserDetailsService en Spring Security, cómo se relacionan durante el login y cómo se utiliza la información del usuario almacenada en la base de datos para autenticarlo?<br>
<br>
Respuesta:<br>
<br>
En Spring Security, UserDetails y UserDetailsService colaboran para obtener la información necesaria para autenticar a un usuario almacenado en la base de datos.<br>
<br>
UserDetails<br>
UserDetails es una interfaz que representa al usuario desde el punto de vista de Spring Security. Contiene principalmente:<br>
<br>
nombre de usuario;<br>
contraseña almacenada, normalmente como hash BCrypt;<br>
roles o autoridades;<br>
estado de la cuenta:<br>
habilitada;<br>
bloqueada;<br>
expirada;<br>
credenciales expiradas.<br>
Una entidad propia de la aplicación, por ejemplo User, no tiene por qué implementar directamente UserDetails. Lo habitual es convertirla en un objeto UserDetails mediante un adaptador:<br>

```
return org.springframework.security.core.userdetails.User
        .withUsername(user.getUsername())
        .password(user.getPasswordHash())
        .authorities(user.getRoles())
        .build();
```

La contraseña devuelta debe ser el hash almacenado, nunca la contraseña original.<br>
<br>
UserDetailsService<br>
UserDetailsService es una interfaz con una operación principal:

```
UserDetails loadUserByUsername(String username)
        throws UsernameNotFoundException;
```

Su responsabilidad es buscar el usuario en la fuente de datos —normalmente una base de datos— y devolverlo en formato UserDetails.<br>
<br>
Ejemplo conceptual:

```
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                    new UsernameNotFoundException("Usuario no encontrado"));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPasswordHash())
                .authorities(user.getRoles())
                .build();
    }
}
```

Si el usuario no existe, debe lanzarse UsernameNotFoundException, no devolverse un usuario vacío o inventado.<br>
<br>
Relación durante el login<br>
El flujo habitual es:

```
Credenciales recibidas
        |
        v
AuthenticationManager
        |
        v
DaoAuthenticationProvider
        |
        v
UserDetailsService.loadUserByUsername(...)
        |
        v
Usuario y hash recuperados de la base de datos
        |
        v
PasswordEncoder.matches(...)
        |
        v
Autenticación aceptada o rechazada
```

Por ejemplo, el cliente envía:

```
{
  "username": "javier",
  "password": "mi-contraseña"
}
```

Spring Security realiza estos pasos:<br>
<br>
Recibe el nombre de usuario y la contraseña proporcionada.<br>
Invoca UserDetailsService.<br>
Busca el usuario en la base de datos.<br>
Obtiene su contraseña almacenada como hash BCrypt.<br>
Usa PasswordEncoder.matches(...) para comparar la contraseña recibida con el hash.<br>
Comprueba que la cuenta esté activa y no bloqueada.<br>
Si todo es correcto, crea un objeto Authentication autenticado.<br>
Sus roles o autoridades quedan disponibles para las reglas de autorización.<br>
En una aplicación JWT, el servidor puede generar entonces un token para el usuario.<br>
La contraseña recibida no se compara directamente con el texto guardado ni se descifra el hash:

```
passwordEncoder.matches(
    contraseñaRecibida,
    hashAlmacenado
);
```

Uso de los roles y autoridades<br>
Las autoridades que proporciona UserDetails se utilizan posteriormente en reglas como:

```
.authorizeHttpRequests(auth -> auth
    .requestMatchers("/admin/**").hasRole("ADMIN")
    .requestMatchers("/tasks/**").hasAnyRole("USER", "ADMIN")
    .anyRequest().authenticated()
)
```

También pueden consultarse desde un controlador:

```
@GetMapping("/profile")
public String profile(Authentication authentication) {
    return authentication.getName();
}
```

Relación con JWT<br>
En un flujo JWT, UserDetailsService suele intervenir durante el login para comprobar las credenciales y crear el token.<br>
<br>
En las peticiones posteriores:

```
Authorization: Bearer <token>
```

el filtro JWT:<br>
<br>
valida la firma y la expiración;<br>
extrae el nombre de usuario o el identificador del token;<br>
crea una autenticación;<br>
la guarda en SecurityContextHolder.<br>
Dependiendo del diseño, el filtro puede:<br>
<br>
confiar en los roles incluidos en el token, si están correctamente protegidos y validados; o<br>
volver a cargar el usuario mediante UserDetailsService para obtener sus autoridades actuales y detectar cuentas deshabilitadas o cambios de permisos.<br>
En el proyecto actual, AuthController.java comprueba temporalmente las credenciales de forma hardcodeada y no utiliza todavía UserDetailsService. Además, JwtAuthenticationFilter.java crea la autenticación con Collections.emptyList(), por lo que actualmente no carga roles desde una base de datos.<br>
<br>
Por tanto, el flujo real del proyecto es simplificado:

```
AuthController
  |
  | Comprueba admin / admin123
  v
JwtService
  |
  | Genera el JWT
  v
JwtAuthenticationFilter
  |
  | Valida el JWT y crea Authentication sin authorities
```

En una implementación completa, el controlador delegaría la autenticación al AuthenticationManager, este usaría UserDetailsService y PasswordEncoder, y las autoridades del usuario se conservarían al crear la autenticación.<br>
<br>
En resumen:<br>
<br>
UserDetails: representa al usuario autenticable, su hash y sus permisos.<br>
UserDetailsService: busca ese usuario en la base de datos.<br>
PasswordEncoder: compara la contraseña recibida con el hash almacenado.<br>
AuthenticationManager: coordina la autenticación.<br>
JWT: se genera después de una autenticación correcta y se valida en las peticiones posteriores.<br>
<br>
Pregunta:<br>
<br>
¿Cuál es la diferencia entre hasRole("ADMIN") y hasAuthority("ROLE_ADMIN") en Spring Security? ¿Qué prefijo aplica hasRole y qué ocurriría si las autoridades del usuario no incluyeran ROLE_?<br>
<br>
Respuesta:<br>
<br>
15:11
En Spring Security, ambas expresiones comprueban una autoridad, pero se diferencian en cómo tratan el prefijo ROLE_.<br>
<br>
hasRole("ADMIN")<br>
hasRole está pensado para comprobar roles. Spring Security añade automáticamente el prefijo ROLE_ al valor recibido:

```
.hasRole("ADMIN")
```

equivale conceptualmente a:

```
.hasAuthority("ROLE_ADMIN")
```

Por eso, al usar hasRole, normalmente no se debe escribir el prefijo:

```
.hasRole("ROLE_ADMIN") // Incorrecto o redundante
```

Spring Security puede rechazar esa configuración porque hasRole espera un nombre de rol sin el prefijo configurado.<br>
<br>
hasAuthority("ROLE_ADMIN")<br>
hasAuthority compara la autoridad exactamente como se indica, sin añadir ningún prefijo:

```
.hasAuthority("ROLE_ADMIN")
```

Solo concede acceso si el usuario tiene literalmente una autoridad llamada:

```
ROLE_ADMIN
```

También podría utilizarse para permisos que no son roles:

```
.hasAuthority("TASK_READ")
.hasAuthority("TASK_DELETE")
```

Comparación

```
.requestMatchers("/admin/**").hasRole("ADMIN")
```

y:

```
.requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
```

normalmente comprueban lo mismo.<br>
<br>
La diferencia conceptual es:<br>
<br>
Configuración	Autoridad que busca<br>
hasRole("ADMIN")	ROLE_ADMIN<br>
hasAuthority("ROLE_ADMIN")	ROLE_ADMIN<br>
hasAuthority("ADMIN")	ADMIN<br>
¿Qué ocurre si las autoridades no incluyen ROLE_?<br>
Si se configura:

```
.hasRole("ADMIN")
```

pero el usuario solo tiene:

```
ADMIN
```

la comprobación falla, porque Spring busca:

```
ROLE_ADMIN
```

El usuario podría estar autenticado, pero no tendría la autoridad requerida. La respuesta habitual sería:

```
403 Forbidden
```

La diferencia entre los códigos es importante:<br>
<br>
401 Unauthorized: el usuario no está autenticado.<br>
403 Forbidden: el usuario está autenticado, pero no tiene permisos suficientes.<br>
También fallaría esta configuración:

```
.hasAuthority("ROLE_ADMIN")
```

si el usuario solo tiene ADMIN, porque hasAuthority realiza una comparación exacta.<br>
<br>
Si las autoridades de la aplicación se almacenan sin prefijo, habría que elegir una estrategia coherente:

```
.hasAuthority("ADMIN")
```

o configurar adecuadamente el GrantedAuthority y seguir usando:

```
.hasRole("ADMIN")
```

Lo habitual en Spring Security es mantener la convención:

```
ROLE_USER
ROLE_ADMIN
```

y escribir las reglas de roles de esta forma:

```
.hasRole("ADMIN")
.hasAnyRole("USER", "ADMIN")
```

En resumen: hasRole("ADMIN") añade automáticamente ROLE_; hasAuthority("ROLE_ADMIN") no modifica el texto y exige coincidencia exacta. Si las autoridades del usuario no contienen ROLE_, hasRole("ADMIN") no encontrará el rol y denegará el acceso con 403 Forbidden.<br>
<br>

---
<br>
Paso 2 - Tabla de usuarios<br>
<br>
Se añade a init.sql:

```
CREATE TABLE app_user (
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role     VARCHAR(20) NOT NULL
) ENGINE = InnoDB;
```

y se ejecuta:

![Captura-09-ejecucion-SQL](..\capturas\Captura-09-ejecucion-SQL.png)

---
<br>
Paso 3 - Diseño previo<br>
<br>
Pregunta:<br>
<br>
¿Qué rutas serán públicas, cuáles exigirán autenticación y cuáles solo un ADMIN?<br>
<br>
Respuesta:<br>
<br>
- Públicas: /auth/login, /auth/register y las rutas de Swagger.<br>
- Autenticación obligatoria: las operaciones de consulta y creación de tareas.<br>
- Solo ADMIN: DELETE /api/v1/tasks/{id}.<br>
<br>
Pregunta:<br>
<br>
¿Cómo se crea el primer administrador? ¿Por qué no puede hacerlo el endpoint de registro?<br>
<br>
Respuesta:<br>
<br>
El primer administrador se crea mediante un CommandLineRunner al arrancar la aplicación, solo para desarrollo. El registro público siempre debe asignar el rol USER, porque permitir que el usuario elija su propio rol permitiría que cualquiera se registrara como administrador.<br>
<br>
Pregunta:<br>
<br>
¿Qué debe devolver /auth/login si las credenciales son incorrectas, y qué un DELETE de una tarea sin permiso?<br>
<br>
Respuesta:<br>
<br>
- Login incorrecto: HTTP 401 Unauthorized con un ErrorResponse.<br>
- DELETE sin permisos de administrador: HTTP 403 Forbidden con un ErrorResponse.<br>
<br>
Pregunta:<br>
<br>
¿Qué comentario exacto escribirás para que el autocompletado lo genere?<br>
<br>
Respuesta:<br>
<br>
//Implementa la gestión de usuarios y roles de la Parte 3 del ejercicio Spring Boot Seguridad siguiendo el PDF: entidad AppUser con UserDetails, enum Role (USER y ADMIN), contraseñas codificadas con BCrypt, UserDetailsService, AuthenticationManager, registro público que siempre asigne USER, login mediante autenticación de Spring Security, JWT con las autoridades del usuario, restricción del DELETE de tareas a ADMIN con @PreAuthorize("hasRole('ADMIN')"), creación de un administrador de desarrollo mediante CommandLineRunner y respuestas ErrorResponse para errores 401 y 403. Añade Javadoc a la clase y a todos los métodos. No cambies otros endpoints del Task Manager ni aceptes roles en el body del registro. — generado por IA<br>
<br>


---
<br>
Paso 4 - Implemnetación (autocompletado)<br>
<br>
enum Role<br>
<br>
Comentario:<br>
<br>
//Crea el enum Role con los valores USER y ADMIN para representar los roles de usuario de la aplicación. Añade Javadoc a nivel de clase. — generado por IA<br>
<br>
Resultado:

```
package com.accenture.taskmanager.enums;

//Crea el enum Role con los valores USER y ADMIN para representar los roles de usuario de la aplicación. Añade Javadoc a nivel de clase. — generado por IA
/**
 * Enumeración que representa los roles de usuario en la aplicación.
 * Los valores son USER y ADMIN.
 * 
 * @author Javier Roldán
 * @version 1.0
 */
public enum Role {
    USER,
    ADMIN
}

```

entidad AppUser<br>
<br>
Comentario:<br>
<br>
// Crea desde cero la entidad JPA AppUser en el paquete com.accenture.taskmanager.model, implementando org.springframework.security.core.userdetails.UserDetails y mapeándola a la tabla app_user mediante @Entity y @Table(name = "app_user"). Incluye los campos id de tipo Long con @Id y @GeneratedValue(strategy = GenerationType.IDENTITY), username de tipo String con @Column(unique = true, nullable = false, length = 50), password de tipo String con @Column(nullable = false, length = 100) y role de tipo Role del paquete com.accenture.taskmanager.enums, almacenado mediante @Enumerated(EnumType.STRING) y obligatorio. Añade un constructor vacío y otro con username, password y role, además de getters y setters para todos los campos. Implementa todos los métodos de UserDetails: getAuthorities() debe devolver Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.name())); getUsername() debe devolver username; getPassword() debe devolver password; isAccountNonExpired(), isAccountNonLocked(), isCredentialsNonExpired() e isEnabled() deben devolver true. Utiliza java.util.Collections, no io.jsonwebtoken.lang.Collections. Añade todos los imports necesarios, Javadoc para la clase, ambos constructores y todos los métodos, y conserva el estilo y la documentación del proyecto. No añadas clases anidadas, no dupliques métodos ni campos y no codifiques la contraseña en esta entidad, ya que se hará con BCrypt en el servicio de autenticación. — generado por IA<br>
<br>
Resultado:

```
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
```

AppUserRepository<br>
<br>
Comentario:<br>
<br>
// Crea la interfaz AppUserRepository en el paquete com.accenture.taskmanager.repository, extendiendo JpaRepository<AppUser, Long> e importando AppUser desde com.accenture.taskmanager.model. Añade el método Optional<AppUser> findByUsername(String username) para buscar un usuario por su nombre de usuario y el método boolean existsByUsername(String username) para comprobar si ya existe. Incluye los imports necesarios y Javadoc para la interfaz y ambos métodos. No añadas implementaciones manuales ni métodos adicionales. — generado por IA<br>
<br>
Resultado:

```
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
```

AppUserDetailsService<br>
<br>
Comentario:<br>
<br>
// Crea la clase AppUserDetailsService en el paquete com.accenture.taskmanager.service que implemente UserDetailsService de Spring Security e inyecte AppUserRepository mediante constructor. Implementa loadUserByUsername(String username) para buscar el usuario con findByUsername(username) y devolver la entidad AppUser encontrada; si no existe, lanza UsernameNotFoundException con un mensaje descriptivo. Añade los imports necesarios, constructor, Javadoc de clase y de todos los métodos, y conserva el estilo del proyecto. No codifiques contraseñas ni añadas lógica de registro o generación de tokens. — generado por IA<br>
<br>
Resultado:

```
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
```

Declarar los beans<br>
<br>
Comentario:<br>
<br>
// Añade los beans necesarios para la autenticación con usuarios almacenados en base de datos: un bean PasswordEncoder que devuelva BCryptPasswordEncoder, un bean UserDetailsService que devuelva la instancia de AppUserDetailsService y un bean AuthenticationManager obtenido de AuthenticationConfiguration mediante getAuthenticationManager(). Importa las clases necesarias y conserva intactos el método securityFilterChain, el filtro JWT, las reglas de autorización, el manejo de errores, los Javadocs y los comentarios existentes. No dupliques beans ni métodos. — generado por IA<br>
<br>
Resultado:

```
    // Añade los beans necesarios para la autenticación con usuarios almacenados en base de datos: un bean PasswordEncoder que devuelva BCryptPasswordEncoder, un bean UserDetailsService que devuelva la instancia de AppUserDetailsService y un bean AuthenticationManager obtenido de AuthenticationConfiguration mediante getAuthenticationManager(). Importa las clases necesarias y conserva intactos el método securityFilterChain, el filtro JWT, las reglas de autorización, el manejo de errores, los Javadocs y los comentarios existentes. No dupliques beans ni métodos. — generado por IA
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(AppUserRepository appUserRepository) {
        return new AppUserDetailsService(appUserRepository);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
```

Modificar AuthController:<br>
<br>
Comentario:<br>
<br>
// Sustituye el login con credenciales hardcodeadas por AuthenticationManager y UsernamePasswordAuthenticationToken. Autentica usando username y password del DTO y, si son correctos, genera el JWT con jwtService.generateToken(username) y devuelve HTTP 200 con LoginResponseDTO. Mantén la estructura existente, añade los imports necesarios y Javadoc. — generado por IA<br>
<br>
// Implementa POST /auth/register para registrar usuarios nuevos. Comprueba si existe el username con appUserRepository.existsByUsername; si existe, devuelve HTTP 400 con ErrorResponse. Si no existe, crea AppUser con username, contraseña codificada mediante passwordEncoder.encode y Role.USER, guarda el usuario y devuelve HTTP 201 con LoginResponseDTO que contenga el JWT generado por jwtService.generateToken(username). Nunca aceptes el rol desde el DTO ni devuelvas la contraseña. Añade Javadoc, utiliza los constructores existentes y no modifiques el login. — generado por IA<br>
<br>
Resultado:

```
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

    // Implementa POST /auth/register para registrar usuarios nuevos. Comprueba si existe el username con appUserRepository.existsByUsername; si existe, devuelve HTTP 400 con ErrorResponse. Si no existe, crea AppUser con username, contraseña codificada mediante passwordEncoder.encode y Role.USER, guarda el usuario y devuelve HTTP 201 con LoginResponseDTO que contenga el JWT generado por jwtService.generateToken(username). Nunca aceptes el rol desde el DTO ni devuelvas la contraseña. Añade Javadoc, utiliza los constructores existentes y no modifiques el login. — generado por IA
    /**
     * Endpoint para registrar nuevos usuarios.
     * Valida si el nombre de usuario ya existe y, si no, crea un nuevo usuario con rol USER.
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
```

JwtAuthenticationFilter<br>
<br>
Comentario:<br>
<br>
// Modifica JwtAuthenticationFilter para inyectar también UserDetailsService mediante el constructor, conservando JwtService. En doFilterInternal, mantén la extracción y validación del JWT, pero carga el usuario con userDetailsService.loadUserByUsername(username) y crea UsernamePasswordAuthenticationToken con el objeto UserDetails, null como credenciales y userDetails.getAuthorities() como authorities. Establece la autenticación en SecurityContextHolder solo si el token es válido y no existe autenticación previa. Elimina el uso de Collections.emptyList() y su import si deja de ser necesario. Conserva el resto del comportamiento, añade los imports y Javadoc necesarios y no dupliques métodos ni constructores. — generado por IA<br>
<br>
Resultado:

```
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
```

se añade @EnableMethodSecurity y .requestMatchers("/auth/register").permitAll() en SecurityConfig<br>
<br>
Se modifica el delete de TaskController y se pone  @PreAuthorize("hasRole('ADMIN')")<br>
<br>
Resultado:

```
    @Operation(
            summary = "Elimina una tarea por ID",
            description = "Permite eliminar una tarea específica identificada por su ID."
    )
    @ApiResponse(responseCode = "204", description = "Tarea eliminada correctamente", content = @Content)
    @ApiResponse(responseCode = "404", description = "Tarea no encontrada", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
```

Se modifica TaskmanagerApplication para crear un administrador al arrancar con un CommandLineRunner:<br>
<br>
Comentario:<br>
<br>
//Implementa un bean CommandLineRunner que cree un usuario administrador al iniciar la aplicación si no existe. Inyecta AppUserRepository y PasswordEncoder. Crea el usuario con username "admin", contraseña "Admin123!" codificada con passwordEncoder.encode() y rol Role.ADMIN. Guarda el usuario mediante el repositorio. Añade los imports necesarios y Javadoc al método. No modifiques el método main ni dupliques métodos existentes. — generado por IA<br>
<br>
Resultado:

```
	//Implementa un bean CommandLineRunner que cree un usuario administrador al iniciar la aplicación si no existe. Inyecta AppUserRepository y PasswordEncoder. Crea el usuario con username "admin", contraseña "Admin123!" codificada con passwordEncoder.encode() y rol Role.ADMIN. Guarda el usuario mediante el repositorio. Añade los imports necesarios y Javadoc al método. No modifiques el método main ni dupliques métodos existentes. — generado por IA
	/**
	 * Crea un usuario administrador al iniciar la aplicación si no existe.
	 *
	 * @param appUserRepository repositorio de usuarios
	 * @param passwordEncoder   codificador de contraseñas
	 * @return proceso de inicialización del administrador
	 */
	@Bean
	CommandLineRunner createAdmin(
			AppUserRepository appUserRepository,
			PasswordEncoder passwordEncoder) {
		return args -> {
			String username = "admin";

			if (!appUserRepository.existsByUsername(username)) {
				AppUser admin = new AppUser(
						username,
						passwordEncoder.encode("Admin123!"),
						Role.ADMIN);

				appUserRepository.save(admin);
			}
		};
	}
```

Modificación GolbalExceptionHandler:<br>
<br>
Comentario:<br>
<br>
//Añade dos métodos al GlobalExceptionHandler existente, sin modificar ni duplicar los manejadores actuales. El primero debe manejar AuthenticationException y devolver HTTP 401 UNAUTHORIZED con un ErrorResponse que incluya timestamp usando LocalDateTime.now().toString(), código HTTP, descripción, mensaje genérico en español "Credenciales inválidas" y la ruta real obtenida de HttpServletRequest. El segundo debe manejar AccessDeniedException y devolver HTTP 403 FORBIDDEN con un ErrorResponse que incluya timestamp, código HTTP, descripción, mensaje en español "No tienes permisos para realizar esta operación" y la ruta real de la petición. Añade los imports necesarios y Javadoc completo para ambos métodos. Conserva el formato ErrorResponse y el resto del código. — generado por IA<br>
<br>
Resultado:

```
    //Añade dos métodos al GlobalExceptionHandler existente, sin modificar ni duplicar los manejadores actuales. El primero debe manejar AuthenticationException y devolver HTTP 401 UNAUTHORIZED con un ErrorResponse que incluya timestamp usando LocalDateTime.now().toString(), código HTTP, descripción, mensaje genérico en español "Credenciales inválidas" y la ruta real obtenida de HttpServletRequest. El segundo debe manejar AccessDeniedException y devolver HTTP 403 FORBIDDEN con un ErrorResponse que incluya timestamp, código HTTP, descripción, mensaje en español "No tienes permisos para realizar esta operación" y la ruta real de la petición. Añade los imports necesarios y Javadoc completo para ambos métodos. Conserva el formato ErrorResponse y el resto del código. — generado por IA
    /**
     * Manejador para excepciones de tipo AuthenticationException.
     * Devuelve una respuesta HTTP 401 UNAUTHORIZED con un ErrorResponse que contiene
     * el timestamp, código HTTP, descripción del error, mensaje genérico en español
     * y la ruta solicitada.
     *
     * @param e La excepción de tipo AuthenticationException.
     * @param request La solicitud HTTP.
     * @return Una ResponseEntity con el ErrorResponse y el código HTTP 401.
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(
            AuthenticationException e, HttpServletRequest request) {

        String message = "Credenciales inválidas.";

        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now().toString(),
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                message,
                request.getRequestURI()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Manejador para excepciones de tipo AccessDeniedException.
     * Devuelve una respuesta HTTP 403 FORBIDDEN con un ErrorResponse que contiene
     * el timestamp, código HTTP, descripción del error, mensaje genérico en español
     * y la ruta solicitada.
     *
     * @param e La excepción de tipo AccessDeniedException.
     * @param request La solicitud HTTP.
     * @return Una ResponseEntity con el ErrorResponse y el código HTTP 403.
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(
            AccessDeniedException e, HttpServletRequest request) {

        String message = "No tienes permisos para realizar esta operación.";

        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now().toString(),
                HttpStatus.FORBIDDEN.value(),
                HttpStatus.FORBIDDEN.getReasonPhrase(),
                message,
                request.getRequestURI()
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }
```

---
<br>
Paso 5 - Comprensción del código (Ask)<br>
<br>
Pregunta:<br>
<br>
¿Por qué dos usuarios con la misma contraseña tienen hashes BCrypt distintos?<br>
<br>
Respuesta:<br>
<br>
Porque BCrypt genera una sal aleatoria diferente cada vez que cifra una contraseña.<br>
<br>
La sal se combina con la contraseña antes de calcular el hash:

```
hash = BCrypt(contraseña + sal)
```

Por eso, aunque dos usuarios tengan la misma contraseña, sus hashes serán distintos. La sal se guarda dentro del propio hash y BCrypt la utiliza después para comprobar la contraseña.<br>
<br>
Esto evita que un atacante pueda:<br>
<br>
detectar qué usuarios comparten contraseña;<br>
reutilizar tablas precalculadas;<br>
obtener el mismo hash para una contraseña común.<br>
La contraseña no se descifra: se vuelve a calcular el hash y se compara con el almacenado.<br>
<br>
Pregunta:<br>
<br>
¿Por qué el rol se guarda con EnumType.STRING y no con ORDINAL?<br>
<br>
Respuesta:<br>
<br>
Porque EnumType.STRING guarda el nombre del rol:

```
ADMIN
USER
```

Mientras que EnumType.ORDINAL guarda su posición numérica:

```
0
1
```

STRING es más seguro y mantenible porque:<br>
<br>
los datos son legibles en la base de datos;<br>
reordenar los valores del enum no cambia su significado;<br>
añadir valores nuevos no desplaza los existentes;<br>
evita que un rol almacenado como 1 termine representando otro rol tras modificar el enum.<br>
Ejemplo:

```
@Enumerated(EnumType.STRING)
private Role role;
```

Con ORDINAL, cambiar el orden del enum puede provocar errores graves de autorización, concediendo o quitando permisos incorrectamente.<br>
<br>
Pregunta:<br>
<br>
¿Por qué hasRole('ADMIN') no funcionaría si getAuthorities() devolviera "ADMIN" sin prefijo?<br>
<br>
Respuesta:<br>
<br>
Porque hasRole("ADMIN") busca automáticamente la autoridad:

```
ROLE_ADMIN
```

Si getAuthorities() devuelve únicamente:

```
ADMIN
```

no hay coincidencia y Spring Security deniega el acceso con 403 Forbidden.<br>
<br>
Correcto:

```
hasRole("ADMIN")       // busca ROLE_ADMIN
```

Alternativa si no quieres usar el prefijo:

```
hasAuthority("ADMIN")   // busca ADMIN exactamente
```

Lo habitual es devolver las autoridades con el prefijo:

```
ROLE_ADMIN
ROLE_USER
```

<br>
Pregunta:<br>
<br>
¿Qué ocurre con @PreAuthorize si falta @EnableMethodSecurity?<br>
<br>
Respuesta:<br>
<br>
Si falta @EnableMethodSecurity, las anotaciones como:

```
@PreAuthorize("hasRole('ADMIN')")
```

no se procesan por Spring Security.<br>
<br>
Por tanto, el método podría ejecutarse aunque el usuario no tenga el rol requerido. La aplicación seguiría aplicando la seguridad de las rutas HTTP, pero no la seguridad específica de los métodos.<br>
<br>
Debes activar la seguridad de métodos en la configuración:

```
@Configuration
@EnableMethodSecurity
public class SecurityConfig {
}
```

Después, @PreAuthorize se evaluará antes de ejecutar el método. Si el usuario no cumple la expresión, normalmente recibirá 403 Forbidden.
<br>
Pregunta:<br>
<br>
¿Qué pasa con el token de un usuario que se borra de la base de datos?<br>
<br>
Respuesta:<br>
<br>
Depende de cómo valide el JWT el filtro:<br>
<br>
Si solo valida la firma y la expiración, el token seguirá siendo válido hasta que caduque. Borrar al usuario de la base de datos no lo invalida automáticamente.<br>
Si el filtro consulta UserDetailsService en cada petición, detectará que el usuario ya no existe y rechazará el token, normalmente con 401 Unauthorized.<br>
Por eso los JWT suelen tener una duración corta. Para invalidarlos antes de su expiración se pueden usar:<br>
<br>
lista de revocación;<br>
tokenVersion asociado al usuario;<br>
comprobación del usuario en cada petición;<br>
rotación de la clave secreta, aunque invalida todos los tokens;<br>
refresh tokens revocables.<br>
En el proyecto actual, el filtro solo extrae el usuario y valida la firma y la expiración, por lo que un token de un usuario eliminado seguiría funcionando hasta caducar.<br>
<br>

---
<br>
Paso 6 - Validación en Postman<br>
<br>
Registro de usuario nuevo

![Captura-09-postman-01-registro-usuario-nuevo](..\capturas\Captura-09-postman-01-registro-usuario-nuevo.png)
<br>

Intentar registrar un usuario existente

![Captura-09-postman-02-registro-usuario-duplicado](..\capturas\Captura-09-postman-02-registro-usuario-duplicado.png)
<br>

Login de usuario con contraseña incorrecta

![Captura-09-postman-03-login-usuario-password-incorrecta](..\capturas\Captura-09-postman-03-login-usuario-password-incorrecta.png)
<br>

Login correcto del usuario ana

![Captura-09-postman-04-login-usuario-correcto](..\capturas\Captura-09-postman-04-login-usuario-correcto.png)
<br>

Acceso a tareas con JWT válido de ana

![Captura-09-postman-05-tareas-con-jwt-usuario](..\capturas\Captura-09-postman-05-tareas-con-jwt-usuario.png)
<br>

DELETE como usuario USER.

![Captura-09-postman-06-delete-tarea-usuario-sin-permiso](..\capturas\Captura-09-postman-06-delete-tarea-usuario-sin-permiso.png)
<br>

Login correcto de administrador

![Captura-09-postman-07-login-admin](..\capturas\Captura-09-postman-07-login-admin.png)
<br>

El administrador puede eliminar una tarea

![Captura-09-postman-09-tareas-sin-autenticacion](..\capturas\Captura-09-postman-09-tareas-sin-autenticacion.png)

Acceso sin autenticación

![Captura-09-postman-09-tareas-sin-autenticacion](..\capturas\Captura-09-postman-09-tareas-sin-autenticacion.png)

---
<br>
Paso 7 - Adaptar los test<br>
<br>
Comprobación previa con mvn test:

![Captura-09-seguridad-07-mvn-test-comprobacion-previa](..\capturas\Captura-09-seguridad-07-mvn-test-comprobacion-previa.png)
<br>

TaskControllerTest<br>
<br>
Se añade:

```
/**
 * MockitoBean for the UserDetailsService.
 */
@MockitoBean
private UserDetailsService userDetailsService;
```

TaskmanagerApplicationTests<br>
<br>
@ActiveProfiles("test") ya lo tenía porque lo puse en el ejercicio anterior para corregir los test así que no faltaría nada.<br>
<br>
Test:<br>
<br>
El registro guarda la contraseña con BCrypt y el rol USER.<br>
<br>
        // Añade un test de integración que realice un registro mediante POST /auth/register y compruebe que el usuario se guarda en la base de datos, que su contraseña está cifrada mediante BCrypt y que se le asigna automáticamente el rol USER. Utiliza los componentes reales, sin mocks, y sigue la estructura Given / When / Then. No modifiques los tests existentes. — generado por IA<br>
<br>
Resultado:

```

        /**
         * Comprueba que el registro guarda la contraseña cifrada con BCrypt
         * y asigna automáticamente el rol USER.
         */
        // Añade un test de integración que realice un registro mediante POST
        // /auth/register
        // y compruebe que el usuario se guarda en la base de datos, que su contraseña
        // está cifrada mediante BCrypt y que se le asigna automáticamente el rol USER.
        // Utiliza los componentes reales, sin mocks, y sigue la estructura Given / When
        // / Then.
        // No modifiques los tests existentes.
        // — generado por IA
        @Test
        public void registerUser_shouldSaveEncryptedPasswordAndUserRole() {

                // Given
                LoginRequestDTO registerRequest = new LoginRequestDTO(
                                "test_register",
                                "password123");

                // When
                ResponseEntity<LoginResponseDTO> response = testRestTemplate.postForEntity(
                                "/auth/register",
                                registerRequest,
                                LoginResponseDTO.class);

                // Then
                assertEquals(HttpStatus.CREATED, response.getStatusCode());

                AppUser savedUser = appUserRepository.findByUsername("test_register").orElseThrow();

                assertTrue(passwordEncoder.matches("password123", savedUser.getPassword()));
                assertFalse(savedUser.getPassword().equals("password123"));
                assertEquals(Role.USER, savedUser.getRole());
        }
```

Comprobación:

![Captura-09-seguridad-07-test-registro-bcrypt-rol-user](..\capturas\Captura-09-seguridad-07-test-registro-bcrypt-rol-user.png)
<br>
Un login incorrecto devuelve 401 con ErrorResponse.<br>
<br>
Comentario:<br>
<br>
// Añade un test de integración que envíe una petición POST a /auth/login con un usuario existente y una contraseña incorrecta. Comprueba que el servidor devuelve HTTP 401 Unauthorized y un cuerpo JSON con los campos timestamp, status, error, message y path de ErrorResponse. Utiliza los componentes reales, sin mocks, y sigue la estructura Given / When / Then. No modifiques los tests existentes. — generado por IA<br>
<br>
Resultado:

```
// Añade un test de integración que envíe una petición POST a /auth/login con un usuario existente y una contraseña incorrecta. Comprueba que el servidor devuelve HTTP 401 Unauthorized y un cuerpo JSON con los campos timestamp, status, error, message y path de ErrorResponse. Utiliza los componentes reales, sin mocks, y sigue la estructura Given / When / Then. No modifiques los tests existentes. — generado por IA
        /**
         * Comprueba que un login con contraseña incorrecta devuelve HTTP 401
         * y un cuerpo con la estructura ErrorResponse.
         */
        @Test
        public void loginWithIncorrectPassword_shouldReturnUnauthorizedWithErrorResponse() {

                // Given
                LoginRequestDTO loginRequest = new LoginRequestDTO(
                                "admin",
                                "incorrecta");

                // When
                ResponseEntity<String> response = testRestTemplate.postForEntity(
                                "/auth/login",
                                loginRequest,
                                String.class);

                // Then
                assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
                assertNotNull(response.getBody());

                assertTrue(response.getBody().contains("\"timestamp\""));
                assertTrue(response.getBody().contains("\"status\""));
                assertTrue(response.getBody().contains("\"error\""));
                assertTrue(response.getBody().contains("\"message\""));
                assertTrue(response.getBody().contains("\"path\""));
        }
```

Comprobación:

![Captura-12-seguridad-07-test-login-password-incorrecta](..\capturas\Captura-12-seguridad-07-test-login-password-incorrecta.png)

<br>
Un usuario USER recibe 403 al borrar una tarea.<br>
<br>
Comentario:<br>
<br>
// Añade un test de integración que cree una tarea mediante POST /api/v1/tasks y realice un login con un usuario que tenga el rol USER. Utiliza el JWT obtenido para intentar eliminar la tarea mediante DELETE /api/v1/tasks/{id}. Comprueba que el servidor devuelve HTTP 403 Forbidden porque el usuario no tiene permisos de administrador. Utiliza los componentes reales, sin mocks, y sigue la estructura Given / When / Then. No modifiques los tests existentes. — generado por IA<br>
<br>
Resultado:

```
// Añade un test de integración que cree una tarea mediante POST /api/v1/tasks y realice un login con un usuario que tenga el rol USER. Utiliza el JWT obtenido para intentar eliminar la tarea mediante DELETE /api/v1/tasks/{id}. Comprueba que el servidor devuelve HTTP 403 Forbidden porque el usuario no tiene permisos de administrador. Utiliza los componentes reales, sin mocks, y sigue la estructura Given / When / Then. No modifiques los tests existentes. — generado por IA
        /**
         * Comprueba que un usuario con rol USER no puede eliminar tareas.
         */
        @Test
        public void userRole_shouldReturnForbiddenWhenDeletingTask() {

                // Given: Crear una tarea
                TaskRequestDTO taskRequest = new TaskRequestDTO(
                                "Task for USER",
                                "Description",
                                TaskStatus.PENDING,
                                TaskPriority.HIGH,
                                LocalDate.of(2027, 12, 31));

                ResponseEntity<TaskResponseDTO> createResponse = testRestTemplate.postForEntity(
                                "/api/v1/tasks",
                                taskRequest,
                                TaskResponseDTO.class);

                assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
                assertNotNull(createResponse.getBody());

                Long taskId = createResponse.getBody().getId();

                // When: Intentar eliminar la tarea con el token de un usuario USER
                LoginRequestDTO loginRequest = new LoginRequestDTO("ana", "TU_CONTRASEÑA_DE_ANA");

                ResponseEntity<LoginResponseDTO> loginResponse = testRestTemplate.postForEntity(
                                "/auth/login",
                                loginRequest,
                                LoginResponseDTO.class);

                assertEquals(HttpStatus.OK, loginResponse.getStatusCode());
                assertNotNull(loginResponse.getBody());

                HttpHeaders headers = new HttpHeaders();
                headers.setBearerAuth(loginResponse.getBody().getToken());

                HttpEntity<Void> request = new HttpEntity<>(headers);

                ResponseEntity<String> deleteResponse = testRestTemplate.exchange(
                                "/api/v1/tasks/" + taskId,
                                HttpMethod.DELETE,
                                request,
                                String.class);

                // Then
                assertEquals(HttpStatus.FORBIDDEN, deleteResponse.getStatusCode());
        }
```

Comprobación:

![Captura-09-seguridad-07-test-user-403-forbidden](..\capturas\Captura-09-seguridad-07-test-user-403-forbidden.png)
<br>
Un usuario ADMIN recibe 204 al borrar una tarea.<br>
<br>
Comentario:<br>
<br>
// Añade un test de integración que cree una tarea mediante POST /api/v1/tasks y compruebe que un usuario con rol ADMIN puede eliminarla mediante DELETE /api/v1/tasks/{id}, utilizando el token JWT obtenido en el setUp. Verifica que el servidor devuelve HTTP 204 NO_CONTENT. Utiliza los componentes reales, sin mocks, y sigue la estructura Given / When / Then. No modifiques los tests existentes. — generado por IA<br>
<br>
Resultado:


````
// Añade un test de integración que cree una tarea mediante POST /api/v1/tasks y compruebe que un usuario con rol ADMIN puede eliminarla mediante DELETE /api/v1/tasks/{id}, utilizando el token JWT obtenido en el setUp. Verifica que el servidor devuelve HTTP 204 NO_CONTENT. Utiliza los componentes reales, sin mocks, y sigue la estructura Given / When / Then. No modifiques los tests existentes. — generado por IA
        /**
         * Comprueba que un usuario con rol ADMIN puede eliminar tareas.
         */
        @Test
        public void adminRole_shouldReturnNoContentWhenDeletingTask() {

                // Given: Crear una tarea con el token ADMIN del setUp
                TaskRequestDTO taskRequest = new TaskRequestDTO(
                                "Task for ADMIN",
                                "Description",
                                TaskStatus.PENDING,
                                TaskPriority.HIGH,
                                LocalDate.of(2027, 12, 31));

                ResponseEntity<TaskResponseDTO> createResponse = testRestTemplate.postForEntity(
                                "/api/v1/tasks",
                                taskRequest,
                                TaskResponseDTO.class);

                assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
                assertNotNull(createResponse.getBody());

                Long taskId = createResponse.getBody().getId();

                // When: Eliminar la tarea con el token ADMIN
                ResponseEntity<Void> deleteResponse = testRestTemplate.exchange(
                                "/api/v1/tasks/" + taskId,
                                HttpMethod.DELETE,
                                new HttpEntity<>(new HttpHeaders()),
                                Void.class);

                // Then: Comprobar que devuelve 204 NO_CONTENT
                assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatusCode());
        }
````

Comprobación:

![capturas\Captura-09-seguridad-07-test-admin-204-no-content](..\capturas\Captura-09-seguridad-07-test-admin-204-no-content.png)
<br>

Test Completo:

![Captura-09-seguridad-07-mvn-test-build-success](..\capturas\Captura-09-seguridad-07-mvn-test-build-success.png)
<br>

---
<br>
Reflexión<br>
<br>
Pregunta:<br>
<br>
¿Qué ventaja tiene guardar el rol en la base de datos frente a incluirlo en el token? ¿Qué problema tendría cambiarle el rol a un usuario si estuviera solo en el token?<br>
<br>
Respuesta:<br>
<br>
Guardar el rol en la base de datos permite modificar los permisos de un usuario sin tener que generar un nuevo token. Si el rol estuviera únicamente en el token, habría que esperar a que caducase o invalidarlo y generar otro para que los cambios de permisos tuvieran efecto.<br>
<br>
Pregunta:<br>
<br>
¿Por qué el administrador no se crea con el endpoint de registro? ¿Qué problema tiene crearlo con una contraseña escrita en el código?<br>
<br>
Respuesta:<br>
<br>
Porque el endpoint de registro está destinado a crear usuarios normales y permitir que cualquiera se registre como administrador supondría un riesgo de seguridad. Crear el administrador con una contraseña escrita en el código también es peligroso, ya que podría quedar expuesta al compartir el repositorio. Es preferible utilizar variables de entorno o un sistema seguro de configuración de credenciales.<br>
<br>
Pregunta:<br>
<br>
¿Qué faltaría para llevar esta autenticación a producción (HTTPS, caducidad corta y renovación del token, bloqueo tras varios intentos fallidos...)?<br>
<br>
Respuesta:<br>
<br>
Sería necesario utilizar HTTPS para proteger las comunicaciones, establecer tokens con una caducidad corta e implementar mecanismos de renovación y revocación. También habría que limitar los intentos de inicio de sesión, bloquear temporalmente cuentas ante múltiples fallos y almacenar las credenciales de forma segura mediante variables de entorno o un gestor de secretos.<br>
<br>
Pregunta:<br>
<br>