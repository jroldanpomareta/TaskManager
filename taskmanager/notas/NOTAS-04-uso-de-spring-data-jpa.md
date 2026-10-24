PARTE 1<br>
<br>
Ejercicio de creación de la Capa Repository y CRUD completo<br>
<br>
Paso 1 - Preparación (Ask):<br>
<br>
Pregunta:<br>
<br>
¿Qué operaciones CRUD proporciona JpaRepository automáticamente y qué ocurre internamente cuando llamamos a cada una?<br>
<br>
Respuesta:<br>
<br>
JpaRepository extiende a PagingAndSortingRepository, que a su vez extiende a CrudRepository.
Con ello, automáticamente dispone de los siguientes métodos CRUD:

![explicacionJpaRepository](..\capturas\Captura-04-explicacion-JpaRepository.png)
<br>
Qué ocurre internamente<br>
<br>
Contexto transaccional – Todos estos métodos se ejecutan dentro de una transacción gestionada por Spring (a menos que el llamador la desactive).<br>
Contexto de persistencia – EntityManager mantiene una caché de primer nivel. Los métodos save/merge añaden o actualizan entidades en esta caché; los métodos find la rellenan.<br>
Generación de SQL – Hibernate (o el proveedor JPA) traduce las llamadas a sentencias SQL (INSERT, SELECT, UPDATE, DELETE).<br>
Flush – Al finalizar la transacción, el contexto se hace flush y las sentencias pendientes se envían a la base de datos.<br>
Carga perezosa – Las relaciones marcadas como lazy no se recuperan hasta que se accede a ellas; find devuelve proxies que cargan los datos bajo demanda.<br>
En resumen, JpaRepository te ofrece métodos CRUD listos para usar, mientras que el proveedor JPA se encarga de generar y ejecutar el SQL, gestionar la transacción y la caché de forma transparente.

---

Pregunta:<br>
<br>
¿Cómo funcionan los query methods de Spring Data JPA por nombre de método? Pon ejemplos con findByStatus, findByPriority y findByStatusAndPriority.<br>
<br>
Respuesta:<br>
<br>
Los query methods de Spring Data JPA se construyen a partir del nombre del método que declaras en tu repositorio.<br>
Spring analiza el nombre, extrae las palabras clave (find, read, query, count, exists, delete, remove) y la parte que sigue (By…) para generar la consulta JPQL automáticamente.<br>
<br>
Sintaxis básica

```
findBy<Propiedad>[And|Or<Propiedad>…]

<Propiedad> debe coincidir con un atributo de la entidad (o con un atributo anidado usando __).
Se pueden combinar varias condiciones con And o Or.
Se pueden usar operadores de comparación (GreaterThan, LessThan, Like, In, Between, etc.).
El método puede devolver T, List<T>, Page<T>, Optional<T>, Long (para count), etc.
Ejemplos concretos
Supongamos una entidad Ticket:
```

```
@Entity
public class Ticket {
    @Id @GeneratedValue
    private Long id;

    private String status;      // e.g. "OPEN", "CLOSED"
    private Integer priority;  // 1‑5
    // getters / setters
}
```
Y un repositorio:

```
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    // 1. findByStatus
    List<Ticket> findByStatus(String status);

    // 2. findByPriority
    List<Ticket> findByPriority(Integer priority);

    // 3. findByStatusAndPriority
    List<Ticket> findByStatusAndPriority(String status, Integer priority);
}
```
```
1. findByStatus
Firma: List<Ticket> findByStatus(String status);
Qué hace: Busca todos los tickets cuyo atributo status coincide exactamente con el valor pasado.
JPQL generado: SELECT t FROM Ticket t WHERE t.status = ?1
2. findByPriority
Firma: List<Ticket> findByPriority(Integer priority);
Qué hace: Devuelve todos los tickets con la prioridad indicada.
JPQL generado: SELECT t FROM Ticket t WHERE t.priority = ?1
3. findByStatusAndPriority
Firma: List<Ticket> findByStatusAndPriority(String status, Integer priority);
Qué hace: Devuelve los tickets que cumplen ambas condiciones: status igual al primer parámetro y priority igual al segundo.
JPQL generado: SELECT t FROM Ticket t WHERE t.status = ?1 AND t.priority = ?2
```

Más ejemplos de operadores
![explicacionQueryMethods](..\capturas\Captura-04-explicacion-query-methods.png)
<br>
Notas prácticas
```
Nombres de propiedades: deben coincidir con los nombres de los campos de la entidad (case‑insensitive).
Anidado: findByOwner_Name(String name) busca ticket.owner.name.
Return types:
T → devuelve la primera coincidencia.
Optional<T> → devuelve Optional.empty() si no hay coincidencias.
List<T> → devuelve todas las coincidencias.
Page<T> → devuelve una página con Pageable.
Transacciones: los métodos de lectura (find…) se ejecutan en modo readOnly por defecto, pero puedes anotar el método con @Transactional(readOnly = true) si lo necesitas explícitamente.
Con estos principios puedes crear consultas complejas sin escribir JPQL ni SQL, simplemente declarando el método con el nombre correcto.
```
---
<br>
Pregunta:<br>
<br>
¿Qué diferencia hay entre un método CRUD heredado de JpaRepository y un query method definido por nosotros?<br>
<br>
Respuesta:<br>
<br>
Los métodos CRUD que vienen con JpaRepository (por ejemplo save, findById, deleteById, etc.) son implementaciones genéricas que Spring Data genera automáticamente para cualquier entidad que extienda JpaRepository.<br>
<br>
Qué hacen: ejecutan la operación básica sobre la tabla correspondiente usando la configuración estándar del proveedor JPA (persist, merge, find, delete).<br>
Qué no hacen: no tienen lógica de negocio ni filtros adicionales; simplemente operan sobre la entidad tal cual está definida.<br>
Un query method que tú defines en tu repositorio (por ejemplo findByStatusAndPriority) es también generado automáticamente por Spring Data, pero con una semántica específica:<br>
<br>
Qué hacen: construyen una consulta JPQL basada en el nombre del método y los parámetros que le pasas.<br>
Qué no hacen: no son operaciones CRUD genéricas; están diseñadas para filtrar, ordenar o contar según criterios particulares.<br>
Resumen de diferencias clave

![explicacionDiferencia](..\capturas\Captura-04-explicacion-diferencia.png)
<<br>
En práctica, usas los CRUD heredados cuando solo necesitas la operación básica y los query methods cuando necesitas filtrar, ordenar o contar según criterios específicos. Ambos son generados por Spring Data, pero el query method te permite expresar la lógica de negocio directamente en la firma del método.

---
<br>
Pregunta:<br>
<br>
¿Qué ocurre cuando usamos findById y no existe ninguna entidad con ese identificador?<br>
<br>
Respuesta:<br>

```
findById(ID id) devuelve un Optional<T>.
Si no existe ninguna entidad con ese identificador:

El método devuelve Optional.empty() – no lanza ninguna excepción y la transacción se completa normalmente.
Si el código llama a optional.get() sobre ese Optional.empty(), se lanzará una NoSuchElementException.
Puedes evitar la excepción usando:
optional.orElse(null) → devuelve null si no se encuentra.
optional.orElseThrow(() -> new EntityNotFoundException(...)) → lanza una excepción personalizada.
optional.ifPresent(...) → ejecuta lógica solo cuando la entidad existe.
En resumen, la ausencia de la entidad se representa con un Optional.empty(); el comportamiento exacto depende de cómo el llamador maneje ese Optional.
```

---
Paso 2 - Diseño previo:<br>
<br>
Pregunta:<br>
<br>
¿Qué interface vas a crear y de qué extiende?<br>
<br>
Respuesta:<br>
<br>
Voy a crear la interfaz TaskRepository que extienda de JpaRepository, porque así Spring Data me dará automáticamente las operaciones CRUD básicas para Task

---
<br>
Pregunta:<br>
<br>
¿Qué métodos adicionales necesitas más allá de los CRUD automáticos?<br>
<br>
Respuesta:<br>
<br>
Necesito métodos para buscar tareas por el status, por la priority y por las dos cosas a la vez.<br>
Serían findByStatus, findByPriority y findByStatusAndPriority.<br>
Spring Data debería poder crear estas consultas automáticamente a partir del nombre de los métodos

---
<br>
Pregunta:<br>
<br>
¿Qué cambios necesita TaskServiceImpl para usar el Repository en lugar de la lista en memoria?<br>
<br>
Respuesta:<br>
<br>
Tengo que sustituir la lista de tareas por una dependencia de TaskRepository y cambiar los métodos del Service para que utilicen el Repository.<br>
Los métodos de buscar usarán las consultas del Repository y los métodos de crear, actualizar y borrar utilizarán las operaciones del JpaRepository.<br>
También tengo que adaptar el findById para el caso posible de que no exista la tarea.
---
<br>
Pregunta:<br>
<br>
¿Qué comentario exacto escribirás para que el autocompletado lo genere?<br>
<br>
Respuesta:<br>
<br>
// Crear TaskRepository extendiendo JpaRepository y añadir los métodos findByStatus, findByPriority y findByStatusAndPriority para realizar búsquedas por estos campos. -> generado por IA

---
Paso 3 - Implementación (autocompletado):<br>
<br>
Crea TaskRepository.java con un comentario para los 3 query methods (findByStatus, findByPriority, findByStatusAndPriority).<br>
<br>
Comentario:<br>
<br>
// Crear TaskRepository extendiendo JpaRepository y añadir los métodos findByStatus, findByPriority y findByStatusAndPriority para realizar búsquedas por estos campos -> generado por IA<br>
<br>
Resultado:

```
package com.accenture.taskmanager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.accenture.taskmanager.model.Task;

// Crear TaskRepository extendiendo JpaRepository y añadir los métodos findByStatus, findByPriority y findByStatusAndPriority para realizar búsquedas por estos campos -> generado por IA
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByStatus(String status);
    List<Task> findByPriority(String priority);
    List<Task> findByStatusAndPriority(String status, String priority);
    

}
```
---
<br>
En la interfaz TaskService, añade los 2 métodos nuevos con un comentario.<br>
<br>
Comentario:<br>
<br>
// Añadir los métodos findByStatus y findByPriority para buscar tareas por estado y prioridad, respectivamente. -> generado por IA<br>
<br>
Resultado: (los javadocs ya los puso él solo)

```
    /**
     * Busca tareas por su estado.
     *
     * @param status estado de las tareas que se desean buscar.
     * @return iterable con las tareas encontradas.
     */
    Iterable<Task> findByStatus(String status);

    /**
     * Busca tareas por su prioridad.
     *
     * @param priority prioridad de las tareas que se desean buscar.
     * @return iterable con las tareas encontradas.
     */
    Iterable<Task> findByPriority(String priority);
```

---
<br>
En Task, añade con un comentario un método con @PrePersist que asigne createdAt = LocalDateTime.now() si viene vacío: ahora la tarea se guarda en MySQL y nadie más rellena esa fecha.<br>
<br>
Comentario:<br>
<br>
// Añadir un método con @PrePersist que asigne LocalDateTime.now() a createdAt únicamente si su valor es null. -> generado por IA<br>
<br>
Resultado:

```
// Añadir un método con @PrePersist que asigne LocalDateTime.now() a createdAt únicamente si su valor es null. -> generado por IA
    @PrePersist
    private void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = java.time.LocalDateTime.now();
        }
    }
```

---
<br>
En TaskServiceImpl, comenta cada método que usa la lista en memoria, escribe debajo el comentario pidiendo que use el Repository (inyectado por constructor, con Optional <T>; si findById() devuelve vacío, lanza la misma excepción de "tarea no encontrada" que ya usabas en el Service), autocompleta uno a uno y borra lo comentado. Es la reescritura más grande de este sub-ejercicio: casi toda la clase cambia.<br>
<br>
Comentario:<br>
<br>
// Sustituir la lista estática por una dependencia de TaskRepository inyectada mediante constructor. -> generado por IA<br>
<br>
Resultado:

```
private final TaskRepository taskRepository;
   //private static final List<Task> tasks = new ArrayList<>();
```

Comentario:<br>
<br>
// Crear el constructor que reciba TaskRepository y lo asigne al atributo taskRepository. -> generado por IA<br>
<br>
Resultado:

```
// Crear el constructor que reciba TaskRepository y lo asigne al atributo taskRepository. -> generado por IA
    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }
```

Comentario del findAll:<br>
<br>
// Devolver todas las tareas utilizando taskRepository.findAll(). -> generado por IA<br>
<br>
//actualiza el javadoc<br>
<br>
Resultado:<br>
<br>

```
    //actualiza el javadoc
    /**
     * Devuelve todas las tareas almacenadas.
     *
     * @return un Iterable con todas las tareas.
     */
    @Override
    public Iterable<Task> findAll() {
        // Lógica para encontrar una tarea por su ID
        // Obtener todas las tareas y devolverlas como un Iterable<Task> -> generado con
        // IA
        // Devolver todas las tareas utilizando taskRepository.findAll(). -> generado por IA
        return taskRepository.findAll();
    }
```

Comentario del findById:<br>
<br>
// Buscar la tarea por ID usando taskRepository y lanzar TaskNotFoundException si no existe. -> generado por IA<br>
<br>
//actualiza el javadoc<br>
<br>
Resultado:<br>
<br>

```
    //actualiza el javadoc
    /**
     * Busca una tarea por su identificador.
     *
     * @param id identificador de la tarea que se desea buscar.
     * @return la tarea encontrada.
     * @throws TaskNotFoundException si no existe una tarea con el identificador
     *                               indicado.
     */
    @Override
    public Task findById(Long id) {
        // Lógica para encontrar una tarea por su ID
        // Buscar la tarea en la lista de tareas por su ID y devolverla -> generado con
        // IA
        // Buscar la tarea por ID usando taskRepository y lanzar TaskNotFoundException si no existe. -> generado por IA
        return taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException("La tarea con ID " + id + " no existe."));
    }
```

Comentario del create:<br>
<br>
// Guardar la tarea utilizando taskRepository y devolver la tarea guardada. -> generado por IA<br>
<br>
//actualiza el javadoc<br>
<br>
Resultado:<br>
<br>

```
//actualiza el javadoc
    /**
     * Crea una nueva tarea.
     *
     * @param task objeto Task que se desea crear.
     * @return la tarea creada.
     */
    @Override
    public Task create(Task task) {
        // Lógica para crear una nueva tarea
        // Añadir la tarea nueva a la lista de tareas y devolverla en el return ->
        // generado con IA
        // Guardar la tarea utilizando taskRepository y devolver la tarea guardada. -> generado por IA
        return taskRepository.save(task);
    }
```

Comentario del update:<br>
<br>
// Actualizar una tarea existente utilizando taskRepository: buscarla por ID y, si existe, actualizar title, description, status, priority, dueDate y categoryId con los valores recibidos en task, conservando createdAt. Guardar y devolver la tarea actualizada. Si no existe, lanzar TaskNotFoundException. -> generado por IA<br>
<br>
//actualiza el javadoc
<br>
Resultado:<br>
<br>

```
//actualiza el javadoc
    /**
     * Actualiza una tarea existente con los datos proporcionados.
     *
     * @param id   identificador de la tarea que se desea actualizar.
     * @param task objeto Task con los nuevos datos para actualizar la tarea.
     * @return la tarea actualizada.
     * @throws TaskNotFoundException si no existe una tarea con el identificador
     *                               indicado.
     */
    @Override
    public Task update(Long id, Task task) {
        // Lógica para actualizar una tarea existente
        // Buscar la tarea en la lista de tareas por su ID y actualizar sus campos con
        // los valores del objeto task -> generado con IA
        // Actualizar una tarea existente utilizando taskRepository: buscarla por ID y, si existe, actualizar title, description, status, priority, dueDate y categoryId con los valores recibidos en task, conservando createdAt. Guardar y devolver la tarea actualizada. Si no existe, lanzar TaskNotFoundException. -> generado por IA
        Task existingTask = findById(id);
        existingTask.setTitle(task.getTitle());
        existingTask.setDescription(task.getDescription());
        existingTask.setStatus(task.getStatus());
        existingTask.setPriority(task.getPriority());
        existingTask.setDueDate(task.getDueDate());
        existingTask.setCategoryId(task.getCategoryId());
        return taskRepository.save(existingTask);
    }
```

Comentario del delete:<br>
<br>
// Eliminar la tarea utilizando taskRepository: buscarla por ID y comprobar que existe mediante findById. Si no existe, lanzar TaskNotFoundException. Si existe, eliminarla utilizando taskRepository.delete(taskToDelete). -> generado por IA<br>
<br>
//actualiza el javadoc
<br>
Resultado:

```
//actualiza el javadoc
    /**
     * Elimina una tarea por su identificador.
     *
     * @param id identificador de la tarea que se desea eliminar.
     * @throws TaskNotFoundException si no existe una tarea con el identificador
     *                               indicado.
     */
    @Override
    public void delete(Long id) {
        // Lógica para eliminar una tarea por su ID
        // Eliminar una tarea de la lista de tareas por su ID, comprobar antes si la
        // tarea existe y si no existe lanzar una excepción -> generado con IA
        // Cambia la excepción lanzada a TaskNotFoundException para indicar que la tarea
        // no existe
        // Eliminar la tarea utilizando taskRepository: buscarla por ID y comprobar que existe mediante findById. Si no existe, lanzar TaskNotFoundException. Si existe, eliminarla utilizando taskRepository.delete(taskToDelete). -> generado por IA
        Task taskToDelete = findById(id);
        taskRepository.delete(taskToDelete);

    }
```

Se ponen los 2 métodos que faltan de la interfaz y se ajustan los tipos:<br>
<br>
Comentarios:

```
    //pon los métodos que faltan de la interfaz TaskService, que son findByStatus y findByPriority, implementados con taskRepository. -> generado por IA
    // Corregir la implementación para que coincida con la interfaz TaskService: el método findByStatus debe recibir un String status, devolver Iterable<Task> y utilizar taskRepository.findByStatus(status) para obtener las tareas. -> generado por IA
    // Corregir la implementación para que coincida con la interfaz TaskService: el método findByPriority debe recibir un String priority, devolver Iterable<Task> y utilizar taskRepository.findByPriority(priority) para obtener las tareas. -> generado por IA
    // Corregir findByPriority para que coincida con la firma real de TaskRepository.findByPriority. Revisar el tipo de parámetro que espera el repositorio y adaptar la implementación manteniendo la firma String priority exigida por TaskService. -> generado por IA
    // corrige los métodos para que utilicen los tipos correctos que son enums en status y priority como en la interfaz. -> generado por IA
```
Resultado:

```
    //pon los métodos que faltan de la interfaz TaskService, que son findByStatus y findByPriority, implementados con taskRepository. -> generado por IA
    // Corregir la implementación para que coincida con la interfaz TaskService: el método findByStatus debe recibir un String status, devolver Iterable<Task> y utilizar taskRepository.findByStatus(status) para obtener las tareas. -> generado por IA
    // Corregir la implementación para que coincida con la interfaz TaskService: el método findByPriority debe recibir un String priority, devolver Iterable<Task> y utilizar taskRepository.findByPriority(priority) para obtener las tareas. -> generado por IA
    // Corregir findByPriority para que coincida con la firma real de TaskRepository.findByPriority. Revisar el tipo de parámetro que espera el repositorio y adaptar la implementación manteniendo la firma String priority exigida por TaskService. -> generado por IA
    // corrige los métodos para que utilicen los tipos correctos que son enums en status y priority como en la interfaz. -> generado por IA
    @Override
    public Iterable<Task> findByStatus(String status) {
        return taskRepository.findByStatus(TaskStatus.valueOf(status));
    }

    @Override
    public Iterable<Task> findByPriority(String priority) {
        TaskPriority taskPriority = TaskPriority.valueOf(priority);
        return taskRepository.findByPriority(taskPriority);
    }
```

---
<br>
Paso 4 - Comprensión del código (Ask):<br>
<br>
Pregunta:<br>
<br>
¿Por qué TaskRepository es una interface y no una clase? ¿Quién proporciona la implementación?<br>
<br>
Respuesta:<br>
<br>
TaskRepository es una interfaz porque no necesitas escribir manualmente la lógica de acceso a la base de datos. En TaskRepository.java solo se declara el contrato:<br>
<br>

```
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByStatus(TaskStatus status);
    List<Task> findByPriority(TaskPriority priority);
    List<Task> findByStatusAndPriority(
        TaskStatus status,
        TaskPriority priority
    );
}
```
¿Qué aporta JpaRepository?<br>
Al extender JpaRepository<Task, Long>, el repositorio hereda operaciones como:

```
save(task);
findById(id);
findAll();
deleteById(id);
existsById(id);
```
El primer parámetro, Task, indica la entidad que se persiste, y Long indica el tipo de su identificador.<br>
<br>
Además, Spring Data interpreta los nombres de los métodos propios:

```
findByStatus(...)
findByPriority(...)
findByStatusAndPriority(...)
```
y genera automáticamente las consultas correspondientes.<br>
<br>
¿Quién proporciona la implementación?<br>
La implementación la proporciona Spring Data JPA en tiempo de ejecución. Al arrancar la aplicación:<br>
<br>
Spring detecta la interfaz TaskRepository.<br>
La infraestructura de Spring Data crea un objeto repositorio mediante una fábrica de repositorios.<br>
Ese objeto suele ser un proxy dinámico respaldado por una implementación base como SimpleJpaRepository.<br>
Spring registra ese objeto como un bean.<br>
Cuando TaskServiceImpl recibe TaskRepository por constructor, Spring inyecta ese bean automáticamente, como se ve en TaskServiceImpl.java.<br>
Por tanto, no existe necesariamente una clase escrita por ti como:

```
class TaskRepositoryImpl implements TaskRepository {
    // ...
}
```
Spring la construye internamente.<br>
<br>
La dependencia que activa esta funcionalidad es spring-boot-starter-data-jpa, declarada en pom.xml.<br>
<br>
¿Qué papel tiene Hibernate?<br>
Spring Data JPA decide cómo exponer el repositorio y construir las consultas. Después utiliza JPA, normalmente mediante Hibernate, para:<br>
<br>
convertir las operaciones en SQL;<br>
gestionar el EntityManager;<br>
leer y guardar entidades;<br>
ejecutar las consultas contra MySQL.<br>
El flujo simplificado es:

```
TaskServiceImpl
      ↓
TaskRepository (interfaz)
      ↓
Proxy generado por Spring Data JPA
      ↓
SimpleJpaRepository / EntityManager
      ↓
Hibernate
      ↓
MySQL
```
La interfaz permite separar el contrato de la implementación y evita acoplar el servicio a detalles de SQL, Hibernate o la gestión de conexiones. Esto es precisamente lo que resume la nota del proyecto en NOTAS-04-uso-de-spring-data-jpa.md.<br>
<br>
---
<br>
Pregunta:<br>
<br>
¿Cómo interpreta Spring Data el método findByStatusAndPriority()? ¿Qué query SQL genera?<br>
<br>
Respuesta:<br>
<br>
En TaskRepository.java, el método es:

```
List<Task> findByStatusAndPriority(
    TaskStatus status,
    TaskPriority priority
);
```
Spring Data analiza el nombre siguiendo esta estructura:

```
find By Status And Priority
│    │  │      │   │
│    │  │      │   └── segunda propiedad: priority
│    │  │      └────── operador lógico AND
│    │  └───────────── primera propiedad: status
│    └──────────────── indica una búsqueda
└──────────────────── devuelve entidades Task
```
Por tanto, equivale conceptualmente a:<br>
Buscar las entidades Task cuyo status sea igual al primer parámetro
y cuyo priority sea igual al segundo parámetro.<br>
<br>
La consulta JPQL equivalente sería:

```
SELECT t
FROM Task t
WHERE t.status = :status
  AND t.priority = :priority
```
Spring Data crea internamente esa consulta a partir del nombre del método. No es necesario escribir @Query.<br>
<br>
¿Qué SQL genera?<br>
Hibernate traduce la consulta JPQL al SQL correspondiente a la tabla configurada en Task.java. Como la entidad usa:

```
@Table(name = "task")
```
y los campos status y priority no tienen un @Column explícito, normalmente el SQL será equivalente a:

```
SELECT
    t.id,
    t.title,
    t.description,
    t.status,
    t.priority,
    t.due_date,
    t.created_at,
    t.category_id
FROM task t
WHERE t.status = ?
  AND t.priority = ?;
```
Los valores reales se envían como parámetros separados, no concatenados directamente en el SQL:

```
primer parámetro  → status
segundo parámetro → priority
```
Por ejemplo, si se invoca:

```
taskRepository.findByStatusAndPriority(
    TaskStatus.IN_PROGRESS,
    TaskPriority.HIGH
);
```
Hibernate ejecutará conceptualmente:

```
SELECT ...
FROM task
WHERE status = 'IN_PROGRESS'
  AND priority = 'HIGH';
```
Sin embargo, en los logs normalmente aparecerá con ?:

```
select
    t1_0.id,
    t1_0.title,
    t1_0.description,
    t1_0.status,
    t1_0.priority,
    t1_0.due_date,
    t1_0.created_at,
    t1_0.category_id
from
    task t1_0
where
    t1_0.status=?
    and t1_0.priority=?
```
Los nombres de alias (t1_0), el orden de las columnas y el formato exacto pueden variar según la versión de Hibernate y el dialecto de MySQL.<br>
<br>
¿Por qué se guardan los enums como texto?<br>
En Task.java y Task.java se utiliza:

```
@Enumerated(EnumType.STRING)
private TaskStatus status;

@Enumerated(EnumType.STRING)
private TaskPriority priority;
```
Por eso, TaskStatus.IN_PROGRESS se convierte en el texto IN_PROGRESS, y TaskPriority.HIGH en HIGH. La búsqueda compara esos valores con las columnas status y priority.<br>
<br>
La propiedad spring.jpa.show-sql=true de application.properties permite ver la consulta SQL en los logs de la aplicación. Los valores de los parámetros pueden aparecer en logs adicionales de Hibernate, dependiendo de la configuración de logging.

---
<br>
Pregunta:<br>
<br>
¿Qué diferencia hay entre findById() que devuelve Optional<Task> y getReferenceById() que devuelve Task directamente?<br>
<br>
Respuesta:<br>
<br>
La diferencia principal es cuándo se comprueba si existe la entidad y qué ocurre cuando no existe.<br>
<br>
findById()<br>
En TaskServiceImpl.java se utiliza así:

```
return taskRepository.findById(id)
    .orElseThrow(() ->
        new TaskNotFoundException(
            "La tarea con ID " + id + " no existe."
        )
    );
```
Su firma es:

```
Optional<Task> findById(Long id);
```
Normalmente:<br>
<br>
Ejecuta inmediatamente una consulta SELECT.<br>
Si encuentra la fila, devuelve Optional<Task> con la entidad.<br>
Si no la encuentra, devuelve Optional.empty().<br>
No lanza automáticamente una excepción por ausencia.<br>
SQL aproximado:

```
SELECT
    id,
    title,
    description,
    status,
    priority,
    due_date,
    created_at,
    category_id
FROM task
WHERE id = ?;
```
Es la opción adecuada cuando necesitas saber si el registro existe, mostrar sus datos o devolver un 404 Not Found. En este proyecto, el servicio transforma la ausencia en TaskNotFoundException.
<br>
getReferenceById()<br>
Su firma es:

```
Task getReferenceById(Long id);
```
Este método normalmente no carga la entidad inmediatamente. Devuelve una referencia gestionada por JPA, habitualmente un proxy de Hibernate:

```
Task task = taskRepository.getReferenceById(id);
```
En ese momento puede que todavía no se haya ejecutado ningún SELECT. La consulta se ejecuta cuando se intenta acceder a los datos de la entidad:

```
Task task = taskRepository.getReferenceById(10L);

// Aquí Hibernate puede ejecutar el SELECT:
String title = task.getTitle();
```
Si el registro con ese identificador no existe, la excepción suele producirse al inicializar o utilizar el proxy, normalmente como:

```
EntityNotFoundException
```
Por tanto, que el método devuelva Task directamente no significa que la tarea exista ya. Puede ser solamente una referencia diferida.<br>
<br>
Comparación

![Captura-parte1-paso4](..\capturas\Captura-04-pregunta-ask-parte1-paso4.png)
<br>
Ejemplo de uso de getReferenceById()<br>
Es útil cuando solo necesitas asociar una entidad existente y no necesitas leer sus campos:

```
Task task = taskRepository.getReferenceById(taskId);
comment.setTask(task);
commentRepository.save(comment);
```
En ese caso, Hibernate puede utilizar el identificador de task como clave foránea sin cargar toda la fila de Task.<br>
<br>
¿Cuál conviene usar aquí?<br>
Para este proyecto, findById() es más apropiado porque:<br>
<br>
el controlador necesita devolver la tarea;<br>
el servicio debe distinguir entre una tarea existente e inexistente;<br>
se quiere lanzar explícitamente TaskNotFoundException;<br><br>
las operaciones update y delete deben devolver un error controlado si el ID no existe.
getReferenceById() sería más apropiado para una referencia diferida, especialmente al establecer relaciones entre entidades. No debería utilizarse como sustituto de findById() cuando se necesita validar inmediatamente la existencia de la tarea.

---
<br>
Pregunta:<br>
<br>
¿Qué hace save() cuando la entidad ya tiene id? ¿Y cuando no tiene id?<br>
<br>
Respuesta:<br>
<br>
En este proyecto, save() pertenece a JpaRepository y delega en Spring Data JPA. Su comportamiento depende de si la entidad se considera nueva o existente.<br>
<br>
En Task.java, el identificador se genera automáticamente:

```
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
```
Cuando no tiene id<br>
Por ejemplo:

```
Task task = new Task();
task.setTitle("Nueva tarea");

Task savedTask = taskRepository.save(task);
```
Como id == null, Spring Data considera que la entidad es nueva y utiliza internamente:

```
entityManager.persist(task);
```
El resultado habitual es un INSERT:

```
INSERT INTO task
    (title, description, status, priority, due_date, created_at, category_id)
VALUES
    (?, ?, ?, ?, ?, ?, ?);
```
Después de insertar, la base de datos genera el identificador y JPA lo asigna al objeto:

```
task.getId(); // por ejemplo, 15L
```
Por eso, en el método create() de TaskServiceImpl.java, lo normal es llamar a:

```
return taskRepository.save(task);
```
Cuando ya tiene id<br>
Por ejemplo:

```
Task task = new Task();
task.setId(15L);
task.setTitle("Título actualizado");

Task savedTask = taskRepository.save(task);
```
Spring Data considera normalmente que la entidad no es nueva y utiliza:

```
entityManager.merge(task);
```
merge() copia los valores del objeto recibido en una instancia gestionada por JPA. No convierte necesariamente el objeto original en gestionado; por eso conviene utilizar el objeto devuelto:

```
Task managedTask = taskRepository.save(task);
```
Si el registro con ese ID existe, Hibernate genera un UPDATE parecido a:

```
UPDATE task
SET
    title = ?,
    description = ?,
    status = ?,
    priority = ?,
    due_date = ?,
    created_at = ?,
    category_id = ?
WHERE id = ?;
```
En el método update() del proyecto, primero se busca la tarea y después se modifican sus propiedades antes de guardarla, como se muestra en TaskServiceImpl.java.<br>
<br>
Importante: tener un id no garantiza que exista una fila<br>
Esta llamada:

```
Task task = new Task();
task.setId(999L);
taskRepository.save(task);
```
no comprueba por sí sola que 999L exista en la base de datos.<br>
<br>
En términos generales, merge() busca o intenta asociar la entidad con ese identificador. Si no existe una fila correspondiente, el proveedor JPA puede terminar haciendo un INSERT con ese ID, dependiendo de la estrategia y del estado de la entidad. Por eso no se debe utilizar save() como comprobación de existencia.<br>
<br>
Para actualizar de forma segura, el servicio de este proyecto hace primero:

