package com.mantas.appointments.service;

import com.mantas.appointments.exception.ServiceNotFoundException;
import com.mantas.appointments.repository.ServicesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ServicesService {

    private final ServicesRepository servicesRepository;

    public List<com.mantas.appointments.entity.Service> getAllServices() {
        return servicesRepository.findAll();
    }

    public com.mantas.appointments.entity.Service getServiceById(Long id) {
        return servicesRepository.findById(id)
                .orElseThrow(() -> new ServiceNotFoundException("Service not found with id: " + id));
    }

    public com.mantas.appointments.entity.Service createService(com.mantas.appointments.entity.Service service) {
        return servicesRepository.save(service);
    }

    public com.mantas.appointments.entity.Service updateService(Long id, com.mantas.appointments.entity.Service serviceDetails) {
        com.mantas.appointments.entity.Service service = getServiceById(id);

        Optional.ofNullable(serviceDetails.getName()).ifPresent(service::setName);
        Optional.ofNullable(serviceDetails.getDescription()).ifPresent(service::setDescription);
        Optional.ofNullable(serviceDetails.getCategory()).ifPresent(service::setCategory);
        Optional.ofNullable(serviceDetails.getPrice()).ifPresent(service::setPrice);

        return servicesRepository.save(service);
    }

    public void deleteService(Long id) {
        com.mantas.appointments.entity.Service service = getServiceById(id);
        servicesRepository.delete(service);
    }
}