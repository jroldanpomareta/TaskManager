package com.accenture.taskmanager.repository;

// Crear CategoryRepository como interfaz que extienda JpaRepository<Category, Long>, siguiendo el estilo de TaskRepository. Añadir los imports necesarios y Javadoc. — generado por IA

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.accenture.taskmanager.model.Category;

/**
 * Repositorio para la entidad Category.
 * Proporciona métodos para realizar operaciones CRUD en la base de datos relacionadas con las categorías.
 * Extiende JpaRepository para heredar métodos de acceso a datos.
 * 
 * @author Javier Roldán
 * @version 1.0
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}


