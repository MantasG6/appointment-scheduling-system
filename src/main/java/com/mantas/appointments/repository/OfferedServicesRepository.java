package com.mantas.appointments.repository;

import com.mantas.appointments.entity.OfferedService;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing Service entities.
 * Provides methods to perform CRUD operations on Service entities.
 */
public interface OfferedServicesRepository extends JpaRepository<OfferedService, Long> {
}