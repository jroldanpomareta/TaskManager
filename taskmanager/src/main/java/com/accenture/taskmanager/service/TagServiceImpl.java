package com.accenture.taskmanager.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.accenture.taskmanager.dto.TagRequestDTO;
import com.accenture.taskmanager.dto.TagResponseDTO;
import com.accenture.taskmanager.mapper.TagMapper;
import com.accenture.taskmanager.model.Tag;
import com.accenture.taskmanager.repository.TagRepository;
import com.accenture.taskmanager.service.interfaces.TagService;

/**
 * Implementación del servicio TagService.
 * Proporciona métodos para gestionar etiquetas utilizando el repositorio y el mapper.
 *
 * @author Javier Roldán
 * @version 1.0
 */
// Implementar TagService en TagServiceImpl usando @Service e inyección por constructor de TagRepository. Implementar todos los métodos CRUD usando TagMapper y lanzar una excepción cuando no exista la etiqueta solicitada. Añadir Javadoc a la clase y a todos los métodos. — generado por IA
@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    /**
     * Construye el servicio con el repositorio de etiquetas.
     *
     * @param tagRepository repositorio de etiquetas
     */
    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    /**
     * Lista todas las etiquetas.
     *
     * @return lista de etiquetas convertidas a DTO
     */
    @Override
    public List<TagResponseDTO> getAllTags() {
        return tagRepository.findAll()
                .stream()
                .map(TagMapper::toTagResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca una etiqueta por su identificador.
     *
     * @param id identificador de la etiqueta
     * @return DTO de la etiqueta encontrada
     * @throws RuntimeException si no existe la etiqueta
     */
    @Override
    public TagResponseDTO getTagById(Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tag no encontrada con ID: " + id));

        return TagMapper.toTagResponseDTO(tag);
    }

    /**
     * Crea una nueva etiqueta.
     *
     * @param tagRequestDTO datos de la etiqueta
     * @return DTO de la etiqueta creada
     */
    @Override
    public TagResponseDTO createTag(TagRequestDTO tagRequestDTO) {
        Tag tag = TagMapper.toTag(tagRequestDTO);
        Tag savedTag = tagRepository.save(tag);

        return TagMapper.toTagResponseDTO(savedTag);
    }

    /**
     * Actualiza una etiqueta existente.
     *
     * @param id identificador de la etiqueta
     * @param tagRequestDTO datos actualizados de la etiqueta
     * @return DTO de la etiqueta actualizada
     * @throws RuntimeException si no existe la etiqueta
     */
    @Override
    public TagResponseDTO updateTag(Long id, TagRequestDTO tagRequestDTO) {
        Tag existingTag = tagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tag no encontrada con ID: " + id));

        existingTag.setName(tagRequestDTO.getName());
        Tag updatedTag = tagRepository.save(existingTag);

        return TagMapper.toTagResponseDTO(updatedTag);
    }

    /**
     * Elimina una etiqueta por su identificador.
     *
     * @param id identificador de la etiqueta
     * @throws RuntimeException si no existe la etiqueta
     */
    @Override
    public void deleteTag(Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tag no encontrada con ID: " + id));

        tagRepository.delete(tag);
    }
}