```
Task existingTask = findById(id);
```
y findById() lanza TaskNotFoundException si no encuentra la tarea. Solo después se modifican sus campos y se llama a save().<br>
<br>
Resumen<br>

![Captura-parte1-paso4b](..\capturas\Captura-04-pregunta-ask-parte1-paso4b.png)

<br>
Además, save() no siempre ejecuta el SQL inmediatamente. JPA puede esperar hasta el flush o hasta el final de la transacción para enviar el INSERT o UPDATE a la base de datos.

---
<br>
Pregunta:<br>
<br>
¿Qué pasaría si llamas a deleteById() con un id que no existe?<br>
<br>
Respuesta:<br>
<br>
Si llamas directamente a:

```
taskRepository.deleteById(id);
```
con un id que no existe, Spring Data JPA normalmente no lanza una excepción: la operación se ignora silenciosamente.<br>
<br>
Conceptualmente, SimpleJpaRepository hace algo parecido a:

```
findById(id).ifPresent(this::delete);
```
Por tanto:<br>
<br>
Si encuentra la tarea, la elimina.<br>
Si no la encuentra, no hace nada.<br>
El método no devuelve ningún resultado porque su retorno es void.<br>
Puede ejecutar primero una consulta similar a:

```
SELECT
    id,
    title,
    description,
    status,
    priority,
    due_date,
    created_at,
    category_id
FROM task
WHERE id = ?;
```
Si no hay resultados, no se ejecuta ningún DELETE.<br>
<br>
Diferencia con este proyecto<br>
En TaskServiceImpl.java, el servicio no llama directamente a deleteById(). Primero busca la tarea:

```
Task taskToDelete = findById(id);
taskRepository.delete(taskToDelete);
```
Y findById() lanza TaskNotFoundException si no existe:

```
return taskRepository.findById(id)
    .orElseThrow(() ->
        new TaskNotFoundException(
            "La tarea con ID " + id + " no existe."
        )
    );
```
Así, la API puede informar correctamente del error, normalmente mediante una respuesta HTTP 404 Not Found, en lugar de responder como si la eliminación hubiera sido válida.<br>
<br>
Resumen<br>

![Captura-parte1-paso4c](..\capturas\Captura-04-pregunta-ask-parte1-paso4c.png)
<br>
También podría producirse una excepción si la tarea tiene relaciones que impiden borrarla por una restricción de clave foránea en la base de datos.

---
<br>
Paso 5 - Validación en Postman<br>
<br>
INCIDENCIA:<br>
<br>
Se ha adelantado la creación del manejador de excepciones que he visto en el ejercicio de las excepciones que se hace luego porque debido a la restricción de que no se puede tocar el controlador el TaskController no capturaría la excepción en el Get ni el Put y devolvería un 500 en vez de un 404.<br>
<br>
Comentarios:<br>
<br>
// Crear un manejador global de excepciones con @RestControllerAdvice que capture TaskNotFoundException y devuelva HTTP 404 NOT_FOUND. -> generado por IA<br>
<br>
//añade la anotación @RestControllerAdvice para que sea un manejador global de excepciones -> generado por IA<br>
<br>
Resultado:

```
package com.accenture.taskmanager.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//añade la anotación @RestControllerAdvice para que sea un manejador global de excepciones -> generado por IA
@RestControllerAdvice
public class GlobalExceptionHandler {
// Crear un manejador global de excepciones con @RestControllerAdvice que capture TaskNotFoundException y devuelva HTTP 404 NOT_FOUND. -> generado por IA
    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<String> handleTaskNotFound(TaskNotFoundException e) {
        return new ResponseEntity<>("Task not found", HttpStatus.NOT_FOUND);
    }
}
```
Capturas de pantalla de Postman:<br>
<br>
Prueba 1 – Obtener todas las tareas (GET /api/v1/tasks)

![GetTodasLasTareas](..\capturas\Captura-04-Prueba-01-GET-Todas-las-tareas.png)
<br>
Prueba 2 – Obtener una tarea por su ID (GET /api/v1/tasks/1)

![GetTareaPorID](..\capturas\Captura-04-Prueba-02-GET-Tarea-por-ID.png)
<br>
Prueba 3 – Obtener una tarea inexistente (GET /api/v1/tasks/99)

![GetTareaInexistente](..\capturas\Captura-04-Prueba-03-GET-Tarea-Inexistente.png)
<br>
Prueba 4 — Crear una tarea (POST)<br>

![PostCrearTarea](..\capturas\Captura-04-Prueba-04-POST-Crear-Tarea.png)
<br>

Prueba 5 — Actualizar tarea (PUT)

![PostCrearTarea](..\capturas\Captura-04-Prueba-05-PUT-Actualizar-Tarea.png)
<br>

Prueba 6 — Eliminar tarea (DELETE)

![EliminarTarea](..\capturas\Captura-04-Prueba-06-DELETE-Eliminar-Tarea.png)
<br>

Prueba 7 — Comprobar que la tarea eliminada ya no existe

![GetTareaEliminada](..\capturas\Captura-04-Prueba-07-GET-Tarea-Eliminada.png)

<br>
Reflexión:<br>
<br>
Pregunta:<b>
<br>
¿Qué ventaja tiene que TaskRepository sea una interface que extiende JpaRepository frente a escribir las queries a mano?<br>
<br>
Respuesta:<br>
<br>
Menos código porque Spring Data te da automático los métodos del Crud<br>
Las consultas son automáticas porque las genera a partir de los nombres de los métodos<br>
Mantenimiento más sencillo porque hay menos código<br>
Separación de responsabilidades porque el repositorio hace el acceso a los datos y la lógica la hace el service que esto es la S de Solid.

---
<br>
Pregunta:<b>
<br>
¿Dónde debe estar la lógica de negocio: en el Repository o en el Service? ¿Por qué?<br>
<br>
Respuesta:<br>
<br>
La lógica tiene que estar en el service porque es la capa que se encarga de aplicar las reglas y las operaciones de la app<br>
El repository tiene que encargarse del acceso a los datos para consultar, guardar, actualizar y eliminar la información que se quiera de bbdd<br>
Así cada capa tiene una responsabilidad diferenciada, es más fácil de leer y mantener y se cumple la S de Solid.

---
<br>
Pregunta:<b>
<br>
Ahora que los datos vienen de MySQL, ¿qué problema ves en devolver directamente la entidad Task desde los endpoints?<br>
<br>
Respuesta:<br>
<br>
Pues que Task representa directamente la estructura de la bbdd, si la devuelves desde los endpoints puedes exponer datos internos que no quieres mostrar y acomplar la API a la estructura de la bbdd<br>
Además si cambias la entidad puedes tener que cambiar también las respuestas de la API<br>
La solución sería utilizar DTOs para decidir qué datos se envían y reciben en la API separando la estructura interna de la entidad de la información.

---
<br>
Parte 2:<br>
<br>
Ejercicio para la creación de las Relaciones entre entidades<br>
<br>
Paso 1 - Preparación (Ask)<br>
<br>
Pregunta:<br>
<br>
¿Cuál es la diferencia entre @OneToMany, @ManyToOne y @ManyToMany, y cómo se representan en las tablas de una base de datos?<br>
<br>
Respuesta:<br>
<br>
Estas anotaciones expresan la cardinalidad entre entidades Java y determinan cómo se relacionan sus tablas en la base de datos.<br>
<br>
@ManyToOne<br>
Varios registros de una entidad pertenecen a un único registro de otra.<br>
<br>
Ejemplo: muchas tareas pertenecen a una categoría.

```
@Entity
public class Task {

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
```

La entidad Task es normalmente el lado propietario, porque contiene la clave foránea.<br>
<br>
Tablas:

```
category
--------
id
name

task
----
id
title
category_id  → category.id
```

La columna category_id permite representar la relación:


```
SELECT *
FROM task
WHERE category_id = ?;
```

@OneToMany
Una entidad tiene varios elementos relacionados.<br>
<br>
Siguiendo el ejemplo anterior, una categoría tiene muchas tareas:

```
@Entity
public class Category {

    @OneToMany(mappedBy = "category")
    private List<Task> tasks;
}
```
Aquí mappedBy = "category" indica que la relación ya está definida por el atributo category de Task. Por tanto, Category es el lado inverso y no añade una columna nueva.<br>
<br>
La representación en las tablas sigue siendo:

```
category
--------
id
name

task
----
id
title
category_id  → category.id
```

Es decir, aunque en Java aparezca como @OneToMany, la clave foránea se guarda en la tabla del lado “muchos”, task.<br>
<br>
Relación bidireccional<br>
Lo habitual es combinar ambas anotaciones:

```
public class Category {

    @OneToMany(mappedBy = "category")
    private List<Task> tasks;
}
```
```
public class Task {

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
```
Task.category es el lado propietario.<br>
Category.tasks es el lado inverso.<br>
mappedBy debe coincidir con el nombre del atributo Java del lado propietario.<br>
@ManyToMany<br>
Muchos registros de una entidad pueden relacionarse con muchos registros de otra.<br>
<br>
Ejemplo: una tarea puede tener varias etiquetas y una etiqueta puede pertenecer a varias tareas.

```
@Entity
public class Task {

    @ManyToMany
    @JoinTable(
        name = "task_tag",
        joinColumns = @JoinColumn(name = "task_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags;
}
```
```
@Entity
public class Tag {

    @ManyToMany(mappedBy = "tags")
    private Set<Task> tasks;
}
```
Como una tabla no puede representar directamente una relación muchos-a-muchos con una sola clave foránea, JPA crea o utiliza una tabla intermedia:

```
task
----
id
title

tag
---
id
name

task_tag
--------
task_id  → task.id
tag_id   → tag.id
```

Normalmente se utiliza una clave primaria compuesta:

```
PRIMARY KEY (task_id, tag_id)
```

Comparación rápida<br>
<br>

![AskParte2](..\capturas\Captura-04-ask-parte-2.png)

Aplicado a este proyecto<br>
Actualmente Task tiene:

```
@Column(name = "category_id")
private Long categoryId;
```

en Task.java. Eso es solamente un campo Long; no es todavía una relación JPA.<br>
<br>
Para convertirlo en una relación habría que tener una entidad Category y utilizar, por ejemplo:

```
@ManyToOne
@JoinColumn(name = "category_id")
private Category category;
```

La columna category_id seguiría existiendo en la tabla task, pero Hibernate podría navegar desde una tarea hasta su categoría y gestionar la asociación como una relación entre entidades.<br>
<br>
En general, para una relación uno-a-muchos se recomienda modelar @ManyToOne en la entidad hija y @OneToMany(mappedBy = ...) en la entidad padre. La combinación evita una tabla intermedia innecesaria y deja claro qué entidad mantiene la clave foránea.

---
<br>
Pregunta:<br>
<br>
¿Qué diferencia hay entre FetchType.LAZY y FetchType.EAGER, y qué es el problema N+1?<br>
<br>
Respuesta:<br>
<br>
FetchType indica cuándo Hibernate carga las entidades relacionadas.<br>
<br>
FetchType.LAZY<br>
La relación se carga bajo demanda, cuando se accede a ella.

```
@Entity
public class Category {

    @OneToMany(
        mappedBy = "category",
        fetch = FetchType.LAZY
    )
    private List<Task> tasks;
}
```

Al cargar una categoría:

```
Category category = categoryRepository.findById(1L).orElseThrow();
```

Hibernate carga inicialmente solo la categoría:

```
SELECT *
FROM category
WHERE id = 1;
```

La colección tasks queda representada normalmente por un proxy o colección especial. La consulta se ejecuta cuando se accede a ella:

```
category.getTasks().size();
```

Entonces Hibernate puede ejecutar:

```
SELECT *
FROM task
WHERE category_id = 1;
```

Ventajas<br>
Carga menos datos inicialmente.<br>
Reduce consultas y memoria cuando no se necesita la relación.<br>
Es generalmente la opción recomendada para colecciones.<br>
Riesgo<br>
La relación debe inicializarse mientras exista un contexto de persistencia activo, normalmente dentro de una transacción. Si se accede después de cerrar la sesión, puede producirse:

```
LazyInitializationException
```

Por ejemplo, si el controlador intenta serializar una colección lazy después de que haya terminado la transacción.<br>
<br>
FetchType.EAGER<br>
La relación se carga junto con la entidad principal, o al menos Hibernate debe dejarla disponible inmediatamente.

```
@ManyToOne(fetch = FetchType.EAGER)
private Category category;
```

Al obtener una tarea, Hibernate también obtiene su categoría. Dependiendo de la consulta y del proveedor JPA, puede hacerlo mediante un JOIN o mediante consultas adicionales:

```
SELECT *
FROM task
WHERE id = ?;
```

y después:

```
SELECT *
FROM category
WHERE id = ?;
```

EAGER no garantiza necesariamente que todo se resuelva con una única consulta SQL.<br>
<br>
Ventajas<br>
La relación está disponible inmediatamente.<br>
Evita ciertos problemas de acceso a relaciones lazy fuera de la sesión.<br>
Riesgos<br>
Puede cargar datos que no se necesitan.<br>
Puede producir consultas adicionales inesperadas.<br>
Puede generar grandes volúmenes de datos si se encadenan varias relaciones eager.<br>
Hace más difícil controlar el rendimiento de cada caso de uso.<br>
Valores predeterminados de JPA<br>
Si no se especifica fetch, JPA define estos valores por defecto:
<br>

![AskParte2b](..\capturas\Captura-04-ask-parte-2b.png)
<br>
Por eso suele ser habitual declarar explícitamente LAZY en relaciones @ManyToOne:

```
@ManyToOne(fetch = FetchType.LAZY)
private Category category;
```

¿Qué es el problema N+1?<br>
Ocurre cuando se ejecuta:<br>
<br>
Una consulta para obtener una lista de N entidades.<br>
Una consulta adicional por cada entidad para cargar una relación.<br>
En total se realizan:

```
1 + N consultas
```

Por ejemplo:

```
List<Category> categories = categoryRepository.findAll();

for (Category category : categories) {
    System.out.println(category.getTasks().size());
}
```

Hibernate podría ejecutar:

```
-- 1. Consulta inicial
SELECT *
FROM category;
```

Y después una consulta por cada categoría:

```
-- 2. Para la categoría 1
SELECT *
FROM task
WHERE category_id = 1;

-- 3. Para la categoría 2
SELECT *
FROM task
WHERE category_id = 2;

-- 4. Para la categoría 3
SELECT *
FROM task
WHERE category_id = 3;
```

Si hay 100 categorías, serían:

```
1 consulta inicial + 100 consultas adicionales = 101 consultas
```

Aunque cada consulta individual sea rápida, el número total puede degradar mucho el rendimiento.<br>
<br>
¿EAGER soluciona siempre el problema N+1?<br>
No. Cambiar una relación a EAGER no garantiza que Hibernate utilice un único JOIN. Puede seguir realizando una consulta inicial y después consultas adicionales.<br>
<br>
Además, EAGER puede causar problemas distintos al cargar relaciones aunque no se necesiten.<br>
<br>
Formas habituales de evitar N+1<br>
1. JOIN FETCH<br>
Se puede definir una consulta JPQL que cargue explícitamente la relación:

```
@Query("""
    SELECT c
    FROM Category c
    LEFT JOIN FETCH c.tasks
""")
List<Category> findAllWithTasks();
```

Hibernate puede generar una consulta equivalente a:

```
SELECT c.*, t.*
FROM category c
LEFT JOIN task t
    ON t.category_id = c.id;
```

2. @EntityGraph<br>
Permite indicar qué relación debe cargarse para un método concreto:

```
@EntityGraph(attributePaths = "tasks")
List<Category> findAll();
```

Así se mantiene una estrategia lazy por defecto y se solicita la relación solo en el caso de uso que la necesita.<br>
<br>
3. Carga por lotes<br>
Hibernate puede agrupar la carga de varias relaciones, por ejemplo mediante @BatchSize:

```
@BatchSize(size = 20)
@OneToMany(mappedBy = "category")
private List<Task> tasks;
```

En vez de ejecutar una consulta por cada categoría, puede agrupar varios identificadores:

```
SELECT *
FROM task
WHERE category_id IN (?, ?, ?, ...);
```

4. Consultas de proyección o DTO<br>
Si solo se necesitan ciertos campos, puede ser mejor consultar directamente un DTO o una proyección en lugar de cargar todas las entidades y sus relaciones.<br>
<br>
Aplicado a este proyecto<br>
Actualmente Task tiene:

```
@Column(name = "category_id")
private Long categoryId;
```

en Task.java, pero ese campo es solo un Long; todavía no existe una relación JPA que pueda ser LAZY o EAGER.<br>
<br>
Si se transformara en una relación:

```
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "category_id")
private Category category;
```

entonces habría que decidir, según cada consulta, si se necesita cargar la categoría. Como regla general:<br>
<br>
usar LAZY como estrategia por defecto;<br>
cargar con JOIN FETCH o @EntityGraph cuando un caso de uso necesite la relación;<br>
revisar las consultas SQL generadas con spring.jpa.show-sql=true, ya habilitado en application.properties.

---
<br>
Pregunta:<br>
<br>
¿Qué hacen mappedBy, @JoinColumn y @JoinTable, y cómo se determina qué entidad es la propietaria de una relación?<br>
<br>
Respuesta:<br>
<br>
Estas anotaciones indican a JPA cómo se representa una relación entre entidades y qué lado puede modificar la relación en la base de datos.<br>
<br>
@JoinColumn<br>
Indica la columna que contiene la clave foránea.<br>
<br>
Por ejemplo, si muchas tareas pertenecen a una categoría:

```
@Entity
public class Task {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
}
```

La tabla sería:

```
task
----
id
title
category_id  → category.id
```

En este caso:<br>
<br>
category_id es la clave foránea.<br>
Task es el lado propietario.<br>
La relación se mantiene desde Task.<br>
El nombre del atributo Java (category) no tiene por qué coincidir con el nombre de la columna (category_id).<br>
<br>
Si no se especifica @JoinColumn, JPA puede generar un nombre por defecto, pero suele ser preferible declararlo explícitamente.<br>
<br>
mappedBy<br>
Indica que la relación está definida y gestionada por el atributo de la otra entidad.<br>
<br>
Ejemplo bidireccional:

```
@Entity
public class Task {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
}
```
```
@Entity
public class Category {

    @OneToMany(mappedBy = "category")
    private List<Task> tasks;
}
```

Aquí:

```
mappedBy = "category"
```

significa:<br>
<br>
La relación ya está mapeada por el atributo category de la entidad Task.<br>
<br>
Por tanto:<br>
<br>
Task.category es el lado propietario.<br>
Category.tasks es el lado inverso.<br>
mappedBy contiene el nombre del atributo Java, no el nombre de la columna.<br>
Category no crea otra columna ni otra relación independiente.<br>
La estructura de tablas sigue siendo:

```
category
--------
id
name

task
----
id
title
category_id  → category.id
```

No se debe escribir:

```
@OneToMany(mappedBy = "category_id")
```

porque category_id es el nombre de la columna, no el nombre del atributo. Lo correcto es:

```
@OneToMany(mappedBy = "category")
```

@JoinTable<br>
Indica que la relación se almacena en una tabla intermedia.<br>
<br>
Se utiliza principalmente con relaciones @ManyToMany.<br>
<br>
Ejemplo:

```
@Entity
public class Task {

    @ManyToMany
    @JoinTable(
        name = "task_tag",
        joinColumns = @JoinColumn(name = "task_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags;
}
```
```
@Entity
public class Tag {

    @ManyToMany(mappedBy = "tags")
    private Set<Task> tasks;
}
```

Las tablas serían:

```
task
----
id
title

tag
---
id
name

task_tag
--------
task_id  → task.id
tag_id   → tag.id
```

Los elementos de @JoinTable significan:<br>
<br>
name = "task_tag": nombre de la tabla intermedia.<br>
joinColumns: columna que apunta a la entidad propietaria (Task).<br>
inverseJoinColumns: columna que apunta a la otra entidad (Tag).<br>
La tabla intermedia suele tener una clave primaria compuesta:

```
PRIMARY KEY (task_id, tag_id)
```

¿Cuál es la entidad propietaria?<br>
En una relación bidireccional, la entidad propietaria es la que:<br>
<br>
No tiene mappedBy.<br>
Declara la información de unión mediante @JoinColumn o @JoinTable.<br>
Es la que utiliza JPA para actualizar la relación en la base de datos.<br>
Ejemplo:

```
@ManyToOne
@JoinColumn(name = "category_id")
private Category category;
```

Task es propietaria porque define la clave foránea.<br>
<br>
El otro lado:

```
@OneToMany(mappedBy = "category")
private List<Task> tasks;
```

es el lado inverso.<br>
<br>
Importante<br>
Modificar solamente el lado inverso no actualiza necesariamente la relación:

```
category.getTasks().add(task);
```

La relación se mantiene correctamente cuando se actualiza también el lado propietario:

```
task.setCategory(category);
category.getTasks().add(task);
```

Por eso es habitual crear métodos auxiliares:

```
public void addTask(Task task) {
    tasks.add(task);
    task.setCategory(this);
}
```

Ejemplos por tipo de relación<br>
@ManyToOne / @OneToMany

```
class Task {
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
```
```
class Category {
    @OneToMany(mappedBy = "category")
    private List<Task> tasks;
}
```

La clave foránea está en task.<br>
<br>
@OneToOne

```
class User {
    @OneToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;
}
```

La tabla user tendría:

```
profile_id → profile.id
```

El lado que contiene @JoinColumn es el propietario.<br>
<br>
@ManyToMany

```
class Task {
    @ManyToMany
    @JoinTable(
        name = "task_tag",
        joinColumns = @JoinColumn(name = "task_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags;
}
```
```
class Tag {
    @ManyToMany(mappedBy = "tags")
    private Set<Task> tasks;
}
```

Task es el propietario y task_tag almacena las asociaciones.<br>
<br>
Resumen

![AskParte2c](..\capturas\Captura-04-ask-parte-2c.png)

En el proyecto actual, Task tiene:

```
@Column(name = "category_id")
private Long categoryId;
```

en Task.java. Eso representa únicamente una columna numérica; todavía no es una relación JPA. Para que category_id fuese gestionada como una asociación habría que reemplazar o complementar ese campo con una relación como:

```
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "category_id")
private Category category;
```

En ese caso, Task sería la entidad propietaria de la relación.

---
<br>
Pregunta:<br>
<br>
¿Qué riesgos tiene configurar mal CascadeType y qué problemas de serialización JSON pueden aparecer con relaciones bidireccionales?<br>
<br>
Respuesta:<br>
<br>
CascadeType define qué operaciones realizadas sobre una entidad se propagan automáticamente a sus entidades relacionadas. Una configuración incorrecta puede provocar cambios o eliminaciones inesperadas.<br>
<br>
Riesgos de CascadeType<br>
Ejemplo:

```
@Entity
public class Category {

    @OneToMany(
        mappedBy = "category",
        cascade = CascadeType.ALL
    )
    private List<Task> tasks;
}
```

Con CascadeType.ALL, las operaciones sobre Category se propagan a sus tareas:<br>
<br>
PERSIST: guarda las tareas nuevas al guardar la categoría.<br>
MERGE: actualiza las tareas al actualizar la categoría.<br>
REMOVE: elimina las tareas al eliminar la categoría.<br>
REFRESH: refresca las tareas.<br>
DETACH: separa las tareas del contexto de persistencia.<br>
Eliminaciones accidentales<br>
El riesgo más importante suele ser:

```
categoryRepository.delete(category);
```

Con CascadeType.REMOVE o CascadeType.ALL, Hibernate puede intentar eliminar todas las tareas asociadas.<br>
<br>
Esto puede ser correcto si las tareas solo tienen sentido dentro de una categoría, pero peligroso si las tareas deben conservarse.<br>
<br>
Persistencia de entidades no deseadas<br>
Con CascadeType.PERSIST o ALL, guardar una entidad puede guardar automáticamente todo el grafo relacionado:

```
categoryRepository.save(category);
```

Si la colección contiene entidades que no se pretendía persistir todavía, también pueden insertarse.<br>
<br>
Actualizaciones masivas inesperadas<br>
Con CascadeType.MERGE, una actualización de la entidad principal puede copiar al contexto de persistencia el estado de todas las entidades relacionadas. Si alguna contiene datos antiguos o modificados incorrectamente, podrían sobrescribirse valores.<br>
<br>
orphanRemoval = true<br>
Por ejemplo:

```
@OneToMany(
    mappedBy = "category",
    cascade = CascadeType.ALL,
    orphanRemoval = true
)
private List<Task> tasks;
```

Si se elimina una tarea de la colección:

```
category.getTasks().remove(task);
```

Hibernate puede interpretar que esa tarea quedó huérfana y ejecutar un DELETE.<br>
<br>
Esto es útil cuando la entidad hija no puede existir sin su padre, pero es peligroso si quitar una asociación no debería borrar el registro.<br>
<br>
Problemas de rendimiento<br>
Cascadas demasiado amplias pueden:<br>
<br>
cargar muchas entidades;<br>
generar numerosos INSERT, UPDATE o DELETE;<br>
aumentar el tiempo de una transacción;<br>
provocar errores al borrar grafos grandes;<br>
hacer difícil saber qué operaciones SQL se ejecutarán.<br>
Recomendaciones para CascadeType<br>
No conviene utilizar CascadeType.ALL por defecto. Hay que elegir las operaciones necesarias:

```
cascade = { CascadeType.PERSIST, CascadeType.MERGE }
```

Por ejemplo:<br>
<br>
usar PERSIST si los hijos nuevos deben guardarse junto con el padre;<br>
usar MERGE si se desea propagar actualizaciones;<br>
usar REMOVE solo cuando eliminar el padre deba eliminar también a los hijos;<br>
usar orphanRemoval únicamente cuando el hijo no tenga sentido fuera del padre.<br>
Además, la cascada JPA no es exactamente lo mismo que ON DELETE CASCADE de la base de datos. CascadeType controla operaciones del contexto de persistencia Java; una restricción ON DELETE CASCADE es una regla de la base de datos.<br>
<br>
Problemas de serialización JSON con relaciones bidireccionales<br>
Supongamos esta relación:

```
class Category {
    private List<Task> tasks;
}
```
```
class Task {
    private Category category;
}
```

Al convertir una Category a JSON, Jackson puede seguir este ciclo:

```
Category
  └── tasks
        └── category
              └── tasks
                    └── category
                          ...
```

Esto puede provocar:

```
JsonMappingException: Infinite recursion
```

o errores similares de profundidad de anidamiento.<br>
<br>
El JSON esperado quizá era:

```
{
  "id": 1,
  "name": "Trabajo",
  "tasks": [
    {
      "id": 10,
      "title": "Preparar informe"
    }
  ]
}
```

pero la serialización intenta incluir continuamente el objeto category dentro de cada tarea.<br>
<br>
Soluciones habituales<br>
1. @JsonManagedReference y @JsonBackReference<br>
Se marca el lado que se serializa normalmente:

```
public class Category {

    @JsonManagedReference
    @OneToMany(mappedBy = "category")
    private List<Task> tasks;
}
```

Y se evita serializar de vuelta el padre desde el hijo:

```
public class Task {

    @JsonBackReference
    @ManyToOne
    private Category category;
}
```

El JSON incluye Category.tasks, pero no vuelve a incluir Task.category.<br>
<br>
2. @JsonIgnore<br>
Si no se desea exponer una dirección de la relación:

```
public class Task {

    @JsonIgnore
    private Category category;
}
```

Es sencillo, pero elimina esa propiedad del JSON en todos los casos en los que Jackson aplique esa anotación.<br>
<br>
3. @JsonIdentityInfo<br>
Jackson puede representar las entidades repetidas mediante sus identificadores:

```
@JsonIdentityInfo(
    generator = ObjectIdGenerators.PropertyGenerator.class,
    property = "id"
)
```

Así, la primera aparición contiene el objeto completo y las siguientes pueden representarse con su ID.<br>
<br>
4. DTOs<br>
Generalmente es la solución más controlable:

```
public record TaskResponse(
    Long id,
    String title,
    Long categoryId
) {}
```

En lugar de serializar directamente entidades JPA, el controlador devuelve un DTO con los datos que realmente forman parte de la API.<br>
<br>
Ventajas:<br>
<br>
evita ciclos;<br>
evita exponer campos internos;<br>
permite controlar el tamaño de la respuesta;<br>
reduce problemas con relaciones LAZY;<br>
desacopla el JSON público del modelo de persistencia.<br>
Otros problemas relacionados<br>
Carga lazy durante la serialización<br>
Si Jackson intenta acceder a una relación LAZY cuando la sesión de Hibernate ya está cerrada, puede producirse:

```
LazyInitializationException
```

Aunque exista una relación válida, el serializador no puede cargarla fuera del contexto de persistencia.<br>
<br>
Respuestas JSON enormes<br>
Con relaciones anidadas, una respuesta aparentemente simple puede cargar:

```
Category → tasks → category → tasks → ...
```

Incluso si no aparece una recursión infinita, el JSON puede ser demasiado grande y provocar:<br>
<br>
muchas consultas SQL;<br>
alto consumo de memoria;<br>
tiempos de respuesta elevados;<br>
problemas de rendimiento.<br>
Exposición accidental de datos<br>
Serializar entidades directamente puede exponer campos que no deberían formar parte de la API, como:<br>
<br>
identificadores internos;<br>
datos de auditoría;<br>
relaciones completas;<br>
información de otras entidades;<br>
propiedades sensibles.<br>
Aplicado a este proyecto<br>
Actualmente Task.java contiene:

```
@Column(name = "category_id")
private Long categoryId;
```

Eso es solo un identificador numérico y no crea una relación bidireccional. Por tanto, actualmente no hay un ciclo JSON entre Task y Category.<br>
<br>
Si en el futuro se sustituye por:

```
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "category_id")
private Category category;
```

y Category contiene una colección de tareas, convendría decidir explícitamente:<br>
<br>
qué cascadas son realmente necesarias;<br>
si borrar una categoría debe borrar sus tareas;<br>
qué lado se expone en JSON;<br>
si se usarán anotaciones de Jackson o DTOs.<br>
Como regla general:<br>
<br>
evitar CascadeType.ALL sin justificarlo;<br>
tener especial cuidado con CascadeType.REMOVE y orphanRemoval;<br>
evitar devolver entidades JPA bidireccionales directamente desde los controladores;<br>
preferir DTOs para controlar el contrato JSON de la API.

---
<br>
Paso 2 - Diseño previo<br>
<br>
Pregunta:<br>
<br>
¿Qué campos tendrán las entidades Category y Tag?<br>
<br>
Respuesta:<br>
<br>
Category: id, name, description y una lista de tareas tasks
Tag: id, name y una lista de tareas tasks

---
<br>
Pregunta:<br>
<br>
¿Cada relación será unidireccional o bidireccional? ¿Por qué?<br>
<br>
Respuesta:<br>
<br>
Task-Category: bidireccional, porque una categoría puede tener muchas tareas y cada tarea pertenece a una categoría.<br>
Task-Tag: bidireccional, porque una tarea puede tener varios tags y un tag puede estar asociado a varias tareas.

---
<br>
Pregunta:<br>
<br>
¿Quién será el dueño de cada relación? ¿Dónde irán @JoinColumn y @JoinTable?<br>
<br>
Respuesta:<br>
<br>
Task–Category: Task será la propietaria con @ManyToOne y @JoinColumn(name = "category_id"). Category tendrá @OneToMany(mappedBy = "category").<br>
Task–Tag: Task será la propietaria con @ManyToMany y @JoinTable. Tag tendrá @ManyToMany(mappedBy = "tags").

---
<br>
Pregunta:<br>
<br>
¿Qué FetchType y CascadeType usarás en cada relación? ¿Por qué?<br>
<br>
Respuesta:<br>
<br>
FetchType: LAZY en todas las relaciones, para cargar los datos relacionados solo cuando hagan falta<br>
CascadeType: en Task–Tag, PERSIST y MERGE. En Task–Category, no usaré cascadas, porque las tareas y las categorías tienen ciclos de vida independientes.

