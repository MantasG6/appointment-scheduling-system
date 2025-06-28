package com.mantas.appointments.service;

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

    /**
     * Fetches all services from the repository.
     *
     * @return List of all services.
     */
    public List<OfferedService> getAllServices() {
        return servicesRepository.findAll();
    }

    /**
     * Fetches a service by its ID.
     *
     * @param id ID of the service to fetch.
     * @return Service with the specified ID.
     */
    public OfferedService getServiceById(Long id) {
        return servicesRepository.findById(id)
                .orElseThrow(() -> new ServiceNotFoundException("Service not found with id: " + id));
    }

    /**
     * Creates a new service.
     *
     * @param offeredService Service entity to create.
     * @return Created service entity.
     */
    public OfferedService createService(@Valid OfferedService offeredService) {
        return servicesRepository.save(offeredService);
    }

    /**
     * Updates an existing service.
     *
     * @param id             ID of the service to update.
     * @param serviceDetails New details for the service.
     * @return Updated service entity.
     */
    public OfferedService updateService(Long id, @Valid OfferedService serviceDetails) {
        OfferedService offeredService = getServiceById(id);

        Optional.ofNullable(serviceDetails.getName()).ifPresent(offeredService::setName);
        Optional.ofNullable(serviceDetails.getDescription()).ifPresent(offeredService::setDescription);
        Optional.ofNullable(serviceDetails.getCategory()).ifPresent(offeredService::setCategory);
        Optional.ofNullable(serviceDetails.getPrice()).ifPresent(offeredService::setPrice);

        return servicesRepository.save(offeredService);
    }

    /**
     * Deletes a service by its ID.
     *
     * @param id ID of the service to delete.
     */
    public void deleteService(Long id) {
        OfferedService offeredService = getServiceById(id);
        servicesRepository.delete(offeredService);
    }
}