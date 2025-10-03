package com.mantas.appointments.mapper;

/**
 * Interface for mapping between Data Transfer Objects (DTOs) and entities.
 * Provides methods to convert an entity to a DTO and vice versa.
 *
 * @param <D> the type of the Data Transfer Object
 * @param <E> the type of the entity
 */
@Deprecated
public interface DtoMapper<D, E> {

    /**
     * Converts an entity to a Data Transfer Object (DTO).
     *
     * @param entity the entity to convert
     * @return the converted DTO
     */
    D toDto(E entity);

    /**
     * Converts a Data Transfer Object (DTO) to an entity.
     *
     * @param dto the DTO to convert
     * @return the converted entity
     */
    E toEntity(D dto);
}
