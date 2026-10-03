Paso 1 - Preparación (ASK):<br>
<br>
Pregunta 1:<br>
<br>
¿Qué es un Rest Controller en Spring Boot, cuál es su responsabilidad dentro de una aplicación y qué diferencia hay entre un Controller normal y un Rest Controller?<br>
<br>
Respuesta:<br>
<br>
¿Qué es un RestController?<br>
Un REST Controller en Spring Boot es una clase que expone endpoints HTTP para que otras aplicaciones o clientes puedan comunicarse con la aplicación mediante una API REST.<br>
<br>
Se identifica normalmente con:<br>
<br>
@RestController<br>
@RequestMapping("/api/tasks")<br>
public class TaskController {<br>
    // endpoints<br>
}<br>
<br>
Responsabilidades<br>
Un RestController suele encargarse de:<br>
<br>
Recibir peticiones HTTP (GET, POST, PUT, DELETE).<br>
Obtener parámetros y datos del cuerpo de la petición.<br>
Validar la entrada recibida.<br>
Invocar la lógica de negocio de los servicios.<br>
Devolver respuestas HTTP, normalmente en formato JSON.<br>
Utilizar códigos de estado adecuados, como 200, 201, 400 o 404.<br>
No debería contener directamente toda la lógica de negocio; esa responsabilidad corresponde normalmente a la capa de servicios.<br>
<br>
Diferencia entre @Controller y @RestController<br>
@Controller: se utiliza principalmente en aplicaciones MVC. Sus métodos suelen devolver el nombre de una vista HTML.<br>
@RestController: se utiliza para crear APIs REST. Sus métodos devuelven directamente datos en el cuerpo de la respuesta, normalmente JSON.<br>
@RestController equivale conceptualmente a:<br>
<br>
@Controller<br>
@ResponseBody<br>
<br>
Por ejemplo:<br>
<br>
@RestController<br>
public class HelloController {<br>
<br>
    @GetMapping("/hello")<br>
    public String hello() {<br>
        return "Hola";<br>
    }<br>
}<br>
<br>
La respuesta será directamente "Hola" en el cuerpo HTTP, en lugar de intentar buscar una vista llamada Hola.<br>
<br>
---
<br>
Pregunta 2:<br>
<br>
¿Cómo se estructura una respuesta HTTP en una API REST? Dime qué función tienen el código de estado y cúales hay y explícame las cabeceras y el cuerpo de la respueta.<br>
<br>
Respuesta:<br>
<br>
Una respuesta HTTP está formada principalmente por:<br>
<br>
Código de estado<br>
Cabeceras<br>
Cuerpo de la respuesta<br>
Ejemplo:<br>
<br>
HTTP/1.1 200 OK<br>
Content-Type: application/json<br>
<br>
{<br>
  "id": 1,<br>
  "title": "Comprar leche"<br>
}<br>
<br>
Código de estado<br>
Indica el resultado de la petición realizada por el cliente.<br>
<br>
1xx — Informativos: la petición continúa procesándose.<br>
2xx — Éxito:<br>
200 OK: petición procesada correctamente.<br>
201 Created: recurso creado correctamente.<br>
204 No Content: operación correcta, sin contenido que devolver.<br>
3xx — Redirección: el cliente debe realizar alguna acción adicional.<br>
4xx — Error del cliente:<br>
400 Bad Request: petición incorrecta o datos inválidos.<br>
401 Unauthorized: falta autenticación.<br>
403 Forbidden: no se tienen permisos suficientes.<br>
404 Not Found: recurso no encontrado.<br>
5xx — Error del servidor:<br>
500 Internal Server Error: error inesperado en el servidor.<br>
503 Service Unavailable: servicio temporalmente no disponible.<br>
Cabeceras<br>
Las cabeceras proporcionan información adicional sobre la respuesta. Algunos ejemplos son:<br>
<br>
Content-Type: indica el formato del cuerpo, por ejemplo application/json.<br>
Content-Length: indica el tamaño de la respuesta.<br>
Location: indica la ubicación de un recurso recién creado.<br>
Cache-Control: define cómo se puede almacenar la respuesta en caché.<br>
Authorization: puede contener información de autenticación, aunque normalmente se utiliza en la petición.<br>
Cuerpo de la respuesta<br>
El cuerpo contiene los datos que devuelve el servidor. En una API REST suele utilizarse formato JSON, aunque también pueden utilizarse XML u otros formatos.<br>
<br>
Por ejemplo:<br>
<br>
{<br>
  "id": 1,<br>
  "description": "Completar el proyecto",<br>
  "completed": false<br>
}<br>
<br>
En Spring Boot se puede controlar la respuesta completa utilizando ResponseEntity:<br>
<br>
return ResponseEntity<br>
        .status(HttpStatus.CREATED)<br>
        .body(task);<br>