---
<br>
Pregunta:<br>
<br>
¿Qué problema crees que tendrá devolver estas entidades en JSON tal cual, con sus relaciones?<br>
<br>
Respuesta:<br>
<br>
Las relaciones bidireccionales pueden provocar referencias circulares, respuestas Json enormes o errores de serialización.<br>
Podría usarse de solución provisional @JsonIgnore.

---
<br>
Pregunta:<br>
<br>
¿Qué comentario exacto escribirás para que el autocompletado lo genere?<br>
<br>
Respuesta:<br>
<br>
Tengo pensado poner estos pero seguramente tendré que cambiarlos varias veces para que ajusten:
<br>
Category:<br>
<br>
// Crear la entidad JPA Category asociada a la tabla category, con los campos id, name y description, y una relación bidireccional OneToMany con Task usando mappedBy = "category" y FetchType.LAZY. -> generado por IA<br>
Tag:<br>
<br>
// Crear la entidad JPA Tag asociada a la tabla tag, con los campos id y name, y una relación ManyToMany bidireccional con Task usando mappedBy = "tags" y FetchType.LAZY. -> generado por IA<br>
Task:<br>
<br>
// Añadir la relación ManyToOne con Category usando FetchType.LAZY y @JoinColumn(name = "category_id"), y la relación ManyToMany con Tag usando FetchType.LAZY, CascadeType.PERSIST y CascadeType.MERGE, y @JoinTable sobre task_tag. -> generado por IA

---
<br>
Paso 3 - Implementación (autocompletado)<br>
<br>
Category:<br>
<br>
Comentarios:<br>
<br>
// Añadir constructor vacío, constructor con name y description, y getters y setters para todos los atributos. — generado por IA<br>
<br>
// Añadir constructor vacío, constructor con name y description, y getters y setters para todos los atributos. — generado por IA<br>
<br>
Resultado:

```
package com.accenture.taskmanager.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

// Crear la entidad JPA Category asociada a la tabla category, con los campos id, name y description, y una relación bidireccional OneToMany con Task usando mappedBy = "category" y FetchType.LAZY. — generado por IA
@Entity
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<Task> tasks;

    // Añadir constructor vacío, constructor con name y description, y getters y setters para todos los atributos. — generado por IA
    public Category() {
    }

    public Category(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
    
}
```

---
<br>
Tag:<br>
<br>
Comentarios:<br>
<br>
//Crear la entidad JPA Tag asociada a la tabla tag, con los campos id y name, y una relación ManyToMany bidireccional con Task usando mappedBy = "tags" y FetchType.LAZY. — generado por IA<br>
<br>
//Pon la anotación @Entity y @Table(name = "tag") para indicar que es una entidad JPA y que se corresponde con la tabla tag. — generado por IA
<br>
Resultado:

```
package com.accenture.taskmanager.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

//Crear la entidad JPA Tag asociada a la tabla tag, con los campos id y name, y una relación ManyToMany bidireccional con Task usando mappedBy = "tags" y FetchType.LAZY. — generado por IA
//Pon la anotación @Entity y @Table(name = "tag") para indicar que es una entidad JPA y que se corresponde con la tabla tag. — generado por IA
@Entity
@Table(name = "tag")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY)
    private List<Task> tasks;

    // Añadir constructor vacío, constructor con name, y getters y setters para todos los atributos. — generado por IA
    public Tag() {
    }

    public Tag(String name) {
        this.name = name;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}
```

---
<br>
Task:<br>
<br>
Comentarios:<br>
<br>
// Añade @Enumerated(EnumType.STRING) en la clase Task para que status y priority se guarden como texto en la base de datos -> generado por IA<br>
<br>
// Sustituir el atributo categoryId por una relación ManyToOne con Category usando FetchType.LAZY y @JoinColumn(name = "category_id"), conservando el resto del código existente. — generado por IA<br>
<br>
// Añadir una relación ManyToMany con Tag usando FetchType.LAZY, CascadeType.PERSIST y CascadeType.MERGE, y definir task_tag mediante @JoinTable con las columnas task_id y tag_id. — generado por IA<br>
<br>
// Corregir la firma y el cuerpo del constructor de Task para recibir Category category y List<Tag> tags en lugar de Long categoryId, asignando ambos parámetros a sus atributos correspondientes. — generado por IA<br>
<br>
// Sustituir los getters y setters de categoryId por los de Category category, y añadir los getters y setters de List<Tag> tags, conservando el resto de métodos existentes. — generado por IA
<br>
Resultado:

```
package com.accenture.taskmanager.model;

import java.util.List;

import com.accenture.taskmanager.enums.TaskPriority;
import com.accenture.taskmanager.enums.TaskStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.EnumType;

// Crear el modelo Task con los atributos: id, title, description, status, priority, dueDate, incluir sus tipos de datos más adecuados, añadir constructor vacío y por parámetros, añadir getters y setters -> generado por IA
// Crear las anotaciones JPA necesarias en la clase Task para mapearla con la tabla task, incluyendo @Entity, @Table, @Id y @GeneratedValue, y añadir los imports necesarios, sin sustituir ningún código existente. -> generado por IA
// Añade @Enumerated(EnumType.STRING) en la clase Task para que status y priority se guarden como texto en la base de datos -> generado por IA
// Sustituir el atributo categoryId por una relación ManyToOne con Category usando FetchType.LAZY y @JoinColumn(name = "category_id"), conservando el resto del código existente. — generado por IA
// Añadir una relación ManyToMany con Tag usando FetchType.LAZY, CascadeType.PERSIST y CascadeType.MERGE, y definir task_tag mediante @JoinTable con las columnas task_id y tag_id. — generado por IA
/**
 * Modelo que representa una tarea de la aplicación.
 *
 * @author Javier Roldán
 * @version 1.0
 */
@Entity
@Table(name = "task")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    @Enumerated(EnumType.STRING)
    private TaskStatus status;
    @Enumerated(EnumType.STRING)
    private TaskPriority priority;
    @Column(name = "due_date")
    private java.time.LocalDate dueDate;
    @Column(name = "created_at")
    private java.time.LocalDateTime createdAt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "task_tag",
        joinColumns = @JoinColumn(name = "task_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags;

// Añadir un método con @PrePersist que asigne LocalDateTime.now() a createdAt únicamente si su valor es null. -> generado por IA
    @PrePersist
    private void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = java.time.LocalDateTime.now();
        }
    }
    /**
     * Constructor vacío de la clase Task.
     */
    public Task() {
    }

    // Constructor para que los atributos status y priority sean del tipo TaskStatus y TaskPriority, respectivamente, y añade los imports necesarios, sin sustituir ningún código existente. -> generado por IA
    // Actualizar el constructor con parámetros de Task para sustituir categoryId por Category category y añadir List<Tag> tags, conservando el resto de atributos y el constructor vacío. — generado por IA
    // Corregir la firma y el cuerpo del constructor de Task para recibir Category category y List<Tag> tags en lugar de Long categoryId, asignando ambos parámetros a sus atributos correspondientes. — generado por IA
    /**
     * Constructor de la clase Task con todos sus atributos.
     *
     * @param id          identificador de la tarea
     * @param title       título de la tarea
     * @param description descripción de la tarea
     * @param status      estado de la tarea
     * @param priority    prioridad de la tarea
     * @param dueDate     fecha límite de la tarea
     * @param createdAt   fecha de creación de la tarea
     * @param category    categoría a la que pertenece la tarea
     * @param tags        etiquetas de la tarea
     */
    public Task(Long id, String title, String description, TaskStatus status, TaskPriority priority,
                java.time.LocalDate dueDate, java.time.LocalDateTime createdAt, Category category, List<Tag> tags) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.dueDate = dueDate;
        this.createdAt = createdAt;
        this.category = category;
        this.tags = tags;
    }

    // ajusta los getters y setters de los atributos status y priority para que devuelvan y acepten valores de tipo TaskStatus y TaskPriority, respectivamente, y añade los imports necesarios, sin sustituir ningún código existente. —> generado por IA
    // Sustituir los getters y setters de categoryId por los de Category category, y añadir los getters y setters de List<Tag> tags, conservando el resto de métodos existentes. — generado por IA
    /**
     * Obtiene el identificador de la tarea.
     *
     * @return identificador de la tarea
     */
    public Long getId() {
        return id;
    }

    /**
     * Modifica el identificador de la tarea.
     *
     * @param id nuevo identificador de la tarea
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtiene el título de la tarea.
     *
     * @return título de la tarea
     */
    public String getTitle() {
        return title;
    }

    /**
     * Modifica el título de la tarea.
     *
     * @param title nuevo título de la tarea
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Obtiene la descripción de la tarea.
     *
     * @return descripción de la tarea
     */
    public String getDescription() {
        return description;
    }

    /**
     * Modifica la descripción de la tarea.
     *
     * @param description nueva descripción de la tarea
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Obtiene el estado de la tarea.
     *
     * @return estado de la tarea
     */
    public TaskStatus getStatus() {
        return status;
    }

    /**
     * Modifica el estado de la tarea.
     *
     * @param status nuevo estado de la tarea
     */
    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    /**
     * Obtiene la prioridad de la tarea.
     *
     * @return prioridad de la tarea
     */
    public TaskPriority getPriority() {
        return priority;
    }

    /**
     * Modifica la prioridad de la tarea.
     *
     * @param priority nueva prioridad de la tarea
     */
    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }

    /**
     * Obtiene la fecha límite de la tarea.
     *
     * @return fecha límite de la tarea
     */
    public java.time.LocalDate getDueDate() {
        return dueDate;
    }

    /**
     * Modifica la fecha límite de la tarea.
     *
     * @param dueDate nueva fecha límite de la tarea
     */
    public void setDueDate(java.time.LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    // pon los getters y setters de los atributos createdAt y categoryId, y añade los imports necesarios, sin sustituir ningún código existente. —> generado por IA
    /**
     * Obtiene la fecha de creación de la tarea.
     *
     * @return fecha de creación de la tarea
     */
    public java.time.LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Modifica la fecha de creación de la tarea.
     *
     * @param createdAt nueva fecha de creación de la tarea
     */
    public void setCreatedAt(java.time.LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    /**
     * Obtiene la categoría a la que pertenece la tarea.
     *
     * @return categoría de la tarea
     */ 
    public Category getCategory() {
        return category;
    }

    /**
     * Modifica la categoría a la que pertenece la tarea.
     *
     * @param category nueva categoría de la tarea
     */
    public void setCategory(Category category) {
        this.category = category;
    }

    /**
     * Obtiene las etiquetas de la tarea.
     *
     * @return etiquetas de la tarea
     */
    public List<Tag> getTags() {
        return tags;
    }

    /**
     * Modifica las etiquetas de la tarea.
     *
     * @param tags nuevas etiquetas de la tarea
     */
    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }
}
```

---
Paso 4 - Comprensión del código (Ask)<br>
<br>
Pregunta:<br>
<br>
¿Qué hace mappedBy = "category" en la anotación @OneToMany? ¿Qué pasaría si lo eliminas?<br>
<br>
Respuesta:<br>
<br>
mappedBy = "category" indica que la relación ya está definida por el atributo category de la entidad hija.<br>
<br>
Ejemplo:

```
@Entity
public class Task {

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
```
```
@Entity
public class Category {

    @OneToMany(mappedBy = "category")
    private List<Task> tasks;
}
```

En este caso:<br>
<br>
Task.category es el lado propietario.<br>
Category.tasks es el lado inverso.<br>
"category" debe coincidir con el nombre del atributo Java en Task.<br>
mappedBy no contiene el nombre de la columna category_id.<br>
La clave foránea se guarda en task.category_id.<br>
La representación sería:

```
category
--------
id

task
----
id
category_id → category.id
```

¿Qué pasaría si eliminas mappedBy?<br>
Si escribes:


```
@OneToMany
private List<Task> tasks;
```

JPA deja de entender que Category.tasks es el lado inverso de Task.category. Para JPA, esa relación pasa a ser otra asociación independiente y, en un @OneToMany unidireccional, normalmente se representa mediante una tabla intermedia:


```
category
--------
id

task
----
id

category_task
-------------
category_id → category.id
task_id     → task.id
```

Esto puede producir:

una tabla intermedia innecesaria;<br>
más operaciones INSERT y DELETE para mantener la relación;<br>
problemas con ddl-auto=validate si esa tabla no existe;<br>
que modificar category.getTasks() no actualice task.category;<br>
dos representaciones distintas de lo que aparentemente es la misma relación.<br>
El @ManyToOne de Task seguiría usando task.category_id, mientras que el @OneToMany sin mappedBy podría usar category_task. En ese caso habría dos asociaciones independientes.<br>
<br>
¿Qué ocurre al guardar?<br>
Con mappedBy:

```
task.setCategory(category);
category.getTasks().add(task);
```

La asociación que realmente actualiza la clave foránea es:

```
task.setCategory(category);
```

porque Task es el lado propietario.<br>
<br>
Una buena práctica es encapsular la sincronización:

```
public void addTask(Task task) {
    tasks.add(task);
    task.setCategory(this);
}
```
Excepción: @JoinColumn en el @OneToMany<br>
También puede existir un @OneToMany sin mappedBy usando explícitamente @JoinColumn:

```
@OneToMany
@JoinColumn(name = "category_id")
private List<Task> tasks;
```

En ese caso se puede utilizar la columna task.category_id sin tabla intermedia, pero la relación sigue siendo unidireccional y Category actúa como el lado que la gestiona. No es el mismo modelo que una relación bidireccional con:

```
@ManyToOne
@JoinColumn(name = "category_id")
```

y:

```
@OneToMany(mappedBy = "category")
```

Resumen

![AskPaso4](..\capturas\Captura-04-ask-paso4.png)
<br>
En el proyecto actual, Task.java solo tiene categoryId como Long; todavía no existe una asociación JPA Task–Category. Por tanto, mappedBy solo sería aplicable después de modelar ambas entidades y sus relaciones.

---
<br>
Pregunta:<br>
<br>
¿Qué hace @JoinColumn(name = "category_id") en Task? ¿Quién es el dueño de la relación?<br>
<br>
Respuesta:<br>
<br>
En una relación JPA, por ejemplo:

```
@Entity
public class Task {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
}
```

@JoinColumn(name = "category_id") indica que la relación con Category se almacena mediante la columna category_id de la tabla task.<br>
<br>
¿Qué representa?<br>
La tabla podría ser:

```
task
----
id
title
category_id  → category.id
```
Es decir:<br>
<br>
category_id es una clave foránea.<br>
Su valor apunta al id de una fila de category.<br>
Una tarea pertenece a una categoría.<br>
Varias tareas pueden tener el mismo category_id.<br>
Conceptualmente, Hibernate puede generar una restricción similar a:

```
ALTER TABLE task
ADD CONSTRAINT fk_task_category
FOREIGN KEY (category_id)
REFERENCES category(id);
```

@JoinColumn no crea por sí sola la entidad Category; solo especifica qué columna conecta ambas entidades.<br>
<br>
¿Quién es el dueño de la relación?<br>
El dueño es Task, porque es la entidad que contiene:

```
@JoinColumn(name = "category_id")
```

En JPA, el lado propietario es el que controla la clave foránea y cuya modificación actualiza la relación en la base de datos:

```
task.setCategory(category);
```

Al guardar la tarea, Hibernate actualizará task.category_id.<br>
<br>
Una relación bidireccional completa sería:

```
@Entity
public class Task {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
}
```
```
@Entity
public class Category {

    @OneToMany(mappedBy = "category")
    private List<Task> tasks;
}
```

Aquí:<br>
<br>
Task.category es el lado propietario.<br>
Category.tasks es el lado inverso.<br>
mappedBy = "category" indica que la relación la controla el atributo category de Task.<br>
Solo existe una clave foránea: task.category_id.<br>
Para mantener ambos lados sincronizados en memoria, conviene actualizar los dos:

```
public void addTask(Task task) {
    tasks.add(task);
    task.setCategory(this);
}
```

La llamada importante para la base de datos es:

```
task.setCategory(this);
```

porque modifica el lado propietario.<br>
<br>
¿Qué ocurre si Task no tiene @JoinColumn?<br>
Si se escribe:

```
@ManyToOne
private Category category;
```

JPA intentará deducir un nombre de columna, normalmente algo parecido a:

```
category_id
```

Pero declararlo explícitamente es recomendable porque:<br>
<br>
hace visible el esquema esperado;<br>
evita depender de las convenciones de nombres;<br>
permite usar el nombre real de la columna;<br>
facilita mantener compatibilidad con una base de datos existente.<br>
Aplicado al proyecto actual<br>
Actualmente Task.java contiene:

```
@Column(name = "category_id")
private Long categoryId;
```

Eso no es todavía una relación JPA. Es simplemente un campo numérico cuyo valor se guarda en la columna category_id. Hibernate no puede navegar desde ese Long hasta una entidad Category.<br>
<br>
Para convertirlo en una asociación habría que tener una entidad Category y utilizar:

```
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "category_id")
private Category category;
```

En ese modelo, Task sería el propietario de la relación.

---
<br>
Pregunta:<br>
<br>
¿Qué hace mappedBy = "category" en la anotación @OneToMany? ¿Qué pasaría si lo eliminas?<br>
<br>
Respuesta:<br>
<br>


---
<br>
Pregunta:<br>
<br>
¿Por qué se usa FetchType.LAZY y no EAGER? ¿Qué consulta SQL extra generaría EAGER?<br>
<br>
Respuesta:<br>
<br>
Se usa FetchType.LAZY para que la relación se cargue solo cuando realmente se necesita. Por ejemplo:

```
@Entity
public class Task {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
}
```

Al buscar una tarea, Hibernate cargaría inicialmente solo los datos de task:

```
SELECT
    id,
    title,
    description,
    status,
    priority,
    due_date,
    created_at,
    category_id
FROM task
WHERE id = ?;
```

La categoría no se consulta todavía. Solo se cargaría al acceder a ella:

```
task.getCategory().getName();
```

Entonces podría ejecutarse:

```
SELECT
    id,
    name
FROM category
WHERE id = ?;
```

¿Qué haría EAGER?<br>
Con:

```
@ManyToOne(fetch = FetchType.EAGER)
@JoinColumn(name = "category_id")
private Category category;
```

Hibernate debe dejar la categoría disponible inmediatamente al cargar Task. Para conseguirlo puede usar diferentes estrategias, según la consulta, el proveedor y la versión:<br>
<br>
Opción 1: consulta adicional<br>
Primero obtiene la tarea:

```
SELECT
    id,
    title,
    description,
    status,
    priority,
    due_date,
    created_at,
    category_id
FROM task
WHERE id = ?;
```

Después obtiene la categoría:

```
SELECT
    id,
    name
FROM category
WHERE id = ?;
```

Si se cargan varias tareas, podría ocurrir:

```
SELECT ... FROM task;
SELECT ... FROM category WHERE id = ?; -- tarea 1
SELECT ... FROM category WHERE id = ?; -- tarea 2
SELECT ... FROM category WHERE id = ?; -- tarea 3
```

Esto puede producir el problema N+1: una consulta para las tareas y otra por cada categoría necesaria.<br>
<br>
Opción 2: JOIN<br>
En otros casos Hibernate puede resolverlo mediante una consulta con unión:

```
SELECT
    t.id,
    t.title,
    t.description,
    t.status,
    t.priority,
    t.due_date,
    t.created_at,
    t.category_id,
    c.id,
    c.name
FROM task t
LEFT JOIN category c
    ON t.category_id = c.id
WHERE t.id = ?;
```

Por tanto, EAGER no garantiza que siempre se genere una consulta adicional ni que siempre se utilice un JOIN. Lo que garantiza es que la relación debe estar disponible cuando la entidad se obtiene.<br>
<br>
¿Por qué suele preferirse LAZY?<br>
Porque permite controlar la carga según el caso de uso:<br>
<br>
evita traer categorías cuando solo se necesitan los datos de la tarea;<br>
reduce memoria y transferencia de datos;<br>
evita consultas innecesarias;<br>
facilita prevenir el problema N+1;<br>
permite decidir explícitamente cuándo usar un JOIN FETCH o un @EntityGraph.<br>
Por ejemplo, para un caso de uso que sí necesita la categoría, se puede definir una consulta específica:

```
@Query("""
    SELECT t
    FROM Task t
    JOIN FETCH t.category
""")
List<Task> findAllWithCategory();
```

Así la relación normalmente es LAZY, pero se carga mediante JOIN solo en las consultas que lo necesitan.<br>
<br>
Importante en este proyecto<br>
Actualmente Task.java tiene:

```
@Column(name = "category_id")
private Long categoryId;
```

Ese campo es solo un Long; todavía no existe una relación JPA con Category. Por tanto, en el código actual no se genera ninguna consulta adicional por LAZY o EAGER.<br>
<br>
Eso solo ocurriría después de cambiarlo por una asociación como:

```
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "category_id")
private Category category;
```

La propiedad spring.jpa.show-sql=true de application.properties permite observar las consultas que Hibernate ejecuta realmente.

---
<br>
Pregunta:<br>
<br>
¿Por qué @JoinTable va en Task y no en Tag? ¿Qué hace mappedBy = "tags" en la entidad Tag?<br>
<br>
Respuesta:<br>
<br>
En una relación @ManyToMany, una de las entidades debe ser el lado propietario. Ese lado define cómo se utiliza la tabla intermedia mediante @JoinTable.<br>
<br>
Ejemplo:

```
@Entity
public class Task {

    @ManyToMany
    @JoinTable(
        name = "task_tag",
        joinColumns = @JoinColumn(name = "task_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags;
}
```
```
@Entity
public class Tag {

    @ManyToMany(mappedBy = "tags")
    private Set<Task> tasks;
}
```

¿Por qué @JoinTable está en Task?<br>
Porque en este diseño se ha decidido que Task sea el propietario de la relación.<br>
<br>
Task indica:<br>
<br>
el nombre de la tabla intermedia: task_tag;<br>
la columna que apunta a Task: task_id;<br>
la columna que apunta a Tag: tag_id.<br>
La estructura sería:

```
task
----
id
title

tag
---
id
name

task_tag
--------
task_id  → task.id
tag_id   → tag.id
```

Cuando se modifica la colección del lado propietario:

```
task.getTags().add(tag);
```

JPA puede insertar una fila en la tabla intermedia:

```
INSERT INTO task_tag (task_id, tag_id)
VALUES (?, ?);
```

¿Qué significa mappedBy = "tags" en Tag?<br>
Esta declaración:

```
@ManyToMany(mappedBy = "tags")
private Set<Task> tasks;
```

significa:

La relación ya está mapeada por el atributo tags de la entidad Task.<br>
<br>
Por tanto:<br>
<br>
Task.tags es el lado propietario.<br>
Tag.tasks es el lado inverso.<br>
"tags" es el nombre del atributo Java en Task.<br>
mappedBy no es el nombre de una tabla ni de una columna.<br>
Tag no define otra tabla intermedia.<br>
El nombre debe coincidir exactamente con este atributo:

```
private Set<Tag> tags;
```

No sería correcto escribir:

```
mappedBy = "task_tag"
```

ni:

```
mappedBy = "tag_id"
```

porque esos son nombres relacionados con la base de datos, no con la propiedad Java propietaria.<br>
<br>
¿Qué ocurre si se elimina mappedBy?<br>
Si se escribe:

```
@Entity
public class Tag {

    @ManyToMany
    private Set<Task> tasks;
}
```

ambos lados parecen propietarios:

```
Task.tags
Tag.tasks
```

JPA puede interpretar que existen dos relaciones independientes y crear o esperar dos configuraciones de unión distintas.<br> Dependiendo de las anotaciones y de la configuración, esto puede provocar:<br>
<br>
una tabla intermedia adicional;<br>
columnas o nombres de tabla inesperados;<br>
asociaciones que no se sincronizan entre sí;<br>
errores de validación o de arranque;<br>
operaciones redundantes sobre la tabla intermedia.<br>
Para una única relación bidireccional, un lado debe declarar @JoinTable y el otro debe utilizar mappedBy.<br>
<br>
¿Podría estar @JoinTable en Tag?<br>
Sí. La elección es principalmente de diseño:

```
@Entity
public class Tag {

    @ManyToMany
    @JoinTable(
        name = "task_tag",
        joinColumns = @JoinColumn(name = "tag_id"),
        inverseJoinColumns = @JoinColumn(name = "task_id")
    )
    private Set<Task> tasks;
}
```

En ese caso:<br>
<br>
Tag.tasks sería el lado propietario.<br>
Task.tags tendría mappedBy = "tasks".<br>
La tabla física podría seguir siendo exactamente task_tag.<br>
Lo importante no es que Task sea siempre el propietario, sino que:<br>
<br>
haya un único lado propietario;<br>
el otro lado use mappedBy;<br>
ambos lados representen la misma relación.<br>
Mantener ambos lados sincronizados<br>
Aunque Tag.tasks sea el lado inverso, conviene actualizar las dos colecciones en memoria:

```
public void addTag(Tag tag) {
    tags.add(tag);
    tag.getTasks().add(this);
}
```

La parte que controla la persistencia de la relación es la colección propietaria:

```
task.getTags().add(tag);
```

La colección inversa sirve principalmente para navegar desde Tag hacia sus tareas.<br>
<br>
En el proyecto actual, Task.java solo tiene categoryId como Long; todavía no existe una entidad Tag ni una relación @ManyToMany.

---
<br>
Pregunta:<br>
<br>
¿Qué pasaría si usaras CascadeType.REMOVE en la relación @ManyToMany? ¿Por qué es peligroso?<br>
<br>
Respuesta:<br>
<br>
CascadeType.REMOVE indica que al eliminar una entidad también se deben eliminar las entidades relacionadas.<br>
<br>
En una relación @ManyToMany:

```
@Entity
public class Task {

    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(
        name = "task_tag",
        joinColumns = @JoinColumn(name = "task_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags;
}
```

si se ejecuta:

```
taskRepository.delete(task);
```

Hibernate podría intentar:<br>
<br>
eliminar las asociaciones de task_tag;<br>
eliminar las entidades Tag asociadas;<br>
eliminar la propia entidad Task.<br>
¿Por qué es peligroso?<br>
En una relación muchos-a-muchos, las entidades relacionadas suelen ser compartidas. Por ejemplo:

```
Task 1 ─── Tag "Java"
Task 2 ─── Tag "Java"
```

Si se elimina Task 1 con CascadeType.REMOVE, la etiqueta "Java" también podría eliminarse, aunque todavía sea utilizada por Task 2.<br>
<br>
El resultado podría ser:

```
Task 1      eliminado
Tag "Java"  eliminado inesperadamente
Task 2      queda sin esa etiqueta
```

Incluso podría producirse una propagación mucho mayor si se configura la cascada en ambos sentidos:

```
Task.tags  -> Tag.tasks
Tag.tasks  -> Task.tags
```

Eliminar una tarea podría intentar eliminar sus etiquetas, esas etiquetas podrían intentar eliminar otras tareas y así sucesivamente. Además de borrar datos no deseados, esto puede causar:<br>
<br>
violaciones de restricciones de clave foránea;<br>
errores al eliminar entidades compartidas;<br>
cascadas difíciles de predecir;<br>
numerosas operaciones DELETE;<br>
problemas de rendimiento.<br>
¿Qué debería eliminarse normalmente?<br>
Al eliminar una Task, normalmente solo se quieren eliminar:<br>
<br>
la fila de Task;<br>
las filas de asociación correspondientes en task_tag.<br>
No se quieren eliminar las filas de Tag, porque las etiquetas pueden pertenecer a otras tareas.<br>
<br>
La relación se puede definir sin REMOVE:

```
@ManyToMany
@JoinTable(
    name = "task_tag",
    joinColumns = @JoinColumn(name = "task_id"),
    inverseJoinColumns = @JoinColumn(name = "tag_id")
)
private Set<Tag> tags;
```

En ese caso, al eliminar la tarea, JPA puede eliminar las asociaciones de la tabla intermedia, pero conserva las etiquetas.<br>
<br>
Mejor alternativa<br>
Si las etiquetas nuevas deben guardarse junto con la tarea, puede usarse una cascada limitada:

```
@ManyToMany(cascade = {
    CascadeType.PERSIST,
    CascadeType.MERGE
})
private Set<Tag> tags;
```

Esto permite propagar guardados y actualizaciones, pero no eliminaciones.<br>
<br>
Aun así, hay que utilizarlo solo si las etiquetas forman parte del ciclo de vida de la tarea. Si son entidades compartidas o administradas independientemente, suele ser preferible no usar cascadas y guardar cada entidad de forma explícita.<br>
<br>
Diferencia con orphanRemoval<br>
orphanRemoval = true se utiliza habitualmente en relaciones como @OneToMany cuando el hijo no tiene sentido sin su padre. No debe utilizarse para una relación @ManyToMany; las entidades de ambos lados suelen tener su propio ciclo de vida.<br>
<br>
En una relación muchos-a-muchos, quitar una etiqueta de la colección:

```
task.getTags().remove(tag);
```

debería eliminar normalmente solo la asociación:

```
DELETE FROM task_tag
WHERE task_id = ?
  AND tag_id = ?;
```

no la fila de tag.<br>
<br>
Resumen

![AskPaso4b](..\capturas\Captura-04-ask-paso4b.png)
<br>
Por eso CascadeType.REMOVE suele evitarse en relaciones @ManyToMany. En el proyecto actual, Task.java todavía solo tiene categoryId como Long; no existe aún una relación con Tag ni, por tanto, este riesgo en el código actual.

---
<br>
Pregunta:<br>
<br>
¿Qué problema evita @JsonIgnore en las relaciones? ¿Por qué es solo una solución provisional?<br>
<br>
Respuesta:<br>
<br>
@JsonIgnore indica a Jackson que no incluya una propiedad al convertir una entidad Java a JSON.<br>
<br>
Por ejemplo, en una relación bidireccional:

```
public class Category {

    private List<Task> tasks;
}
```
```
public class Task {

    @JsonIgnore
    private Category category;
}
```

Sin @JsonIgnore, Jackson podría recorrer el ciclo:

```
Category
 └── tasks
      └── category
           └── tasks
                └── category
                     ...
```

Esto puede provocar:

```
JsonMappingException: Infinite recursion
```

o una respuesta JSON excesivamente grande.

Con @JsonIgnore, la propiedad Task.category no se serializa:

```
{
  "id": 10,
  "title": "Preparar informe"
}
```

La categoría puede seguir incluyendo sus tareas, pero las tareas no vuelven a incluir la categoría.<br>
<br>
¿Qué problema evita?<br>
Principalmente evita:<br>
<br>
referencias circulares infinitas;<br>
errores de serialización JSON;<br>
respuestas con relaciones repetidas indefinidamente;<br>
respuestas innecesariamente grandes;<br>
parte de las cargas lazy provocadas al serializar esa propiedad.<br>
También puede impedir que Jackson procese esa propiedad durante la deserialización, según cómo se configure y dónde se coloque la anotación. Por eso hay que considerarla parte del comportamiento de entrada y salida JSON, no solo de la salida.<br>
<br>
¿Por qué es una solución provisional?<br>
Porque @JsonIgnore resuelve el problema ocultando completamente la propiedad. No controla realmente el diseño de la respuesta.<br>
<br>
1. Oculta información de forma global<br>
Si se escribe:

```
@JsonIgnore
private Category category;
```

esa relación no se incluirá en ninguna respuesta que serialice directamente Task, aunque un endpoint concreto sí necesite mostrarla.<br>
<br>
Por ejemplo, quizá un endpoint necesite devolver:

```
{
  "id": 10,
  "title": "Preparar informe",
  "category": {
    "id": 2,
    "name": "Trabajo"
  }
}
```

