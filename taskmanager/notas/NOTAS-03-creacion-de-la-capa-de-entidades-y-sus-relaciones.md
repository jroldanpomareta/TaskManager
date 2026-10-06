Paso 1 - Preparación de la BBDD<br>
<br>
Se ejecutan los scripts del init.sql en orden y se deja funcionando.
<br>
Se verifica que la BBDD "taskmanager" se ha creado bien y que las tabla "task", "category", "tag" y "task_tag" están y tienen los datos de prueba.

---
<br>
Paso 2 - Preparación (Ask):<br>
<br>
Pregunta:<br>
<br>
¿Qué es JPA, para qué sirve y qué relación tiene con Hibernate en una aplicación Spring Boot?<br>
<br>
Respuesta:<br>
<br>
JPA (Java Persistence API) es una especificación de Java para mapear objetos Java a tablas de una base de datos mediante ORM.<br>
<br>
Sirve para:<br>
<br>
Convertir entidades Java en registros de tablas.<br>
Guardar, consultar, actualizar y eliminar datos usando objetos.<br>
Definir relaciones entre entidades, como @OneToMany o @ManyToMany.<br>
Hibernate es una implementación de JPA. JPA define las reglas y Hibernate proporciona la funcionalidad concreta para ejecutarlas.<br>
<br>
En Spring Boot, normalmente la relación es:<br>
<br>
Spring Data JPA → JPA → Hibernate → MySQL<br>
<br>
Spring Data JPA simplifica el uso de JPA mediante repositorios como JpaRepository, mientras Hibernate realiza internamente las consultas SQL y la persistencia de las entidades.

