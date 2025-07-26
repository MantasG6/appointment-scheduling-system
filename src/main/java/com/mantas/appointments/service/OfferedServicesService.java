package com.mantas.appointments.service;

import com.mantas.appointments.dto.OfferedServiceDTO;
import com.mantas.appointments.dto.mapper.OfferedServiceDtoMapper;
import com.mantas.appointments.entity.OfferedService;
import com.mantas.appointments.exception.ServiceNotFoundException;
import com.mantas.appointments.repository.OfferedServicesRepository;
import jakarta.validation.Valid;
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
public class OfferedServicesService {

    private final OfferedServicesRepository servicesRepository;
    private final OfferedServiceDtoMapper mapper;

    /**
     * Fetches all services from the repository.
     *
     * @return List of all services as DTOs.
     */
    public List<OfferedServiceDTO> getAllServices() {
        return servicesRepository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    /**
     * Fetches a service by its ID.
     *
     * @param id ID of the service to fetch.
     * @return OfferedServiceDTO representing the service with the specified ID.
     * @throws ServiceNotFoundException if no service is found with the given ID.
     */
    public OfferedServiceDTO getServiceById(Long id) {
        return mapper.toDto(getServiceFromRepoById(id));
    }

    private OfferedService getServiceFromRepoById(Long id) {
        return servicesRepository.findById(id)
                .orElseThrow(() -> new ServiceNotFoundException("Service not found with id: " + id));
    }

    /**
     * Creates a new service.
     *
     * @param offeredServiceDto DTO representing the service to create.
     * @return OfferedServiceDTO representing the created service.
     */
    public OfferedServiceDTO createService(OfferedServiceDTO offeredServiceDto) {
        OfferedService offeredService = mapper.toEntity(offeredServiceDto);

        return mapper.toDto(servicesRepository.save(offeredService));
    }

    /**
     * Updates an existing service.
     *
     * @param id             ID of the service to update.
     * @param serviceDetails DTO containing the new details for the service.
     * @return OfferedServiceDTO representing the updated service.
     */
    public OfferedServiceDTO updateService(Long id, OfferedServiceDTO serviceDetails) {
        OfferedService offeredService = getServiceFromRepoById(id);

        Optional.ofNullable(serviceDetails.name()).ifPresent(offeredService::setName);
        Optional.ofNullable(serviceDetails.description()).ifPresent(offeredService::setDescription);
        Optional.ofNullable(serviceDetails.category()).ifPresent(offeredService::setCategory);
        Optional.ofNullable(serviceDetails.price()).ifPresent(offeredService::setPrice);

        return mapper.toDto(servicesRepository.save(offeredService));
    }

    /**
     * Deletes a service by its ID.
     *
     * @param id ID of the service to delete.
     */
    public void deleteService(Long id) {
        OfferedService offeredService = getServiceFromRepoById(id);
        servicesRepository.delete(offeredService);
    }
}