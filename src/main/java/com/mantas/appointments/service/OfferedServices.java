package com.mantas.appointments.service;

import com.mantas.appointments.dto.OfferedServiceDTO;
import com.mantas.appointments.exception.EntityNotFoundException;

import java.util.List;

public interface OfferedServices {

    /**
     * Fetches all services from the repository.
     *
     * @return List of all services as DTOs.
     */
    List<OfferedServiceDTO> getAllServices();

    /**
     * Fetches a service by its ID.
     *
     * @param id ID of the service to fetch.
     * @return {@link OfferedServiceDTO} representing the service with the specified ID.
     * @throws EntityNotFoundException if no service is found with the given ID.
     */
    OfferedServiceDTO getServiceById(Long id);

    /**
     * Creates a new service.
     *
     * @param offeredServiceDto DTO representing the service to create.
     * @return {@link OfferedServiceDTO} representing the created service.
     */
    OfferedServiceDTO createService(OfferedServiceDTO offeredServiceDto);

    /**
     * Updates an existing service.
     *
     * @param id                ID of the service to update.
     * @param offeredServiceDto DTO containing the new details for the service.
     * @return {@link OfferedServiceDTO} representing the updated service.
     */
    OfferedServiceDTO updateService(Long id, OfferedServiceDTO offeredServiceDto);

    /**
     * Deletes a service by its ID.
     *
     * @param id ID of the service to delete.
     * @throws EntityNotFoundException if no service is found with the given ID.
     */
    void deleteService(Long id);
}