Con @JsonIgnore, no será posible obtener esa respuesta sin cambiar anotaciones o introducir configuraciones adicionales.<br>
<br>
2. Mezcla persistencia y API<br>
Las entidades JPA representan el modelo de base de datos, mientras que el JSON forma parte del contrato público de la API. Al poner anotaciones de Jackson en la entidad, ambos modelos quedan acoplados.<br>
<br>
Un cambio necesario para resolver una relación JPA puede modificar accidentalmente las respuestas HTTP.<br>
<br>
3. No permite respuestas diferentes según el caso de uso<br>
Un endpoint puede necesitar:<br>
<br>
una tarea con categoryId;<br>
otro, una tarea con la categoría resumida;<br>
otro, una categoría con sus tareas;<br>
otro, ninguna relación.<br>
@JsonIgnore solo ofrece una decisión fija: mostrar u ocultar siempre esa propiedad.<br>
<br>
4. No resuelve todos los problemas de rendimiento<br>
Aunque evita recorrer una dirección de la relación, no sustituye una estrategia adecuada de consultas. Todavía pueden existir:<br>
<br>
consultas N+1;<br>
carga innecesaria de entidades;<br>
problemas con relaciones LAZY;<br>
respuestas demasiado grandes por otras relaciones;<br>
exposición de campos que no deberían formar parte del JSON.<br>
Solución más robusta: DTOs<br>
Lo habitual es no devolver directamente entidades JPA desde los controladores, sino objetos diseñados para la API:

```
public record TaskResponse(
    Long id,
    String title,
    Long categoryId
) {
}
```

O, si se necesita incluir una categoría resumida:

```
public record TaskResponse(
    Long id,
    String title,
    CategoryResponse category
) {
}

public record CategoryResponse(
    Long id,
    String name
) {
}
```

Así se decide explícitamente qué se expone, sin ciclos ni campos accidentales:
```
{
  "id": 10,
  "title": "Preparar informe",
  "category": {
    "id": 2,
    "name": "Trabajo"
  }
}
```

Comparación

![AskPaso4c](..\capturas\Captura-04-ask-paso4c.png)
<br>
En el proyecto actual, Task.java solo tiene un Long categoryId; todavía no hay una relación JPA bidireccional que produzca este ciclo. Si posteriormente se añade una asociación con Category, @JsonIgnore puede ser un parche válido para evitar la recursión, pero los DTOs serían una solución más sólida y flexible.

---
<br>
Paso 5 - Verificación<br>
<br>
Se arranca la app y se comprueba que arranque:

![Paso5Arranque](..\capturas\Captura-04-paso5-arranque.png)

Comprobaciones:<br>
<br>
GET<br>

![Paso5Get](..\capturas\Captura-04-paso5-get.png)

GET por ID<br>

![Paso5GetId](..\capturas\Captura-04-paso5-getId.png)

<br>
Reflexión<br>
<br>
Pregunta:<br>
¿Por qué es importante que la relación de una tarea con su categoría sea opcional?<br>
<br>
Respuesta:<br>
<br>
Porque una tarea puede existir sin tener una categoría asignada. Esto aporta flexibilidad y evita obligar a crear o asignar una categoría cuando no sea necesaria.<br>
<br>
Pregunta:<br>
<br>
¿Qué diferencia práctica hay entre una relación `@ManyToOne` y una `@ManyToMany` a nivel de tablas?<br>
<br>
Respuesta:<br>
<br>
Con `@ManyToOne`, muchas tareas pueden apuntar a una misma categoría mediante una clave foránea, como `category_id` en la tabla `task`. Con `@ManyToMany`, muchas tareas pueden relacionarse con muchas etiquetas y se necesita una tabla intermedia, en este caso `task_tag`, que contiene las claves de ambas tablas.<br>
<br>
Pregunta:<br>
<br>
¿Qué solución definitiva crees que habrá para devolver estas relaciones en la API sin `@JsonIgnore`?<br>
<br>
Respuesta:<br>
<br>
Utilizar DTOs (*Data Transfer Objects*) para controlar qué datos se devuelven en las respuestas de la API. Así se puede incluir la información necesaria de las relaciones sin serializar directamente las entidades ni provocar referencias circulares.

---
<br>
Parte 3<br>
<br>
Ejercicio para la creación de Queries personalizadas:<br>
<br>
Paso 1 - Preparación (Ask)<br>
<br>
Pregunta:<br>
<br>
¿Cómo se escribe una consulta personalizada con @Query y JPQL en Spring Data JPA, y en qué se diferencia de un método derivado por nombre?<br>
<br>
Respuesta:<br>
<br>
Una consulta personalizada se escribe con @Query sobre un método del repositorio. Por defecto, @Query utiliza JPQL, que consulta entidades y atributos Java, no tablas y columnas directamente.<br>
<br>
Por ejemplo, en TaskRepository.java:

```
@Query("""
    SELECT t
    FROM Task t
    WHERE t.status = :status
      AND t.priority = :priority
""")
List<Task> searchByStatusAndPriority(
    @Param("status") TaskStatus status,
    @Param("priority") TaskPriority priority
);
```

La consulta significa:

```
SELECT las tareas
FROM la entidad Task
WHERE su status coincide con :status
  AND su priority coincide con :priority
```

Se invocaría así:

```
List<Task> tasks = taskRepository.searchByStatusAndPriority(
    TaskStatus.IN_PROGRESS,
    TaskPriority.HIGH
);
```

Parámetros con @Param<br>
El nombre de @Param debe coincidir con el parámetro utilizado en JPQL:

```
@Param("status") TaskStatus status
```

se corresponde con:

```
t.status = :status
```

También se pueden usar parámetros posicionales:

```
@Query("""
    SELECT t
    FROM Task t
    WHERE t.status = ?1
      AND t.priority = ?2
""")
List<Task> search(TaskStatus status, TaskPriority priority);
```

Sin embargo, los parámetros con nombre suelen ser más claros y resistentes a cambios en el orden de los argumentos.<br>
<br>
Ejemplo con LIKE<br>
Para buscar tareas cuyo título contenga un texto:

```
@Query("""
    SELECT t
    FROM Task t
    WHERE LOWER(t.title) LIKE LOWER(CONCAT('%', :text, '%'))
""")
List<Task> searchByTitle(@Param("text") String text);
```

JPQL utiliza:<br>
<br>
Task, que es el nombre de la entidad;<br>
t.title, que es el atributo Java;<br>
no task, que es el nombre de la tabla;<br>
no title o task_title, salvo que se esté escribiendo SQL nativo.<br>
Ejemplo de consulta que devuelve un boolean

```
@Query("""
    SELECT COUNT(t) > 0
    FROM Task t
    WHERE t.status = :status
""")
boolean existsWithStatus(@Param("status") TaskStatus status);
```

También es posible utilizar las operaciones derivadas de Spring Data, como:

```
boolean existsByStatus(TaskStatus status);
```

@Query para actualizar o eliminar<br>
Las consultas UPDATE y DELETE necesitan normalmente @Modifying:

```
@Modifying
@Query("""
    UPDATE Task t
    SET t.status = :newStatus
    WHERE t.id = :id
""")
int updateStatus(
    @Param("id") Long id,
    @Param("newStatus") TaskStatus newStatus
);
```

El valor devuelto indica cuántas filas se modificaron. Estas operaciones deben ejecutarse dentro de una transacción, por ejemplo:

```
@Transactional
```

Puede colocarse en el método del repositorio o, normalmente de forma preferible, en el servicio que coordina la operación.<br>
<br>
Diferencia con un método derivado por nombre<br>
Un método derivado expresa la consulta en su propio nombre:

```
List<Task> findByStatusAndPriority(
    TaskStatus status,
    TaskPriority priority
);
```

Spring Data interpreta:

```
find By Status And Priority
```

y genera una consulta equivalente a:

```
SELECT t
FROM Task t
WHERE t.status = ?1
  AND t.priority = ?2
```

Con @Query, escribes la consulta explícitamente:

```
@Query("""
    SELECT t
    FROM Task t
    WHERE t.status = :status
      AND t.priority = :priority
""")
List<Task> searchByStatusAndPriority(
    @Param("status") TaskStatus status,
    @Param("priority") TaskPriority priority
);
```

Comparación

![Parte3Aska](..\capturas\Captura-04-parte3Aska.png)

Ejemplo donde @Query resulta más apropiado<br>
Una consulta con JOIN FETCH:

```
@Query("""
    SELECT t
    FROM Task t
    JOIN FETCH t.category
    WHERE t.status = :status
""")
List<Task> findByStatusWithCategory(
    @Param("status") TaskStatus status
);
```

Esto no se expresa de forma tan clara mediante un método derivado, porque la intención no es solo filtrar por status, sino también cargar explícitamente la relación category.<br>
<br>
JPQL frente a SQL nativo<br>
@Query usa JPQL por defecto:

```
@Query("""
    SELECT t
    FROM Task t
    WHERE t.status = :status
""")
```

Si se necesita escribir SQL directamente, se puede indicar:

```
@Query(
    value = """
        SELECT *
        FROM task
        WHERE status = :status
    """,
    nativeQuery = true
)
List<Task> findNativeByStatus(
    @Param("status") String status
);
```

La diferencia es importante:<br>
<br>
JPQL<br>
<br>
Utiliza:<br>
<br>
Task: entidad Java;<br>
status: atributo Java;<br>
asociaciones JPA;<br>
una sintaxis independiente del motor de base de datos.<br>
SQL nativo<br>
<br>
Utiliza:<br>
<br>
task: tabla física;<br>
status: columna;<br>
sintaxis específica de la base de datos;<br>
funciones y características propias de MySQL.<br>
Como regla general:<br>
<br>
usa métodos derivados para filtros sencillos;<br>
usa @Query con JPQL para consultas complejas pero portables;<br>
usa nativeQuery = true solo cuando necesites SQL específico, funciones del motor o una optimización que JPQL no exprese bien.<br>
En todos los casos, los parámetros deben enlazarse como parámetros de consulta, nunca concatenarse manualmente en una cadena.

---
<br>
Pregunta:<br>
<br>
¿Cómo se consulta una entidad relacionada, como Task por Category o por Tag, usando JPQL?<br>
<br>
Respuesta:<br>
<br>
En JPQL se consulta la entidad Task y se navega por sus atributos Java relacionados. No se utilizan directamente los nombres de las tablas o columnas SQL.<br>
<br>
1. Task por Category con @ManyToOne<br>
Suponiendo este modelo:<br>

```
@Entity
public class Task {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
}
```

La consulta JPQL sería:

```
@Query("""
    SELECT t
    FROM Task t
    WHERE t.category = :category
""")
List<Task> findByCategory(@Param("category") Category category);
```

También se puede consultar por el identificador de la categoría:

```
@Query("""
    SELECT t
    FROM Task t
    WHERE t.category.id = :categoryId
""")
List<Task> findByCategoryId(@Param("categoryId") Long categoryId);
```

La parte:

```
t.category.id
```

significa:

```
Task → category → id
```

Hibernate traducirá conceptualmente la segunda consulta a:

```
SELECT t.*
FROM task t
WHERE t.category_id = ?;
```

No es necesario escribir explícitamente un JOIN cuando solo se navega por una propiedad relacionada.<br>
<br>
Usando JOIN explícito<br>
La misma consulta puede escribirse así:

```
@Query("""
    SELECT t
    FROM Task t
    JOIN t.category c
    WHERE c.id = :categoryId
""")
List<Task> findByCategoryId(@Param("categoryId") Long categoryId);
```

Aquí:<br>
<br>
Task t define la entidad principal;<br>
JOIN t.category c une Task con Category;<br>
c.id permite filtrar por el identificador de la categoría.<br>
También se puede filtrar por una propiedad de Category:

```
@Query("""
    SELECT t
    FROM Task t
    JOIN t.category c
    WHERE c.name = :categoryName
""")
List<Task> findByCategoryName(
    @Param("categoryName") String categoryName
);
```

SQL aproximado:

```
SELECT t.*
FROM task t
JOIN category c
    ON t.category_id = c.id
WHERE c.name = ?;
```

2. Task por Tag con @ManyToMany<br>
Suponiendo una relación como esta:

```
@Entity
public class Task {

    @ManyToMany
    @JoinTable(
        name = "task_tag",
        joinColumns = @JoinColumn(name = "task_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags;
}
```

Para buscar tareas que tengan una etiqueta concreta:

```
@Query("""
    SELECT t
    FROM Task t
    JOIN t.tags tag
    WHERE tag.id = :tagId
""")
List<Task> findByTagId(@Param("tagId") Long tagId);
```

El JOIN recorre:

```
Task → tags → Tag
```

Hibernate tendrá que utilizar también la tabla intermedia task_tag. El SQL equivalente sería parecido a:

```
SELECT DISTINCT t.*
FROM task t
JOIN task_tag tt
    ON tt.task_id = t.id
JOIN tag tag
    ON tag.id = tt.tag_id
WHERE tag.id = ?;
```

¿Por qué usar DISTINCT?<br>
Si una consulta une una colección, una misma tarea puede aparecer varias veces en el resultado SQL. Por ejemplo, si una tarea tiene varias filas relacionadas en la tabla intermedia.<br>
<br>
Para evitar duplicados en el resultado de entidades:

```
@Query("""
    SELECT DISTINCT t
    FROM Task t
    JOIN t.tags tag
    WHERE tag.name = :tagName
""")
List<Task> findByTagName(@Param("tagName") String tagName);
```

También se puede buscar por el nombre de la etiqueta:

```
@Query("""
    SELECT DISTINCT t
    FROM Task t
    JOIN t.tags tag
    WHERE LOWER(tag.name) = LOWER(:tagName)
""")
List<Task> findByTagName(@Param("tagName") String tagName);
```

Tareas que tengan varias etiquetas<br>
Para buscar tareas que tengan una etiqueta con un nombre y además otra con otro nombre, se necesitan dos joins con alias distintos:

```
@Query("""
    SELECT DISTINCT t
    FROM Task t
    JOIN t.tags tagJava
    JOIN t.tags tagSpring
    WHERE tagJava.name = :firstTag
      AND tagSpring.name = :secondTag
""")
List<Task> findByTwoTags(
    @Param("firstTag") String firstTag,
    @Param("secondTag") String secondTag
);
```

La misma etiqueta no debe reutilizarse con dos condiciones diferentes, porque un mismo alias representa una única fila de Tag.<br>
<br>
JOIN frente a JOIN FETCH<br>
Una consulta normal:

```
@Query("""
    SELECT DISTINCT t
    FROM Task t
    JOIN t.tags tag
    WHERE tag.name = :tagName
""")
List<Task> findByTagName(@Param("tagName") String tagName);
```

usa la relación para filtrar las tareas. No significa necesariamente que la colección tags quede cargada para su uso posterior.<br>
<br>
Si además se desea cargar la relación en la misma consulta, se utiliza JOIN FETCH:

```
@Query("""
    SELECT DISTINCT t
    FROM Task t
    JOIN FETCH t.tags tag
    WHERE tag.name = :tagName
""")
List<Task> findByTagNameWithTags(@Param("tagName") String tagName);
```

La intención es:

```
buscar las tareas que tengan esa etiqueta
y cargar también sus etiquetas.
```

Esto puede evitar consultas adicionales al acceder a task.getTags() dentro del contexto adecuado.<br>
<br>
Con LEFT JOIN<br>
JOIN equivale normalmente a INNER JOIN: solo devuelve tareas que tengan una categoría o etiqueta que cumpla la condición.<br>
<br>
Para conservar tareas aunque no tengan relación, se puede usar LEFT JOIN:

```
@Query("""
    SELECT t
    FROM Task t
    LEFT JOIN t.category c
    WHERE c.name = :categoryName
       OR c IS NULL
""")
List<Task> findByCategoryNameOrWithoutCategory(
    @Param("categoryName") String categoryName
);
```

Sin embargo, si se coloca una condición sobre la relación en el WHERE, un LEFT JOIN puede comportarse en la práctica como un INNER JOIN para esas filas. Cuando sea necesario conservar la semántica del LEFT JOIN, algunas condiciones deben colocarse en el propio ON o WITH, según la versión y el proveedor JPA.<br>
<br>
Métodos derivados equivalentes<br>
Para una relación Category, Spring Data también puede derivar la consulta:

```
List<Task> findByCategoryId(Long categoryId);
List<Task> findByCategoryName(String categoryName);
```

Para Tag:

```
List<Task> findDistinctByTagsId(Long tagId);
List<Task> findDistinctByTagsName(String tagName);
```

El prefijo Distinct ayuda a evitar tareas repetidas cuando se atraviesa una colección @ManyToMany.<br>
<br>
Situación actual del proyecto<br>
Actualmente Task.java tiene:

```
@Column(name = "category_id")
private Long categoryId;
```

Eso es un campo numérico, no una asociación JPA. Por tanto, con el modelo actual solo se puede consultar así:

```
@Query("""
    SELECT t
    FROM Task t
    WHERE t.categoryId = :categoryId
""")
List<Task> findByCategoryId(@Param("categoryId") Long categoryId);
```

Para poder escribir:

```
t.category.id
```

o:

```
JOIN t.category c
```

habría que modelar la relación como:

```
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "category_id")
private Category category;
```

Y para consultar por Tag, habría que añadir las entidades Tag y la asociación @ManyToMany.

---
<br>
Pregunta:<br>
<br>
¿Para qué sirve @Param y cómo se combinan varias condiciones con And en un método derivado?<br>
<br>
Respuesta:<br>
<br>
@Param sirve para asociar un parámetro Java con un parámetro nombrado dentro de una consulta escrita con @Query.<br>
<br>
Uso de @Param con @Query

```
@Query("""
    SELECT t
    FROM Task t
    WHERE t.status = :status
      AND t.priority = :priority
""")
List<Task> searchByStatusAndPriority(
    @Param("status") TaskStatus status,
    @Param("priority") TaskPriority priority
);
```

Las correspondencias son:

```
:status   ← @Param("status") TaskStatus status
:priority ← @Param("priority") TaskPriority priority
```

El nombre debe coincidir exactamente. Si se escribe:

```
WHERE t.status = :taskStatus
```

entonces el parámetro debe ser:

```
@Param("taskStatus") TaskStatus status
```

Si el nombre no coincide, Spring Data puede fallar al crear o ejecutar la consulta.<br>
<br>
¿Hace falta @Param en un método derivado?<br>
No. En un método derivado no se escribe una consulta con parámetros nombrados. Spring Data interpreta el nombre del método y relaciona los argumentos por posición:

```
List<Task> findByStatusAndPriority(
    TaskStatus status,
    TaskPriority priority
);
```

Spring interpreta:

```
find By Status And Priority
     │    │      │   │
     │    │      │   └── segundo argumento: priority
     │    │      └────── operador lógico AND
     │    └───────────── primer argumento: status
     └────────────────── operación de búsqueda
```

Este método ya existe en TaskRepository.java.<br>
<br>
Una llamada sería:

```
List<Task> tasks = taskRepository.findByStatusAndPriority(
    TaskStatus.IN_PROGRESS,
    TaskPriority.HIGH
);
```

La consulta equivalente sería conceptualmente:

```
SELECT t
FROM Task t
WHERE t.status = ?1
  AND t.priority = ?2
```

y el SQL resultante sería parecido a:

```
SELECT *
FROM task
WHERE status = ?
  AND priority = ?;
```

El primer argumento se compara con status y el segundo con priority.<br>
<br>
Más ejemplos con And<br>
Se pueden combinar varias propiedades:

```
List<Task> findByStatusAndPriorityAndCategoryId(
    TaskStatus status,
    TaskPriority priority,
    Long categoryId
);
```

Equivale conceptualmente a:

```
SELECT t
FROM Task t
WHERE t.status = ?1
  AND t.priority = ?2
  AND t.categoryId = ?3
```

También se pueden combinar And y Or:

```
List<Task> findByStatusAndPriorityOrCategoryId(
    TaskStatus status,
    TaskPriority priority,
    Long categoryId
);
```

La intención habitual es:

```
(status = ?1 AND priority = ?2) OR categoryId = ?3
```

Cuando la lógica es compleja o puede resultar ambigua, es preferible usar @Query para expresar las condiciones explícitamente.<br>
<br>
Diferencia resumida

![Parte3Askb](..\capturas\Captura-04-parte3Askb.png)

Por ejemplo, estas dos consultas son equivalentes:

```
@Query("""
    SELECT t
    FROM Task t
    WHERE t.status = :status
      AND t.priority = :priority
""")
List<Task> search(
    @Param("status") TaskStatus status,
    @Param("priority") TaskPriority priority
);
```
```
@Query("""
    SELECT t
    FROM Task t
    WHERE t.status = ?1
      AND t.priority = ?2
""")
List<Task> search(TaskStatus status, TaskPriority priority);
```

Los parámetros nombrados con @Param suelen ser más legibles y menos propensos a errores si cambia el orden de los argumentos.

---
<br>
Paso 2 - Diseño previo<br>
<br>
Antes de generar código, copia este diseño en notas/NOTAS-04-uso-de-spring-data-jpa.md.<br>
<br>
1. Consultas y endpoints<br>
<br>
Consulta	                                        Tipo	                    Endpoint
Buscar tareas por categoría	JPQL                    (@Query)	                GET /api/v1/tasks/by-category/{categoryId}<br>
Buscar tareas por etiqueta	JPQL                    (@Query)	                GET /api/v1/tasks/by-tag/{tagId}<br>
Buscar por estado y prioridad	                    Query method por nombre	    GET /api/v1/tasks/by-status-priority?status=PENDING&priority=HIGH<br>
Buscar por fecha límite anterior a una fecha	    JPQL (@Query)	            GET /api/v1/tasks/before-date?date=2026-02-01<br>
Contar tareas de un estado	                        JPQL (@Query)	            GET /api/v1/tasks/count-by-status?status=PENDING<br><br>
<br>
2. Comentarios exactos para el autocompletado<br>
Guarda también estos comentarios en tus notas. Los utilizaremos uno por uno, sin generar todo de golpe.<br>
<br>
A. En TaskRepository — búsqueda por categoría<br>
<br>
// Buscar tareas por categoría usando JPQL y @Query, filtrando por el ID de la categoría relacionada con Task. — generado por IA<br>
<br>
B. En TaskRepository — búsqueda por etiqueta<br>
<br>
// Buscar tareas por tag usando JPQL y @Query, realizando un JOIN con la relación tags de Task y filtrando por el ID del tag. — generado por IA<br>
<br>
C. En TaskService y TaskServiceImpl — métodos de servicio<br>
<br>
// Añadir los métodos de servicio para las cinco consultas de la Parte 3, delegando en TaskRepository y devolviendo los resultados correspondientes, sin añadir lógica de negocio innecesaria. — generado por IA<br>
<br>
D. En TaskController — endpoints<br>
<br>
// Añadir los cinco endpoints GET de las consultas personalizadas de la Parte 3, usando @PathVariable o @RequestParam según corresponda y delegando en TaskService, sin modificar el endpoint GET /api/v1/tasks existente. — generado por IA<br>
<br>
E. En TaskRepository — búsqueda por fecha<br>
<br>
// Buscar tareas cuya fecha límite dueDate sea anterior a una fecha recibida como parámetro, utilizando JPQL con @Query y @Param. — generado por IA<br>
<br>
F. En TaskRepository — conteo por estado<br>
<br>
// Contar las tareas que tienen un estado concreto utilizando JPQL con COUNT y un parámetro status. — generado por IA

---
<br>
Paso 3 - Implementación (autocompletado)<br>
<br>
Comentario:<br>
<br>
// Buscar tareas por categoría usando JPQL y @Query, filtrando por el ID de la categoría relacionada con Task. — generado por IA<br>
<br>
Resultado:
```
// Buscar tareas por categoría usando JPQL y @Query, filtrando por el ID de la categoría relacionada con Task. — generado por IA
    @Query("SELECT t FROM Task t WHERE t.category.id = :categoryId")
    List<Task> findByCategoryId(@Param("categoryId") Long categoryId);
```

Comentario:<br>
<br>
// Buscar tareas por tag usando JPQL y @Query, realizando un JOIN con la relación tags de Task y filtrando por el ID del tag. — generado por IA<br>
<br>
Resultado:

```
    // Buscar tareas por tag usando JPQL y @Query, realizando un JOIN con la relación tags de Task y filtrando por el ID del tag. — generado por IA
    @Query("SELECT t FROM Task t JOIN t.tags tag WHERE tag.id = :tagId")
    List<Task> findByTagId(@Param("tagId") Long tagId);
```

Comentario:<br>
<br>
// Buscar tareas cuya fecha límite dueDate sea anterior a una fecha recibida como parámetro, utilizando JPQL con @Query y @Param. — generado por IA<br>
<br>
Resultado:

```
    // Buscar tareas cuya fecha límite dueDate sea anterior a una fecha recibida como parámetro, utilizando JPQL con @Query y @Param. — generado por IA
    @Query("SELECT t FROM Task t WHERE t.dueDate < :date")
    List<Task> findByDueDateBefore(@Param("date") java.time.LocalDate date );
```

Comentario:<br>
<br>
// Contar las tareas que tienen un estado concreto utilizando JPQL con COUNT y un parámetro status. — generado por IA<br>
<br>
Resultado:

```
    // Contar las tareas que tienen un estado concreto utilizando JPQL con COUNT y un parámetro status. — generado por IA
    @Query("SELECT COUNT(t) FROM Task t WHERE t.status = :status")
    long countByStatus(@Param("status") TaskStatus status);
```

---
<br>
TaskService<br>
<br>
Comentario:<br>
<br>
// Añadir los métodos de servicio para las cinco consultas, delegando en TaskRepository y devolviendo los resultados correspondientes, sin añadir lógica de negocio innecesaria. — generado por IA<br>
<br>
Resultado:

```
    // Añadir los métodos de servicio para las cinco consultas, delegando en TaskRepository y devolviendo los resultados correspondientes, sin añadir lógica de negocio innecesaria. — generado por IA
    Iterable<Task> findByCategoryId(Long categoryId);
    Iterable<Task> findByTagId(Long tagId);
    Iterable<Task> findByDueDateBefore(java.time.LocalDate date);
    long getCountByStatus(TaskStatus status);
```

TaskServiceImpl<br>
<br>
Comentario:<br>
<br>
    // Implementar findByStatusAndPriority convirtiendo los parámetros String a sus enums TaskStatus y TaskPriority y delegando la búsqueda en TaskRepository. — generado por IA<br>
    // Añadir los métodos de servicio para las cinco consultas, delegando en TaskRepository y devolviendo los resultados correspondientes, sin añadir lógica de negocio innecesaria. — generado por IA<br>
<br>
Resultado:

```
    // Implementar findByStatusAndPriority convirtiendo los parámetros String a sus enums TaskStatus y TaskPriority y delegando la búsqueda en TaskRepository. — generado por IA
    // Añadir los métodos de servicio para las cinco consultas, delegando en TaskRepository y devolviendo los resultados correspondientes, sin añadir lógica de negocio innecesaria. — generado por IA
    @Override
    public Iterable<Task> findByStatusAndPriority(String status, String priority) {
        TaskStatus taskStatus = TaskStatus.valueOf(status);
        TaskPriority taskPriority = TaskPriority.valueOf(priority);
        return taskRepository.findByStatusAndPriority(taskStatus, taskPriority);
    }

    @Override
    public Iterable<Task> findByCategoryId(Long categoryId) {
        return taskRepository.findByCategoryId(categoryId);
    }

    @Override
    public Iterable<Task> findByTagId(Long tagId) {
        return taskRepository.findByTagId(tagId);
    }

    @Override
    public Iterable<Task> findByDueDateBefore(java.time.LocalDate date) {
        return taskRepository.findByDueDateBefore(date);
    }

    @Override
    public long getCountByStatus(TaskStatus status) {
        return taskRepository.countByStatus(status);
    }
```

TaskController<br>
<br>
Comentario:<br>
<br>
// Añadir los cinco endpoints GET de las consultas personalizadas, usando @PathVariable o @RequestParam según corresponda y delegando en TaskService, sin modificar el endpoint GET /api/v1/tasks existente. — generado por IA<br>
<br>
Resultado:

```
// Añadir los cinco endpoints GET de las consultas personalizadas, usando @PathVariable o @RequestParam según corresponda y delegando en TaskService, sin modificar el endpoint GET /api/v1/tasks existente. — generado por IA

@GetMapping("/by-category/{categoryId}")
public ResponseEntity<Iterable<Task>> getTasksByCategoryId(@PathVariable Long categoryId) {
    Iterable<Task> tasks = taskService.findByCategoryId(categoryId);
    return new ResponseEntity<>(tasks, HttpStatus.OK);
}

@GetMapping("/by-tag/{tagId}")
public ResponseEntity<Iterable<Task>> getTasksByTagId(@PathVariable Long tagId) {
    Iterable<Task> tasks = taskService.findByTagId(tagId);
    return new ResponseEntity<>(tasks, HttpStatus.OK);
}

@GetMapping("/by-status-priority")
public ResponseEntity<Iterable<Task>> getTasksByStatusAndPriority(
        @RequestParam TaskStatus status,
        @RequestParam TaskPriority priority) {
    Iterable<Task> tasks = taskService.findByStatusAndPriority(status.name(), priority.name());
    return new ResponseEntity<>(tasks, HttpStatus.OK);
}

@GetMapping("/before-date")
public ResponseEntity<Iterable<Task>> getTasksByDueDateBefore(
        @RequestParam java.time.LocalDate date) {
    Iterable<Task> tasks = taskService.findByDueDateBefore(date);
    return new ResponseEntity<>(tasks, HttpStatus.OK);
}

@GetMapping("/count-by-status")
public ResponseEntity<Long> getTaskCountByStatus(@RequestParam TaskStatus status) {
    long count = taskService.getCountByStatus(status);
    return new ResponseEntity<>(count, HttpStatus.OK);
}
```

---
<br>
Paso 4 - Comprensión del código (Ask)<br>
<br>
Pregunta:<br>
<br>
¿Qué diferencia hay entre SELECT t FROM Task t en JPQL y SELECT * FROM task en SQL? ¿Por qué JPQL usa el nombre de la clase y no de la tabla?<br>
<br>
Respuesta:<br>
<br>
La diferencia es que JPQL trabaja con el modelo de objetos Java, mientras que SQL trabaja directamente con el modelo relacional de la base de datos.<br>
<br>
JPQL

```
SELECT t
FROM Task t
```

Significa:

```
Selecciona objetos de tipo Task y asígnales el alias t.
```

Aquí:<br>
<br>
Task es el nombre de la entidad Java;<br>
t es un alias;<br>
t representa objetos Task completos;<br>
el resultado suele ser List<Task>.<br>
En el proyecto, Task está marcada como entidad:

```
@Entity
public class Task {
    // ...
}
```

Por tanto, JPQL la conoce como Task.<br>
<br>
SQL

```
SELECT *
FROM task;
```

Significa:

```
Selecciona todas las columnas de la tabla física task.
```

Aquí:<br>
<br>
task es el nombre de la tabla;<br>
* significa todas sus columnas;<br>
el resultado son filas y columnas;<br>
no son objetos Java directamente.<br>
La tabla se configura en Task.java:

```
@Entity
@Table(name = "task")
public class Task {
}
```

La entidad se llama Task, mientras que la tabla se llama task.<br>
<br>
¿Por qué JPQL usa Task y no task?<br>
JPQL es un lenguaje orientado a entidades y atributos Java. No debería depender de los nombres físicos de cada base de datos.<br>
<br>
Por eso utiliza:

```
FROM Task t
```

y no:

```
FROM task t
```

Esto permite escribir consultas basadas en el dominio:

```
SELECT t
FROM Task t
WHERE t.status = :status
```

en lugar de depender directamente del esquema SQL:

```
SELECT *
FROM task
WHERE status = ?;
```

en lugar de depender directamente del esquema SQL:

