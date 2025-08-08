package com.mantas.appointments.mapper;

import com.mantas.appointments.dto.OfferedServiceDTO;
import com.mantas.appointments.entity.OfferedService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper interface for converting between OfferedServiceDTO and OfferedService.
 * Uses MapStruct for automatic mapping generation.
 */
@Mapper(componentModel = "spring")
public interface OfferedServiceMapper {

    /**
     * Converts {@link OfferedService} entity to OfferedServiceDTO.
     *
     * @param entity the {@link OfferedService} entity to convert
     * @return the converted {@link OfferedServiceDTO}
     */
    OfferedServiceDTO toDto(OfferedService entity);

    /**
     * Converts {@link OfferedServiceDTO} to {@link OfferedService} entity.
     *
     * @param dto the {@link OfferedServiceDTO} to convert
     * @return the converted {@link OfferedService} entity
     */
    @Mapping(target = "id", ignore = true)
    OfferedService toEntity(OfferedServiceDTO dto);
}
