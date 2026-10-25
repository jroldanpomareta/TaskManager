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
/**
 * Entidad JPA que representa una categoría de tareas.
 *
 * @author Javier Roldán
 * @version 1.0
 */
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
    /**
     * Constructor vacío.
     */
    public Category() {
    }

    /**
     * Constructor con parámetros.
     *
     * @param name        nombre de la categoría
     * @param description descripción de la categoría
     */
    public Category(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // Getters and setters
    /**
     * Obtiene el ID de la categoría.
     *
     * @return el ID de la categoría
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece el ID de la categoría.
     *
     * @param id el ID de la categoría
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre de la categoría.
     *
     * @return el nombre de la categoría
     */
    public String getName() {
        return name;
    }

    /**
     * Establece el nombre de la categoría.
     *
     * @param name el nombre de la categoría
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Obtiene la descripción de la categoría.
     *
     * @return la descripción de la categoría
     */
    public String getDescription() {
        return description;
    }

    /**
     * Establece la descripción de la categoría.
     *
     * @param description la descripción de la categoría
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Obtiene la lista de tareas asociadas a la categoría.
     *
     * @return la lista de tareas
     */
    public List<Task> getTasks() {
        return tasks;
    }

    /**
     * Establece la lista de tareas asociadas a la categoría.
     *
     * @param tasks la lista de tareas
     */
    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
    
}