package com.mantas.appointments.controller;

import com.mantas.appointments.dto.OfferedServiceDTO;
import com.mantas.appointments.service.OfferedServicesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller for managing services.
 * Handles requests related to services.
 */
@RestController
@RequestMapping("/api/v1/services")
@RequiredArgsConstructor
public class ServicesController {

    private final OfferedServicesService servicesService;

    /**
     * Retrieves all services.
     *
     * @return a list of all services
     */
    @GetMapping
    public ResponseEntity<List<OfferedServiceDTO>> getAllServices() {
        return ResponseEntity.ok(servicesService.getAllServices());
    }

    /**
     * Retrieves a service by its ID.
     *
     * @param id the ID of the service to retrieve
     * @return the service with the specified ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<OfferedServiceDTO> getServiceById(@PathVariable Long id) {
        return ResponseEntity.ok(servicesService.getServiceById(id));
    }

    /**
     * Creates a new service.
     *
     * @param offeredServiceDto the DTO representing the service to create
     * @return the created service
     */
    @PostMapping
    public ResponseEntity<OfferedServiceDTO> createService(@RequestBody @Valid OfferedServiceDTO offeredServiceDto) {
        return ResponseEntity.ok(servicesService.createService(offeredServiceDto));
    }

    /**
     * Updates an existing service.
     *
     * @param id the ID of the service to update
     * @param updatedService the DTO representing the updated service
     * @return the updated service
     */
    @PutMapping("/{id}")
    public ResponseEntity<OfferedServiceDTO> updateService(@PathVariable Long id,
                                                           @RequestBody @Valid OfferedServiceDTO updatedService) {
        return ResponseEntity.ok(servicesService.updateService(id, updatedService));
    }

    /**
     * Deletes a service by its ID.
     *
     * @param id the ID of the service to delete
     * @return a response indicating the deletion was successful
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable Long id) {
        servicesService.deleteService(id);
        return ResponseEntity.noContent().build();
    }
}
