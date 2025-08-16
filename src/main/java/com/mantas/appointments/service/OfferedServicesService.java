package com.mantas.appointments.service;

import com.mantas.appointments.dto.OfferedServiceDTO;
import com.mantas.appointments.mapper.OfferedServiceMapper;
import com.mantas.appointments.entity.OfferedService;
import com.mantas.appointments.repository.OfferedServicesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.mantas.appointments.service.utils.ServiceUtils.getEntityFromRepoById;

/**
 * Service class for managing services.
 * Provides methods to fetch, create, update, and delete services.
 */
@Service
@RequiredArgsConstructor
public class OfferedServicesService implements OfferedServices {

    private final OfferedServicesRepository servicesRepository;
    private final OfferedServiceMapper mapper;

    @Override
    public List<OfferedServiceDTO> getAllServices() {
        return servicesRepository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public OfferedServiceDTO getServiceById(Long id) {
        return mapper.toDto(getEntityFromRepoById(id, servicesRepository));
    }

    @Override
    public OfferedServiceDTO createService(OfferedServiceDTO offeredServiceDto) {
        OfferedService offeredService = mapper.toEntity(offeredServiceDto);

        return mapper.toDto(servicesRepository.save(offeredService));
    }

    @Override
    public OfferedServiceDTO updateService(Long id, OfferedServiceDTO serviceDetails) {
        OfferedService offeredService = getEntityFromRepoById(id, servicesRepository);

        Optional.ofNullable(serviceDetails.name()).ifPresent(offeredService::setName);
        Optional.ofNullable(serviceDetails.description()).ifPresent(offeredService::setDescription);
        Optional.ofNullable(serviceDetails.category()).ifPresent(offeredService::setCategory);
        Optional.ofNullable(serviceDetails.price()).ifPresent(offeredService::setPrice);

        return mapper.toDto(servicesRepository.save(offeredService));
    }

    @Override
    public void deleteService(Long id) {
        servicesRepository.deleteById(id);
    }
}