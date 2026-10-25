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
/**
 * Represents a tag that can be associated with tasks.
 * This entity is mapped to the "tag" table in the database.
 * 
 * @author Javier Roldán
 * @version 1.0
 */
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
    /**
     * Default constructor for JPA.
     */
    public Tag() {
    }

    /**
     * Constructs a Tag with the specified name.
     *
     * @param name the name of the tag
     */
    public Tag(String name) {
        this.name = name;
    }

    // Getters and setters
    /**
     * Returns the ID of the tag.
     *
     * @return the ID of the tag
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the ID of the tag.
     *
     * @param id the ID to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Returns the name of the tag.
     *
     * @return the name of the tag
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the tag.
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the list of tasks associated with this tag.
     *
     * @return the list of tasks
     */
    public List<Task> getTasks() {
        return tasks;
    }

    /**
     * Sets the list of tasks associated with this tag.
     *
     * @param tasks the list of tasks to set
     */
    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}
