package com.mantas.appointments.mapper;

import com.mantas.appointments.dto.OfferedServiceRequest;
import com.mantas.appointments.dto.OfferedServiceResponse;
import com.mantas.appointments.entity.OfferedService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper interface for converting between OfferedServiceResponse DTO and OfferedService entity classes.
 * Uses MapStruct for automatic mapping generation.
 */
@Mapper(componentModel = "spring")
public interface OfferedServiceMapper {

    /**
     * Converts {@link OfferedService} entity to {@link OfferedServiceResponse}.
     *
     * @param entity the {@link OfferedService} entity to convert
     * @return the converted {@link OfferedServiceResponse}
     */
    OfferedServiceResponse toDto(OfferedService entity);

    /**
     * Converts {@link OfferedServiceResponse} to {@link OfferedService} entity.
     *
     * @param dto the {@link OfferedServiceResponse} to convert
     * @return the converted {@link OfferedService} entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "updated", ignore = true)
    OfferedService toEntity(OfferedServiceRequest dto);
}