<br>
En este caso, se devuelve el código 201, las cabeceras correspondientes y el objeto task en el cuerpo de la respuesta.

---
<br>
Pregunta 3:<br>
<br>
¿Cómo se versionan las URIs de una API REST? Dime todo sobre las URIs, buenas prácticas, ventajas, casos de uso, etc.<br>
<br>
Respuesta:<br>
<br>
¿Qué es una URI?<br>
Una URI (Uniform Resource Identifier) identifica un recurso dentro de una API REST.<br>
<br>
Ejemplo:<br>
<br>
https://api.ejemplo.com/api/v1/tasks/15<br>
<br>
Sus partes principales son:<br>
<br>
https: protocolo.<br>
api.ejemplo.com: dominio.<br>
/api/v1: ruta base y versión de la API.<br>
/tasks: colección de recursos.<br>
/15: identificador de un recurso concreto.<br>
Una URI debe identificar recursos, no acciones. Las acciones se expresan mediante los métodos HTTP:<br>
<br>
Método	Operación<br>
GET	Consultar<br>
POST	Crear<br>
PUT	Reemplazar o actualizar<br>
PATCH	Actualizar parcialmente<br>
DELETE	Eliminar<br>
<br>
Ejemplos:<br>
<br>
GET    /api/v1/tasks<br>
GET    /api/v1/tasks/15<br>
POST   /api/v1/tasks<br>
PUT    /api/v1/tasks/15<br>
PATCH  /api/v1/tasks/15<br>
DELETE /api/v1/tasks/15<br>
<br>
¿Por qué versionar una API?<br>
La versionación permite modificar una API sin romper los clientes que utilizan una versión anterior.<br>
<br>
Por ejemplo, si la respuesta de una tarea cambia de:v
<br>
{<br>
  "title": "Comprar leche"<br>
}<br>
<br>
a:<br>
<br>
{<br>
  "name": "Comprar leche",<br>
  "completed": false<br>
}<br>
<br>
los clientes antiguos podrían dejar de funcionar. Una nueva versión permite mantener ambas estructuras durante un periodo de transición.<br>
<br>
Principales estrategias de versionado<br>
1. Versión en la URI<br>
Es la estrategia más habitual:<br>
<br>
/api/v1/tasks<br>
/api/v2/tasks<br>
<br>
En Spring Boot:<br>
<br>
@RequestMapping("/api/v1/tasks")<br>
public class TaskController {<br>
    // endpoints de la versión 1<br>
}<br>
<br>
Ventajas:<br>
<br>
Es sencilla y fácil de entender.<br>
Resulta visible en la documentación y en las URLs.<br>
Facilita mantener varias versiones simultáneamente.<br>
Es fácil de probar desde un navegador o herramientas como Postman.<br>
Desventajas:<br>
<br>
La URI cambia al cambiar la versión.<br>
Puede generar duplicación de controladores o lógica.<br>
La URI representa también información sobre la evolución de la API.<br>
2. Versión mediante cabeceras<br>
La versión se indica mediante una cabecera personalizada:<br>
<br>
GET /api/tasks<br>
Accept-Version: 2<br>
<br>
Ventajas:<br>
<br>
Mantiene estables las URIs.<br>
Separa la identificación del recurso de su representación.<br>
Desventajas:<br>
<br>
Es menos visible.<br>
Puede ser más difícil de probar y documentar.<br>
El cliente debe conocer y enviar correctamente la cabecera.<br>
3. Versionado mediante Accept<br>
Se utiliza negociación de contenido:<br>
<br>
GET /api/tasks<br>
Accept: application/vnd.ejemplo.v2+json<br>
<br>
Ventajas:<br>
<br>
Es una solución más alineada con HTTP.<br>
Permite solicitar diferentes representaciones del mismo recurso.<br>
Desventajas:<br>
<br>
Es más compleja.<br>
Requiere mayor configuración y conocimiento por parte de los clientes.<br>
4. Versión mediante parámetros de consulta<br>
<br>
/api/tasks?version=2<br>
<br>
Es sencilla, pero suele utilizarse menos porque la versión forma parte de la API y no de los criterios de filtrado del recurso.<br>
<br>
Buenas prácticas para diseñar URIs<br>
Usar sustantivos y no verbos:<br>
<br>
/api/v1/tasks<br>
<br>
Mejor que:<br>
<br>
/api/v1/getTasks<br>
<br>
Utilizar nombres en plural para las colecciones:<br>
<br>
/api/v1/tasks/15<br>
<br>
Mantener una nomenclatura coherente.<br>
Utilizar minúsculas y guiones si fueran necesarios:<br>
<br>
/api/v1/task-lists<br>
<br>
Evitar URIs excesivamente profundas.<br>
Utilizar parámetros de consulta para filtrar, ordenar o paginar:<br>
<br>
/api/v1/tasks?completed=false&page=0&size=20<br>
<br>
No incluir información sensible en las URIs.<br>
Evitar que las URIs dependan de detalles internos de la base de datos.<br>
Documentar las versiones disponibles.<br>
Mantener una versión antigua durante un periodo de compatibilidad.<br>
Informar de la retirada de una versión mediante documentación o cabeceras como Deprecation.<br>
Recomendación habitual<br>
Para la mayoría de proyectos, una opción clara es versionar mediante la URI:<br>
<br>
/api/v1/tasks<br>
/api/v2/tasks<br>
<br>
Normalmente se incrementa la versión principal cuando existen cambios incompatibles. Los cambios compatibles, como añadir campos opcionales, no siempre requieren crear una nueva versión.

