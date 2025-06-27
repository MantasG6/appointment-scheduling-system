package com.mantas.appointments.controller;

import com.mantas.appointments.entity.Service;
import com.mantas.appointments.service.ServicesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

    private final ServicesService servicesService;

    /**
     * Retrieves all services.
     *
     * @return a list of all services
     */
    @GetMapping
    public ResponseEntity<List<Service>> getAllServices() {
        return ResponseEntity.ok(servicesService.getAllServices());
    }
}
