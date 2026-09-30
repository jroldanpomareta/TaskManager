Paso 2 - Explorar la estructura (Ask)

Pregunta:
Explícame la estructura del proyecto Spring Boot que acaba de generar Spring Initializr: qué contiene cada carpeta y fichero importante, qué función tiene la clase principal de la aplicación y para qué sirven los ficheros de configuración y el pom.xml.

Respuesta:
Estructura típica del proyecto Spring Boot
Un proyecto generado con Spring Initializr suele tener esta estructura:

taskmanager/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── .../
│   │   │       └── TaskmanagerApplication.java
│   │   └── resources/
│   │       ├── static/
│   │       ├── templates/
│   │       └── application.properties
│   └── test/
│       └── java/
├── pom.xml
├── mvnw
├── mvnw.cmd
├── .gitignore
└── HELP.md

Contiene el código fuente principal de la aplicación:

Controladores REST o MVC.
Servicios.
Repositorios.
Entidades o modelos.
Clases de configuración.
La clase principal de Spring Boot.
Normalmente se organiza utilizando paquetes, por ejemplo:

com.ejemplo.taskmanager
├── controller
├── service
├── repository
└── model

Clase principal de la aplicación
Es una clase similar a:

@SpringBootApplication
public class TaskmanagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskmanagerApplication.class, args);
    }
}

Su función es iniciar la aplicación.

main es el punto de entrada de Java.
SpringApplication.run(...) crea el contexto de Spring y arranca el servidor embebido, normalmente Tomcat.
@SpringBootApplication combina tres funcionalidades:
@Configuration: permite definir configuración de Spring.
@EnableAutoConfiguration: configura automáticamente componentes según las dependencias disponibles.
@ComponentScan: busca componentes Spring en el paquete de la clase y sus subpaquetes.
Por eso se recomienda colocar esta clase en el paquete raíz del proyecto.

resources
Contiene recursos que no son código Java.

application.properties o application.yml
Contiene la configuración de la aplicación, por ejemplo:

Puerto del servidor.
Conexión con la base de datos.
Configuración de JPA o Hibernate.
Niveles de logging.
Perfiles de ejecución.
Propiedades personalizadas.
Ejemplo:

server.port=8081
spring.application.name=taskmanager

También puede utilizarse application.yml, que expresa la configuración con una estructura jerárquica.
Contiene recursos estáticos que se sirven directamente:

CSS.
JavaScript.
Imágenes.
Fuentes.
templates
Contiene plantillas HTML para motores como Thymeleaf. Esta carpeta solo suele utilizarse si el proyecto incluye una dependencia de vistas.

test
Contiene las pruebas automatizadas de la aplicación.

Se pueden incluir:

Pruebas unitarias.
Pruebas de integración.
Pruebas de controladores.
Pruebas de repositorios.
Normalmente, las clases de prueba mantienen la misma estructura de paquetes que el código principal.

pom.xml
Es el fichero principal de configuración de Maven. Define:

Identidad del proyecto: grupo, nombre y versión.
Versión de Java.
Dependencias de Spring Boot.
Plugins de compilación y ejecución.
Configuración de empaquetado.
Gestión de versiones.
Por ejemplo, las dependencias pueden incluir:

spring-boot-starter-web: aplicaciones web y APIs REST.
spring-boot-starter-data-jpa: persistencia con JPA.
spring-boot-starter-test: herramientas de testing.
Driver de MySQL, PostgreSQL u otra base de datos.
Maven utiliza el pom.xml para descargar dependencias, compilar el proyecto, ejecutar pruebas y generar el fichero .jar.

mvnw y mvnw.cmd
Son los Maven Wrapper:

mvnw: para Linux y macOS.
mvnw.cmd: para Windows.
Permiten ejecutar Maven sin instalarlo globalmente. En Windows, por ejemplo:

mvnw.cmd spring-boot:run

Indica a Git qué ficheros no debe incluir en el control de versiones, como:

Ficheros compilados.
La carpeta target.
Configuración específica del IDE.
Ficheros temporales.
HELP.md
Suele contener información y enlaces generados por Spring Initializr sobre las dependencias seleccionadas.

Resumen
java: código Java de la aplicación.
resources: configuración y recursos.
test: pruebas.
application.properties: configuración de ejecución.
Clase principal: arranca Spring Boot.
pom.xml: dependencias y configuración de Maven.
mvnw.cmd: permite ejecutar Maven en Windows sin instalación global.



Resultado del arranque de la app en localhost:8080 (no hay endpoints):

Whitelabel Error Page
This application has no explicit mapping for /error, so you are seeing this as a fallback.

Wed Sep 30 12:36:00 CEST 2026
There was an unexpected error (type=Not Found, status=404).






Reflexión

¿Qué hace @SpringBootApplication exactamente? ¿Qué tres anotaciones agrupa?

Es una anotación que se coloca en la clase principal de la aplicación de Spring Boot y agrupa:
@Configuration -> indica que la clase puede definir la configuración de Spring.
@EnableAutoConfiguration -> permite que Spring Boot configure automáticamente la aplicación según las dependencias que haya en el proyecto.
@ComponentScan -> hace que Spring busque los componentes de Spring (@Component, @Service, @Repository, @Controller, etc.) en el paquete de la aplicación y los subpaquetes que tenga.



¿Por qué el navegador devuelve un 404 si la aplicación está arrancada?

Porque la aplicación está funcionando pero no existe ningún endpoint que atienda la ruta /. por eso devuelve error 404.
(la explicación a esta pregunta la dice el propio pdf del ejercicio)



¿Para qué crees que se usará application.properties más adelante?

Se utilizará para configurar diferentes aspectos de la app Spring Boot como el nombre de la aplicación, el puerto que usa para funcionar y otras propiedades de configuración que podamos necesitar según avance el proyecto.
