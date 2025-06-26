package com.mantas.appointments.repository;

import com.mantas.appointments.entity.Service;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServicesRepository extends JpaRepository<Service, Long> {
}