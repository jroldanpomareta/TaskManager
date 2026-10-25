package com.accenture.taskmanager.mapper;

import com.accenture.taskmanager.dto.TagRequestDTO;
import com.accenture.taskmanager.dto.TagResponseDTO;
import com.accenture.taskmanager.model.Tag;

/**
 * Mapper para convertir entre objetos Tag, TagRequestDTO y TagResponseDTO.
 * Contiene métodos estáticos para realizar las conversiones necesarias.
 *
 * @author Javier Roldán
 * @version 1.0
 */
// Crear TagMapper con métodos estáticos y Javadoc para convertir de Tag a TagResponseDTO y de TagRequestDTO a Tag, copiando los campos id y name cuando corresponda. Comprobar valores null y mantener el estilo de los mappers existentes del proyecto. — generado por IA
public class TagMapper {

    /**
     * Constructor privado para evitar la instanciación de esta clase.
     */
    private TagMapper() {
    }

    /**
     * Convierte un objeto Tag en un objeto TagResponseDTO.
     *
     * @param tag entidad Tag que se va a convertir
     * @return DTO de respuesta, o null si la entidad es null
     */
    public static TagResponseDTO toTagResponseDTO(Tag tag) {
        if (tag == null) {
            return null;
        }

        TagResponseDTO tagResponseDTO = new TagResponseDTO();
        tagResponseDTO.setId(tag.getId());
        tagResponseDTO.setName(tag.getName());

        return tagResponseDTO;
    }

    /**
     * Convierte un objeto TagRequestDTO en una entidad Tag.
     *
     * @param tagRequestDTO DTO de solicitud que se va a convertir
     * @return entidad Tag, o null si el DTO es null
     */
    public static Tag toTag(TagRequestDTO tagRequestDTO) {
        if (tagRequestDTO == null) {
            return null;
        }

        Tag tag = new Tag();
        tag.setName(tagRequestDTO.getName());

        return tag;
    }
}