```
SELECT *
FROM task
WHERE status = ?;
```

Hibernate conoce el mapeo:

```
Entidad Task  → tabla task
atributo id   → columna id
atributo dueDate → columna due_date
atributo categoryId → columna category_id
```

y traduce la consulta JPQL a SQL para el motor configurado, en este caso MySQL.<br>
<br>
Diferencia entre atributos y columnas<br>
En JPQL se utilizan los nombres de las propiedades Java:

```
SELECT t
FROM Task t
WHERE t.dueDate = :date
```

No se utiliza el nombre de columna:

```
-- Incorrecto en JPQL
WHERE t.due_date = :date
```

El mapeo está definido en la entidad:

```
@Column(name = "due_date")
private LocalDate dueDate;
```

El mapeo está definido en la entidad:

```
@Column(name = "due_date")
private LocalDate dueDate;
```

Hibernate traducirá la consulta a algo parecido a:

```
SELECT
    t.id,
    t.title,
    t.description,
    t.status,
    t.priority,
    t.due_date,
    t.created_at,
    t.category_id
FROM task t
WHERE t.due_date = ?;
```

SELECT t frente a SELECT *<br>
Aunque parezcan equivalentes, no son exactamente la misma operación:

```
SELECT t
FROM Task t
```

devuelve entidades Task gestionadas por el contexto de persistencia.

```
SELECT *
FROM task
```

devuelve entidades Task gestionadas por el contexto de persistencia.

```
SELECT *
FROM task
```

devuelve datos tabulares de la tabla, que Hibernate después debe transformar en una entidad si la consulta SQL nativa está vinculada a Task.<br>
<br>
Además, en JPQL se puede seleccionar una propiedad concreta:

```
SELECT t.title
FROM Task t
```

En SQL equivalente se seleccionarían columnas concretas:

```
SELECT id, title
FROM task;
```

Asociaciones<br>
La diferencia es especialmente útil con relaciones.<br>
<br>
En JPQL:

```
SELECT t
FROM Task t
JOIN t.category c
WHERE c.name = :categoryName
```

se navega por los atributos de las entidades.<br>
<br>
En SQL hay que conocer las tablas y las claves foráneas:

```SELECT t.*
FROM task t
JOIN category c
    ON t.category_id = c.id
WHERE c.name = ?;
```

JPQL usa t.category porque conoce la relación JPA; SQL necesita escribir explícitamente la condición t.category_id = c.id.<br>
<br>
Resumen

![Parte3Askc](..\capturas\Captura-04-parte3Askc.png)

En resumen, JPQL usa el nombre de la clase porque consulta el modelo de entidades. La anotación @Table(name = "task") y las anotaciones @Column permiten que Hibernate traduzca ese modelo Java al esquema físico de la base de datos.

--
<br>
Pregunta:<br>
<br>
¿Qué hace JOIN t.tags tag en la query de búsqueda por tag? ¿Cómo sabe JPA que debe usar la tabla task_tag?<br>
<br>
Respuesta:<br>
<br>
En una consulta como:

```
@Query("""
    SELECT DISTINCT t
    FROM Task t
    JOIN t.tags tag
    WHERE tag.name = :tagName
""")
List<Task> findByTagName(@Param("tagName") String tagName);
```

Une cada entidad Task con los elementos de su colección tags y asigna el alias tag a cada entidad Tag relacionada.<br>
<br>
Descomposición

```
FROM Task t
```

Indica que la consulta parte de la entidad Task. t es su alias.

```
JOIN t.tags tag
```

Navega desde el atributo tags de Task hasta las entidades Tag relacionadas. El segundo tag es el alias de la entidad relacionada.

```
WHERE tag.name = :tagName
```

Filtra usando un atributo de Tag.<br>
<br>
Por ejemplo, busca las tareas que tengan una etiqueta llamada "Java".<br>
<br>
¿Cómo conoce JPA task_tag?<br>
JPA no deduce la tabla intermedia a partir del texto t.tags. La conoce porque está definida en el mapeo de la relación:

```
@Entity
public class Task {

    @ManyToMany
    @JoinTable(
        name = "task_tag",
        joinColumns = @JoinColumn(name = "task_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags;
}
```

Ese mapeo le indica a Hibernate:

```
Task.tags utiliza la tabla task_tag
task_tag.task_id apunta a task.id
task_tag.tag_id apunta a tag.id
```

La entidad inversa podría ser:

```
@Entity
public class Tag {

    @ManyToMany(mappedBy = "tags")
    private Set<Task> tasks;
}
```

mappedBy = "tags" indica que Tag.tasks utiliza la relación definida por Task.tags. No crea una segunda tabla intermedia.<br>
<br>
SQL equivalente<br>
La consulta JPQL:

```
SELECT DISTINCT t
FROM Task t
JOIN t.tags tag
WHERE tag.name = :tagName
```

se traduce aproximadamente a:

```
SELECT DISTINCT t.*
FROM task t
JOIN task_tag tt
    ON tt.task_id = t.id
JOIN tag tag
    ON tag.id = tt.tag_id
WHERE tag.name = ?;
```

El SQL exacto puede variar según Hibernate, la versión y el dialecto de MySQL, pero conceptualmente contiene esos tres elementos:<br>
<br>
la tabla task;<br>
la tabla intermedia task_tag;<br>
la tabla tag.<br>
¿Por qué hacen falta dos JOIN?<br>
Porque Task y Tag no se relacionan directamente mediante una columna en una de sus tablas:

```
task
----
id

tag
---
id

task_tag
--------
task_id
tag_id
```

La tabla intermedia conecta ambas:

```
task
----
id

tag
---
id

task_tag
--------
task_id
tag_id
```

La tabla intermedia conecta ambas:

```
task.id
   ↑
task_tag.task_id

task_tag.tag_id
   ↓
tag.id
```

Por eso Hibernate genera conceptualmente:

```
task
  JOIN task_tag
  JOIN tag
```

Aunque en JPQL solo se escribe:

```
JOIN t.tags tag
```

¿Qué tipo de JOIN es?<br>
JOIN sin más equivale normalmente a un INNER JOIN. Por tanto, solo devuelve tareas que tengan una etiqueta que cumpla la condición.<br>
<br>
Una tarea sin etiquetas no aparecerá en el resultado.<br>
<br>
Para conservar también tareas sin etiquetas se usaría LEFT JOIN, aunque al filtrar por tag.name hay que colocar las condiciones cuidadosamente:

```
SELECT DISTINCT t
FROM Task t
LEFT JOIN t.tags tag
WHERE tag.name = :tagName
   OR tag IS NULL
```

¿Por qué se usa DISTINCT?<br>
Si una tarea cumple varias coincidencias en la relación, la unión SQL puede producir varias filas para la misma tarea. DISTINCT evita que la lista de entidades contenga duplicados:

```
SELECT DISTINCT t
```

Por ejemplo, sin DISTINCT, una tarea con varias filas coincidentes en la unión podría aparecer repetida.<br>
<br>
Importante: el lado propietario<br>
En este diseño, Task es el lado propietario porque declara @JoinTable:

```
@ManyToMany
@JoinTable(name = "task_tag", ...)
private Set<Tag> tags;
```

Pero para realizar esta consulta, lo esencial es que JPA conozca el atributo persistente tags y su asociación @ManyToMany. El hecho de que Task sea propietario determina principalmente qué lado actualiza la tabla intermedia cuando se modifica la relación.<br>
<br>
Por ejemplo:

```
task.getTags().add(tag);
```

modifica la asociación persistida desde el lado propietario.<br>
<br>
Situación actual del proyecto<br>
Actualmente Task.java no tiene un atributo tags; solo contiene:

```
@Column(name = "category_id")
private Long categoryId;
```

Por tanto, JOIN t.tags tag no funcionaría todavía en este proyecto. Solo sería válido después de añadir la entidad Tag y una relación como:

```
@ManyToMany
@JoinTable(
    name = "task_tag",
    joinColumns = @JoinColumn(name = "task_id"),
    inverseJoinColumns = @JoinColumn(name = "tag_id")
)
private Set<Tag> tags;
```

En resumen, JPQL navega por la asociación Java t.tags, y Hibernate consulta la tabla task_tag porque esa tabla está declarada en el metamodelo JPA mediante @JoinTable.

--
<br>
Pregunta:<br>
<br>
¿Qué diferencia hay entre una consulta derivada del nombre del método (findByStatusAndPriority) y una escrita con @Query? ¿Cuál de las consultas de esta parte podría haberse resuelto también solo con el nombre del método?<br>
<br>
Respuesta:<br>
<br>
Una consulta derivada se define únicamente mediante el nombre del método:

```
List<Task> findByStatusAndPriority(
    TaskStatus status,
    TaskPriority priority
);
```

Spring Data interpreta el nombre:

```
find By Status And Priority
```

y genera una consulta equivalente a:

```
SELECT t
FROM Task t
WHERE t.status = ?1
  AND t.priority = ?2
```

No se escribe JPQL ni se necesita @Param.<br>
<br>
En cambio, con @Query, la consulta se escribe explícitamente:

```
@Query("""
    SELECT t
    FROM Task t
    WHERE t.status = :status
      AND t.priority = :priority
""")
List<Task> search(
    @Param("status") TaskStatus status,
    @Param("priority") TaskPriority priority
);
```

Aquí @Param vincula:

```
:status   → parámetro status
:priority → parámetro priority
```

Diferencias principales

![Parte3Askd](..\capturas\Captura-04-parte3Askd.png)

Consultas de esta parte que podían resolverse con el nombre<br>
1. Por estado y prioridad<br>
La consulta:

```
@Query("""
    SELECT t
    FROM Task t
    WHERE t.status = :status
      AND t.priority = :priority
""")
List<Task> searchByStatusAndPriority(...);
```

podría escribirse simplemente como:

```
List<Task> findByStatusAndPriority(
    TaskStatus status,
    TaskPriority priority
);
```

De hecho, este método ya existe en TaskRepository.java.<br>
<br>
2. Por categoryId<br>
Con el modelo actual, donde Task tiene un campo Long categoryId, bastaría:

```
List<Task> findByCategoryId(Long categoryId);
```

Equivale conceptualmente a:

```
SELECT t
FROM Task t
WHERE t.categoryId = ?1
```

3. Por una propiedad de Category<br>
Si existiera la relación:

3. Por una propiedad de Category<br>
Si existiera la relación:

```
private Category category;
```

se podría escribir:

```
List<Task> findByCategoryId(Long categoryId);

List<Task> findByCategoryName(String categoryName);
```

Spring Data navega automáticamente por las propiedades relacionadas.<br>
<br>
También se puede usar la forma explícita con guion bajo:

```
List<Task> findByCategory_Name(String categoryName);
```

La forma depende de la convención y de la versión, pero ambas expresan la navegación hacia category.name.<br>
<br>
4. Por Tag<br>
Para una relación:

```
private Set<Tag> tags;
```

podrían usarse métodos derivados como:

```
List<Task> findDistinctByTagsId(Long tagId);

List<Task> findDistinctByTagsName(String tagName);
```

Distinct es importante porque una relación @ManyToMany puede producir varias filas SQL para la misma tarea.<br>
<br>
Estos métodos podrían sustituir a una consulta como:

```
@Query("""
    SELECT DISTINCT t
    FROM Task t
    JOIN t.tags tag
    WHERE tag.name = :tagName
""")
List<Task> findByTagName(@Param("tagName") String tagName);
```

Consultas que necesitan o justifican @Query<br>
JOIN FETCH<br>
Esta consulta:

```
@Query("""
    SELECT DISTINCT t
    FROM Task t
    JOIN FETCH t.tags tag
    WHERE tag.name = :tagName
""")
List<Task> findByTagNameWithTags(@Param("tagName") String tagName);
```

no se expresa de forma equivalente y clara mediante un método derivado, porque no solo filtra por tags.name: también ordena a JPA cargar la colección mediante JOIN FETCH.<br>
<br>
Dos etiquetas diferentes<br>
La consulta:

```
@Query("""
    SELECT DISTINCT t
    FROM Task t
    JOIN t.tags tagJava
    JOIN t.tags tagSpring
    WHERE tagJava.name = :firstTag
      AND tagSpring.name = :secondTag
""")
List<Task> findByTwoTags(...);
```

es mucho más adecuada con @Query. Un nombre derivado sería demasiado largo y difícil de leer:

```
findDistinctByTagsNameAndTagsName(...)
```

Además, esa forma puede no expresar con claridad que se necesitan dos alias o dos coincidencias distintas sobre la colección.<br>
<br>
Condiciones complejas con OR<br>
Una consulta como:

```
WHERE (t.status = :status AND t.priority = :priority)
   OR t.categoryId = :categoryId
```

podría intentar expresarse mediante un nombre derivado, pero sería poco legible y la precedencia de And y Or puede resultar confusa. @Query deja el agrupamiento explícito:

```
@Query("""
    SELECT t
    FROM Task t
    WHERE (t.status = :status AND t.priority = :priority)
       OR t.categoryId = :categoryId
""")
List<Task> search(...);
```

Regla práctica<br>
Usa un método derivado cuando la consulta sea un filtro directo:

```
findByStatus(...)
findByStatusAndPriority(...)
findByCategoryId(...)
findDistinctByTagsName(...)
```

Usa @Query cuando necesites:<br>
<br>
JOIN FETCH;<br>
varias uniones o alias distintos;<br>
agrupaciones complejas con AND y OR;<br>
proyecciones específicas;<br>
funciones JPQL;<br>
controlar explícitamente la consulta generada.<br>
En resumen, las consultas sencillas de esta sección —por status, priority, status y priority, categoryId, category.name o tag.name— podrían resolverse mediante nombres de método. Las consultas con JOIN FETCH, dos etiquetas, condiciones complejas o un control más preciso de la carga se benefician de @Query.

--
<br>
Pregunta:<br>
<br>
¿Qué SQL genera Hibernate para SELECT COUNT(t) FROM Task t WHERE t.status = :status? ¿Por qué se usa COUNT(t) y no COUNT(*)?<br>
<br>
Respuesta:<br>
<br>
Para una consulta JPQL como:

```
SELECT COUNT(t)
FROM Task t
WHERE t.status = :status
```

Hibernate la traduce a SQL aproximadamente así:

```
SELECT COUNT(id)
FROM task
WHERE status = ?;
```

El parámetro ? recibe el valor de :status, por ejemplo:

```
IN_PROGRESS
```

Como Task.java tiene id como clave primaria, ese campo nunca puede ser NULL.<br>
<br>
¿Por qué COUNT(t)?<br>
En JPQL, t representa una entidad Task:

```
FROM Task t
```

Por tanto:

```
COUNT(t)
```

Cuenta las entidades Task que cumplen la condición.<br>
<br>
JPQL trabaja con entidades y atributos Java, no directamente con columnas SQL. Hibernate conoce que la identidad de Task es id:

```
@Id
private Long id;
```

y puede traducir COUNT(t) a:

```
COUNT(t1_0.id)
```

¿Por qué no COUNT(*)?<br>
COUNT(*) pertenece a SQL y significa:<br>
<br>
Cuenta todas las filas que cumplen la condición.<br>
<br>
En JPQL, la sintaxis estándar es contar una entidad o una propiedad:

```
COUNT(t)
```

o:

```
COUNT(t.id)
```

COUNT(*) no es la forma portable habitual de escribir una consulta JPQL. Si se desea utilizar SQL directamente, hay que emplear una consulta nativa:

```
@Query(
    value = """
        SELECT COUNT(*)
        FROM task
        WHERE status = :status
    """,
    nativeQuery = true
)
long countNativeByStatus(@Param("status") String status);
```

¿Son diferentes COUNT(t) y COUNT(*)?<br>
Para este caso concreto, normalmente producen el mismo resultado porque t.id es una clave primaria no nula:

```
COUNT(t.id)
```

y:

```
COUNT(*)
```

contarán todas las filas de task cuyo status coincida.<br>
<br>
La diferencia general en SQL es:

```
COUNT(*)       -- cuenta filas
COUNT(columna) -- cuenta valores no nulos de esa columna
```

Por ejemplo, si description pudiera ser NULL:

```
COUNT(description)
```

no contaría las filas cuya descripción sea nula, mientras que:

```
COUNT(*)
```

sí las contaría.<br>
<br>
Como la clave primaria id nunca es nula, esto es seguro:

```
COUNT(id)
```

¿Qué devuelve el método?<br>
Una consulta de conteo devuelve normalmente un Long:

```
@Query("""
    SELECT COUNT(t)
    FROM Task t
    WHERE t.status = :status
""")
long countByStatus(@Param("status") TaskStatus status);
```

También puede utilizarse Long:

```
Long countByStatus(TaskStatus status);
```

Ejemplo de resultado:

```
status = IN_PROGRESS
resultado = 5
```

Conclusión

```
COUNT(t)
```

es la forma orientada a entidades y portable de JPQL. Hibernate sabe que t es una entidad Task y suele generar:

```
SELECT COUNT(t1_0.id)
FROM task t1_0
WHERE t1_0.status = ?;
```

COUNT(*) es una expresión SQL válida, pero no es la forma estándar de expresar este conteo en JPQL.

--
<br>
Pregunta:<br>
<br>
¿Cuándo escribirías una consulta con @Query en JPQL en vez de dejar que Spring Data la genere por el nombre del método?<br>
<br>
Respuesta:<br>
<br>
Escribiría una consulta con @Query cuando la consulta deja de ser un filtro sencillo que pueda expresarse de forma clara y mantenible mediante el nombre del método.<br>
<br>
Usaría un método derivado cuando…<br>
La condición es simple y el nombre sigue siendo legible:

```
List<Task> findByStatus(TaskStatus status);

List<Task> findByStatusAndPriority(
    TaskStatus status,
    TaskPriority priority
);

List<Task> findByCategoryId(Long categoryId);
```

Spring Data puede interpretar esos nombres y generar la consulta automáticamente:

```
SELECT t
FROM Task t
WHERE t.status = ?1
  AND t.priority = ?2
```

Esto evita escribir código JPQL innecesario.<br>
<br>
Usaría @Query cuando…<br>
1. La consulta necesita JOIN FETCH<br>
Si se quiere filtrar por una relación y cargarla en la misma consulta:

```
@Query("""
    SELECT DISTINCT t
    FROM Task t
    JOIN FETCH t.tags tag
    WHERE tag.name = :tagName
""")
List<Task> findByTagNameWithTags(@Param("tagName") String tagName);
```

Un método derivado como:

```
findDistinctByTagsName(...)
```

puede filtrar por la etiqueta, pero no expresa claramente el JOIN FETCH.<br>
<br>
2. Hay varias uniones o alias distintos<br>
Para buscar tareas que tengan dos etiquetas diferentes:

```
@Query("""
    SELECT DISTINCT t
    FROM Task t
    JOIN t.tags firstTag
    JOIN t.tags secondTag
    WHERE firstTag.name = :firstName
      AND secondTag.name = :secondName
""")
List<Task> findByTwoTags(
    @Param("firstName") String firstName,
    @Param("secondName") String secondName
);
```

El nombre derivado equivalente sería demasiado largo y poco claro.<br>
<br>
3. Las condiciones tienen agrupaciones complejas<br>
Por ejemplo:

```
@Query("""
    SELECT t
    FROM Task t
    WHERE (t.status = :status AND t.priority = :priority)
       OR t.categoryId = :categoryId
""")
List<Task> searchTasks(
    @Param("status") TaskStatus status,
    @Param("priority") TaskPriority priority,
    @Param("categoryId") Long categoryId
);
```

@Query deja explícitos los paréntesis y evita depender de cómo se interpreten And y Or en un nombre largo.<br>
<br>
4. Se necesita una proyección concreta<br>
Si solo se necesitan algunos campos:

```
@Query("""
    SELECT new com.accenture.taskmanager.dto.TaskSummary(
        t.id,
        t.title
    )
    FROM Task t
    WHERE t.status = :status
""")
List<TaskSummary> findSummariesByStatus(
    @Param("status") TaskStatus status
);
```

Esto evita cargar toda la entidad cuando la respuesta solo necesita un subconjunto de datos.<br>
<br>
5. Se necesita una consulta de agregación<br>
Para contar o agrupar:

```
@Query("""
    SELECT t.status, COUNT(t)
    FROM Task t
    GROUP BY t.status
""")
List<Object[]> countByStatus();
```

También se puede proyectar el resultado a un DTO o una interfaz.<br>
<br>
6. La consulta necesita funciones o expresiones<br>
Por ejemplo:

```
@Query("""
    SELECT t
    FROM Task t
    WHERE LOWER(t.title) LIKE LOWER(CONCAT('%', :text, '%'))
""")
List<Task> searchByTitle(@Param("text") String text);
```

Aunque algunas funciones también tienen palabras clave derivadas, con @Query se expresan de forma más controlada.<br>
<br>
7. Se necesita actualizar o eliminar directamente<br>
Para una operación masiva:

```
@Modifying
@Query("""
    UPDATE Task t
    SET t.status = :newStatus
    WHERE t.status = :oldStatus
""")
int updateStatus(
    @Param("oldStatus") TaskStatus oldStatus,
    @Param("newStatus") TaskStatus newStatus
);
```

Estas consultas suelen necesitar una transacción en el servicio.<br>
<br>
8. El nombre derivado sería demasiado largo<br>
Un método como:

```
findByStatusAndPriorityAndDueDateBetweenAndCategoryIdAndTitleContainingIgnoreCase(...)
```

puede ser válido, pero difícil de leer y mantener. Una consulta con @Query puede expresar la misma lógica de forma más clara.<br>
<br>
Comparación

![Parte3Aske](..\capturas\Captura-04-parte3Aske.png)

Aplicado a las consultas de este proyecto<br>
Estas consultas pueden ser métodos derivados:

```
List<Task> findByStatus(TaskStatus status);

List<Task> findByPriority(TaskPriority priority);

List<Task> findByStatusAndPriority(
    TaskStatus status,
    TaskPriority priority
);
```

También podrían escribirse con @Query, pero no aportaría ventaja:

```
@Query("""
    SELECT t
    FROM Task t
    WHERE t.status = :status
      AND t.priority = :priority
""")
List<Task> search(
    @Param("status") TaskStatus status,
    @Param("priority") TaskPriority priority
);
```

En cambio, una búsqueda por etiquetas con carga explícita:

```
@Query("""
    SELECT DISTINCT t
    FROM Task t
    JOIN FETCH t.tags tag
    WHERE tag.name = :tagName
""")
```

sí justifica @Query, porque expresa una estrategia de carga que el simple nombre del método no comunica con claridad.<br>
<br>
La regla práctica es:<br>
<br>
Usa métodos derivados para consultas cortas, directas y legibles. Usa @Query cuando necesites controlar explícitamente los joins, la carga, las proyecciones, las agregaciones o la lógica de la consulta.

---
<br>
Paso 5 - Validación en Postman<br>
<br>
Get Tareas por Categoría

![GetTareasPorCategoria](..\capturas\Captura-05-Prueba-01-GET-Tareas-por-Categoria.png)

<br>

Get Tareas por Tag

![GetTareasPorTag](..\capturas\Captura-05-Prueba-02-GET-Tareas-por-Tag.png)

<br>

Get Tareas por Estado y Prioridad

![GetTareasPorEstadoYPrioridad](..\capturas\Captura-05-Prueba-03-GET-Tareas-por-Estado-y-Prioridad.png)

Get Tareas antes de una Fecha

![GetTareasAntesDeUnaFecha](..\capturas\Captura-05-Prueba-04-GET-Tareas-Antes-de-Fecha.png)

Get Contar Tareas Por Estado

![GetContarTareasPorEstado](..\capturas\Captura-05-Prueba-05-GET-Contar-Tareas-por-Estado.png)

Get Categoría sin Tareas

![GetContarTareasPorEstado](..\capturas\Captura-05-Prueba-06-GET-Categoria-Sin-Tareas.png)

Comprobación de que está el show-sql=true

![showSqlTrue](Captura-05-show-sql-true.png)

---
<br>
INCIDENCIA<br>
Se me habían olvidado las anotaciones @JsonIgnore así que repito las pruebas:<br>
<br>
Get Tareas por Categoría

![GetTareasPorCategoriab](..\capturas\Captura-05-Prueba-01-GET-Tareas-por-Categoriab.png)

<br>

Get Tareas por Tag

![GetTareasPorTagb](..\capturas\Captura-05-Prueba-02-GET-Tareas-por-Tagb.png)

<br>

Get Tareas por Estado y Prioridad

![GetTareasPorEstadoYPrioridadb](..\capturas\Captura-05-Prueba-03-GET-Tareas-por-Estado-y-Prioridad.png)

Get Tareas antes de una Fecha

![GetTareasAntesDeUnaFechab](..\capturas\Captura-05-Prueba-04-GET-Tareas-Antes-de-Fechab.png)

INCIDENCIA:<br>
<br>
Vuelve a fallar, se repite la prueba y funciona sí que funciona bien, vete a saber porqué falló...

![GetContarAntesDeUnaFechac](..\capturas\Captura-05-Prueba-04-GET-Tareas-Antes-de-Fechac.png)

Get Contar Tareas Por Estado

![GetContarTareasPorEstadob](..\capturas\Captura-05-Prueba-05-GET-Contar-Tareas-por-Estadob.png)

Get Categoría sin Tareas

![GetContarTareasPorEstadob](..\capturas\Captura-05-Prueba-06-GET-Categoria-Sin-Tareasb.png)

---
<br>
Reflexión:<br>
<br>
Pregunta:<br>
<br>
1. ¿Qué ventaja tiene un query method por nombre frente a escribir el JPQL con @Query?<br>
<br>
Respuesta:<br>
<br>
Es más sencillo y legible para consultas simples, y evita escribir la consulta manualmente.<br>
<br>
Pregunta:<br>
<br>
2. ¿Qué problema de rendimiento podría surgir si la tabla task tuviera millones de registros sin índices en las columnas filtradas?<br>
<br>
Respuesta:<br>
<br>
La base de datos podría tener que recorrer muchos registros para encontrar los resultados, aumentando el tiempo de respuesta.
<br>
<br>
Pregunta:<br>
<br>
3. ¿Cuándo dejarías de usar query methods por nombre y empezaría a usar @Query?<br>
<br>
Respuesta:<br>
<br>
Cuando la consulta sea compleja y necesite condiciones, joins o lógica que resulte difícil de expresar mediante el nombre del método.

---
<br>
Parte 4<br>
<br>
Ejercicio para la creación de Paginación y ordenación<br>
<br>
Paso 1 - Preparación (Ask)<br>
<br>
Pregunta:<br>
<br>
¿Qué es Pageable en Spring Data JPA y cómo permite controlar la paginación de los resultados?<br>
<br>
Respuesta:<br>
<br>
Pageable es una interfaz de Spring Data que representa los parámetros de paginación y ordenación de una consulta:<br>
<br>
número de página;<br>
tamaño de página;<br>
orden de los resultados.<br>
La numeración de las páginas empieza en 0.<br>
<br>
Ejemplo básico<br>
Un repositorio puede declarar:

```
public interface TaskRepository extends JpaRepository<Task, Long> {

    Page<Task> findByStatus(
        TaskStatus status,
        Pageable pageable
    );
}
```

Y se utilizaría así:

```
Pageable pageable = PageRequest.of(0, 10);
Page<Task> page = taskRepository.findByStatus(
    TaskStatus.IN_PROGRESS,
    pageable
);
```

Esto solicita:

```
página 0
10 tareas por página
```

Ordenación<br>
También se puede indicar el orden:

```
Pageable pageable = PageRequest.of(
    0,
    10,
    Sort.by(Sort.Direction.DESC, "dueDate")
);
```

La propiedad "dueDate" es el atributo Java de la entidad, no necesariamente el nombre físico de la columna. Hibernate conoce que ese atributo está mapeado a due_date.<br>
<br>
Se pueden encadenar varios criterios:

```
Sort sort = Sort.by(
    Sort.Order.asc("status"),
    Sort.Order.desc("dueDate")
);

Pageable pageable = PageRequest.of(0, 10, sort);
```

Page<T><br>
El resultado más habitual es Page<T>:

```
Page<Task> page = taskRepository.findAll(pageable);
```

Además de los elementos de la página, proporciona información como:

```
page.getContent();       // tareas de la página actual
page.getNumber();        // número de página, empezando en 0
page.getSize();          // tamaño solicitado
page.getTotalElements(); // número total de registros
page.getTotalPages();    // número total de páginas
page.hasNext();          // si existe una página siguiente
page.hasPrevious();      // si existe una página anterior
```

Por ejemplo, una respuesta JSON podría tener esta forma:

```
{
  "content": [
    {
      "id": 1,
      "title": "Preparar informe"
    }
  ],
  "number": 0,
  "size": 10,
  "totalElements": 37,
  "totalPages": 4,
  "first": true,
  "last": false
}
```

SQL que genera Hibernate<br>
Para una consulta paginada en MySQL, Hibernate generará conceptualmente:

```
SELECT
    id,
    title,
    description,
    status,
    priority,
    due_date,
    created_at,
    category_id
FROM task
ORDER BY due_date DESC
LIMIT ? OFFSET ?;
```

Si se solicita:

```
PageRequest.of(2, 10)
```

la página es la tercera, porque la primera es la página 0. Conceptualmente:

```
LIMIT 10 OFFSET 20
```

Cuando el resultado es un Page<T>, Spring Data normalmente ejecuta también una consulta de conteo para conocer el total:

```
SELECT COUNT(id)
FROM task;
```

o, si hay filtros:

```
SELECT COUNT(id)
FROM task
WHERE status = ?;
```

Por eso una consulta con Page<T> puede provocar dos operaciones:<br>
<br>
obtener los registros de la página;<br>
contar el total de registros.<br>
Page frente a Slice<br>
Si no necesitas conocer el número total de registros, puedes utilizar Slice<T>:

```
Slice<Task> findByStatus(
    TaskStatus status,
    Pageable pageable
);
```

Slice informa de si existe una página siguiente, pero no proporciona:

```
getTotalElements()
getTotalPages()
```

Al no necesitar el total, puede evitar la consulta adicional COUNT.

![Paso5Ask](..\capturas\Captura-05-Paso-5-Ask.png)
<br>
Uso desde un controlador<br>
Un controlador podría recibir la paginación mediante parámetros HTTP:

```
@GetMapping
public Page<Task> findAll(
    @PageableDefault(size = 10, sort = "dueDate") Pageable pageable
) {
    return taskRepository.findAll(pageable);
}
```

Una petición como:

```
GET /tasks?page=0&size=10&sort=dueDate,desc
```

significaría:

```
página 0
10 elementos
ordenados por dueDate descendente
```

En una arquitectura con servicio, lo habitual sería pasar Pageable al servicio:

```
public Page<Task> findAll(Pageable pageable) {
    return taskRepository.findAll(pageable);
}
```

Aplicado al proyecto<br>
El repositorio de TaskRepository.java hereda de JpaRepository, por lo que ya dispone de:

```
Page<Task> findAll(Pageable pageable);
```

No hace falta declarar ese método de nuevo.<br>
<br>
Para una búsqueda por estado se podría añadir:

```
Page<Task> findByStatus(
    TaskStatus status,
    Pageable pageable
);
```

La paginación no cambia la consulta lógica; añade límites, desplazamiento y, si se utiliza Page, una consulta para contar los resultados.<br>
<br>
Como regla práctica:<br>
<br>
usa Pageable para controlar página, tamaño y orden;<br>
usa Page<T> cuando necesites el total de resultados;<br>
usa Slice<T> cuando solo necesites saber si existe una página siguiente;<br>
valida los tamaños máximos recibidos desde el cliente para evitar peticiones excesivamente grandes.

---
<br>
Pregunta:<br>
<br>
¿Qué diferencia hay entre devolver Page<Task> y List<Task>, y qué información adicional proporciona Page<Task>?<br>
<br>
Respuesta:<br>
<br>
La diferencia principal es que List<Task> contiene solo los resultados, mientras que Page<Task> contiene los resultados más los metadatos de paginación.<br>
<br>
List<Task>