---
<br>
Paso 2 - Diseño previo:<br>
<br>
¿Qué clase vas a crear y qué responsabilidad tiene?<br>
<br>
La clase será TaskController y su responsabilidad será la de gestionar las peticiones HTTP relacionadas con las tareas de la app exponiendo los endpoints Rest necesarios para poder hacer el CRUD (leer, consultar, actualizar y borrar).

---
<br>
¿Qué endpoints vas a implementar? Para cada uno indica: método HTTP, URI y qué debe devolver.<br>
<br>
Método HTTP      URI                     Descripción                         Respuesta<br>
<br>
GET              /api/v1/tasks           obtener todas las tareas.           200 ok + lista de tareas<br>
GET              /api/v1/tasks/{id}      obtener una tarea por ID.           200 ok + tarea / 404 si no existe<br>
POST             /api/v1/tasks           crear una tarea.                    201 + tarea creada<br>
PUT              /api/v1/tasks/{id}      actualizar una tarea.               200 + tarea actualizada / 404 si no existe<br>
DELETE           /api/v1/tasks/{id}      eliminar una tarea.                 204 sin contenido / 404 si no existe

---
<br>
Paso 3 - Implementación (autocompletado)<br>
<br>
// Crear el modelo Task con los atributos: id, title, description, status, priority, dueDate, incluir sus tipos de datos más adecuados, añadir constructor vacío y por parámetros, añadir getters y setters<br>
<br>
public class Task {<br>
    private int id;<br>
    private String title;<br>
    private String description;<br>
    private String status;<br>
    private String priority;<br>
    private java.time.LocalDate dueDate;<br>
<br>
    // Constructor vacío<br>
    public Task() {<br>
    }<br>
<br>
    // Constructor por parámetros<br>
    public Task(int id, String title, String description, String status, String priority, java.time.LocalDate dueDate) {<br>
        this.id = id;<br>
        this.title = title;<br>
        this.description = description;<br>
        this.status = status;<br>
        this.priority = priority;<br>
        this.dueDate = dueDate;<br>
    }<br>
<br>
    // Getters y Setters<br>
    public int getId() {<br>
        return id;<br>
    }<br>
<br>
    public void setId(int id) {<br>
        this.id = id;<br>
    }<br>
<br>
    public String getTitle() {<br>
        return title;<br>
    }<br>
<br>
    public void setTitle(String title) {<br>
        this.title = title;<br>
    }<br>
<br>
    public String getDescription() {<br>
        return description;<br>
    }<br>
<br>
    public void setDescription(String description) {<br>
        this.description = description;<br>
    }<br>
<br>
    public String getStatus() {<br>
        return status;<br>
    }<br>
<br>
    public void setStatus(String status) {<br>
        this.status = status;<br>
    }<br>
<br>
    public String getPriority() {<br>
        return priority;<br>
    }<br>
<br>
    public void setPriority(String priority) {<br>
        this.priority = priority;<br>
    }<br>
<br>
    public java.time.LocalDate getDueDate() {<br>
        return dueDate;<br>
    }<br>
<br>
    public void setDueDate(java.time.LocalDate dueDate) {<br>
        this.dueDate = dueDate;<br>
    }<br>
}<br>
<br>
Añadir tareas precargadas en la lista mediante autocompletado:<br>
<br>
// Añadir 2 tareas de ejemplo al ArrayList para poder probar los endpoints -> generado con IA<br>
<br>
    static {<br>
        tasks.add(new Task(1, "Tarea 1", "Descripción de la tarea 1", "Pendiente", "Alta", java.time.LocalDate.of(2024, 6, 30)));<br>
        tasks.add(new Task(2, "Tarea 2", "Descripción de la tarea 2", "En progreso", "Media", java.time.LocalDate.of(2024, 7, 15)));<br>
    }


