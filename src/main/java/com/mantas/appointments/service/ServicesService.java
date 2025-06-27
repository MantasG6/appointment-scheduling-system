package com.mantas.appointments.service;

import com.mantas.appointments.exception.ServiceNotFoundException;
import com.mantas.appointments.repository.ServicesRepository;
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
public class ServicesService {

    private final ServicesRepository servicesRepository;

    /**
     * Fetches all services from the repository.
     *
     * @return List of all services.
     */
    public List<com.mantas.appointments.entity.Service> getAllServices() {
        return servicesRepository.findAll();
    }

    /**
     * Fetches a service by its ID.
     *
     * @param id ID of the service to fetch.
     * @return Service with the specified ID.
     */
    public com.mantas.appointments.entity.Service getServiceById(Long id) {
        return servicesRepository.findById(id)
                .orElseThrow(() -> new ServiceNotFoundException("Service not found with id: " + id));
    }

    /**
     * Creates a new service.
     *
     * @param service Service entity to create.
     * @return Created service entity.
     */
    public com.mantas.appointments.entity.Service createService(@Valid com.mantas.appointments.entity.Service service) {
        return servicesRepository.save(service);
    }

    /**
     * Updates an existing service.
     *
     * @param id             ID of the service to update.
     * @param serviceDetails New details for the service.
     * @return Updated service entity.
     */
    public com.mantas.appointments.entity.Service updateService(Long id, @Valid com.mantas.appointments.entity.Service serviceDetails) {
        com.mantas.appointments.entity.Service service = getServiceById(id);

        Optional.ofNullable(serviceDetails.getName()).ifPresent(service::setName);
        Optional.ofNullable(serviceDetails.getDescription()).ifPresent(service::setDescription);
        Optional.ofNullable(serviceDetails.getCategory()).ifPresent(service::setCategory);
        Optional.ofNullable(serviceDetails.getPrice()).ifPresent(service::setPrice);

        return servicesRepository.save(service);
    }

    /**
     * Deletes a service by its ID.
     *
     * @param id ID of the service to delete.
     */
    public void deleteService(Long id) {
        com.mantas.appointments.entity.Service service = getServiceById(id);
        servicesRepository.delete(service);
    }
}