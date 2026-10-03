Paso 2 - Explorar la estructura (Ask)<br>
<br>
Pregunta:<br>
Explícame la estructura del proyecto Spring Boot que acaba de generar Spring Initializr: qué contiene cada carpeta y fichero importante, qué función tiene la clase principal de la aplicación y para qué sirven los ficheros de configuración y el pom.xml.<br>
<br>
Respuesta:<br>
Estructura típica del proyecto Spring Boot<br>
Un proyecto generado con Spring Initializr suele tener esta estructura:<br>
<br>
taskmanager/<br>
├── src/<br>
│   ├── main/<br>
│   │   ├── java/<br>
│   │   │   └── .../<br>
│   │   │       └── TaskmanagerApplication.java<br>
│   │   └── resources/<br>
│   │       ├── static/<br>
│   │       ├── templates/<br>
│   │       └── application.properties<br>
│   └── test/<br>
│       └── java/<br>
├── pom.xml<br>
├── mvnw<br>
├── mvnw.cmd<br>
├── .gitignore<br>
└── HELP.md<br>
<br>
Contiene el código fuente principal de la aplicación:<br>
<br>
Controladores REST o MVC.<br>
Servicios.<br>
Repositorios.<br>
Entidades o modelos.<br>
Clases de configuración.<br>
La clase principal de Spring Boot.<br>
Normalmente se organiza utilizando paquetes, por ejemplo:<br>
<br>
com.ejemplo.taskmanager<br>
├── controller<br>
├── service<br>
├── repository<br>
└── model<br>
<br>
Clase principal de la aplicación<br>
Es una clase similar a:<br>
<br>
@SpringBootApplication<br>
public class TaskmanagerApplication {<br>
<br>
    public static void main(String[] args) {<br>
        SpringApplication.run(TaskmanagerApplication.class, args);<br>
    }<br>
}<br>
<br>
Su función es iniciar la aplicación.<br>
<br>
main es el punto de entrada de Java.<br>
SpringApplication.run(...) crea el contexto de Spring y arranca el servidor embebido, normalmente Tomcat.<br>
@SpringBootApplication combina tres funcionalidades:<br>
@Configuration: permite definir configuración de Spring.<br>
@EnableAutoConfiguration: configura automáticamente componentes según las dependencias disponibles.<br>
@ComponentScan: busca componentes Spring en el paquete de la clase y sus subpaquetes.<br>
Por eso se recomienda colocar esta clase en el paquete raíz del proyecto.<br>
<br>
resources<br>
Contiene recursos que no son código Java.<br>
<br>
application.properties o application.yml<br>
Contiene la configuración de la aplicación, por ejemplo:<br>
<br>
Puerto del servidor.<br>
Conexión con la base de datos.<br>
Configuración de JPA o Hibernate.<br>
Niveles de logging.<br>
Perfiles de ejecución.<br>
Propiedades personalizadas.<br>
Ejemplo:<br>
<br>
server.port=8081<br>
spring.application.name=taskmanager<br>
<br>
También puede utilizarse application.yml, que expresa la configuración con una estructura jerárquica.<br>
Contiene recursos estáticos que se sirven directamente:<br>
<br>
CSS.<br>
JavaScript.<br>
Imágenes.<br>
Fuentes.<br>
templates<br>
Contiene plantillas HTML para motores como Thymeleaf. Esta carpeta solo suele utilizarse si el proyecto incluye una dependencia de vistas.<br>
<br>
test<br>
Contiene las pruebas automatizadas de la aplicación.<br>
<br>
Se pueden incluir:<br>
<br>
Pruebas unitarias.<br>
Pruebas de integración.<br>
Pruebas de controladores.<br>
Pruebas de repositorios.<br>
Normalmente, las clases de prueba mantienen la misma estructura de paquetes que el código principal.<br>
<br>
pom.xml<br>
Es el fichero principal de configuración de Maven. Define:<br>
<br>
Identidad del proyecto: grupo, nombre y versión.<br>
Versión de Java.<br>
Dependencias de Spring Boot.<br>
Plugins de compilación y ejecución.<br>
Configuración de empaquetado.<br>
Gestión de versiones.<br>
Por ejemplo, las dependencias pueden incluir:<br>
<br>
spring-boot-starter-web: aplicaciones web y APIs REST.<br>
spring-boot-starter-data-jpa: persistencia con JPA.<br>
spring-boot-starter-test: herramientas de testing.<br>
Driver de MySQL, PostgreSQL u otra base de datos.<br>
Maven utiliza el pom.xml para descargar dependencias, compilar el proyecto, ejecutar pruebas y generar el fichero .jar.<br>
<br>
mvnw y mvnw.cmd<br>
Son los Maven Wrapper:<br>
<br>
mvnw: para Linux y macOS.<br>
mvnw.cmd: para Windows.<br>
Permiten ejecutar Maven sin instalarlo globalmente. En Windows, por ejemplo:<br>
<br>
mvnw.cmd spring-boot:run<br>
<br>
Indica a Git qué ficheros no debe incluir en el control de versiones, como:<br>
<br>
Ficheros compilados.<br>
La carpeta target.v
Configuración específica del IDE.<br>
Ficheros temporales.<br>
HELP.md<br>
Suele contener información y enlaces generados por Spring Initializr sobre las dependencias seleccionadas.<br>
<br>
Resumen<br>
java: código Java de la aplicación.<br>
resources: configuración y recursos.<br>
test: pruebas.<br>
application.properties: configuración de ejecución.<br>
Clase principal: arranca Spring Boot.<br>
pom.xml: dependencias y configuración de Maven.<br>
mvnw.cmd: permite ejecutar Maven en Windows sin instalación global.<br>
<br>
<br>
<br>
Resultado del arranque de la app en localhost:8080 (no hay endpoints):<br>
<br>
Whitelabel Error Page<br>
This application has no explicit mapping for /error, so you are seeing this as a fallback.<br>
<br>
Wed Sep 30 12:36:00 CEST 2026<br>
There was an unexpected error (type=Not Found, status=404).

---

Reflexión<br>
<br>
¿Qué hace @SpringBootApplication exactamente? ¿Qué tres anotaciones agrupa?<br>
<br>
Es una anotación que se coloca en la clase principal de la aplicación de Spring Boot y agrupa:<br>
@Configuration -> indica que la clase puede definir la configuración de Spring.<br>
@EnableAutoConfiguration -> permite que Spring Boot configure automáticamente la aplicación según las dependencias que haya en el proyecto.<br>
@ComponentScan -> hace que Spring busque los componentes de Spring (@Component, @Service, @Repository, @Controller, etc.) en el paquete de la aplicación y los subpaquetes que tenga.<br>
<br>
<br>
<br>
¿Por qué el navegador devuelve un 404 si la aplicación está arrancada?<br>
<br>
Porque la aplicación está funcionando pero no existe ningún endpoint que atienda la ruta /. por eso devuelve error 404.<br>
(la explicación a esta pregunta la dice el propio pdf del ejercicio)<br>
<br>
<br>
<br>
¿Para qué crees que se usará application.properties más adelante?<br>
<br>
Se utilizará para configurar diferentes aspectos de la app Spring Boot como el nombre de la aplicación, el puerto que usa para funcionar y otras propiedades de configuración que podamos necesitar según avance el proyecto.<br>