---
<br>
Pregunta:<br>
<br>
¿Qué es una entidad JPA y cómo se mapea una clase Java con una tabla de una base de datos?<br>
<br>
Respuesta:<br>
<br>
Una entidad JPA es una clase Java que representa una tabla de la base de datos. Sus objetos representan filas y sus atributos representan columnas.<br>
<br>
Se mapea mediante anotaciones JPA:
```
@Entity
@Table(name = "task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String title;
}


@Entity: indica que la clase es una entidad gestionada por JPA.<br>
@Table(name = "task"): relaciona la clase con la tabla task.<br>
@Id: identifica la clave primaria.<br>
@GeneratedValue: indica que el identificador se genera automáticamente.<br>
@Column: permite configurar la columna asociada al atributo.<br>
Por defecto, JPA relaciona el nombre de cada atributo con una columna de nombre equivalente.Hibernate utiliza este mapeo para generar y ejecutar las consultas SQL contra MySQL.

---
<br>
Pregunta:<br>
¿Qué anotaciones son las más importantes para realizar ese mapeo?<br>
<br>
Respuesta:<br>
<br>
Las anotaciones JPA más importantes son:<br>
<br>
@Entity: indica que la clase es una entidad persistente.<br>
@Table: especifica la tabla asociada.<br>
@Id: identifica la clave primaria.<br>
@GeneratedValue: configura la generación automática del identificador.<br>
@Column: configura una columna, por ejemplo su nombre, longitud o si admite valores nulos.<br>
@OneToMany, @ManyToOne, @OneToOne y @ManyToMany: definen las relaciones entre entidades.<br>
@JoinColumn: especifica la columna utilizada como clave foránea.<br>
@JoinTable: configura una tabla intermedia, como task_tag.<br>
@Enumerated: indica cómo guardar un enum.<br>
@Transient: marca un atributo que no debe persistirse.<br>
Ejemplo:

```
@Entity
@Table(name = "task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String title;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
```

---
<br>
Pregunta:<br>
<br>
¿Cómo funcionan las relaciones entre entidades JPA, como @OneToMany, @ManyToOne y @ManyToMany, y cómo se corresponden con las relaciones entre tablas de una base de datos?<br>
<br>
Respuesta:<br>
<br>
Las relaciones JPA permiten representar en Java las relaciones entre tablas de una base de datos:<br>
<br>
@ManyToOne: muchas entidades pertenecen a una entidad. En la base de datos se representa mediante una clave foránea en la tabla del lado “muchos”.<br>
@OneToMany: una entidad se relaciona con muchas entidades. Es el lado inverso de @ManyToOne y suele utilizar mappedBy.<br>
@ManyToMany: muchas entidades se relacionan con muchas entidades. Se representa mediante una tabla intermedia con dos claves foráneas.<br>
Ejemplo con Task y Category:


```
@Entity
public class Task {

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}

@Entity
public class Category {

    @OneToMany(mappedBy = "category")
    private List<Task> tasks;
}
```
Esto corresponde a la columna category_id de la tabla task, que referencia a category.id.<br>
<br>
Ejemplo entre Task y Tag:<br>
```
@ManyToMany
@JoinTable(
    name = "task_tag",
    joinColumns = @JoinColumn(name = "task_id"),
    inverseJoinColumns = @JoinColumn(name = "tag_id")
)
private Set<Tag> tags;
```

La relación se almacena en la tabla intermedia task_tag, que contiene task_id y tag_id.<br>
<br>
@JoinColumn define la clave foránea y mappedBy indica el lado inverso de una relación ya gestionada por la otra entidad.

---
<br>
Paso 3 - Incluir dependencias:<br>
<br>
Se añade el comentario y se acepta la sugerencia del autocompletado.<br>
<br>
		Comentario:

```
        <!-- dependencia spring boot starter data jpa ->generado con IA -->
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-jpa</artifactId>
		</dependency>
		Comentario:
        <!-- Añadir la dependencia de MySQL Connector/J para conectar Spring Boot con MySQL ->generado con IA -->
		<dependency>
			<groupId>com.mysql </groupId>
			<artifactId>mysql-connector-j </artifactId>
		    <scope>runtime </scope>
		</dependency>
```

---
<br>
Paso 4 - Configuración de MySQL:<br>
<br>
Sólo se copia del pdf y se pega poniendo mi contraseña de MySQL:<br>
<br>
#MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/taskmanager<br>
spring.datasource.username=root<br>
spring.datasource.password=1234abcd<br>
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver<br>
<br>
#JPA/Hibernate<br>
spring.jpa.hibernate.ddl-auto=validate<br>
spring.jpa.show-sql=true<br>
spring.jpa.properties.hibernate.format_sql=true<br>

---
<br>
Paso 5 - Diseño previo:<br>
<br>
Pregunta:<br>
<br>
¿Qué cambios necesita la clase Task para convertirse en una entidad JPA?<br>
<br>
Respuesta:<br>
<br>
Poner @Entity y mapearla con la tabla task con @Table.<br>
En el identificador hay que marcarlo con @Id y utilizar @GeneratedValue(strategy = GenerationType.IDENTITY).<br>
También hay que usar @Column cuando el nombre del atributo no sea como el de la columna (Israel nos dijo el curso pasado que es buena práctica ponerlo siempre).<br>
Los campos status y priority se convierten en Enums y se mapean mediante @Enumerated(EnumType.STRING).<br>
<br>
Pregunta:<br>
<br>
¿Qué anotaciones esperas ver y para qué sirve cada una?<br>
<br>
Respuesta:<br>
<br>
@Entity ------------------------> indica que la clase es una entidad JPA.<br>
@Table -------------------------> indica con qué tabla de la BBDD se relaciona la entidad.<br>
@Id ----------------------------> indica cuál es el campo identificador de la entidad.<br>
@GeneratedValue ----------------> indica cómo se genera automáticamente el identificador.<br>
@Column ------------------------> permite indicar el nombre de la columna de la BBDD cuando no coincide con el nombre del atributo Java.<br>
@Enumerated(EnumType.STRING) ---> indica que un atributo de tipo enum se guarda en la BBDD mediante su nombre como texto.<br>
<br>
Pregunta:<br>
<br>
¿Qué comentario exacto escribirás para que el autocompletado lo genere?<br>
<br>
Respuesta:<br>
<br>
// Añadir las anotaciones JPA necesarias en la clase Task para mapearla con la tabla task, incluyendo @Entity, @Table, @Id, @GeneratedValue y @Column, sin sustituir ningún código existente. —> generado por IA

Paso 6 - Implementación (autocompletado):<br>
<br>
Primer comentario:<br>
<br>
// Añadir las anotaciones JPA necesarias y sus imports en la clase Task para mapearla con la tabla task, incluyendo @Entity, @Table, @Id, @GeneratedValue, sin sustituir ningún código existente. —> generado por IA<br>
<br>
Resultado:

```
package com.accenture.taskmanager;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

// Crear el modelo Task con los atributos: id, title, description, status, priority, dueDate, incluir sus tipos de datos más adecuados, añadir constructor vacío y por parámetros, añadir getters y setters
// Añadir las anotaciones JPA necesarias en la clase Task para mapearla con la tabla task, incluyendo @Entity, @Table, @Id y @GeneratedValue, y añadir los imports necesarios, sin sustituir ningún código existente. — generado por IA
// — generado por IA
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
    private String status;
    private String priority;
    private java.time.LocalDate dueDate;
```
---
<br>
Segundo comentario:<br>
<br>
// Añadir las anotaciones JPA necesarias en la clase Task para mapearla con la tabla task, incluyendo @Column en los campos cuyo nombre no coincide con la columna SQL (due_date, created_at, category_id) y añadir los imports necesarios, sin sustituir ningún código existente. — generado por IA
<br>
Resultado:

```
package com.accenture.taskmanager;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

// Crear el modelo Task con los atributos: id, title, description, status, priority, dueDate, incluir sus tipos de datos más adecuados, añadir constructor vacío y por parámetros, añadir getters y setters
// Añadir las anotaciones JPA necesarias en la clase Task para mapearla con la tabla task, incluyendo @Column en los campos cuyo nombre no coincide con la columna SQL (due_date, created_at, category_id) y añadir los imports necesarios, sin sustituir ningún código existente. — generado por IA
// Añadir las anotaciones JPA necesarias en la clase Task para mapearla con la tabla task, incluyendo @Entity, @Table, @Id y @GeneratedValue, y añadir los imports necesarios, sin sustituir ningún código existente. — generado por IA
// — generado por IA
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
    private String status;
    private String priority;
    @Column(name = "due_date")
    private java.time.LocalDate dueDate;
    @Column(name = "created_at")
    private java.time.LocalDateTime createdAt;
    @Column(name = "category_id")
    private Long categoryId;
```

---
<br>
Creación de los enums:<br>
<br>
Se añade el comentario:<br>
<br>
// Añade @Enumerated(EnumType.STRING) a los campos status y priority y conviértelos en enums con los nombres TaskStatus y TaskPriority -> generado por IA

```
// Crear el modelo Task con los atributos: id, title, description, status, priority, dueDate, incluir sus tipos de datos más adecuados, añadir constructor vacío y por parámetros, añadir getters y setters -> generado por IA
// Añadir las anotaciones JPA necesarias en la clase Task para mapearla con la tabla task, incluyendo @Column en los campos cuyo nombre no coincide con la columna SQL (due_date, created_at, category_id) y añadir los imports necesarios, sin sustituir ningún código existente. —> generado por IA
// Añadir las anotaciones JPA necesarias en la clase Task para mapearla con la tabla task, incluyendo @Entity, @Table, @Id y @GeneratedValue, y añadir los imports necesarios, sin sustituir ningún código existente. —> generado por IA
// Añade @Enumerated(EnumType.STRING) a los campos status y priority y conviértelos en enums con los nombres TaskStatus y TaskPriority -> generado por IA
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
    @Column(name = "category_id")
    private Long categoryId;
```

Y se completan los enums con estos comentarios:<br>
<br>
//completar el enum TaskPriority con los valores de bbdd-> generado por IA

```
package com.accenture.taskmanager.enums;

//completar el enum TaskPriority con los valores de bbdd-> generado por IA
/**
 * Representa las posibles prioridades de una tarea.
 *
 * @author Javier Roldán
 * @version 1.0
 */
public enum TaskPriority {
    LOW,
    MEDIUM,
    HIGH
}
```
//completar el enum TaskStatus con los valores de bbdd Done, In Progress, Pending-> generado por IA
```
package com.accenture.taskmanager.enums;

//completar el enum TaskStatus con los valores de bbdd Done, In Progress, Pending-> generado por IA
/**
 * Representa los posibles estados de una tarea.
 *
 * @author Javier Roldán
 * @version 1.0
 */
public enum TaskStatus {
   DONE,
   IN_PROGRESS,
   PENDING
}
```

Se cambia el constructor para que ajuste poniendo este comentario:<br>
<br>
// cambia el constructor para que los atributos status y priority sean de tipo TaskStatus y TaskPriority, respectivamente, y añade los imports necesarios, sin sustituir ningún código existente. —> generado por IA

```
    // cambia el constructor para que los atributos status y priority sean de tipo TaskStatus y TaskPriority, respectivamente, y añade los imports necesarios, sin sustituir ningún código existente. —> generado por IA
    /**
     * Constructor de la clase Task con todos sus atributos.
     *
     * @param id          identificador de la tarea
     * @param title       título de la tarea
     * @param description descripción de la tarea
     * @param status      estado de la tarea
     * @param priority    prioridad de la tarea
     * @param dueDate     fecha límite de la tarea
     */
    public Task(Long id, String title, String description, TaskStatus status, TaskPriority priority,
            java.time.LocalDate dueDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.dueDate = dueDate;
    }

```

Se ajustan los getters y setters con este comentario:<br>
<br>
// ajusta los getters y setters de los atributos status y priority para que devuelvan y acepten valores de tipo TaskStatus y TaskPriority, respectivamente, y añade los imports necesarios, sin sustituir ningún código existente. —> generado por IA

```
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
```

Se ponen los getters y setters que faltaban con este comentario:<br>
<br>
//pon los getters y setters de los atributos createdAt y categoryId, y añade los imports necesarios, sin sustituir ningún código existente. —> generado por IA

```
    //pon los getters y setters de los atributos createdAt y categoryId, y añade los imports necesarios, sin sustituir ningún código existente. —> generado por IA
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
     * Obtiene la categoría de la tarea.
     *
     * @return categoría de la tarea
     */
    public Long getCategoryId() {
        return categoryId;
    }

    /**
     * Modifica la categoría de la tarea.
     *
     * @param categoryId nueva categoría de la tarea
     */
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
```

---
<br>
Paso 7 - Comprensión del código (Ask):<br>
<br>
Pregunta:<br>
<br>
¿Qué diferencia hay entre @Column(name = "due_date") y no poner @Column? ¿Cuándo es obligatorio?<br>
<br>
Respuesta:<br>
<br>
En JPA/Hibernate la anotación @Column sirve para describir cómo se mapea una propiedad (campo o getter) de la entidad a una columna de la tabla de la base de datos.<br>
<br>
1. ¿Qué ocurre si no se pone @Column?<br>
Caso	Comportamiento implícito<br>
Nombre de la columna	JPA usa el nombre del atributo (o del getter) tal cual. Si el proyecto tiene habilitado un naming strategy (por ejemplo PhysicalNamingStrategyStandardImpl o ImplicitNamingStrategyJpaCompliantImpl), ese nombre puede transformarse automáticamente (p.ej. dueDate → due_date).<br>
Tipo de la columna	Se infiere del tipo Java (String → VARCHAR, LocalDate → DATE, etc.).<br>
Longitud, precisión, nullable, unique, insertable, updatable, columnDefinition, …	Se aplican los valores por defecto de JPA (p.ej. nullable = true, length = 255 para String, insertable = true, updatable = true).<br>
Columnas de la tabla	Se crean automáticamente con los nombres e índices que genere el dialect de la base de datos y la estrategia de nombrado.<br>
En otras palabras, no es obligatorio declarar @Column mientras el nombre que JPA genera por defecto coincida con el que realmente tiene la tabla y no necesites cambiar ninguna de las propiedades de la columna.<br>
<br>
2. ¿Qué aporta @Column(name = "due_date")?

```
@Column(name = "due_date")
private LocalDate dueDate;
```
Nombre explícito: le indica a JPA que el atributo dueDate debe mapearse a la columna due_date. Esto es necesario cuando el nombre de la columna no coincide con el que produciría la convención implícita (p.ej. la tabla tiene due_date y la estrategia de nombrado de tu proyecto mantiene los nombres tal cual, es decir, dueDate → dueDate).<br>
Control de otras características (aunque en el ejemplo solo se especifica name, @Column permite definir nullable, unique, length, precision, scale, columnDefinition, insertable, updatable, etc.).<br>
3. ¿Cuándo es obligatorio usar @Column?<br>
El nombre de la columna difiere del nombre del atributo y la estrategia de nombrado que tienes configurada no lo ajusta automáticamente.

```
// Tabla: due_date   →   atributo: dueDate
@Column(name = "due_date")
private LocalDate dueDate;
```
Necesitas personalizar alguna propiedad de la columna:<br>
<br>
nullable = false para que la columna sea NOT NULL.<br>
unique = true para crear un índice único.<br>
length = 50 (para String) o precision/scale (para números).<br>
columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP" cuando quieres especificar DDL propio.<br>
insertable = false / updatable = false si la columna es gestionada sólo por la base de datos (p.ej. una columna de auditoría).<br>
Mapeo de columnas que no siguen la convención estándar (por ejemplo, columnas con caracteres especiales, mayúsculas, prefijos, etc.).<br>
<br>
Cuando la entidad hereda de una superclase y deseas sobrescribir el mapeo de una columna ya definida en la superclase.<br>
<br>
En casos de columnas calculadas o de solo lectura (usando @Column(insertable = false, updatable = false)).<br>
<br>
4. Resumen rápido<br>
Necesidad	Necesitas @Column?<br>
El nombre de la columna es idéntico al atributo (o se ajusta con la estrategia de nombrado) y no necesitas cambiar otras propiedades	No<br>
El nombre de la columna es distinto al atributo	Sí<br>
Quieres cambiar nullable, unique, length, precision, etc.	Sí<br>
Quieres indicar que la columna no se inserta o actualiza desde la entidad	Sí<br>
Necesitas especificar un columnDefinition propio (tipo SQL, default, etc.)	Sí<br>
En la práctica, muchos desarrolladores añaden @Column sólo cuando es estrictamente necesario (nombre diferente o restricciones especiales). Si todo el mapeo sigue las convenciones por defecto, puedes omitirla sin problemas.

---
<br>
Pregunta:<br>
<br>
¿Qué hace @Enumerated(EnumType.STRING) y qué pasaría si usaras EnumType.ORDINAL?<br>
<br>
Respuesta:<br>
<br>
@Enumerated en JPA/Hibernate

```
@Enumerated(EnumType.STRING)   // o EnumType.ORDINAL
private Estado estado;
```

La anotación @Enumerated le indica a JPA cómo debe persistir un atributo cuyo tipo es enum.<br>
Hay dos estrategias:<br>
<br>
![imagenPaso7](..\capturas\Captura-03-imagenPaso7.png)

1. ¿Qué hace @Enumerated(EnumType.STRING)?<br>
Al persistir la entidad, JPA convierte la constante del enum en su nombre (Enum.name()) y lo escribe en la columna como texto.<br>
Al leer la fila, JPA toma el texto, busca la constante del enum cuyo name() coincide y la asigna al atributo.<br>
La columna normalmente se crea como VARCHAR (o NVARCHAR según el dialecto) con la longitud suficiente para el nombre más largo del enum.<br>
Ejemplo

```
public enum Estado {
    ACTIVO,
    INACTIVO,
    SUSPENDIDO
}

@Entity
public class Usuario {
    @Id
    private Long id;

    @Enumerated(EnumType.STRING)
    private Estado estado;
}
```

Valor en la entidad	Valor guardado en la tabla<br>
Estado.ACTIVO	"ACTIVO"<br>
Estado.SUSPENDIDO	"SUSPENDIDO"<br>
<br>
2. ¿Qué pasaría si usas EnumType.ORDINAL?<br>
a) Mapeo y almacenamiento

```
@Enumerated(EnumType.ORDINAL)
private Estado estado;
```

Persistencia: JPA escribe el ordinal (enum.ordinal()) en la columna (tipo entero).<br>
<br>
ACTIVO → 0<br>
INACTIVO → 1<br>
SUSPENDIDO → 2<br>
Lectura: JPA recupera el entero y lo traduce a la constante del enum cuyo índice coincide (Estado.values()[ordinal]).<br>
<br>
![imagenPaso7b](..\capturas\Captura-03-imagenPaso7b.png)

Resumen rápido<br>
@Enumerated(EnumType.STRING) → almacena el nombre del enum (texto). Seguro frente a cambios de orden; legible.<br>
@Enumerated(EnumType.ORDINAL) → almacena el ordinal (número). Menor espacio, pero muy frágil ante cualquier reordenación, inserción o eliminación de constantes.<br>
Si no tienes una razón muy clara y controlada para usar ORDINAL, opta por STRING.

---
<br>
Pregunta:<br>
<br>
¿Por qué se usa GenerationType.IDENTITY para MySQL y no SEQUENCE?<br>
<br>
Respuesta:<br>
<br>
![imagenPaso7c](..\capturas\Captura-03-imagenPaso7c.png)
<br>
1. Qué es cada estrategia<br>
1.1 GenerationType.IDENTITY<br>
Modo de funcionamiento:<br>
Cada vez que se ejecuta INSERT sin especificar el valor de la columna de la clave primaria, MySQL genera automáticamente el siguiente número usando la propiedad AUTO_INCREMENT de la columna.<br>
Ventajas:<br>
No necesita una tabla o objeto extra para gestionar los valores.<br>
La generación ocurre dentro de la propia sentencia INSERT, por lo que el número está garantizado como único y no hay riesgo de “huecos” entre transacciones concurrentes.<br>
El valor generado está disponible inmediatamente después del INSERT (SELECT LAST_INSERT_ID()), y los drivers JDBC lo exponen a JPA/Hibernate sin pasos intermedios.<br>
Desventajas (poco frecuentes):<br>
No permite pre‑generar el identificador antes de la inserción (no puedes conocer el id sin ejecutar el INSERT).<br>
Algunas optimizaciones de batch‑inserts son más difíciles porque el motor necesita ejecutar cada INSERT para obtener el id.<br>
1.2 GenerationType.SEQUENCE<br>
Modo de funcionamiento:<br>
Se crea un objeto secuencia (CREATE SEQUENCE …).<br>
Cada vez que la aplicación necesita un nuevo id, ejecuta SELECT NEXTVAL('seq_name') (o la variante que use el dialecto).<br>
El valor obtenido se usa en la sentencia INSERT.<br>
Ventajas:<br>
Permite pre‑generar varios identificadores antes de hacer los INSERT, lo que facilita los batch inserts y reduce round‑trips.<br>
Puedes definir la incremente, el valor inicial, y el cache de la secuencia para afinar el rendimiento.<br>
Desventajas:<br>
Requiere que la base de datos soporte objetos SEQUENCE.<br>
Si la secuencia se usa fuera de transacciones, pueden aparecer “huecos” (valores que nunca llegan a usarse) – algo aceptado normalmente en sistemas de producción.<br>
2. MySQL no tiene objetos SEQUENCE<br>
2.1 Historia de MySQL<br>
MySQL 5.x y MySQL 8.x (las versiones más usadas) no implementan la sintaxis estándar CREATE SEQUENCE ….<br>
En lugar de secuencias, MySQL expone la característica AUTO_INCREMENT a nivel de columna, que es su mecanismo propio para generar claves únicas de forma incremental.<br>
La ausencia de SEQUENCE no es un “bug”; es una decisión de diseño del motor. MySQL prefiere mantener la generación de ids en la propia tabla mediante AUTO_INCREMENT.<br>
2.2 Intentos de simulación<br>
Es posible simular una secuencia en MySQL usando una tabla auxiliar y triggers (INSERT INTO seq_table VALUES (NULL)), o mediante funciones almacenadas que incrementan un contador.<br>
Sin embargo, esas soluciones son menos eficientes y más complejas que simplemente usar AUTO_INCREMENT.<br>
Hibernate (y otras JPA implementaciones) no provee un SequenceGenerator nativo para MySQL porque la emulación implica una sobrecarga que suele ser innecesaria.<br>
3. Consecuencias para JPA/Hibernate<br>
3.1 Configuración típica

```
@Entity
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // <-- recomendado para MySQL
    private Long id;

    // …
}
```

Con IDENTITY, Hibernate delega al driver JDBC el trabajo de obtener el valor generado (Statement.getGeneratedKeys() o SELECT LAST_INSERT_ID()).<br>
No se necesita definir ninguna tabla o secuencia extra en el script DDL.<br>
3.2 ¿Y si insistes en SEQUENCE?

```
@Entity
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cliente_seq")
    @SequenceGenerator(name = "cliente_seq", sequenceName = "cliente_seq", allocationSize = 1)
    private Long id;
}
```
En MySQL, al arrancar la aplicación Hibernate lanzará una excepción del tipo org.hibernate.MappingException: Unable to locate SequenceGenerator porque no encuentra la secuencia.<br>
Para “hacer que funcione” tendrías que crear una tabla de soporte y un SequenceStyleGenerator de Hibernate, lo cual complica la configuración y elimina la ventaja principal de SEQUENCE (pre‑generar ids sin tocar la tabla).<br>
![imagenPaso7d](..\capturas\Captura-03-imagenPaso7d.png)
5. Buenas prácticas y recomendaciones<br>
Usa IDENTITY para MySQL (y MariaDB) a menos que tengas una razón muy concreta para simular una secuencia.<br>
Si necesitas batch inserts con MySQL y notas que el rendimiento es bajo, considera:<br>
hibernate.jdbc.batch_size (permite agrupar varios INSERT aunque cada uno genere su propio id).<br>
hibernate.id.new_generator_mappings y el enhanced‑sequence de Hibernate que, aunque usa una tabla de apoyo (hibernate_sequence), es más eficiente que una secuencia real.<br>
Si tu proyecto debe ser portátil entre MySQL y una BD que sí tenga secuencias (p.ej. PostgreSQL), puedes:<br>
Definir dos perfiles de configuración y usar @GeneratedValue(strategy = GenerationType.AUTO), que delega a la estrategia “más adecuada” según el dialecto.<br>
O bien, crear una abstracción de generación de id mediante una clase base que tenga el @Id y el @GeneratedValue con strategy = GenerationType.IDENTITY para MySQL y SEQUENCE para los demás, usando @Profile o archivos persistence.xml diferentes.<br>
No intentes crear una secuencia en MySQL a menos que realmente necesites una numeración global fuera de cualquier tabla (p.ej. para códigos de factura que deben ser continuos entre distintas tablas). En ese caso, la solución más sencilla es una tabla de control (next_val) y un procedimiento almacenado que haga UPDATE … SET val = val + 1 RETURNING val.<br>
6. Conclusión<br>
MySQL no dispone de objetos SEQUENCE. Su mecanismo nativo para generar claves incrementales es AUTO_INCREMENT, que JPA expone mediante GenerationType.IDENTITY.<br>
GenerationType.IDENTITY es la estrategia recomendada y más simple para MySQL porque:<br>
Aprovecha la funcionalidad built‑in de la base de datos.<br>
No requiere artefactos adicionales (tablas de secuencia, triggers, etc.).<br>
Garantiza unicidad y atomicidad con mínima sobrecarga.<br>
GenerationType.SEQUENCE solo es útil en DBMS que implementan el estándar SQL SEQUENCE (PostgreSQL, Oracle, DB2, etc.). Intentarlo en MySQL obligaría a crear una capa de emulación que, en la práctica, complica el código y suele ser menos eficiente que simplemente usar IDENTITY.<br>
Así que, en la práctica, cuando trabajas con MySQL (o MariaDB) elige siempre IDENTITY; reserva SEQUENCE para bases de datos que lo soporten de forma nativa.

---
<br>
Pregunta:<br>
<br>
¿Qué ocurre si el nombre de un campo Java no coincide con el nombre de la columna en base de datos y no añades @Column?<br>
<br>
Respuesta:<br>
<br>
Cuando el nombre del atributo Java no coincide con el nombre real de la columna y no se usa @Column
JPA (y, por extensión, Hibernate) intenta inferir el nombre de la columna a partir del nombre del atributo (o del getter) mediante dos fases:

![imagenPaso7e](..\capturas\Captura-03-imagenPaso7e.png)

<br>
1️⃣ Si la columna real en la tabla sigue la convención que aplica la Physical Naming Strategy<br>
Ejemplo: atributo dueDate → JPA genera due_date → la tabla tiene una columna due_date.<br>
<br>
En este caso no hay problema: la entidad funciona sin necesidad de @Column.<br>
<br>
2️⃣ Si la columna real tiene otro nombre que no coincide con la transformación anterior<br>
Ejemplo 1: atributo dueDate → JPA genera due_date → la tabla tiene fecha_vencimiento.<br>
Ejemplo 2: atributo status → JPA genera status (sin transformación) → la tabla tiene estado.<br>
<br>
Entonces, al iniciar la aplicación o al ejecutar una operación que implique esa columna, ocurrirá uno de los siguientes errores:<br>
<br>
Momento del error	Tipo de excepción	Mensaje típico<br>
Arranque del EntityManagerFactory (validación del esquema)	org.hibernate.MappingException o org.hibernate.tool.schema.spi.SchemaManagementException	Unable to find column with logical name due_date in table X<br>
Al ejecutar un SELECT / INSERT / UPDATE	java.sql.SQLSyntaxErrorException (MySQL) / org.hibernate.exception.SQLGrammarException	Column 'due_date' not found o Unknown column 'status' in 'field list'<br>
Durante la generación automática del DDL (si hibernate.hbm2ddl.auto=update o create)	Hibernate intentará crear una columna con el nombre inferido, lo que puede sobreescribir o añadir columnas inesperadas.	La tabla termina con columnas duplicadas o con nombres diferentes a los esperados.<br>
En resumen, sin @Column JPA confía en sus estrategias de nombrado; si la convención de la BD no coincide, la aplicación fallará o producirá un esquema incorrecto.<br>
<br>
2️⃣ ¿Cómo resolverlo sin @Column?<br>
Opción A – Cambiar la Physical Naming Strategy del proyecto<br>
En application.properties (o application.yml) puedes indicar a Hibernate que use una estrategia que no transforme los nombres, o que siga una regla distinta:<br>
<br>
<br>
Apply<br>
# No transformar camelCase → snake_case<br>
spring.jpa.hibernate.naming.physical-strategy=org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl<br>
# O bien, usar tu propia implementación:<br>
# spring.jpa.hibernate.naming.physical-strategy=com.miempresa.MyPhysicalNamingStrategy<br>
Con PhysicalNamingStrategyStandardImpl el nombre implícito será exactamente el del atributo (dueDate), por lo que la columna debe llamarse igual en la BD.<br>
<br>
Opción B – Modificar la Implicit Naming Strategy<br>
<br>
Apply<br>
spring.jpa.hibernate.naming.implicit-strategy=org.hibernate.boot.model.naming.ImplicitNamingStrategyLegacyJpaImpl<br>
Esta estrategia también mantiene el nombre tal cual, evitando la conversión a snake_case.<br>
<br>
Nota: Cambiar la estrategia afecta todas las entidades del proyecto; asegúrate de que sea coherente con el resto del modelo.<br>
<br>
Opción C – Renombrar la columna en la base de datos<br>
Si tienes acceso a los scripts DDL, simplemente renombra la columna para que siga la convención esperada por la estrategia actual:<br>
<br>
<br>
Apply<br>
ALTER TABLE orders RENAME COLUMN fecha_vencimiento TO due_date;<br>
Opción D – Añadir explícitamente @Column(name = "...")<br>
Esta es la solución más puntual y no rompe la configuración global. Solo se modifica el mapeo del atributo que difiere:

```
@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // La columna en la tabla se llama "fecha_vencimiento"
    @Column(name = "fecha_vencimiento")
    private LocalDate dueDate;
}

