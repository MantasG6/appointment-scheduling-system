package com.mantas.appointments.repository;

import com.mantas.appointments.entity.Service;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing Service entities.
 * Provides methods to perform CRUD operations on Service entities.
 */
public interface ServicesRepository extends JpaRepository<Service, Long> {
}