package com.mantas.appointments.dto.mapper;

import com.mantas.appointments.dto.OfferedServiceDTO;
import com.mantas.appointments.entity.OfferedService;
import org.springframework.stereotype.Component;

/**
 * Mapper class for converting between OfferedServiceDTO and OfferedService entities.
 * Implements the DtoMapper interface.
 */
@Component
@Deprecated
public class OfferedServiceDtoMapper implements DtoMapper<OfferedServiceDTO, OfferedService> {

    @Override
    public OfferedServiceDTO toDto(OfferedService entity) {
        return new OfferedServiceDTO(
                entity.getName(),
                entity.getDescription(),
                entity.getPrice(),
                entity.getCategory()
        );
    }

    @Override
    public OfferedService toEntity(OfferedServiceDTO dto) {
        return new OfferedService(
                null,
                dto.name(),
                dto.description(),
                dto.price(),
                dto.category()
        );
    }
}
