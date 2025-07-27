package com.mantas.appointments.service;

import com.mantas.appointments.dto.OfferedServiceDTO;
import com.mantas.appointments.dto.mapper.OfferedServiceDtoMapper;
import com.mantas.appointments.entity.OfferedService;
import com.mantas.appointments.exception.ServiceNotFoundException;
import com.mantas.appointments.repository.OfferedServicesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing services.
 * Provides methods to fetch, create, update, and delete services.
 */
@Service
@RequiredArgsConstructor
public class OfferedServicesService implements IOfferedServicesService {

    private final OfferedServicesRepository servicesRepository;
    private final OfferedServiceDtoMapper mapper;

    @Override
    public List<OfferedServiceDTO> getAllServices() {
        return servicesRepository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public OfferedServiceDTO getServiceById(Long id) {
        return mapper.toDto(getServiceFromRepoById(id));
    }

    private OfferedService getServiceFromRepoById(Long id) {
        return servicesRepository.findById(id)
                .orElseThrow(() -> new ServiceNotFoundException("Service not found with id: " + id));
    }

    @Override
    public OfferedServiceDTO createService(OfferedServiceDTO offeredServiceDto) {
        OfferedService offeredService = mapper.toEntity(offeredServiceDto);

        return mapper.toDto(servicesRepository.save(offeredService));
    }

    @Override
    public OfferedServiceDTO updateService(Long id, OfferedServiceDTO serviceDetails) {
        OfferedService offeredService = getServiceFromRepoById(id);

        Optional.ofNullable(serviceDetails.name()).ifPresent(offeredService::setName);
        Optional.ofNullable(serviceDetails.description()).ifPresent(offeredService::setDescription);
        Optional.ofNullable(serviceDetails.category()).ifPresent(offeredService::setCategory);
        Optional.ofNullable(serviceDetails.price()).ifPresent(offeredService::setPrice);

        return mapper.toDto(servicesRepository.save(offeredService));
    }

    @Override
    public void deleteService(Long id) {
        OfferedService offeredService = getServiceFromRepoById(id);
        servicesRepository.delete(offeredService);
    }
}