---
<br>
Primer comentario:<br>
<br>
// El Rest Controller debe implementar el endpoint GET para obtener todas las tareas, utilizar ResponseEntity<br>
<br>
@GetMapping("/tasks")<br>
    public ResponseEntity<Iterable<Task>> getAllTasks() {<br>
        // Implementar la lógica para obtener todas las tareas<br>
        return ResponseEntity.ok().build();<br>
<br>
Incidencia: Se quita ("/tasks") porque lo ha puesto mal la ruta porque se ha comido api/v1/ y es más fácil quitarlo que quitarlo del RequestMapping y luego incluir aquí el api/v1/<br>
Incidencia: Se cambia el return para que devuelva las tasks.

---
<br>
Segundo comentario:<br>
<br>
// El Rest Controller debe implementar el endpoint Get para obtener una tarea por ID, utilizar ResponseEntity<br>
<br>
@GetMapping("/tasks/{id}") (esto es lo que devolvía originalmente el autocompletado, se cambia por ("/{id}"))<br>
    public ResponseEntity<Task> getTaskById(int id) {<br>
        // Implementar la lógica para obtener una tarea por ID<br>
        return ResponseEntity.ok().build();<br>
    }<br>
<br>
Incidencia: Se quita ("/tasks") porque lo ha puesto mal la ruta porque se ha comido api/v1/ y es más fácil quitarlo que quitarlo del RequestMapping y luego incluir aquí el api/v1/, se añade ("/{id}") para que se combine.<br>
Incidencia: Se pone también @PathVariable en int id porque no lo había puesto para decirle que lo coja de la url.<br>
Incidencia: Al pinchar para poner la lógica y recorrer los tasks aparece sola con el autocompletado.<br>
Incidencia: El autocompletado dejaba ResponseEntity.ok().build() si no encontraba la tarea, esto daría un 200. Se cambia por ResponseEntity.notFound().build() para que dé un 404 cuando la tarea no existe.

---
<br>
Tercer comentario:<br>
<br>
// El Rest Controller debe implementar el endpoint POST para crear una tarea, utilizar ResponseEntity<br>
<br>
@PostMapping("/tasks")<br>
    public ResponseEntity<Task> createTask(Task task) {<br>
        // Implementar la lógica para crear una tarea<br>
        return ResponseEntity.ok().build();<br>
    }<br>
<br>
Incidencia: Se quita ("/tasks") porque lo ha puesto mal la ruta porque se ha comido api/v1/ y es más fácil quitarlo que quitarlo del RequestMapping y luego incluir aquí el api/v1/<br>
Incidencia: Se pone también @RequestBody en Task task para decirle que lo coja del cuerpo de la petición.<br>
Incidencia: No pone automático el import del @RequestBody, se pone manual.<br>
Incidencia: Al pinchar en la lógica el autocompletado pone tasks.add(task);<br>
Incidencia: Se cambia el return para que en vez de 200 devuelva 201 + tarea creada.

---
<br>
Cuarto comentario:<br>
<br>
// El Rest Controller debe implementar el endpoint PUT para actualizar una tarea por ID, utilizar ResponseEntity<br>
<br>
@PutMapping("/tasks/{id}") (esto es lo que devolvía originalmente el autocompletado, se cambia por ("/{id}"))<br>
    public ResponseEntity<Task> updateTask(int id, Task task) {<br>
        // Implementar la lógica para actualizar una tarea por ID<br>
        return ResponseEntity.ok().build();<br>
    }<br>
<br>
Incidencia: Se quita ("/tasks") porque lo ha puesto mal la ruta porque se ha comido api/v1/ y es más fácil quitarlo que quitarlo del RequestMapping y luego incluir aquí el api/v1/, se añade ("/{id}") para que se combine.<br>
Incidencia: Se ponen también @PathVariable en int id y @RequestBody en Task task para decirle que lo coja del cuerpo de la petición, aquí al empezar a escribir sí que lo puso sólo el autocompletado.<br>
Incidencia: Al pinchar para poner la lógica y recorrer los tasks aparece sola con el autocompletado.<br>
Incidencia: El autocompletado dejaba ResponseEntity.ok().build() si no encuentra la tarea, lo que da un 200. Se cambia por ResponseEntity.notFound().build() para que de un 404 cuando la tarea que se quiera actualizar no existe.

---
<br>
Quinto comentario:<br>
<br>
// El Rest Controller debe implementar el endpoint DELETE para eliminar una tarea por ID, utilizar ResponseEntity<br>
<br>
@DeleteMapping("/tasks/{id}") (esto es lo que devolvía originalmente el autocompletado, se cambia por ("/{id}"))<br>
    public ResponseEntity<Void> deleteTask(int id) {<br>
        // Implementar la lógica para eliminar una tarea por ID<br>
        return ResponseEntity.ok().build();<br>
    }<br>
<br>
Incidencia: El autocompletado no añade automático el import de @DeteleMapping -> se corrige manual.<br>
Incidencia: Se quita ("/tasks") porque lo ha puesto mal la ruta porque se ha comido api/v1/ y es más fácil quitarlo que quitarlo del RequestMapping y luego incluir aquí el api/v1/, se añade ("/{id}") para que se combine.<br>
Incidencia: Se pone @PathVariable en int id para decirle que lo coja de la url.<br>
Incidencia: Se añaden manualmente también las anotaciones de la clase:<br>
                @RestController<br>
                @RequestMapping ("/api/v1/tasks")<br>
Incidencia: Al pinchar para poner la lógica el autocompletado da esto:<br>
<br>
   for (Task task : tasks) {<br>
            if (task.getId() == id) {<br>
                tasks.remove(task);<br>
                return ResponseEntity.ok().build();<br>
            }<br>
        }<br>
pero no vale porque sería un 200 y tiene que ser 204 sin contenido, se pone así:<br>
<br>
    for (int i = 0; i < tasks.size(); i++) {<br>
            if (tasks.get(i).getId() == id) {<br>
                tasks.remove(i);<br>
                return ResponseEntity.noContent().build();<br>
            }<br>
        }<br>
<br>
return ResponseEntity.notFound().build();<br>
<br>
porque así sí devuelve un 204 de sin contenido.

---
<br>
Paso 4 – Comprensión del código (Ask):<br>
<br>
Pregunta 1:<br>
<br>
¿Qué hace @RequestMapping("/api/v1/tasks") a nivel de clase? ¿Qué ocurriría si lo eliminas?<br>
<br>
Respuesta:<br>
<br>
@RequestMapping("/api/v1/tasks") define una ruta base común para todos los endpoints de TaskController.<br>
<br>
Por tanto, las rutas de los métodos se combinan así:<br>
<br>
@GetMapping → GET /api/v1/tasks<br>
@GetMapping("/{id}") → GET /api/v1/tasks/{id}<br>
@PostMapping → POST /api/v1/tasks<br>
@PutMapping("/{id}") → PUT /api/v1/tasks/{id}<br>
@DeleteMapping("/{id}") → DELETE /api/v1/tasks/{id}<br>
Si se elimina, las rutas perderían ese prefijo:<br>
<br>
GET /<br>
GET /{id}<br>
POST /<br>
PUT /{id}<br>
DELETE /{id}<br>
Esto haría que los endpoints quedasen expuestos en la raíz de la aplicación, provocando posibles conflictos con otros controladores y perdiendo la organización y versionado de la API.

---
<br>
Pregunta 2:<br>
<br>
¿Qué diferencia hay entre @GetMapping y @GetMapping("/{id}")?<br>
<br>
Respuesta:<br>
<br>
La diferencia está en la ruta que atiende cada método:<br>
<br>
@GetMapping<br>
No añade ninguna ruta adicional a la definida en la clase:<br>
<br>
@GetMapping<br>
public ResponseEntity<Iterable<Task>> getAllTasks() {<br>
    // ...<br>
}<br>
<br>
Como la clase tiene @RequestMapping("/api/v1/tasks"), responde a:<br>
<br>
GET /api/v1/tasks<br>
<br>
Se utiliza para obtener la colección completa de tareas.<br>
<br>
@GetMapping("/{id}")<br>
Añade /{id} a la ruta base:<br>
<br>
@GetMapping("/{id}")<br>
public ResponseEntity<Task> getTaskById(@PathVariable int id) {<br>
    // ...<br>
}<br>
<br>
Responde a:<br>
<br>
GET /api/v1/tasks/5<br>
<br>
El valor 5 se recoge mediante @PathVariable int id y permite buscar una tarea concreta.<br>
<br>
En resumen:<br>
<br>
GET /api/v1/tasks → obtiene todas las tareas.<br>
GET /api/v1/tasks/5 → obtiene la tarea cuyo identificador es 5.

---
<br>
Pregunta 3:<br>
<br>
¿Qué hace @PathVariable y qué pasaría si no lo incluyes en el método GET/tasks/{id}?<br>
<br>
Respuesta:<br>
<br>
@PathVariable vincula una variable de la URI con un parámetro del método.<br>
<br>
En este caso:<br>
<br>
@GetMapping("/{id}")<br>
public ResponseEntity<Task> getTaskById(@PathVariable int id) {<br>
    // ...<br>
}<br>
<br>
La petición:<br>
<br>
GET /api/v1/tasks/5<br>
<br>
hace que id tome el valor 5.<br>
<br>
Si eliminas @PathVariable:<br>
<br>
@GetMapping("/{id}")<br>
public ResponseEntity<Task> getTaskById(int id) {<br>
    // ...<br>
}<br>
<br>
Spring ya no sabrá que id debe obtenerse de la ruta. Normalmente intentará obtenerlo de otro lugar, como un parámetro de consulta, y la petición /api/v1/tasks/5 fallará, habitualmente con un 400 Bad Request.<br>
<br>
También puede escribirse de forma explícita:<br>
<br>
public ResponseEntity<Task> getTaskById(@PathVariable("id") int id)<br>
<br>
En resumen, @PathVariable es necesario para extraer el identificador incluido en la URI.

---
<br>
Pregunta 4:<br>
<br>
¿Por qué el método POST devuelve ResponseEntity.status(HttpStatus.CREATED) y no ResponseEntity.ok()?<br>
<br>
Respuesta:<br>
<br>
El método POST devuelve:<br>
<br>
ResponseEntity.status(HttpStatus.CREATED).body(task)<br>
<br>
porque POST /api/v1/tasks crea una nueva tarea. El código 201 Created indica explícitamente que el recurso se ha creado correctamente.<br>
<br>
ResponseEntity.ok() devolvería 200 OK, que significa que la petición se procesó correctamente, pero no comunica de forma tan precisa que se creó un recurso nuevo.<br>
<br>
También podría incluirse la ubicación del recurso creado mediante la cabecera Location, por ejemplo:<br>
<br>
HTTP/1.1 201 Created<br>
Location: /api/v1/tasks/1<br>
<br>
En resumen:<br>
<br>
201 Created: recomendado cuando un POST crea un recurso.<br>
200 OK: válido si la operación tiene éxito, pero es menos específico.<br>
204 No Content: éxito sin devolver contenido.

---
<br>
Pregunta 5:<br>
<br>
¿Qué hace @RequestBody y qué ocurre si envías un JSON mal formado sin él?<br>
<br>
Respuesta:<br>
<br>
@RequestBody indica a Spring que debe leer el cuerpo de la petición y convertir el JSON recibido en un objeto Java.<br>
<br>
En tu código:<br>
<br>
@PostMapping<br>
public ResponseEntity<Task> createTask(@RequestBody Task task) {<br>
    // ...<br>
}<br>
<br>
Un JSON como este se convierte en un objeto Task:<br>
<br>
{<br>
  "id": 1,<br>
  "title": "Comprar leche"<br>
}<br>
<br>
Si el JSON está mal formado, Spring no puede deserializarlo y normalmente devuelve:<br>
<br>
400 Bad Request<br>
<br>
Si eliminas @RequestBody, Spring no tratará el contenido como JSON del cuerpo. Intentará resolver Task como un modelo mediante parámetros de la petición, por lo que el JSON puede ignorarse y sus propiedades quedarían con valores null o predeterminados. Además, un JSON mal formado normalmente no produciría el error de conversión esperado, porque Spring no estaría intentando interpretarlo como cuerpo JSON.

---
<br>
Paso 5 – Validación en Postman:<br>

Get -> Recuperar todas las tareas<br>
<br>
![get](../capturas/Captura-01-creacion-del-controller-postman-1.png)<br>
<br>
Get por Id -> Recuperar 1 tarea por su Id<br>
<br>
![getId](../capturas/Captura-01-creacion-del-controller-postman-2.png)<br>
<br>
Get por Id -> Tarea inexistente<br>
<br>
![getIdInexistente](../capturas/Captura-01-creacion-del-controller-postman-3.png)<br>
<br>
Post -> Enviar una tarea<br>
<br>
![post](../capturas/Captura-01-creacion-del-controller-postman-4.png)<br>
<br>
Put -> Cambiar algo en una tarea<br>
<br>
![put](../capturas/Captura-01-creacion-del-controller-postman-5.png)<br>
<br>
Put -> Cambiar algo en tarea inexistente<br>
<br>
![putInexistente](../capturas/Captura-01-creacion-del-controller-postman-6.png)<br>
<br>
Delete -> Borrar una tarea<br>
<br>
![delete](../capturas/Captura-01-creacion-del-controller-postman-7.png)<br>
<br>
Delete -> Borrar una tarea inexistente<br>
<br>
![deleteInexistente](../capturas/Captura-01-creacion-del-controller-postman-8.png)

---
<br>
Reflexión:<br>
<br>
Pregunta 1:<br>
<br>
¿Qué responsabilidades tiene el Controller y cuáles NO debe tener?<br>
<br>
Respuesta:<br>
<br>
Es el punto de entrada de las peticiones HTTP, obtiene los parámetros y los datos del cuerpo de la petición y conecta con el service.<br>
Lo que no debe de tener es la lógica de negocio ni el acceso a BBDD.<br>
<br>
<br>
<br>
Pregunta 2:<br>
<br>
¿Qué problema tiene tener los datos en una lista dentro del Controller? ¿Qué pasaría si reinicias la aplicación?<br>
<br>
Respuesta:<br>
<br>
El problema es que se pierden los datos al reiniciar y lo que pasaría es eso, que se pierden porque no hay persistencia.<br>
<br>
<br>
<br>
Pregunta 3:<br>
<br>
¿Qué mejorarías en el diseño actual pensando en los ejercicios siguientes?<br>
<br>
Respuesta:<br>
<br>
Sustituir la lista del Controller por una BBDD H2 para que no se pierdan los datos.<br>