```

3️⃣ ¿Cuándo es obligatorio usar @Column(name = "...")?<br>
Situación	Necesidad de @Column<br>
El nombre de la columna no sigue la convención implícita + física del proyecto.	Sí<br>
Necesitas cambiar propiedades como nullable, length, unique, columnDefinition, etc.	Sí (aunque el nombre coincida)<br>
La tabla pertenece a un esquema legacy con nombres totalmente arbitrarios (p.ej., FEC_VENC, ESTADO_ORD).	Sí<br>
La entidad hereda de una super‑clase y quieres sobrescribir el nombre de la columna definida allí.	Sí<br>
Quieres que una columna sea solo de lectura (insertable = false, updatable = false).	Sí<br>
Necesitas mapear una columna que contiene caracteres especiales (espacios, guiones, mayúsculas) que no se generan automáticamente.	Sí<br>
En cualquier otro caso (nombres que ya coinciden tras aplicar la estrategia de nombrado y sin requerimientos especiales) no es obligatorio usar @Column.<br>
<br>
4️⃣ Recapitulación práctica<br>
Sin @Column JPA usa dos estrategias de nombrado:<br>
Implicit → nombre del atributo.<br>
Physical → transformación (camelCase → snake_case por defecto en Spring Boot).<br>
Si la columna de la BD no coincide con el nombre resultante, obtendrás errores de mapeo o de SQL.<br>
Puedes solucionar el desajuste de tres formas principales:<br>
Cambiar la estrategia de nombrado global (propiedades spring.jpa.hibernate.naming.*).<br>
Renombrar la columna en la BD.<br>
Añadir @Column(name = "...") al atributo que difiere (la solución más localizada y segura).<br>
@Column es obligatorio siempre que necesites:<br>
Un nombre de columna distinto al inferido.<br>
Configurar atributos de la columna (nullability, longitud, etc.).<br>
Cumplir con requisitos de legibilidad o de compatibilidad con esquemas heredados.<br>
Con estos conceptos podrás decidir cuándo es necesario usar @Column y cómo evitar errores de mapeo cuando los nombres no están alineados.

---
<br>
Pregunta:<br>
<br>
¿Qué hace ddl-auto = validate al arrancar la aplicación? ¿Qué error daría si un campo no coincide con la tabla?<br>
<br>
Respuesta:<br>
<br>
spring.jpa.hibernate.ddl-auto=validate hace que Hibernate, al arrancar la aplicación, compruebe que las entidades JPA coinciden con la estructura de las tablas existentes en la base de datos.<br>
Con validate, Hibernate no crea, modifica ni elimina tablas o columnas; únicamente valida el esquema.<br>
Si un campo de la entidad no coincide con la tabla —por ejemplo, si la entidad tiene un atributo que no corresponde con ninguna columna esperada, o el tipo/estructura no coincide— Hibernate detectará la discrepancia durante el arranque y la aplicación fallará al iniciar, mostrando una excepción de validación del esquema, normalmente un mensaje del tipo:<br>
Schema-validation: missing column [...] in table [...]<br>
Por ejemplo, si Task tuviera createdAt pero no estuviera correctamente mapeado a created_at, podría aparecer un error indicando que falta la columna correspondiente.

---

<br>
Paso 8 - Verificación:<br>
<br>
Comprobación del arranque:

![comprobacionArranque](..\capturas\Captura-03-comprobacion-arranque.png)
<br>
Confirmación de que se pueden consultar datos con JPQL:<br>
<br>
Se crea la clase de prueba con el comentario que se indica en el pdf:

```
package com.accenture.taskmanager;

