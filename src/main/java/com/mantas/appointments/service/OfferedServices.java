package com.mantas.appointments.service;

import com.mantas.appointments.dto.OfferedServiceRequest;
import com.mantas.appointments.dto.OfferedServiceResponse;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

public interface OfferedServices {

    /**
     * Fetches all services from the repository.
     *
     * @return List of all services as DTOs.
     */
    List<OfferedServiceResponse> getAllServices();

    /**
     * Fetches a service by its ID.
     *
     * @param id ID of the service to fetch.
     * @return {@link OfferedServiceResponse} representing the service with the specified ID.
     * @throws EntityNotFoundException if no service is found with the given ID.
     */
    OfferedServiceResponse getServiceById(Long id);

    /**
     * Creates a new service.
     *
     * @param offeredServiceRequest representing the service to create.
     * @return {@link OfferedServiceResponse} representing the created service.
     */
    OfferedServiceResponse createService(OfferedServiceRequest offeredServiceRequest);

    /**
     * Updates an existing service.
     *
     * @param id                    ID of the service to update.
     * @param offeredServiceRequest containing the new details for the service.
     * @return {@link OfferedServiceResponse} representing the updated service.
     */
    OfferedServiceResponse updateService(Long id, OfferedServiceRequest offeredServiceRequest);

    /**
     * Deletes a service by its ID.
     *
     * @param id ID of the service to delete.
     * @throws EntityNotFoundException if no service is found with the given ID.
     */
    void deleteService(Long id);
}
