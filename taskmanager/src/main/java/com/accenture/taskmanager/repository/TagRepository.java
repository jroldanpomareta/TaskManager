package com.accenture.taskmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.accenture.taskmanager.model.Tag;

/**
 * Repositorio para la entidad Tag.
 * Proporciona operaciones CRUD para la entidad Tag.
 * 
 * @author Javier Roldán
 * @version 1.0
 */
// Crear TagRepository como una interfaz que extienda JpaRepository<Tag, Long>, importando la entidad Tag y JpaRepository. Añadir Javadoc a nivel de interfaz y mantener el estilo de los repositorios existentes del proyecto. — generado por IA
public interface TagRepository extends JpaRepository<Tag, Long> {
    
}