```
List<Task> findByStatus(TaskStatus status);
```

Devuelve una lista de tareas:

```
List<Task> tasks = taskRepository.findByStatus(TaskStatus.IN_PROGRESS);
```

Devuelve una lista de tareas:

```
List<Task> tasks = taskRepository.findByStatus(TaskStatus.IN_PROGRESS);
```

Permite:<br>
<br>
recorrer los resultados;<br>
consultar su tamaño con tasks.size();<br>
acceder a cada elemento.<br>
Pero no informa directamente de:<br>
<br>
número de página;<br>
tamaño solicitado;<br>
total de registros disponibles;<br>
total de páginas;<br>
si existe una página anterior o siguiente.<br>
Si el método no recibe Pageable, normalmente devuelve todos los resultados que cumplen el filtro:

```
SELECT *
FROM task
WHERE status = ?;
```

Con muchos registros, esto puede consumir demasiada memoria y aumentar el tiempo de respuesta.<br>
<br>
Page<Task>

```
Page<Task> findByStatus(
    TaskStatus status,
    Pageable pageable
);
```

Ejemplo:

```
Pageable pageable = PageRequest.of(0, 10);
Page<Task> page = taskRepository.findByStatus(
    TaskStatus.IN_PROGRESS,
    pageable
);
```

La página proporciona los resultados actuales:

```
List<Task> tasks = page.getContent();
```

y datos adicionales:

```
page.getNumber();          // número de página actual, empieza en 0
page.getSize();            // tamaño solicitado
page.getNumberOfElements();// elementos de esta página
page.getTotalElements();   // total de elementos que cumplen el filtro
page.getTotalPages();      // total de páginas
page.hasNext();            // existe página siguiente
page.hasPrevious();        // existe página anterior
page.isFirst();            // es la primera página
page.isLast();             // es la última página
```

Ejemplo<br>
Si existen 37 tareas y se solicitan 10 por página:

```
PageRequest.of(1, 10)
```

el resultado representa la segunda página:

```
number          = 1
size            = 10
numberOfElements= 10
totalElements   = 37
totalPages      = 4
hasNext         = true
hasPrevious     = true
isFirst         = false
isLast          = false
```

page.getContent() contiene únicamente las 10 tareas de esa página.<br>
<br>
Consultas SQL<br>
Con List<Task> sin paginación:

```
SELECT *
FROM task
WHERE status = ?;
```

Con Page<Task>:

```
SELECT
    id,
    title,
    description,
    status,
    priority,
    due_date,
    created_at,
    category_id
FROM task
WHERE status = ?
ORDER BY due_date DESC
LIMIT ? OFFSET ?;
```

Y normalmente una segunda consulta para obtener el total:

```
SELECT COUNT(id)
FROM task
WHERE status = ?;
```

Por eso Page<Task> proporciona información completa de paginación, pero puede ejecutar una consulta adicional de conteo.<br>
<br>
Comparación

![Paso5Askb](..\capturas\Captura-05-Paso-5-Askb.png)

¿Cuál usar?<br>
Usa List<Task> cuando:<br>
<br>
el conjunto de resultados es pequeño;<br>
no necesitas paginación;<br>
una consulta devuelve una lista completa razonablemente limitada;<br>
estás trabajando con datos internos acotados.<br>
Usa Page<Task> cuando:<br>
<br>
la API expone paginación;<br>
necesitas mostrar controles de páginas;<br>
el cliente necesita conocer el total;<br>
la consulta puede devolver muchos registros.<br>
Si solo necesitas saber si existe una página siguiente, pero no el total, puedes usar Slice<Task>:

```
Slice<Task> findByStatus(
    TaskStatus status,
    Pageable pageable
);
```

Slice evita normalmente la consulta COUNT, aunque no proporciona getTotalElements() ni getTotalPages().<br>
<br>
En resumen, List<Task> representa únicamente una colección de tareas; Page<Task> representa una página de resultados y describe su posición, tamaño, total y navegación.

---
<br>
Pregunta:<br>
<br>
¿Por qué la paginación mejora el rendimiento de una API cuando la tabla contiene muchos registros y qué ocurre si no se utiliza?<br>
<br>
Respuesta:<br>
<br>
La paginación mejora el rendimiento porque evita recuperar y devolver todos los registros de una tabla en una sola petición.<br>
<br>
Sin paginación<br>
Si el repositorio devuelve:

```
List<Task> findAll();
```

Hibernate puede ejecutar:

```
SELECT *
FROM task;
```

Si la tabla contiene 100.000 tareas:<br>
<br>
la base de datos debe buscar y devolver todas;<br>
Hibernate debe convertirlas en 100.000 objetos Task;<br>
la aplicación consume más memoria;<br>
la respuesta JSON es muy grande;<br>
aumenta el tiempo de consulta, serialización y transferencia;<br>
el cliente recibe muchos datos que quizá no necesita.<br>
Además, varias peticiones simultáneas pueden provocar un consumo elevado de CPU, memoria y conexiones de base de datos.<br>
<br>
Con paginación<br>
El repositorio puede recibir un Pageable:

```
Page<Task> findAll(Pageable pageable);
```

Por ejemplo:

```
Pageable pageable = PageRequest.of(0, 20);
Page<Task> page = taskRepository.findAll(pageable);
```

Hibernate generará conceptualmente:

```
SELECT
    id,
    title,
    description,
    status,
    priority,
    due_date,
    created_at,
    category_id
FROM task
ORDER BY id
LIMIT 20 OFFSET 0;
```

Solo se recuperan 20 tareas. La respuesta también es más pequeña y rápida.<br>
<br>
Si se devuelve Page<Task>, Spring Data suele ejecutar además una consulta para conocer el total:

```
SELECT COUNT(id)
FROM task;
```

Esta consulta adicional tiene un coste, pero normalmente es mucho menor que cargar todas las entidades y devolverlas al cliente.<br>
<br>
Ventajas principales<br>
La paginación reduce:<br>
<br>
memoria utilizada por Hibernate;<br>
cantidad de datos transferidos;<br>
tamaño de la respuesta JSON;<br>
tiempo de serialización;<br>
tiempo de respuesta de la API;<br>
carga sobre el cliente;<br>
riesgo de respuestas demasiado grandes.<br>
También permite que la interfaz muestre los datos progresivamente, por ejemplo, 20 tareas cada vez.<br>
<br>
¿Qué ocurre si no se utiliza?<br>
No usar paginación no siempre es incorrecto. Puede ser razonable para una tabla pequeña. Pero si la tabla crece, pueden aparecer:<br>
<br>
respuestas lentas;<br>
consumo excesivo de memoria;<br>
errores OutOfMemoryError;<br>
timeouts;<br>
alto tráfico de red;<br>
ralentización de la base de datos;<br>
bloqueo o saturación de la API;<br>
una experiencia de usuario deficiente.<br>
El problema no es solo el número de filas actuales, sino que el código queda expuesto al crecimiento futuro de la tabla.<br>
<br>
Page, Slice y List

```
Page<Task> findAll(Pageable pageable);
```

Devuelve los datos de la página y metadatos como total de elementos y total de páginas. Normalmente necesita una consulta COUNT.

```
Slice<Task> findAll(Pageable pageable);
```

Devuelve los datos y si existe una página siguiente, pero normalmente no necesita contar todos los registros.

```
List<Task> findAll(Pageable pageable);
```

Puede limitar los resultados, pero no proporciona los metadatos de Page.<br>
<br>
Si solo se necesita una lista limitada y no el total:

```
List<Task> findAllBy(Pageable pageable);
```

puede ser suficiente, aunque Slice suele expresar mejor la intención de navegación.<br>
<br>
Un matiz: OFFSET en páginas muy profundas<br>
La paginación tradicional suele utilizar:

```
SELECT *
FROM task
WHERE id > ?
ORDER BY id
LIMIT 20;
```

Este enfoque suele escalar mejor, aunque no proporciona exactamente la misma navegación por número de página.<br>
<br>
Aplicado al proyecto<br>
Como TaskRepository extiende JpaRepository, ya hereda:

```
Page<Task> findAll(Pageable pageable);
```

Por tanto, el servicio podría exponer algo como:

```
public Page<Task> findAll(Pageable pageable) {
    return taskRepository.findAll(pageable);
}
```

La paginación no sustituye a los índices ni a unos filtros adecuados. Para que sea eficiente también conviene:<br>
<br>
ordenar por una columna indexada;<br>
filtrar cuando sea posible;<br>
limitar el tamaño máximo de página;<br>
no permitir tamaños arbitrariamente grandes enviados por el cliente;<br>
seleccionar solo las columnas necesarias mediante proyecciones cuando la entidad sea muy grande.<br>
En resumen, la paginación evita que una petición tenga que cargar, convertir y transmitir toda la tabla. Mantiene acotado el coste de cada respuesta y hace que la API sea más estable cuando el número de tareas aumenta.

---
<br>
Pregunta:<br>
<br>
¿Cómo se integran Pageable, PageRequest y Sort para permitir que el cliente controle la página, el tamaño y el orden de los resultados?<br>
<br>
Respuesta:<br>
<br>
Pageable, PageRequest y Sort trabajan juntos para definir la paginación:<br>
<br>
Pageable: representa la petición de paginación.<br>
PageRequest: crea una implementación concreta de Pageable.<br>
Sort: indica cómo ordenar los resultados.<br>
Creación manual

```
Sort sort = Sort.by(
    Sort.Direction.DESC,
    "dueDate"
);

Pageable pageable = PageRequest.of(
    0,       // página, empieza en 0
    10,      // tamaño
    sort     // orden
);

Page<Task> result = taskRepository.findAll(pageable);
```

Esto solicita la primera página, con 10 tareas, ordenadas por dueDate descendente.<br>
<br>
Hibernate generará conceptualmente:

```
SELECT *
FROM task
ORDER BY due_date DESC
LIMIT 10 OFFSET 0;
```

Recibir los valores desde el cliente<br>
Spring MVC puede construir automáticamente el Pageable a partir de los parámetros de la petición:

```
@GetMapping
public Page<Task> findAll(Pageable pageable) {
    return taskService.findAll(pageable);
}
```

El cliente podría llamar:

```
GET /tasks?page=0&size=10&sort=dueDate,desc
```

Spring interpretará:

```
page = 0
size = 10
sort = dueDate descendente
```

El flujo sería:

```
Parámetros HTTP
      ↓
Spring construye Pageable
      ↓
Servicio recibe Pageable
      ↓
Repositorio ejecuta findAll(pageable)
      ↓
Hibernate genera LIMIT, OFFSET y ORDER BY
```

Pasarlo por la capa de servicio<br>
Lo habitual es mantener el controlador separado del repositorio:

```
@Service
public class TaskServiceImpl implements TaskService {

    public Page<Task> findAll(Pageable pageable) {
        return taskRepository.findAll(pageable);
    }
}
```

Y en el controlador:

```
@GetMapping
public Page<Task> findAll(Pageable pageable) {
    return taskService.findAll(pageable);
}
```

Como TaskRepository hereda de JpaRepository, ya dispone de:

```
Page<Task> findAll(Pageable pageable);
```
Valores predeterminados<br>
Se pueden definir valores por defecto:

```
@GetMapping
public Page<Task> findAll(
    @PageableDefault(
        size = 20,
        sort = "dueDate",
        direction = Sort.Direction.DESC
    )
    Pageable pageable
) {
    return taskService.findAll(pageable);
}
```
Si el cliente no envía parámetros, se utilizarán:

```
página 0
tamaño 20
orden por dueDate descendente
```
También se puede limitar el tamaño máximo mediante configuración o validación propia. No conviene permitir que el cliente solicite, por ejemplo, un millón de registros.<br>
<br>
Filtrar y paginar simultáneamente<br>
El repositorio puede combinar un filtro derivado con Pageable:

```
Page<Task> findByStatus(
    TaskStatus status,
    Pageable pageable
);
```
El controlador podría ser:

```
@GetMapping("/by-status")
public Page<Task> findByStatus(
    @RequestParam TaskStatus status,
    Pageable pageable
) {
    return taskService.findByStatus(status, pageable);
}
```

Petición:

```
GET /tasks/by-status?status=IN_PROGRESS&page=1&size=5&sort=title,asc
```

La consulta conceptual sería:

```
SELECT *
FROM task
WHERE status = ?
ORDER BY title ASC
LIMIT 5 OFFSET 5;
```

La página 1 es la segunda página, porque la numeración empieza en 0.<br>
<br>
Varios criterios de ordenación<br>
El parámetro sort puede repetirse:

```
GET /tasks?sort=status,asc&sort=dueDate,desc
```

Equivale aproximadamente a:

```
Sort sort = Sort.by(
    Sort.Order.asc("status"),
    Sort.Order.desc("dueDate")
);
```

Y genera:

```
ORDER BY status ASC, due_date DESC;
```

Propiedades Java frente a columnas SQL<br>
El cliente debe enviar normalmente el nombre de la propiedad Java:

```
sort=dueDate,desc
```

no necesariamente el nombre de la columna:

```
sort=due_date,desc
```

Hibernate conoce el mapeo de Task.java:

```
@Column(name = "due_date")
private LocalDate dueDate;
```

Por tanto, dueDate se traduce internamente a due_date.<br>
<br>
Validar los campos ordenables<br>
No siempre conviene aceptar cualquier propiedad recibida del cliente. Puede definirse una lista permitida:

```
private static final Set<String> ALLOWED_SORTS = Set.of(
    "id",
    "title",
    "status",
    "priority",
    "dueDate"
);
```

Después se valida el valor antes de construir el Sort. Esto evita errores por propiedades inexistentes y permite impedir que se expongan campos que no deberían utilizarse para ordenar.<br>
<br>
También conviene validar:

```
page >= 0
size > 0
size <= máximo permitido
```

Resumen

![Paso5Askc](..\capturas\Captura-05-Paso-5-Askc.png)

Ejemplo completo:

```
GET /tasks?page=0&size=10&sort=dueDate,desc
```
```
@GetMapping
public Page<Task> findAll(Pageable pageable) {
    return taskService.findAll(pageable);
}
```
```
public Page<Task> findAll(Pageable pageable) {
    return taskRepository.findAll(pageable);
}
```

Así, el cliente controla la página, el tamaño y el orden sin que el controlador tenga que construir manualmente una consulta SQL.

---
<br>
Paso 2 - Diseño previo<br>
<br>
1. ¿Qué parámetros de query expondrás en el endpoint?<br>
<br>
Expondré page para indicar el número de página, size para el número de elementos por página y sort para indicar el campo y el sentido de ordenación.<br>
<br>
Ejemplo: ?page=0&size=10&sort=priority,asc.<br>
<br>
Los valores por defecto serán page=0, size=10 y sort=createdAt,desc. El tamaño máximo de página será 100.<br>
<br>
2. ¿Qué información trae Page<Task> en la respuesta al cliente?<br>
<br>
Incluye el contenido de la página y metadatos como el número de página actual, el tamaño, el total de elementos, el total de páginas y si es la última página.<br>
<br>
3. ¿Cómo afecta la paginación a las queries personalizadas de la Parte 3? ¿Necesitas modificarlas?<br>
<br>
Los métodos de listado por categoría, por tag y por estado y prioridad deben aceptar un Pageable y devolver un Page<Task>. Los endpoints de conteo por estado y búsqueda anterior a una fecha no se paginarán.<br>
<br>
4. ¿Qué comentario exacto escribirás para que el autocompletado lo genere?<br>
<br>
En TaskRepository:<br>
<br>
//Modificar los métodos findByCategoryId, findByTagId y findByStatusAndPriority para aceptar Pageable y devolver Page<Task>, manteniendo sus consultas actuales. —> generado por IA<br>
<br>
En TaskService y TaskServiceImpl:<br>
<br>
//Adaptar los métodos de listado para aceptar Pageable, delegar la paginación al Repository y devolver Page<Task>, conservando la lógica existente. —> generado por IA<br>
<br>
En TaskController:<br>
<br>
//Adaptar los endpoints GET de listado general, por categoría, por tag y por estado y prioridad para aceptar page, size y sort, con valores por defecto page=0, size=10 y sort=createdAt,desc, limitando size a un máximo de 100. —> generado por IA

---
<br>
Paso 3 - Implementación (autocompletado)<br>
<br>
TaskRepository<br>
<br>
Comentario:<br>
<br>
// Modificar los métodos findByCategoryId, findByTagId y findByStatusAndPriority para aceptar Pageable y devolver Page<Task>, manteniendo sus consultas actuales. — generado por IA<br>
<br>
Resultado:

```
// Modificar los métodos findByCategoryId, findByTagId y findByStatusAndPriority para aceptar Pageable y devolver Page<Task>, manteniendo sus consultas actuales. — generado por IA
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByStatus(TaskStatus status);
    List<Task> findByPriority(TaskPriority priority);
    Page<Task> findByStatusAndPriority(TaskStatus status, TaskPriority priority, Pageable pageable);
    // Buscar por categoría
    // Buscar tareas por categoría usando JPQL y @Query, filtrando por el ID de la categoría relacionada con Task. — generado por IA
    @Query("SELECT t FROM Task t WHERE t.category.id = :categoryId")
    Page<Task> findByCategoryId(@Param("categoryId") Long categoryId, Pageable pageable);
    // Buscar tareas por tag usando JPQL y @Query, realizando un JOIN con la relación tags de Task y filtrando por el ID del tag. — generado por IA
    @Query("SELECT t FROM Task t JOIN t.tags tag WHERE tag.id = :tagId")
    Page<Task> findByTagId(@Param("tagId") Long tagId, Pageable pageable);
    // Buscar tareas cuya fecha límite dueDate sea anterior a una fecha recibida como parámetro, utilizando JPQL con @Query y @Param. — generado por IA
    @Query("SELECT t FROM Task t WHERE t.dueDate < :date")
    List<Task> findByDueDateBefore(@Param("date") java.time.LocalDate date );
    // Contar las tareas que tienen un estado concreto utilizando JPQL con COUNT y un parámetro status. — generado por IA
    @Query("SELECT COUNT(t) FROM Task t WHERE t.status = :status")
    long countByStatus(@Param("status") TaskStatus status);

```

<br>
TaskService<br>
<br>
Comentario:<br>
<br>
// Adaptar los métodos de listado para aceptar Pageable, delegar la paginación al Repository y devolver Page<Task>, conservando la lógica existente. —> generado por IA<br>
<br>
Resultado:

```
package com.accenture.taskmanager.service.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.accenture.taskmanager.enums.TaskPriority;

import com.accenture.taskmanager.enums.TaskStatus;
import com.accenture.taskmanager.exceptions.TaskNotFoundException;
import com.accenture.taskmanager.model.Task;

/**
 * Interfaz que define las operaciones de servicio para gestionar las tareas.
 *
 * @author Javier Roldán
 * @version 1.0
 */
// Crear la interfaz TaskService que tenga los métodos findAll, findById,
// create, update y delete para la lógica de negocio de las tareas. ->generado
// con IA
// Añadir los métodos findByStatus y findByPriority para buscar tareas por
// estado y prioridad, respectivamente. -> generado por IA
// Adaptar los métodos de listado para aceptar Pageable, delegar la paginación
// al Repository y devolver Page<Task>, conservando la lógica existente. —>
// generado por IA
public interface TaskService {

    /**
     * Obtiene todas las tareas.
     *
     * @return iterable con todas las tareas.
     */
    Page<Task> findAll(Pageable pageable);

    /**
     * Busca una tarea por su identificador.
     *
     * @param id identificador de la tarea que se desea buscar.
     * @return la tarea encontrada o null si no existe.
     */
    Task findById(Long id);

    /**
     * Crea una nueva tarea.
     *
     * @param task tarea que se desea crear.
     * @return la tarea creada.
     */
    Task create(Task task);

    /**
     * Actualiza una tarea existente.
     *
     * @param id   identificador de la tarea que se desea actualizar.
     * @param task datos actualizados de la tarea.
     * @return la tarea actualizada o null si no existe.
     */
    Task update(Long id, Task task);

    /**
     * Elimina una tarea por su identificador.
     *
     * @param id identificador de la tarea que se desea eliminar.
     * @throws TaskNotFoundException si no existe una tarea con el identificador
     *                               indicado.
     */
    void delete(Long id);

    /**
     * Busca tareas por su estado.
     *
     * @param status estado de las tareas que se desean buscar.
     * @return tareas encontradas.
     */
    Iterable<Task> findByStatus(TaskStatus status);

    /**
     * Busca tareas por su prioridad.
     *
     * @param priority prioridad de las tareas que se desean buscar.
     * @return tareas encontradas.
     */
    Iterable<Task> findByPriority(TaskPriority priority);

    /**
     * Busca tareas por su estado y prioridad.
     *
     * @param status   estado de las tareas que se desean buscar.
     * @param priority prioridad de las tareas que se desean buscar.
     * @param pageable información de paginación.
     * @return iterable con las tareas encontradas.
     */
    Page<Task> findByStatusAndPriority(String status, String priority, Pageable pageable);

    // Añadir los métodos de servicio para las cinco consultas, delegando en
    // TaskRepository y devolviendo los resultados correspondientes, sin añadir
    // lógica de negocio innecesaria. — generado por IA
    Page<Task> findByCategoryId(Long categoryId, Pageable pageable);

    Page<Task> findByTagId(Long tagId, Pageable pageable);

    Iterable<Task> findByDueDateBefore(java.time.LocalDate date);

    long getCountByStatus(TaskStatus status);
}
```

TaskServiceImpl<br>
<br>
Comentario:<br>
<br>
// Adaptar los métodos de listado para aceptar Pageable, delegar la paginación al Repository y devolver Page<Task>, conservando la lógica existente. —> generado por IA<br>
<br>
Resultado:

```
package com.accenture.taskmanager.service;

import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.accenture.taskmanager.enums.TaskPriority;
import com.accenture.taskmanager.enums.TaskStatus;
import com.accenture.taskmanager.exceptions.TaskNotFoundException;
import com.accenture.taskmanager.model.Task;
import com.accenture.taskmanager.repository.TaskRepository;
import com.accenture.taskmanager.service.interfaces.TaskService;

/**
 * Implementación del servicio encargado de gestionar la lógica de negocio
 * relacionada con las tareas.
 *
 * <p>
 * Esta clase implementa las operaciones definidas en la interfaz
 * {@link TaskService} y utiliza {@link TaskRepository} para acceder
 * a las tareas almacenadas en la base de datos.
 * </p>
 *
 * @author Javier Roldán
 * @version 1.0
 */
@Service
public class TaskServiceImpl implements TaskService {

    // Crea una lista igual que en el controlador para simular la persistencia de
    // datos y añade 2 tareas de ejemplo
    // al ArrayList usando el constructor de la clase Task para poder probar los
    // endpoints -> generado con IA
    // Actualiza las tareas de ejemplo para adaptarlas al nuevo constructor de Task,
    // Para los valores de status, consulta directamente el archivo
    // src/main/java/com/accenture/taskmanager/enums/TaskStatus.java y utiliza
    // exactamente
    // los nombres de los enums declarados allí, sin traducirlos ni inventarlos.
    // Para los valores de priority, consulta directamente el archivo
    // src/main/java/com/accenture/taskmanager/enums/TaskPriority.java y utiliza
    // exactamente
    // los nombres de los enums declarados allí, sin traducirlos ni inventarlos.
    // Añade también los valores correspondientes a createdAt y categoryId, no
    // pongas PENDIENTE, ni ALTA, ni EN_PROGRESO ni MEDIA.
    // —> generado con IA (sería el comentario genérico para que actualizara pero
    // con los null en campos nuevos ya no aplica)
    // Sustituir la lista estática por una dependencia de TaskRepository inyectada
    // mediante constructor. -> generado por IA
    private final TaskRepository taskRepository;
    // private static final List<Task> tasks = new ArrayList<>();

    // Crear el constructor que reciba TaskRepository y lo asigne al atributo
    // taskRepository. -> generado por IA
    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    // static {
    // tasks.add(new Task(1L, "Tarea 1", "Descripción de la tarea 1",
    // // esto no lo he sacado a constantes porque has dicho que no toquemos nada de
    // aquí pero sí que me acuerdo eh?
    // TaskStatus.PENDING, TaskPriority.LOW,
    // java.time.LocalDate.of(2024, 6, 30), null, null));

    // tasks.add(new Task(2L, "Tarea 2", "Descripción de la tarea 2",
    // TaskStatus.IN_PROGRESS, TaskPriority.HIGH,
    // java.time.LocalDate.of(2024, 7, 15), null, null));
    // }

    // actualiza el javadoc
    /**
     * Devuelve una página de tareas.
     *
     * @param pageable información de paginación y ordenación.
     * @return página de tareas con sus metadatos.
     */
    @Override
    public Page<Task> findAll(Pageable pageable) {
        return taskRepository.findAll(pageable);
    }

    // actualiza el javadoc
    /**
     * Busca una tarea por su identificador.
     *
     * @param id identificador de la tarea que se desea buscar.
     * @return la tarea encontrada.
     * @throws TaskNotFoundException si no existe una tarea con el identificador
     *                               indicado.
     */
    @Override
    public Task findById(Long id) {
        // Lógica para encontrar una tarea por su ID
        // Buscar la tarea en la lista de tareas por su ID y devolverla -> generado con
        // IA
        // Buscar la tarea por ID usando taskRepository y lanzar TaskNotFoundException
        // si no existe. -> generado por IA
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("La tarea con ID " + id + " no existe."));
    }

    // actualiza el javadoc
    /**
     * Crea una nueva tarea.
     *
     * @param task objeto Task que se desea crear.
     * @return la tarea creada.
     */
    @Override
    public Task create(Task task) {
        // Lógica para crear una nueva tarea
        // Añadir la tarea nueva a la lista de tareas y devolverla en el return ->
        // generado con IA
        // Guardar la tarea utilizando taskRepository y devolver la tarea guardada. ->
        // generado por IA
        return taskRepository.save(task);
    }

    // actualiza el javadoc
    /**
     * Actualiza una tarea existente con los datos proporcionados.
     *
     * @param id   identificador de la tarea que se desea actualizar.
     * @param task objeto Task con los nuevos datos para actualizar la tarea.
     * @return la tarea actualizada.
     * @throws TaskNotFoundException si no existe una tarea con el identificador
     *                               indicado.
     */
    @Override
    public Task update(Long id, Task task) {
        // Lógica para actualizar una tarea existente
        // Buscar la tarea en la lista de tareas por su ID y actualizar sus campos con
        // los valores del objeto task -> generado con IA
        // Actualizar una tarea existente utilizando taskRepository: buscarla por ID y,
        // si existe, actualizar title, description, status, priority, dueDate y
        // categoryId con los valores recibidos en task, conservando createdAt. Guardar
        // y devolver la tarea actualizada. Si no existe, lanzar TaskNotFoundException.
        // -> generado por IA
        Task existingTask = findById(id);
        existingTask.setTitle(task.getTitle());
        existingTask.setDescription(task.getDescription());
        existingTask.setStatus(task.getStatus());
        existingTask.setPriority(task.getPriority());
        existingTask.setDueDate(task.getDueDate());
        existingTask.setCategory(task.getCategory());
        existingTask.setTags(task.getTags());
        return taskRepository.save(existingTask);
    }

    // actualiza el javadoc
    /**
     * Elimina una tarea por su identificador.
     *
     * @param id identificador de la tarea que se desea eliminar.
     * @throws TaskNotFoundException si no existe una tarea con el identificador
     *                               indicado.
     */
    @Override
    public void delete(Long id) {
        // Lógica para eliminar una tarea por su ID
        // Eliminar una tarea de la lista de tareas por su ID, comprobar antes si la
        // tarea existe y si no existe lanzar una excepción -> generado con IA
        // Cambia la excepción lanzada a TaskNotFoundException para indicar que la tarea
        // no existe
        // Eliminar la tarea utilizando taskRepository: buscarla por ID y comprobar que
        // existe mediante findById. Si no existe, lanzar TaskNotFoundException. Si
        // existe, eliminarla utilizando taskRepository.delete(taskToDelete). ->
        // generado por IA
        Task taskToDelete = findById(id);
        taskRepository.delete(taskToDelete);

    }

    // pon los métodos que faltan de la interfaz TaskService, que son findByStatus y
    // findByPriority, implementados con taskRepository. -> generado por IA
    // Corregir la implementación para que coincida con la interfaz TaskService: el
    // método findByStatus debe recibir un String status, devolver Iterable<Task> y
    // utilizar taskRepository.findByStatus(status) para obtener las tareas. ->
    // generado por IA
    // Corregir la implementación para que coincida con la interfaz TaskService: el
    // método findByPriority debe recibir un String priority, devolver
    // Iterable<Task> y utilizar taskRepository.findByPriority(priority) para
    // obtener las tareas. -> generado por IA
    // Corregir findByPriority para que coincida con la firma real de
    // TaskRepository.findByPriority. Revisar el tipo de parámetro que espera el
    // repositorio y adaptar la implementación manteniendo la firma String priority
    // exigida por TaskService. -> generado por IA
    // corrige los métodos para que utilicen los tipos correctos que son enums en
    // status y priority como en la interfaz. -> generado por IA
    // Adaptar los métodos de listado para aceptar Pageable, delegar la paginación
    // al Repository y devolver Page<Task>, conservando la lógica existente. —>
    // generado por IA
    @Override
    public Iterable<Task> findByStatus(TaskStatus status) {
        return taskRepository.findByStatus(status);
    }

    @Override
    public Iterable<Task> findByPriority(TaskPriority priority) {
        return taskRepository.findByPriority(priority);
    }

    @Override
    public Page<Task> findByCategoryId(Long categoryId, Pageable pageable) {
        return taskRepository.findByCategoryId(categoryId, pageable);
    }

    @Override
    public Page<Task> findByTagId(Long tagId, Pageable pageable) {
        return taskRepository.findByTagId(tagId, pageable);
    }

    // Implementar findByStatusAndPriority convirtiendo los parámetros String a sus
    // enums TaskStatus y TaskPriority y delegando la búsqueda en TaskRepository. —
    // generado por IA
    // Añadir los métodos de servicio para las cinco consultas, delegando en
    // TaskRepository y devolviendo los resultados correspondientes, sin añadir
    // lógica de negocio innecesaria. — generado por IA
    @Override
    public Page<Task> findByStatusAndPriority(
            String status, String priority, Pageable pageable) {
        TaskStatus taskStatus = TaskStatus.valueOf(status);
        TaskPriority taskPriority = TaskPriority.valueOf(priority);
        return taskRepository.findByStatusAndPriority(
                taskStatus, taskPriority, pageable);
    }

    @Override
    public Iterable<Task> findByDueDateBefore(java.time.LocalDate date) {
        return taskRepository.findByDueDateBefore(date);
    }

    @Override
    public long getCountByStatus(TaskStatus status) {
        return taskRepository.countByStatus(status);
    }

}
```

TaskController<br>
<br>
Comentario:<br>
<br>
// Adaptar los endpoints GET de listado general, por categoría, por tag y por estado y prioridad para aceptar page, size y sort, con valores por defecto page=0, size=10 y sort=createdAt,desc, limitando size a un máximo de 100 -> generado por IA<br>
<br>
Resultado:

