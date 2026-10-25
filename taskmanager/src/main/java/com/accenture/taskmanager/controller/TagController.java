package com.accenture.taskmanager.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.accenture.taskmanager.dto.TagRequestDTO;
import com.accenture.taskmanager.dto.TagResponseDTO;
import com.accenture.taskmanager.service.interfaces.TagService;

/**
 * Controlador para la gestión de etiquetas.
 * Proporciona endpoints para realizar operaciones CRUD sobre las etiquetas.
 *
 * @author Javier Roldán
 * @version 1.0
 */
// Crear TagController con @RestController y @RequestMapping("/api/v1/tags"), inyectando TagService por constructor. Implementar los endpoints GET para listar todas las etiquetas y obtener una por ID, POST para crear, PUT para actualizar por ID y DELETE para eliminar por ID. Utilizar los DTOs correspondientes, devolver códigos HTTP adecuados y añadir Javadoc a la clase y a todos los métodos. Mantener el estilo de los controladores existentes. — generado por IA
@RestController
@RequestMapping("/api/v1/tags")
public class TagController {

    /**
     * Servicio de gestión de etiquetas.
     */
    private final TagService tagService;

    /**
     * Constructor del controlador.
     *
     * @param tagService servicio de gestión de etiquetas
     */
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    /**
     * Lista todas las etiquetas.
     *
     * @return lista de etiquetas con estado HTTP 200
     */
    @GetMapping
    public ResponseEntity<List<TagResponseDTO>> getAllTags() {
        return ResponseEntity.ok(tagService.getAllTags());
    }

    /**
     * Obtiene una etiqueta por su identificador.
     *
     * @param id identificador de la etiqueta
     * @return etiqueta encontrada con estado HTTP 200
     */
    @GetMapping("/{id}")
    public ResponseEntity<TagResponseDTO> getTagById(@PathVariable Long id) {
        return ResponseEntity.ok(tagService.getTagById(id));
    }

    /**
     * Crea una nueva etiqueta.
     *
     * @param tagRequestDTO datos de la etiqueta
     * @return etiqueta creada con estado HTTP 201
     */
    @PostMapping
    public ResponseEntity<TagResponseDTO> createTag(@RequestBody TagRequestDTO tagRequestDTO) {
        TagResponseDTO createdTag = tagService.createTag(tagRequestDTO);
        return new ResponseEntity<>(createdTag, HttpStatus.CREATED);
    }

    /**
     * Actualiza una etiqueta existente.
     *
     * @param id identificador de la etiqueta
     * @param tagRequestDTO datos actualizados
     * @return etiqueta actualizada con estado HTTP 200
     */
    @PutMapping("/{id}")
    public ResponseEntity<TagResponseDTO> updateTag(
            @PathVariable Long id,
            @RequestBody TagRequestDTO tagRequestDTO) {
        return ResponseEntity.ok(tagService.updateTag(id, tagRequestDTO));
    }

    /**
     * Elimina una etiqueta por su identificador.
     *
     * @param id identificador de la etiqueta
     * @return respuesta vacía con estado HTTP 204
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
        tagService.deleteTag(id);
        return ResponseEntity.noContent().build();
    }
}