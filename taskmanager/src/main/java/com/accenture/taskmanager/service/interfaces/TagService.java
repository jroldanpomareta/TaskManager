package com.accenture.taskmanager.service.interfaces;

import java.util.List;

import com.accenture.taskmanager.dto.TagRequestDTO;
import com.accenture.taskmanager.dto.TagResponseDTO;

/**
 * Interfaz para el servicio de gestión de etiquetas.
 * Proporciona métodos para listar todas las etiquetas, buscar una por ID, crear una etiqueta, actualizarla y eliminarla.
 * Utiliza TagResponseDTO para las respuestas, TagRequestDTO para crear y actualizar, y Long como identificador.
 * Añade Javadoc a la interfaz y a todos los métodos, manteniendo el estilo de los servicios existentes del proyecto.
 */
// Crear la interfaz TagService con métodos para listar todas las etiquetas, buscar una por ID, crear una etiqueta, actualizarla y eliminarla. Utilizar TagResponseDTO para las respuestas, TagRequestDTO para crear y actualizar, y Long como identificador. Añadir Javadoc a la interfaz y a todos los métodos, manteniendo el estilo de los servicios existentes del proyecto. — generado por IA
/**
 * Interfaz para el servicio de gestión de etiquetas.
 * Proporciona métodos para listar todas las etiquetas, buscar una por ID, crear una etiqueta, actualizarla y eliminarla.
 * Utiliza TagResponseDTO para las respuestas, TagRequestDTO para crear y actualizar, y Long como identificador.
 * Añade Javadoc a la interfaz y a todos los métodos, manteniendo el estilo de los servicios existentes del proyecto.
 * 
 * @author Javier Roldán
 * @version 1.0
 */
public interface TagService {

    /**
     * Lista todas las etiquetas.
     *
     * @return una lista de TagResponseDTO que representan todas las etiquetas
     */
    List<TagResponseDTO> getAllTags();

    /**
     * Busca una etiqueta por su ID.
     *
     * @param id el ID de la etiqueta a buscar
     * @return un TagResponseDTO que representa la etiqueta encontrada, o null si no se encuentra
     */
    TagResponseDTO getTagById(Long id);

    /**
     * Crea una nueva etiqueta.
     *
     * @param tagRequestDTO el DTO de solicitud que contiene los datos de la nueva etiqueta
     * @return un TagResponseDTO que representa la etiqueta creada
     */
    TagResponseDTO createTag(TagRequestDTO tagRequestDTO);

    /**
     * Actualiza una etiqueta existente.
     *
     * @param id            el ID de la etiqueta a actualizar
     * @param tagRequestDTO el DTO de solicitud que contiene los datos actualizados de la etiqueta
     * @return un TagResponseDTO que representa la etiqueta actualizada, o null si no se encuentra la etiqueta
     */
    TagResponseDTO updateTag(Long id, TagRequestDTO tagRequestDTO);

    /**
     * Elimina una etiqueta por su ID.
     *
     * @param id el ID de la etiqueta a eliminar
     */
    void deleteTag(Long id);
}