```
package com.accenture.taskmanager.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.accenture.taskmanager.enums.TaskPriority;
import com.accenture.taskmanager.enums.TaskStatus;
import com.accenture.taskmanager.exceptions.TaskNotFoundException;
import com.accenture.taskmanager.model.Task;
import com.accenture.taskmanager.service.interfaces.TaskService;

/**
 * Controlador REST encargado de gestionar las peticiones HTTP relacionadas
 * con las tareas de la aplicación.
 *
 * @author Javier Roldán
 * @version 1.0
 */
@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {

    // Crea una inyección de dependencias para el service, usa la interfaz
    // TaskService y la clase TaskServiceImpl, y utiliza el service para implementar
    // los endpoints del controlador
    private final TaskService taskService;

    /**
     * Crea el controlador de tareas e inyecta el servicio.
     *
     * @param taskService servicio encargado de gestionar las tareas
     */
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * Obtiene todas las tareas.
     *
     * @return respuesta HTTP con la lista de tareas y estado 200 OK.
     */
    // Traer los métodos findAll, findById, create, update y delete, del service
    // para implementar los endpoints del controlador -> generado con IA Adaptar los
    // endpoints GET de listado general, por categoría, por tag y por estado y
    // prioridad para aceptar page, size y sort, con valores por defecto page=0,
    // size=10 y sort=createdAt,desc, limitando size a un máximo de 100 -> generado
    // por IA
    @GetMapping
    public ResponseEntity<Page<Task>> getAllTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort) {

        size = Math.min(size, 100);

        String[] sortParams = sort.split(",");
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.fromString(sortParams[1]), sortParams[0]));

        Page<Task> tasks = taskService.findAll(pageable);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    /**
     * Obtiene una tarea mediante su identificador.
     *
     * @param id identificador de la tarea.
     * @return respuesta HTTP con la tarea y estado 200 OK,
     *         o estado 404 NOT_FOUND si no existe.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        Task task = taskService.findById(id);
        if (task != null) {
            return new ResponseEntity<>(task, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Crea una nueva tarea.
     *
     * @param task tarea que se desea crear.
     * @return respuesta HTTP con la tarea creada y estado 201 CREATED.
     */
    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        Task createdTask = taskService.create(task);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    /**
     * Actualiza una tarea existente.
     *
     * @param id   identificador de la tarea que se desea actualizar.
     * @param task objeto que contiene los nuevos datos.
     * @return respuesta HTTP con la tarea actualizada y estado 200 OK,
     *         o estado 404 NOT_FOUND si no existe.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task task) {
        Task updatedTask = taskService.update(id, task);
        if (updatedTask != null) {
            return new ResponseEntity<>(updatedTask, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Elimina una tarea mediante su identificador.
     *
     * @param id identificador de la tarea que se desea eliminar.
     * @throws TaskNotFoundException si no existe una tarea con el identificador
     *                               indicado.
     * @return respuesta HTTP con estado 204 NO_CONTENT si se elimina correctamente,
     *         o estado 404 NOT_FOUND si la tarea no existe.
     */
    // modifica el método deleteTask para que capture la excepción
    // TaskNotFoundException y devuelva un estado 404 NOT_FOUND en lugar de
    // IllegalArgumentException -> generado con IA
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        try {
            taskService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (TaskNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Añadir los cinco endpoints GET de las consultas personalizadas, usando
    // @PathVariable o @RequestParam según corresponda y delegando en TaskService,
    // sin modificar el endpoint GET /api/v1/tasks existente. — generado por IA
    // Traer los métodos findAll, findById, create, update y delete, del service
    // para implementar los endpoints del controlador -> generado con IA Adaptar los
    // endpoints GET de listado general, por categoría, por tag y por estado y
    // prioridad para aceptar page, size y sort, con valores por defecto page=0,
    // size=10 y sort=createdAt,desc, limitando size a un máximo de 100 -> generado
    // por IA
    @GetMapping("/by-category/{categoryId}")
    public ResponseEntity<Page<Task>> getTasksByCategoryId(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort) {

        size = Math.min(size, 100);
        String[] sortParams = sort.split(",");
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.fromString(sortParams[1]), sortParams[0]));

        Page<Task> tasks = taskService.findByCategoryId(categoryId, pageable);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    // Traer los métodos findAll, findById, create, update y delete, del service
    // para implementar los endpoints del controlador -> generado con IA
    // Adaptar los
    // endpoints GET de listado general, por categoría, por tag y por estado y
    // prioridad para aceptar page, size y sort, con valores por defecto page=0,
    // size=10 y sort=createdAt,desc, limitando size a un máximo de 100 -> generado
    // por IA
    @GetMapping("/by-tag/{tagId}")
    public ResponseEntity<Page<Task>> getTasksByTagId(
            @PathVariable Long tagId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort) {

        size = Math.min(size, 100);
        String[] sortParams = sort.split(",");
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.fromString(sortParams[1]), sortParams[0]));

        Page<Task> tasks = taskService.findByTagId(tagId, pageable);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    // Traer los métodos findAll, findById, create, update y delete, del service
    // para implementar los endpoints del controlador -> generado con IA Adaptar los
    // endpoints GET de listado general, por categoría, por tag y por estado y
    // prioridad para aceptar page, size y sort, con valores por defecto page=0,
    // size=10 y sort=createdAt,desc, limitando size a un máximo de 100 -> generado
    // por IA
    @GetMapping("/by-status-priority")
    public ResponseEntity<Page<Task>> getTasksByStatusAndPriority(
            @RequestParam TaskStatus status,
            @RequestParam TaskPriority priority,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sort) {

        size = Math.min(size, 100);
        String[] sortParams = sort.split(",");
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.fromString(sortParams[1]), sortParams[0]));

        Page<Task> tasks = taskService.findByStatusAndPriority(
                status.name(), priority.name(), pageable);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @GetMapping("/before-date")
    public ResponseEntity<Iterable<Task>> getTasksByDueDateBefore(@RequestParam java.time.LocalDate date) {
        Iterable<Task> tasks = taskService.findByDueDateBefore(date);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @GetMapping("/count-by-status")
    public ResponseEntity<Long> getTaskCountByStatus(@RequestParam TaskStatus status) {
        long count = taskService.getCountByStatus(status);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }
}

```

---
Paso 4 - Comprensión del código (Ask)<br>
<br>
Pregunta:<br>
<br>
¿Qué hace PageRequest.of(page, size, Sort.by(sort)) internamente? ¿Qué SQL genera Hibernate?<br>
<br>
Respuesta:<br>
<br>
PageRequest.of(page, size, Sort.by(sort)) construye un objeto inmutable que contiene tres datos para la consulta:

```
Pageable pageable = PageRequest.of(
    page,
    size,
    Sort.by(sort)
);
```

1. Qué significa cada parámetro

```
PageRequest.of(page, size, Sort.by(sort))
```
page: número de página, empezando en 0.<br>
size: número máximo de elementos por página.<br>
Sort.by(sort): orden ascendente por la propiedad indicada.<br>
Por ejemplo:

```
Pageable pageable = PageRequest.of(
    2,
    10,
    Sort.by("dueDate")
);
```

significa:

```
tercera página
10 elementos
orden ascendente por dueDate
```

Sort.by("dueDate") equivale a:

```
Sort.by(Sort.Direction.ASC, "dueDate");
```

Para ordenar de forma descendente:

```
Sort.by(Sort.Direction.DESC, "dueDate");
```

2. Validaciones internas<br>
PageRequest calcula el desplazamiento:

```
offset = page × size
```

Para el ejemplo anterior:

```
offset = 2 × 10 = 20
```

También valida que:<br>
<br>
page no sea negativo;<br>
size sea mayor que cero;<br>
la propiedad de orden exista en el modelo persistente cuando se construya o ejecute la consulta.<br>
3. Qué ocurre al pasar el Pageable al repositorio<br>
Por ejemplo:

```
Page<Task> findAll(Pageable pageable);
```

o, como el repositorio hereda de JpaRepository:

```
Page<Task> page = taskRepository.findAll(pageable);
```

Spring Data construye la consulta aplicando:<br>
<br>
la consulta base sobre Task;<br>
la ordenación indicada;<br>
la paginación;<br>
una consulta de conteo si el resultado es Page<Task>.<br>
4. SQL que genera Hibernate<br>
Con:

```
PageRequest.of(2, 10, Sort.by("dueDate"))
```

el SQL será aproximadamente:

```
SELECT
    t.id,
    t.title,
    t.description,
    t.status,
    t.priority,
    t.due_date,
    t.created_at,
    t.category_id
FROM task t
ORDER BY t.due_date ASC
LIMIT 10 OFFSET 20;
```

el SQL será aproximadamente:

```
SELECT
    t.id,
    t.title,
    t.description,
    t.status,
    t.priority,
    t.due_date,
    t.created_at,
    t.category_id
FROM task t
ORDER BY t.due_date ASC
LIMIT 10 OFFSET 20;
```

Hibernate conoce que:

```
dueDate
```

está mapeado a:

```
@Column(name = "due_date")
private LocalDate dueDate;
```

en Task.java, por eso traduce el nombre de la propiedad Java a la columna due_date.<br>
<br>
El formato exacto depende de la versión de Hibernate y del dialecto de la base de datos. En MySQL suele utilizar LIMIT y OFFSET, aunque también puede aparecer como:

```
LIMIT 20, 10;
```

5. Consulta adicional si se devuelve Page<Task><br>
Como Page debe conocer el total de resultados, Spring Data suele ejecutar además:

```
SELECT COUNT(t.id)
FROM task t;
```

Por tanto, una llamada con Page<Task> normalmente implica:

```
1. SELECT paginado con ORDER BY, LIMIT y OFFSET
2. SELECT COUNT(...) para obtener totalElements y totalPages
```

Si existe un filtro:

```
Page<Task> findByStatus(
    TaskStatus status,
    Pageable pageable
);
```

las consultas serían aproximadamente:

```
SELECT
    t.id,
    t.title,
    t.description,
    t.status,
    t.priority,
    t.due_date,
    t.created_at,
    t.category_id
FROM task t
WHERE t.status = ?
ORDER BY t.due_date ASC
LIMIT 10 OFFSET 20;
```

y:
```
SELECT COUNT(t.id)
FROM task t
WHERE t.status = ?;
```

El valor del parámetro se envía separado, por ejemplo:
```
status = IN_PROGRESS
```

6. Si se usa List o Slice<br>
Si el repositorio devuelve:

```
List<Task> findAll(Pageable pageable);
```

Hibernate ejecuta la consulta paginada, pero no se necesita normalmente la consulta COUNT.<br>
<br>
Si devuelve:
```
Slice<Task> findAll(Pageable pageable);
```

Spring Data puede solicitar un elemento adicional para saber si existe una página siguiente:
```
LIMIT 11 OFFSET 20;
```

Si el tamaño solicitado es 10, ese undécimo registro sirve para calcular hasNext() y no suele formar parte del contenido devuelto.<br>
<br>
7. Varios campos de ordenación<br>
Sort.by(sort) solo utiliza una propiedad y la ordena ascendentemente:

```
Sort.by("status")
```

Para varios criterios:

```
Sort sort = Sort.by(
    Sort.Order.asc("status"),
    Sort.Order.desc("dueDate")
);

Pageable pageable = PageRequest.of(0, 10, sort);
```

SQL aproximado:

```
ORDER BY t.status ASC, t.due_date DESC
LIMIT 10 OFFSET 0;
```

Importante si sort procede del cliente<br>
No conviene aceptar cualquier texto sin validarlo:

```
PageRequest.of(page, size, Sort.by(sort));
```

Es preferible permitir solo propiedades conocidas:
```
Set<String> allowedSorts = Set.of(
    "id",
    "title",
    "status",
    "priority",
    "dueDate"
);
```

Así se evitan errores por nombres inexistentes y se controla qué campos puede utilizar el cliente.<br>
<br>
En resumen, PageRequest.of(page, size, Sort.by(sort)) no ejecuta SQL por sí mismo. Construye la descripción de la página; cuando se pasa al repositorio, Spring Data y Hibernate la convierten en ORDER BY, LIMIT y OFFSET, y añaden un COUNT cuando el resultado es un Page<Task>.<br>
<br>

Pregunta:<br>
<br>
¿Qué información incluye el Page<Task> en el JSON de la respuesta? ¿Qué ventaja tiene frente a devolver una List<Task>?<br>
<br>
Respuesta:<br>
<br>
Si un controlador devuelve directamente:

```
@GetMapping
public Page<Task> findAll(Pageable pageable) {
    return taskService.findAll(pageable);
}
```
Spring MVC y Jackson serializan el objeto Page<Task> incluyendo normalmente el contenido y los metadatos de paginación.<br>
<br>
Una respuesta puede tener una estructura parecida a:

```
{
  "content": [
    {
      "id": 1,
      "title": "Preparar informe",
      "description": "Descripción",
      "status": "IN_PROGRESS",
      "priority": "HIGH",
      "dueDate": "2026-10-30",
      "createdAt": "2026-10-23T18:00:00",
      "categoryId": 2
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10,
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "totalElements": 37,
  "totalPages": 4,
  "last": false,
  "size": 10,
  "number": 0,
  "sort": {
    "sorted": true,
    "unsorted": false,
    "empty": false
  },
  "numberOfElements": 10,
  "first": true,
  "empty": false
}
```
Los nombres y la cantidad exacta de propiedades pueden variar según la versión de Spring Data y la configuración de Jackson. Lo esencial es:<br>
<br>
content: tareas de la página actual;<br>
number: número de página, empezando en 0;<br>
size: tamaño solicitado;<br>
numberOfElements: tareas incluidas en esta página;<br>
totalElements: total de tareas que cumplen la consulta;<br>
totalPages: número total de páginas;<br>
first y last: indican si es la primera o última página;<br>
sort: información sobre el orden aplicado;<br>
pageable: datos internos de paginación.<br>
Por ejemplo, una petición:

```
GET /tasks?page=1&size=10&sort=dueDate,desc
```
representa la segunda página, con hasta 10 tareas ordenadas por dueDate descendente.<br>
<br>
Ventaja frente a List<Task><br>
Una respuesta basada en List<Task> contendría únicamente los elementos:
```
[
  {
    "id": 1,
    "title": "Preparar informe"
  },
  {
    "id": 2,
    "title": "Revisar documentación"
  }
]
```
El cliente recibiría las tareas, pero no sabría directamente:<br>
<br>
qué página está viendo;<br>
cuál es el tamaño de página;<br>
cuántos resultados existen en total;<br>
cuántas páginas hay;<br>
si puede avanzar o retroceder;<br>
si está en la primera o última página.<br>
Con Page<Task> puede construir una interfaz de navegación:

```
Página 2 de 4
Mostrando elementos 11-20 de 37
Existe página siguiente: sí
```

Además, el repositorio limita los registros recuperados mediante LIMIT y OFFSET, en lugar de cargar toda la tabla:

```
SELECT ...
FROM task
ORDER BY due_date DESC
LIMIT 10 OFFSET 10;
```

Cuando se utiliza Page<Task>, Spring Data suele ejecutar también una consulta COUNT para calcular totalElements y totalPages:

```
SELECT COUNT(id)
FROM task;
```

Consideración de diseño<br>
Aunque devolver Page<Task> directamente es práctico, la estructura JSON queda ligada a la implementación de Spring Data y las entidades JPA. En una API más estable se puede transformar la página a un DTO propio:

```
public record TaskPageResponse(
    List<TaskResponse> content,
    int page,
    int size,
    long totalElements,
    int totalPages,
    boolean first,
    boolean last
) {
}
```
Así se controla exactamente el contrato JSON y se evita exponer detalles internos de PageImpl o de la entidad Task.<br>
<br>
En resumen: List<Task> devuelve solo una colección de tareas; Page<Task> devuelve esa colección junto con la información necesaria para navegar y representar correctamente una respuesta paginada.<br>
<br>
Pregunta:<br>
<br>
¿Qué ocurre si el cliente solicita una página que no existe, por ejemplo ?page=999? ¿Qué devuelve Spring Data?<br>
<br>
Respuesta:<br>
<br>
Si el cliente solicita:

```
GET /tasks?page=999&size=10
```

Spring Data no considera automáticamente que sea un error. Construye un PageRequest válido porque la página 999 es un número no negativo, ejecuta la consulta con un desplazamiento muy grande y no encuentra registros.<br>
<br>
El SQL será conceptualmente:

```
SELECT ...
FROM task
ORDER BY ...
LIMIT 10 OFFSET 9990;
```

Si existen, por ejemplo, 37 tareas, el resultado podría ser:

```
{
  "content": [],
  "number": 999,
  "size": 10,
  "numberOfElements": 0,
  "totalElements": 37,
  "totalPages": 4,
  "first": false,
  "last": true,
  "empty": true
}
```

Es decir:<br>
<br>
content es una lista vacía;<br>
number sigue siendo 999;<br>
totalPages indica que solo existen 4 páginas;<br>
empty es true;<br>
normalmente last es true;<br>
no se lanza automáticamente una excepción 404.<br>
Si se devuelve List<Task> en lugar de Page<Task>, simplemente se obtiene:

```
[]
```

y se pierde la información sobre el total y sobre el motivo por el que no hay resultados.<br>
<br>
¿Se ejecuta también el COUNT?<br>
Cuando el repositorio devuelve Page<Task>, Spring Data necesita conocer el total para calcular totalElements y totalPages, por lo que normalmente ejecuta:

```
SELECT COUNT(id)
FROM task;
```

La consulta paginada no devuelve filas, pero el conteo permite informar de que la página 999 está fuera del rango real.<br>
<br>
¿Qué ocurre con page=-1?<br>
La numeración empieza en cero:

```
page=0 → primera página
page=1 → segunda página
```

Un valor negativo no es válido. Spring normalmente rechaza esa petición al construir el Pageable, y la aplicación puede responder con un error 400 Bad Request, dependiendo de la configuración del controlador y del manejo de excepciones.<br>
<br>
¿Debería la API devolver 404?<br>
Depende del contrato de la API. El comportamiento predeterminado suele ser devolver una página vacía, pero se puede validar explícitamente:

```
Page<Task> result = taskService.findAll(pageable);

if (pageable.getPageNumber() >= result.getTotalPages()
        && result.getTotalPages() > 0) {
    throw new ResponseStatusException(
        HttpStatus.NOT_FOUND,
        "La página solicitada no existe"
    );
}
```

O también puede aceptarse una página vacía, que es una convención habitual en APIs paginadas.<br>
<br>
La decisión depende de la semántica deseada:

![Parte4Paso5Ask](..\capturas\Captura-05-Parte4-paso-4-Ask.png)
<br>
Por tanto, ?page=999 normalmente no produce un error de Spring Data: devuelve una Page<Task> vacía con sus metadatos de paginación.<br>
<br>
Pregunta:<br>
<br>
¿Qué diferencia hay entre ordenar por createdAt,desc y por priority,asc en términos de la query SQL generada?<br>
<br>
Respuesta:<br>
<br>
La diferencia está en la columna utilizada en ORDER BY y en la dirección del orden.<br>
<br>
createdAt,desc<br>
El cliente podría solicitar:

```
GET /tasks?sort=createdAt,desc
```
createdAt es la propiedad Java de Task.java, mapeada a la columna created_at:

```
@Column(name = "created_at")
private LocalDateTime createdAt;
```

Hibernate generaría algo parecido a:

```
SELECT
    t.id,
    t.title,
    t.description,
    t.status,
    t.priority,
    t.due_date,
    t.created_at,
    t.category_id
FROM task t
ORDER BY t.created_at DESC
LIMIT ? OFFSET ?;
```

DESC significa descendente:

```
más reciente → más antiguo
```

Las tareas con una fecha de creación más reciente aparecen primero.<br>
<br>
priority,asc<br>
La petición sería:

```
GET /tasks?sort=priority,asc
```

priority se mapea a la columna priority de la tabla:

```
private TaskPriority priority;
```

Como el campo utiliza:

```
@Enumerated(EnumType.STRING)
```

los valores se almacenan como texto, por ejemplo:

```
LOW
MEDIUM
HIGH
```

Hibernate generaría:

```
SELECT
    t.id,
    t.title,
    t.description,
    t.status,
    t.priority,
    t.due_date,
    t.created_at,
    t.category_id
FROM task t
ORDER BY t.priority ASC
LIMIT ? OFFSET ?;
```

ASC significa ascendente. Pero, al almacenarse el enum como texto, el orden será alfabético, no necesariamente el orden lógico de prioridad:

```
HIGH
LOW
MEDIUM
```

Por tanto, priority,asc no significa automáticamente:

```
LOW → MEDIUM → HIGH
```

Para obtener ese orden lógico habría que utilizar, por ejemplo:<br>
<br>
un campo numérico de prioridad;<br>
una consulta con CASE;<br>
un orden personalizado en la aplicación;<br>
un AttributeConverter o un diseño distinto del almacenamiento.<br>
Comparación

![Parte4Paso5Askb](..\capturas\Captura-05-Parte4-paso-4-Askb.png)
<br>
El resto de la consulta permanece igual; solo cambia la cláusula ORDER BY:

```
ORDER BY t.created_at DESC
```

frente a:

```
ORDER BY t.priority ASC
```

Con paginación<br>
Si se solicita la página 1 con 10 elementos:

```
PageRequest.of(1, 10, Sort.by(...))
```

el SQL incluirá:

```
LIMIT 10 OFFSET 10;
```

Por ejemplo:

```
ORDER BY t.created_at DESC
LIMIT 10 OFFSET 10;
```

o:

```
ORDER BY t.priority ASC
LIMIT 10 OFFSET 10;
```

Si el método devuelve Page<Task>, Spring Data también puede ejecutar una consulta independiente para contar los resultados:

```
SELECT COUNT(t.id)
FROM task t;
```

Importancia de los empates<br>
Si varias tareas tienen el mismo created_at o la misma priority, el orden entre ellas puede no ser estable. Para paginar de forma más predecible, se puede añadir un segundo criterio:

```
GET /tasks?sort=createdAt,desc&sort=id,asc
```

SQL aproximado:

```
ORDER BY t.created_at DESC, t.id ASC
LIMIT 10 OFFSET 0;
```

Así, las tareas con la misma fecha se ordenan por id, evitando que puedan cambiar de página de forma impredecible entre peticiones.<br>
<br>
Pregunta:<br>
<br>
¿Por qué es importante limitar el tamaño máximo de página en un endpoint público?<br>
<br>
Respuesta:<br>
<br>
Limitar el tamaño máximo de página evita que el cliente solicite una cantidad excesiva de registros en una sola petición:

```
GET /tasks?page=0&size=1000000
```
Aunque el endpoint use Pageable, Spring podría intentar procesar esa cantidad si no existe un límite.<br>
<br>
Riesgos de no limitarlo<br>
1. Consumo excesivo de memoria<br>
Hibernate debe materializar muchas entidades Task y Jackson debe convertirlas a JSON. Esto puede provocar:<br>
<br>
alto consumo de memoria;<br>
pausas del recolector de basura;<br>
OutOfMemoryError;<br>
reinicio de la aplicación.<br>
2. Consultas y respuestas lentas<br>
La base de datos debe devolver más filas, y la aplicación debe:<br>
<br>
leerlas;<br>
convertirlas en entidades;<br>
serializarlas;<br>
enviarlas por red.<br>
El tiempo de respuesta aumenta considerablemente.<br>
<br>
3. Saturación de la base de datos<br>
Varias peticiones con tamaños enormes pueden ocupar conexiones, CPU y memoria del servidor de base de datos. Un cliente mal configurado o malicioso podría provocar una denegación de servicio sin necesidad de enviar muchas peticiones.<br>
<br>
4. Respuestas HTTP demasiado grandes<br>
Una respuesta de varios megabytes puede causar:<br>
<br>
mayor consumo de ancho de banda;<br>
timeouts;<br>
problemas en proxies y gateways;<br>
errores o lentitud en aplicaciones móviles y navegadores.<br>
5. La paginación deja de cumplir su objetivo<br>
La paginación debe acotar el coste de cada petición. Si se permite size=1000000, el endpoint sigue siendo técnicamente paginado, pero en la práctica puede devolver casi toda la tabla.<br>
<br>
Cómo establecer un límite<br>
Se pueden definir valores predeterminados en el controlador:

```
@GetMapping
public Page<Task> findAll(
    @PageableDefault(size = 20, sort = "createdAt")
    Pageable pageable
) {
    return taskService.findAll(pageable);
}
```

Pero el valor por defecto no siempre es un límite: el cliente puede enviar otro size. Para imponer un máximo global en Spring MVC se puede personalizar el resolver:

```
@Configuration
public class PageableConfig
        implements PageableHandlerMethodArgumentResolverCustomizer {

    @Override
    public void customize(PageableHandlerMethodArgumentResolver resolver) {
        resolver.setMaxPageSize(100);
        resolver.setFallbackPageable(PageRequest.of(0, 20));
    }
}
```

Así:<br>
<br>
el tamaño predeterminado es 20;<br>
el tamaño máximo permitido es 100.<br>
También es recomendable controlar los valores en la lógica de la API cuando existan distintos límites por endpoint.<br>
<br>
Ejemplo de política

```
size omitido       → 20
size=50             → 50
size=100             → 100
size=1000           → se limita o se rechaza
size=0 o negativo   → petición inválida
```

Hay dos estrategias habituales para un tamaño superior al máximo:<br>
<br>
ajustarlo al máximo permitido, por ejemplo size=1000 pasa a ser size=100;<br>
rechazarlo con 400 Bad Request.<br>
Lo importante es que la decisión sea explícita y coherente con el contrato de la API.<br>
<br>
Además del tamaño, conviene validar:<br>
<br>
que page >= 0;<br>
que las propiedades de sort estén permitidas;<br>
que el número de peticiones esté controlado mediante rate limiting si el endpoint es público.<br>
En resumen, el límite de tamaño protege la memoria de la aplicación, la base de datos, la red y la disponibilidad del servicio. La paginación solo ofrece una protección real cuando el tamaño máximo de cada página está controlado.

---
<br>
Paso 5 - Validación en Postman<br>
<br>
Paginación - Primera página<br>

![primeraPagina](..\capturas\01-paginacion-primera-pagina.png)
<br>
Paginación - Segunda página<br>

![segundaPagina](..\capturas\02-paginacion-segunda-pagina.png)

Ordenación - Prioridad ascendente

![ordenacionPrioridadAscendente](..\capturas\03-ordenacion-prioridad-ascendente.png)

Ordenación - Fecha límite descendente

![ordenacionFechaLimite](..\capturas\04-ordenacion-fecha-limite-descendente.png)

Paginación - Página inexistente

![paginaExistente](..\capturas\05-paginacion-pagina-inexistente.png)

Paginación - Filtrado por categoría

![paginacionFiltradoPorCategoria](..\capturas\06-paginacion-filtrado-por-categoria.png)

Validación - Tamaño máximo de página

![validacionTamañoMaximoPagina](..\capturas\07-validacion-tamano-maximo.png)