import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Component
public class JpaTest {

    @PersistenceContext
    private EntityManager em;
    // DECISIÓN: contar cuántas Task hay, con EntityManager y una consulta JPQL
    public long countTasks() {
        return em.createQuery("SELECT COUNT(t) FROM Task t", Long.class).getSingleResult();

    }

}


```

Probar que imprime:

```
    // usar el método countTasks() para contar cuántas Task hay y mostrarlo por consola
    @PostConstruct 
    public void printTaskCount() {
        long count = countTasks();
        System.out.println("Número de tareas: " + count);
    }

```

Comprobación de que imprime:

![comprobacionImprime](..\capturas\Captura-03-comprobacion-imprime.png)

---
<br>
Reflexión:<br>
<br>
Pregunta:<br>
<br>
¿Qué ventaja tiene usar ddl-auto=validate frente a ddl-auto=update en un entorno profesional?<br>
<br>
Respuesta:<br>
<br>
ddl-auto=validate tienes más control porque comprueba que las entitys coinciden con BBDD pero no hace cambios y ddl-auto=update puede cambiar automáticamente la BBDD para que coincida con las entitys y no lo controlas.<br>
<br>
Pregunta:<br>
<br>
¿Qué problema tendría exponer directamente la entidad Task en los endpoints de la API? (Pista: lo resolverás en el ejercicio de creación de los DTO’s.)<br>
<br>
Respuesta:<br>
<br>
Pues que si expones la entity Task directamente puedes exponer datos que no deberían ser parte de la API y además cualquier cambio en la entity podría afectar a los endpoints.<br>
Los DTOs permiten separar la entity del model que se expone en la API y así se elige qué datos se envían y reciben.<br>
<br>
Pregunta:<br>
<br>
¿Qué ocurriría si cambias el nombre de un campo en la entidad sin actualizar la base de datos?<br>
<br>
Respuesta:<br>
<br>
Pues que si tienes validate y no update detectará que no coincide la entity con la BBDD y dará fallo al arrancar, con update no pasa porque lo rellena solo.

---
<br>
Adaptación de los datos de ejemplo en TaskServiceImpl:<br>
<br>
El PDF indica que en TaskServiceImpl únicamente se pueden modificar los new Task de los datos de ejemplo para adaptarlos a los enums y a los
nuevos campos de Task por lo que se sustituyen los valores String de status y priority por los valores correspondientes de los enums TaskStatus y TaskPriority, y se añaden los nuevos parámetros createdAt y categoryId del constructor mediante el siguiente comentario:

```
    // Crea una lista igual que en el controlador para simular la persistencia de
    // datos y añade 2 tareas de ejemplo
    // al ArrayList usando el constructor de la clase Task para poder probar los
    // endpoints -> generado con IA
    // Actualiza las tareas de ejemplo para adaptarlas al nuevo constructor de Task,
    // Para los valores de status, consulta directamente el archivo
    // src/main/java/com/accenture/taskmanager/enums/TaskStatus.java y utiliza exactamente
    // los nombres de los enums declarados allí, sin traducirlos ni inventarlos.
    // Para los valores de priority, consulta directamente el archivo
    // src/main/java/com/accenture/taskmanager/enums/TaskPriority.java y utiliza exactamente
    // los nombres de los enums declarados allí, sin traducirlos ni inventarlos.
    // Añade también los valores correspondientes a createdAt y categoryId, no pongas PENDIENTE, ni ALTA, ni EN_PROGRESO ni MEDIA.
    // —> generado con IA (sería el comentario genérico para que actualizara pero con los null en campos nuevos ya no aplica)
    private static final List<Task> tasks = new ArrayList<>();
    static {
        tasks.add(new Task(1L, "Tarea 1", "Descripción de la tarea 1", TaskStatus.PENDING, TaskPriority.LOW,
                java.time.LocalDate.of(2024, 6, 30), java.time.LocalDateTime.now(), 1L));
        tasks.add(new Task(2L, "Tarea 2", "Descripción de la tarea 2", TaskStatus.IN_PROGRESS, TaskPriority.HIGH,
                java.time.LocalDate.of(2024, 7, 15), java.time.LocalDateTime.now(), 2L));
    }