---
<br>
Reflexión:<br>
<br>
Pregunta:<br>
<br>
¿Qué problema concreto resuelve lapaginación en una API con muchos registros?
<br>
Respuesta:<br>
<br>
La paginación evita devolver todos los registros en una sola petición, reduciendo el consumo de memoria, el tamaño de las respuestas y el tiempo de transferencia. Así, la API funciona de forma más eficiente cuando hay muchos registros.
<br>
Pregunta:<br>
<br>
¿Qué diferencia hay entre devolver un Page <Task> y una List <Task>? ¿Qué información extra aporta Page?
<br>
Respuesta:<br>
<br>
Una List<Task> devuelve una colección de tareas, mientras que un Page<Task> devuelve una página de resultados junto con metadatos de paginación, como el número de página, el tamaño, el total de elementos, el total de páginas y si es la primera o la última página.
<br>
Pregunta:<br>
<br>
¿Cómo afectaría la paginación a un cliente frontend que necesita mostrar todos los registros? ¿Cómo lo gestionarías?
<br>
Respuesta:<br>
<br>
El frontend no recibiría todos los registros en una única petición, sino por páginas. Lo gestionaría solicitando cada página sucesivamente y acumulando los resultados hasta llegar a la última, utilizando los metadatos que devuelve Page.
<br>
Pregunta:<br>
<br>
¿Qué inconveniente ves en devolver directamente el Page <Task> de Spring Data al cliente? (Pista: lo resolverás en el ejercicio de creación de los DTO's.)
<br>
Respuesta:<br>
<br>
Devolver directamente Page<Task> acopla la respuesta de la API a la estructura interna de Spring Data y expone directamente las entidades Task. Esto puede dificultar el control de los datos que se muestran al cliente y hacer que cambios internos afecten a la API. Utilizar DTOs permite definir una respuesta propia y exponer únicamente los datos necesarios.

---
Parte 5<br>
<br>
Ejercicio para la creación de Transacciones<br>
<br>
Paso 1 - Preparación (Ask)<br>
<br>
Pregunta:<br>
<br>
¿Qué es una transacción en Spring y qué garantías ofrece cuando se realizan varias operaciones sobre una base de datos?
<br>
Respuesta:<br>
<br>
Una transacción es una unidad lógica de trabajo formada por una o varias operaciones de base de datos que deben tratarse como un único bloque.<br>
<br>
En Spring se suele declarar con @Transactional:

```
@Service
public class TaskServiceImpl {

    @Transactional
    public void updateTask(Long id, Task data) {
        Task task = taskRepository.findById(id)
            .orElseThrow();

        task.setTitle(data.getTitle());
        task.setStatus(data.getStatus());

        // Otras operaciones relacionadas...
    }
}
```

Spring abre la transacción antes de ejecutar el método y, normalmente:<br>
<br>
obtiene una conexión transaccional;<br>
ejecuta las operaciones JPA;<br>
hace flush al finalizar;<br>
confirma la transacción si todo termina correctamente;<br>
realiza rollback si se produce un error que deba provocar reversión.<br>
Garantías ACID<br>
Una transacción ofrece las garantías clásicas ACID:<br>
<br>
Atomicidad<br>
Todas las operaciones se confirman o ninguna.<br>
<br>
Por ejemplo, si un método crea una tarea y registra un evento:

```
1. INSERT de Task
2. INSERT de TaskEvent
```

Si la segunda operación falla, se revierten ambas. No queda la tarea creada sin su evento.<br>
<br>
Consistencia<br>
La base de datos pasa de un estado válido a otro estado válido, respetando:<br>
<br>
claves primarias;<br>
claves foráneas;<br>
restricciones NOT NULL;<br>
índices únicos;<br>
reglas de integridad.<br>
Aislamiento<br>
Las operaciones concurrentes no deberían observar estados intermedios de otras transacciones. El nivel exacto depende de la base de datos y de la configuración:

```
READ COMMITTED
REPEATABLE READ
SERIALIZABLE
```
Cada nivel ofrece un equilibrio distinto entre consistencia y concurrencia.<br>
<br>
Durabilidad<br>
Después de un commit correcto, los cambios deben permanecer aunque la aplicación se detenga, según las garantías del motor de base de datos.<br>
<br>
¿Qué ocurre si falla una operación?<br>
Sin una transacción única, podría suceder:

```
1. Se actualiza Task correctamente
2. Falla la actualización de Category
3. Task queda modificada
4. Category queda sin actualizar
```
Con @Transactional, Spring intenta revertir las operaciones realizadas dentro de la misma transacción:

```
1. Se actualiza Task
2. Falla la actualización de Category
3. Se hace rollback
4. Task vuelve a su estado anterior
```

¿Cuándo hace rollback Spring?<br>
Por defecto, Spring suele hacer rollback ante excepciones no comprobadas:

```
RuntimeException
```

y errores:

```
Error
```

Las excepciones comprobadas (Exception, salvo las anteriores) no siempre provocan rollback automáticamente. Si se necesita, puede indicarse:

```
@Transactional(rollbackFor = Exception.class)
```

También se puede definir explícitamente una excepción concreta:

```
@Transactional(rollbackFor = TaskOperationException.class)
```

No conviene capturar una excepción y ocultarla dentro del método si se espera que Spring haga rollback:

```
@Transactional
public void process() {
    try {
        // operación
    } catch (Exception ex) {
        // Si se oculta el error, Spring puede creer que todo terminó bien
    }
}
```

Si se captura, normalmente hay que registrar el error y relanzarlo, o marcar la transacción para rollback.<br>
<br>
¿Dónde colocar @Transactional?<br>
Habitualmente en la capa de servicio, porque allí se define la unidad de negocio:

```
@Transactional
public void createTaskAndAudit(Task task) {
    taskRepository.save(task);
    auditRepository.save(new AuditEntry(...));
}
```

No suele ser ideal que cada operación del repositorio determine por separado la transacción si el caso de uso necesita coordinar varias operaciones.<br>
<br>
Los métodos CRUD de Spring Data ya tienen comportamiento transaccional predeterminado para operaciones individuales, pero si varias llamadas deben ser atómicas, conviene envolverlas en una transacción común desde el servicio.<br>
<br>
Importante: llamadas internas y proxies<br>
Spring aplica normalmente @Transactional mediante un proxy. Por eso una llamada desde fuera del bean funciona:

```
taskService.updateTask(...);
```

Pero una llamada interna directa entre métodos del mismo objeto puede no pasar por el proxy:

```
this.updateTask(...); // puede no activar @Transactional
```

Por eso la frontera transaccional suele colocarse en el método público del servicio que invoca el controlador u otro bean.<br>
<br>
flush no es lo mismo que commit<br>
JPA puede enviar SQL a la base de datos durante un flush:

```
UPDATE task ...
```

pero eso no significa todavía que la transacción esté confirmada. El cambio puede revertirse hasta que se produzca el commit.<br>
<br>
La confirmación suele ocurrir al terminar correctamente el método anotado con @Transactional.<br>
<br>
Aplicado a este proyecto<br>
El método update() de TaskServiceImpl.java busca una tarea, modifica sus campos y la guarda. Si en el futuro esa operación también tuviera que actualizar otras entidades o registrar información adicional, sería conveniente que todo el caso de uso estuviera dentro de una única transacción:

```
@Transactional
public Task update(Long id, Task task) {
    Task existingTask = findById(id);
    // actualizar campos
    return taskRepository.save(existingTask);
}
```

La anotación no hace que una operación sea mágicamente correcta: solo garantiza la coordinación transaccional de las operaciones que se ejecutan dentro de su alcance y de la configuración del gestor de transacciones.<br>
<br>
En resumen, una transacción permite que varias operaciones relacionadas se confirmen juntas o se deshagan juntas, protegiendo la integridad de los datos frente a errores y operaciones concurrentes.<br>
<br>
Pregunta:<br>
<br>
Si un método del Service modifica la base de datos y falla a mitad de la operación, ¿qué ocurre con los cambios realizados si el método tiene @Transactional? ¿Y si no la tiene?<br>
<br>
Respuesta:<br>
<br>
Con @Transactional<br>
Si el método del servicio está anotado:

```
@Transactional
public void processTask() {
    taskRepository.save(task);
    auditRepository.save(audit);
    notificationRepository.save(notification); // falla aquí
}
```

Spring abre una única transacción al entrar en el método. Si se produce una excepción no controlada, normalmente una RuntimeException, Spring hace rollback:

```
1. Se guarda Task
2. Se guarda Audit
3. Falla el guardado de Notification
4. Se deshacen Task y Audit
5. La base de datos queda como antes de iniciar el método
```

Aunque Hibernate haya ejecutado ya algún INSERT o UPDATE durante un flush, esos cambios todavía pertenecen a la transacción y pueden revertirse antes del commit.<br>
<br>
Por defecto, Spring hace rollback ante:<br>
<br>
RuntimeException;<br>
subclases de RuntimeException;<br>
Error.<br>
Para excepciones comprobadas se puede indicar explícitamente:

```
@Transactional(rollbackFor = Exception.class)
public void processTask() throws Exception {
    // operaciones
}
```

La excepción tampoco debe ocultarse sin más:

```
try {
    // operación
} catch (Exception ex) {
    // Si no se relanza, Spring puede confirmar la transacción
}
```

Sin @Transactional en el servicio<br>
Sin una transacción común alrededor del método, cada llamada al repositorio puede ejecutarse en su propia transacción. Los métodos CRUD de Spring Data JPA tienen configuración transaccional individual.<br>
<br>
Por ejemplo:

```
public void processTask() {
    taskRepository.save(task);      // transacción 1: commit correcto
    auditRepository.save(audit);    // transacción 2: commit correcto
    notificationRepository.save(notification); // transacción 3: falla
}
```

El resultado puede ser:

```
Task guardada
Audit guardado
Notification no guardada
```

Los cambios anteriores ya se confirmaron y no se revierten automáticamente cuando falla la operación posterior. Esto puede dejar los datos en un estado parcialmente actualizado.<br>
<br>
Comparación

![parte5Ask](..\capturas\parte-05-ask.png)
<br>
Ejemplo aplicado<br>
Si un servicio realiza varias operaciones:

```
@Transactional
public void updateTaskAndCategory(Long taskId, Long categoryId) {
    Task task = taskRepository.findById(taskId)
        .orElseThrow();

    Category category = categoryRepository.findById(categoryId)
        .orElseThrow();

    task.setCategory(category);
    taskRepository.save(task);

    auditRepository.save(new AuditEntry(taskId));

    // Si falla aquí, se revierten las operaciones anteriores
    externalRepository.save(...);
}
```

La transacción debe declararse normalmente en la capa de servicio porque ahí se define la unidad completa de negocio, no en cada operación aislada del repositorio.<br>
<br>
Matices importantes<br>
@Transactional funciona normalmente mediante un proxy de Spring; debe invocarse el método desde otro bean para que el proxy lo intercepte.<br>
Una llamada interna como this.otroMetodoTransaccional() puede no activar la anotación.<br>
flush no equivale a commit: enviar SQL a la base de datos no significa que el cambio sea definitivo.<br>
La base de datos debe utilizar un motor transaccional, como InnoDB en MySQL.<br>
Las operaciones externas, como enviar un correo o llamar a otra API, no se deshacen automáticamente con el rollback de la base de datos.<br>
En resumen: @Transactional permite que las operaciones de base de datos del método se confirmen juntas o se reviertan juntas. Sin una transacción común, las operaciones anteriores pueden quedar confirmadas aunque una operación posterior falle.<br>
<br>
Pregunta:<br>
<br>
¿Por qué debe colocarse @Transactional en la capa Service y no en el Controller o en el Repository?
<br>
Respuesta:<br>
<br>
No es una obligación técnica absoluta: @Transactional puede funcionar en un Controller o en un método de repositorio. Pero la capa Service es el lugar recomendado porque representa la unidad de trabajo del negocio.<br>
<br>
En la capa Service<br>
Un caso de uso puede necesitar varias operaciones:

```
@Service
public class TaskServiceImpl {

    @Transactional
    public void moveTask(Long taskId, Long categoryId) {
        Task task = taskRepository.findById(taskId)
            .orElseThrow();

        Category category = categoryRepository.findById(categoryId)
            .orElseThrow();

        task.setCategory(category);
        taskRepository.save(task);

        auditRepository.save(new AuditEntry(taskId));
    }
}
```

Al poner @Transactional en el Service:<br>
<br>
todas las operaciones participan en la misma transacción;<br>
si falla una operación, se pueden revertir las anteriores;<br>
la transacción coincide con el caso de uso completo;<br>
el controlador no necesita conocer detalles de persistencia.<br>
El flujo queda así:

```
Controller
    ↓
Service: frontera transaccional
    ↓
Repository 1
Repository 2
    ↓
Base de datos
```

¿Por qué no en el Controller?<br>
El Controller debe encargarse principalmente de:<br>
<br>
recibir la petición HTTP;<br>
validar o convertir parámetros;<br>
invocar al servicio;<br>
construir la respuesta.<br>
Si se anota el Controller:

```
@Transactional
@PostMapping
public Task update(...) {
    // ...
}
```

la transacción queda ligada a la capa HTTP. Esto tiene varios inconvenientes:<br>
<br>
mezcla transporte y persistencia;<br>
puede mantener una conexión abierta durante toda la preparación de la respuesta;<br>
puede prolongar la sesión mientras se serializa JSON;<br>
hace más difícil reutilizar el mismo caso de uso desde otro punto, como un proceso batch o un evento;<br>
puede ocultar problemas de carga lazy durante la serialización.<br>
Además, un Controller podría llamar a varios métodos de servicio con necesidades transaccionales diferentes, haciendo menos clara la frontera de cada operación.<br>
<br>
¿Por qué no en el Repository?<br>
Un repositorio representa operaciones de persistencia individuales:

```
taskRepository.save(task);
taskRepository.deleteById(id);
```

Los métodos CRUD de Spring Data ya tienen configuración transaccional para operaciones aisladas. Pero eso no garantiza que varias llamadas estén dentro de una única transacción:

```
taskRepository.save(task);       // transacción propia
auditRepository.save(audit);     // otra transacción
```

Si falla la segunda operación, la primera podría haber quedado confirmada.<br>
<br>
La transacción debe envolver ambas llamadas desde el Service:

```
@Transactional
public void updateTaskAndAudit(Task task, AuditEntry audit) {
    taskRepository.save(task);
    auditRepository.save(audit);
}
```

Cuando existe una transacción iniciada por el Service, los repositorios normalmente se incorporan a ella mediante la propagación predeterminada REQUIRED.<br>
<br>
Qué problema evita<br>
Sin una transacción común:

```
1. taskRepository.save(task)  → commit
2. auditRepository.save(audit) → falla
```

Resultado:

```
Task guardada
Audit no guardado
```

Con @Transactional en el Service:

```
1. save(task)
2. save(audit) → falla
3. rollback de toda la transacción
```

Resultado:

```
No se confirma ninguna de las operaciones
```

Aplicado a este proyecto<br>
En TaskServiceImpl.java, update() define una operación de negocio completa: busca una tarea, modifica sus campos y la guarda. Si en el futuro esa operación también modificase categorías, etiquetas o auditoría, el Service sería el lugar natural para delimitar la transacción.

```
@Transactional
@Override
public Task update(Long id, Task task) {
    Task existingTask = findById(id);
    // modificar campos
    return taskRepository.save(existingTask);
}
```

Matiz importante<br>
No siempre es necesario añadir @Transactional a cada método del Service. Si una operación solo llama a un método CRUD y no coordina varias acciones, la configuración transaccional del repositorio puede ser suficiente.<br>
<br>
La regla práctica es:<br>
<br>
Controller: HTTP y presentación.<br>
Service: caso de uso y frontera transaccional.<br>
Repository: acceso a datos.<br>
Por eso @Transactional se coloca normalmente en el Service: ahí se sabe qué operaciones forman una unidad indivisible de negocio.<br>
<br>
Pregunta:<br>
<br>
¿Qué diferencia hay entre aplicar @Transactional a un método de escritura y a uno de solo lectura? ¿Por qué en este ejercicio se pide utilizarla únicamente en los métodos que crean, actualizan o borran una tarea?<br>
<br>
Respuesta:<br>
<br>
objetivos diferentes.<br>
<br>
Método de escritura<br>
En una operación que crea, actualiza o elimina una tarea:

```
@Transactional
public Task update(Long id, Task data) {
    Task existingTask = findById(id);
    existingTask.setTitle(data.getTitle());
    existingTask.setStatus(data.getStatus());

    return taskRepository.save(existingTask);
}
```

La transacción permite que:<br>
<br>
todas las modificaciones formen una unidad atómica;<br>
Hibernate haga flush y commit al finalizar correctamente;<br>
se haga rollback si ocurre una excepción;<br>
varias operaciones sobre repositorios distintos se confirmen o reviertan juntas;<br>
las entidades permanezcan gestionadas dentro del mismo contexto de persistencia.<br>
Por ejemplo, si una actualización modifica la tarea y registra una auditoría, ambas operaciones pueden quedar protegidas por la misma transacción.<br>
<br>
Método de solo lectura<br>
Para una consulta se puede utilizar:

```
@Transactional(readOnly = true)
public Task findById(Long id) {
    return taskRepository.findById(id)
        .orElseThrow();
}
```

readOnly = true comunica que la transacción no debería modificar datos. Según el gestor y la configuración, puede permitir optimizaciones como:<br>
<br>
reducir comprobaciones de cambios de Hibernate;<br>
evitar o retrasar ciertos flush;<br>
indicar a la base de datos que la operación es de lectura;<br>
mantener disponible el contexto de persistencia mientras se recorren relaciones LAZY.<br>
Pero readOnly = true no debe interpretarse como un mecanismo de seguridad absoluto. No siempre impide físicamente que se ejecute una modificación. Si el método cambia una entidad gestionada o llama a una operación de escritura, el comportamiento puede depender del proveedor JPA y de la base de datos.<br>
<br>
¿Por qué el ejercicio la pide solo para crear, actualizar y borrar?<br>
En este proyecto, los métodos de escritura de TaskServiceImpl.java son:<br>
<br>
create;<br>
update;<br>
delete.<br>
Son los que cambian el estado de la base de datos y necesitan una frontera transaccional explícita.<br>
<br>
Los métodos de lectura, como findAll() y findById(), no coordinan varias modificaciones. Además, los métodos de consulta de Spring Data JPA ya tienen comportamiento transaccional de lectura en su infraestructura. Por eso, para el objetivo del ejercicio, no es necesario añadir otra anotación al Service.<br>
<br>
La intención es mantener una regla sencilla:

```
crear, actualizar o borrar → @Transactional
consultar                  → sin @Transactional explícito
```

¿Significa que nunca se debe anotar una lectura?<br>
No. @Transactional(readOnly = true) puede ser útil cuando:<br>
<br>
se accede a relaciones LAZY durante el caso de uso;<br>
se realizan varias lecturas que deben observarse dentro de la misma transacción;<br>
se quiere documentar explícitamente que el método es de lectura;<br>
se busca aprovechar optimizaciones del proveedor;<br>
se necesita un nivel de aislamiento concreto.<br>
Por ejemplo:

```
@Transactional(readOnly = true)
public TaskDetails getTaskDetails(Long id) {
    Task task = taskRepository.findById(id)
        .orElseThrow();

    task.getTags().size(); // se carga dentro de la transacción
    return mapToDetails(task);
}
```

Diferencia resumida

![parte5Askb](..\capturas\parte-05-askb.png)
<br>
Por tanto, en este ejercicio se utiliza @Transactional únicamente en los métodos que modifican tareas porque son los que necesitan garantizar que sus cambios se confirmen juntos o se deshagan juntos. No es que las lecturas no puedan ser transaccionales, sino que no necesitan una transacción de escritura explícita para cumplir el alcance de la práctica.

---
<br>
Paso 2 – Diseño previo<br>
<br>
Métodos de escritura identificados en TaskServiceImpl:<br>
<br>
-create: crea una tarea.<br>
-update: actualiza una tarea.<br>
-delete: elimina una tarea.<br>
<br>
Comentario previsto para el autocompletado:<br>
<br>
// Añadir @Transactional a este método de escritura del Service para garantizar que sus operaciones sobre la base de datos se ejecuten dentro de una transacción. — generado por IA

---
<br>
Paso 3 - Implementación (autocompletado)<br>
<br>
Resultado:

```
    // Añadir @Transactional a este método de escritura del Service para garantizar que sus operaciones sobre la base de datos se ejecuten dentro de una transacción. — generado por IA
    // actualiza el javadoc
    /**
     * Crea una nueva tarea.
     *
     * @param task objeto Task que se desea crear.
     * @return la tarea creada.
     */
    @Transactional
    @Override
    public Task create(Task task) {
        // Lógica para crear una nueva tarea
        // Añadir la tarea nueva a la lista de tareas y devolverla en el return ->
        // generado con IA
        // Guardar la tarea utilizando taskRepository y devolver la tarea guardada. ->
        // generado por IA
        return taskRepository.save(task);
    }
```
```
    // Añadir @Transactional a este método de escritura del Service para garantizar que sus operaciones sobre la base de datos se ejecuten dentro de una transacción. — generado por IA
    // actualiza el javadoc
    /**
     * Actualiza una tarea existente con los datos proporcionados.
     *
     * @param id   identificador de la tarea que se desea actualizar.
     * @param task objeto Task con los nuevos datos para actualizar la tarea.
     * @return la tarea actualizada.
     * @throws TaskNotFoundException si no existe una tarea con el identificador
     *                               indicado.
     */
    @Transactional
    @Override
    public Task update(Long id, Task task) {
        // Lógica para actualizar una tarea existente
        // Buscar la tarea en la lista de tareas por su ID y actualizar sus campos con
        // los valores del objeto task -> generado con IA
        // Actualizar una tarea existente utilizando taskRepository: buscarla por ID y,
        // si existe, actualizar title, description, status, priority, dueDate y
        // categoryId con los valores recibidos en task, conservando createdAt. Guardar
        // y devolver la tarea actualizada. Si no existe, lanzar TaskNotFoundException.
        // -> generado por IA
        Task existingTask = findById(id);
        existingTask.setTitle(task.getTitle());
        existingTask.setDescription(task.getDescription());
        existingTask.setStatus(task.getStatus());
        existingTask.setPriority(task.getPriority());
        existingTask.setDueDate(task.getDueDate());
        existingTask.setCategory(task.getCategory());
        existingTask.setTags(task.getTags());
        return taskRepository.save(existingTask);
    }
```
```
    // Añadir @Transactional a este método de escritura del Service para garantizar que sus operaciones sobre la base de datos se ejecuten dentro de una transacción. — generado por IA
    // actualiza el javadoc
    /**
     * Elimina una tarea por su identificador.
     *
     * @param id identificador de la tarea que se desea eliminar.
     * @throws TaskNotFoundException si no existe una tarea con el identificador
     *                               indicado.
     */
    @Transactional
    @Override
    public void delete(Long id) {
        // Lógica para eliminar una tarea por su ID
        // Eliminar una tarea de la lista de tareas por su ID, comprobar antes si la
        // tarea existe y si no existe lanzar una excepción -> generado con IA
        // Cambia la excepción lanzada a TaskNotFoundException para indicar que la tarea
        // no existe
        // Eliminar la tarea utilizando taskRepository: buscarla por ID y comprobar que
        // existe mediante findById. Si no existe, lanzar TaskNotFoundException. Si
        // existe, eliminarla utilizando taskRepository.delete(taskToDelete). ->
        // generado por IA
        Task taskToDelete = findById(id);
        taskRepository.delete(taskToDelete);

    }
```

---
<br>
Paso 4 - Comprensión del código (Ask)<br>
<br>
Pregunta:<br>
<br>
¿Qué hace Spring cuando encuentra @Transactional en un método? ¿Qué ocurre si el método lanza una excepción?<br>
<br>
Respuesta:<br>
<br>
Cuando Spring encuentra @Transactional en un método, normalmente crea un proxy alrededor del bean y utiliza ese proxy para gestionar la transacción.<br>
<br>
Por ejemplo:

```
@Service
public class TaskService {

    @Transactional
    public Task create(Task task) {
        return taskRepository.save(task);
    }
}
```

Cuando otro bean invoca:

```
taskService.create(task);
```

el flujo es aproximadamente:<br>
<br>
El proxy intercepta la llamada.<br>
Busca o crea una transacción según la configuración.<br>
Ejecuta el método dentro de esa transacción.<br>
Si el método termina correctamente, hace commit.<br>
Si se produce una excepción que requiere rollback, hace rollback.

```
llamada al Service
        ↓
proxy transaccional
        ↓
BEGIN
        ↓
operaciones JPA
        ↓
COMMIT o ROLLBACK
```

Si el método termina correctamente

```
@Transactional
public void updateTask(Task task) {
    taskRepository.save(task);
}
```

Si no se produce ninguna excepción:

```
BEGIN
UPDATE ...
COMMIT
```

Hibernate puede enviar el SQL durante un flush, normalmente al finalizar la transacción, antes del commit.<br>
<br>
Si lanza una excepción<br>
Por defecto, Spring marca la transacción para rollback cuando se lanza una:<br>
<br>
RuntimeException;<br>
subclase de RuntimeException;<br>
Error.<br>
Ejemplo:

```
@Transactional
public void createTask(Task task) {
    taskRepository.save(task);

    throw new IllegalStateException("No se pudo completar la operación");
}
```

Aunque el INSERT ya se haya enviado mediante flush, la transacción todavía no se ha confirmado:

```
BEGIN
INSERT ...
se lanza RuntimeException
ROLLBACK
```

El registro no queda guardado.<br>
<br>
Esto es especialmente importante cuando hay varias operaciones:

```
@Transactional
public void createTaskAndAudit(Task task, Audit audit) {
    taskRepository.save(task);
    auditRepository.save(audit);

    throw new IllegalStateException();
}
```

El rollback revierte ambas operaciones.<br>
<br>
Excepciones comprobadas<br>
Las excepciones comprobadas no provocan rollback automáticamente en todos los casos:

```
@Transactional
public void process() throws Exception {
    taskRepository.save(task);
    throw new Exception("Error");
}
```

Si se quiere forzar el rollback:

```
@Transactional(rollbackFor = Exception.class)
public void process() throws Exception {
    // ...
}
```

También puede especificarse una excepción concreta:

```
@Transactional(rollbackFor = TaskOperationException.class)
```

No se debe ocultar la excepción<br>
Si el método captura la excepción y no la relanza:

```
@Transactional
public void process() {
    try {
        taskRepository.save(task);
        throw new IllegalStateException();
    } catch (Exception ex) {
        // Se oculta el error
    }
}
```

Spring puede considerar que el método terminó correctamente y hacer commit.<br>
<br>
Si se captura, normalmente hay que relanzarla:

```
@Transactional
public void process() {
    try {
        taskRepository.save(task);
    } catch (Exception ex) {
        throw new TaskOperationException("No se pudo procesar la tarea", ex);
    }
}
```

También se puede marcar explícitamente la transacción para rollback, aunque relanzar una excepción suele ser más claro.<br>
<br>
Importante: debe pasar por el proxy<br>
@Transactional se aplica normalmente mediante un proxy. Por eso funciona cuando un objeto externo llama al bean:

```
taskService.update(...);
```

Pero una llamada interna directa puede no ser interceptada:

```
this.update(...);
```

En ese caso, la anotación de update() podría no activar una nueva frontera transaccional. Por eso suele colocarse @Transactional en el método público del Service que inicia el caso de uso.<br>
<br>
En este proyecto<br>
Los métodos de creación, actualización y borrado de TaskServiceImpl.java serían candidatos naturales:

```
@Transactional
public Task create(Task task) {
    return taskRepository.save(task);
}
```
```
@Transactional
public Task update(Long id, Task task) {
    // buscar y modificar
    return taskRepository.save(existingTask);
}
```

Si uno de ellos falla con una excepción que provoque rollback, Spring deshace las modificaciones realizadas dentro de esa transacción. En resumen, @Transactional no ejecuta por sí sola la lógica de negocio: delimita la transacción y decide si los cambios se confirman o se revierten según cómo termine el método.<br>
<br>
Pregunta:<br>
<br>
¿Qué pasaría si pusieras @Transactional en el Repository en lugar del Service?<br>
<br>
Respuesta:<br>
<br>
Poner @Transactional en el Repository puede funcionar, pero normalmente no protege el caso de uso completo.<br>
<br>
Si se pone en una operación del repositorio<br>
Por ejemplo:

```
@Transactional
Task save(Task task);
```

o:

```
@Transactional
void deleteById(Long id);
```

la transacción cubrirá esa llamada concreta al repositorio:

```
1. Entra en save()
2. Spring abre una transacción
3. Ejecuta el INSERT o UPDATE
4. Hace commit al terminar save()
```

Pero si el Service ejecuta varias operaciones:

```
public void processTask(Task task, Audit audit) {
    taskRepository.save(task);
    auditRepository.save(audit);
}
```

cada repositorio puede iniciar y confirmar su propia transacción:

```
save(task)  → BEGIN → INSERT → COMMIT
save(audit) → BEGIN → INSERT → COMMIT
```

Si la segunda operación falla, la primera ya está confirmada y no se revierte:

```
Task guardada
Audit no guardado
```

Si se pone @Transactional en toda la interfaz del Repository<br>
También se puede anotar la interfaz:

```
@Transactional
public interface TaskRepository extends JpaRepository<Task, Long> {
}
```

protege las operaciones de TaskRepository, pero no necesariamente una operación posterior de AuditRepository.<br>
<br>
El Service sigue siendo la frontera adecuada<br>
En el Service:

```
@Transactional
public void processTask(Task task, Audit audit) {
    taskRepository.save(task);
    auditRepository.save(audit);
}
```

Spring abre una única transacción al entrar:

```
BEGIN
save(task)
save(audit)
COMMIT
```

Esto permite que la transacción represente la unidad lógica de negocio.<br>
<br>
Los repositorios de Spring Data ya tienen transacciones<br>
Los métodos CRUD heredados de JpaRepository ya tienen configuración transaccional predeterminada:<br>
<br>
las operaciones de lectura usan una transacción de lectura;<br>
las operaciones de escritura utilizan una transacción de escritura.<br>
Por eso añadir @Transactional en el Repository muchas veces es redundante para operaciones simples.<br>
<br>
La necesidad aparece cuando se quiere cambiar características concretas, por ejemplo:

```
@Transactional(readOnly = true)
List<Task> findByStatus(TaskStatus status);
```

Pero estas opciones deben justificarse según el caso.<br>
<br>
Un repositorio puede participar en la transacción del Service<br>
Si el Service inicia una transacción:

```
@Transactional
public void updateTaskAndAudit(...) {
    taskRepository.save(task);
    auditRepository.save(audit);
}
```

las llamadas a ambos repositorios normalmente se unen a la transacción existente gracias a la propagación REQUIRED.<br>
<br>
Por tanto, no hace falta anotar cada repositorio para que participe:

```
Service abre la transacción
    ↓
TaskRepository se une
    ↓
AuditRepository se une
    ↓
commit o rollback conjunto
```

Comparación<br>
<br>
Ubicación	Qué protege principalmente<br>
Repository	Una operación de persistencia concreta<br>
Service	Todo el caso de uso y varias operaciones coordinadas<br>
Controller	Toda la petición HTTP, mezclando transporte y persistencia<br>
<br>
En resumen, poner @Transactional en el Repository no es incorrecto, pero limita la transacción a las operaciones de ese repositorio y puede dejar parte del caso de uso fuera. En este ejercicio se coloca en el Service porque ahí se decide qué operaciones de negocio deben confirmarse o revertirse juntas.<br>
<br>
Pregunta:<br>
<br>
¿Qué diferencia hay entre un método de lectura y uno de escritura desde el punto de vista de la transacción?<br>
<br>
Respuesta:<br>
<br>
Desde el punto de vista transaccional, la diferencia principal es si el método modifica el estado de la base de datos o solo consulta datos.<br>
<br>
Método de lectura<br>
Un método de lectura ejecuta consultas sin modificar los datos:

```
@Transactional(readOnly = true)
public Task findById(Long id) {
    return taskRepository.findById(id)
        .orElseThrow();
}
```

Sus características habituales son:<br>
<br>
ejecuta SELECT;<br>
no necesita confirmar cambios;<br>
puede usar una transacción de solo lectura;<br>
puede beneficiarse de optimizaciones de Hibernate;<br>
puede mantener abiertas las relaciones LAZY mientras se ejecuta el método.<br>
readOnly = true es una indicación para Spring y el proveedor de persistencia de que no se esperan modificaciones:

```
@Transactional(readOnly = true)
```

No significa necesariamente que la base de datos vaya a bloquear físicamente cualquier INSERT, UPDATE o DELETE.<br>
<br>
Método de escritura<br>
Un método de escritura crea, modifica o elimina datos:

```
@Transactional
public Task update(Long id, Task data) {
    Task task = taskRepository.findById(id)
        .orElseThrow();

    task.setTitle(data.getTitle());
    task.setStatus(data.getStatus());

    return taskRepository.save(task);
}
```

Sus características:<br>
<br>
puede ejecutar INSERT, UPDATE o DELETE;<br>
necesita flush y commit;<br>
si termina correctamente, confirma los cambios;<br>
si falla con una excepción que provoque rollback, revierte los cambios;<br>
puede coordinar varias operaciones atómicas.<br>
Comparación

![parte5Paso4Ask](..\capturas\parte-05-paso4-ask.png)
<br>
En Spring Data JPA<br>
Los métodos CRUD del repositorio ya tienen configuración transaccional predeterminada. Aun así, la capa Service puede declarar la intención:

```
@Transactional(readOnly = true)
public Iterable<Task> findAll() {
    return taskRepository.findAll();
}
```
```
@Transactional
public Task create(Task task) {
    return taskRepository.save(task);
}
```

La diferencia no es que un método de lectura nunca pueda estar dentro de una transacción. Una lectura puede necesitarla para:<br>
<br>
mantener un contexto de persistencia;<br>
cargar relaciones lazy;<br>
obtener una vista consistente mientras se ejecutan varias consultas;<br>
definir un nivel de aislamiento.<br>
La diferencia es que una transacción de escritura permite confirmar o revertir cambios, mientras que una transacción de lectura se utiliza para controlar la consulta sin esperar modificaciones.<br>
<br>
Aplicado al ejercicio<br>
Por eso se pide utilizar @Transactional en los métodos que:<br>
<br>
crean una tarea;<br>
actualizan una tarea;<br>
borran una tarea.<br>
Esos métodos modifican la base de datos y deben quedar protegidos por commit o rollback.<br>
<br>
Los métodos de consulta:<br>
<br>
findAll;<br>
findById;<br>
findByStatus;<br>
findByPriority;<br>
solo leen información. No necesitan una transacción de escritura explícita, aunque podrían anotarse con:

```
@Transactional(readOnly = true)
```

si el caso de uso lo necesitara.<br>
<br>
En resumen:<br>
<br>
un método de lectura obtiene datos dentro de una transacción, pero no tiene cambios que confirmar; un método de escritura necesita una transacción que confirme todos los cambios o los revierta si la operación falla.

---
<br>
Paso 5 - Validación<br>
<br>
Application.properties ->se añaden los logs de transacciones:

```
spring.application.name=taskmanager
server.port=8080

#MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/taskmanager
spring.datasource.username=root
spring.datasource.password=1234abcd
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

#JPA/Hibernate
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

#Logging
logging.level.org.springframework.orm.jpa.JpaTransactionManager=DEBUG
```

---
<br>
Paso 5 - Validación<br>
<br>
Creación de Transacción:<br>

![TransaccionCreacionExitosa](..\capturas\05-transaccion-creacion-exitosa.png)
<br>
Por consola:<br>

![TransaccionCommitConsola](..\capturas\05-transaccion-commit-consola.png)
<br>
Transacción rollback:<br>

![TransaccionRollback](..\capturas\06-transaccion-rollback.png)
<br>
Por consola:<br>

![TransaccionRollbackConsola](..\capturas\06-transaccion-rollback-consola.png)

---
<br>
Reflexión<br>
<br>
Pregunta:<br>
<br>
¿Por qué @Transactional pertenece al Service y no al Repository<br>
<br>
Respuesta:<br>
<br>
Porque el Service coordina la operación de negocio completa y puede involucrar varias operaciones sobre la base de datos. Así, la transacción abarca toda la operación y garantiza que los cambios se confirmen juntos o se reviertan si ocurre un error.<br>
<br>
Pregunta:<br>
<br>
¿Qué problema concreto podría ocurrir en un método de escritura sin @Transactional si falla a mitad de la operación?<br>
<br>
Respuesta:<br>
<br>
Podrían quedar cambios parciales en la base de datos. Por ejemplo, si se guarda una parte de la información y después ocurre una excepción, los cambios anteriores podrían mantenerse, dejando los datos en un estado inconsistente.<br>
<br>
Pregunta:<br>
<br>
¿Qué ocurriría si un método del Service llama a otro método del mismo Service que también tiene @Transactional?<br>
<br>
Respuesta:<br>
<br>
En una llamada directa entre métodos de la misma instancia, Spring normalmente no intercepta la llamada interna mediante su proxy. Por tanto, no se aplica de forma independiente la configuración transaccional del segundo método; se mantiene el contexto transaccional del método externo, si existe.<br>