```
Y luego después de currarme mil comentarios hasta encontrar uno que hiciera bien el autocompletado leí en el pdf que los campos nuevos hay que declararlos con null 😥 así que se cambian para que coincida con las instrucciones:

```
/**
 * Implementación del servicio encargado de gestionar la lógica de negocio
 * relacionada con las tareas.
 *
 * <p>
 * Esta clase implementa las operaciones definidas en la interfaz
 * {@link TaskService} y mantiene temporalmente las tareas en una lista
 * en memoria.
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
    // src/main/java/com/accenture/taskmanager/enums/TaskStatus.java y utiliza exactamente
    // los nombres de los enums declarados allí, sin traducirlos ni inventarlos.
    // Para los valores de priority, consulta directamente el archivo
    // src/main/java/com/accenture/taskmanager/enums/TaskPriority.java y utiliza exactamente
    // los nombres de los enums declarados allí, sin traducirlos ni inventarlos.
    // Añade también los valores correspondientes a createdAt y categoryId, no pongas PENDIENTE, ni ALTA, ni EN_PROGRESO ni MEDIA.
    // —> generado con IA (sería el comentario genérico para que actualizara pero con los null en campos nuevos ya no aplica)
    private static final List<Task> tasks = new ArrayList<>();
    static {
        tasks.add(new Task(1L, "Tarea 1", "Descripción de la tarea 1",
                TaskStatus.PENDING, TaskPriority.LOW,
                java.time.LocalDate.of(2024, 6, 30), null, null));

        tasks.add(new Task(2L, "Tarea 2", "Descripción de la tarea 2",
                TaskStatus.IN_PROGRESS, TaskPriority.HIGH,
                java.time.LocalDate.of(2024, 7, 15), null, null));
    }
```