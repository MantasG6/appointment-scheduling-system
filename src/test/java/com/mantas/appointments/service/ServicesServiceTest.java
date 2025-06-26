package com.mantas.appointments.service;

import com.mantas.appointments.entity.Category;
import com.mantas.appointments.entity.Service;
import com.mantas.appointments.exception.ServiceNotFoundException;
import com.mantas.appointments.integration.AbstractIntegrationTest;
import com.mantas.appointments.repository.ServicesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static com.mantas.appointments.utils.TestUtils.serviceNotFoundMessage;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ServicesServiceTest extends AbstractIntegrationTest {

    @Autowired
    private ServicesService servicesService;

    @Autowired
    private ServicesRepository servicesRepository;

    private final Long invalidId = 999L;

    @BeforeEach
    void cleanDB() {
        servicesRepository.deleteAll();
    }

    @Test
    void given3ExistingServices_whenGetAllServices_thenReturns3Services() {
        servicesRepository.save(new Service(null, "test1", "test1", 100.0, Category.DIET));
        servicesRepository.save(new Service(null, "test2", "test2", 10.0, Category.HAIRCARE));
        servicesRepository.save(new Service(null, "test3", "test3", 1000.0, Category.FITNESS));

        List<Service> result = servicesService.getAllServices();

        assertEquals(3, result.size());
    }

    @Test
    void givenValidId_whenGetServiceById_thenReturnsCorrectService() {
        Service service = servicesRepository.save(new Service(null, "test1", "test1", 100.0, Category.DIET));
        Service result = servicesService.getServiceById(service.getId());

        assertEquals("test1", result.getName());
    }

    @Test
    void givenInvalidId_whenGetServiceById_thenThrowsServiceNotFoundException() {
        Exception exception = assertThrows(ServiceNotFoundException.class, () -> servicesService.getServiceById(invalidId));

        assertEquals(serviceNotFoundMessage(invalidId), exception.getMessage());
    }

    @Test
    void givenValidCreateRequest_whenCreateService_thenSavesAndReturnsService() {
        Service service = new Service(null, "testCreate", "testCreate", 1.0, Category.OTHER);
        Service result = servicesService.createService(service);

        assertEquals("testCreate", result.getName());
    }

    @Test
    void givenFullUpdate_whenUpdateService_thenUpdatesAndReturnsService() {
        Service service = servicesRepository.save(new Service(null, "test1", "test1", 100.0, Category.DIET));

        Service updatedService = new Service(null, "testUpdate", "testUpdate", 1.0, Category.OTHER);
        Service result = servicesService.updateService(service.getId(), updatedService);

        assertEquals("testUpdate", result.getName());
        assertEquals("testUpdate", result.getDescription());
        assertEquals(1.0, result.getPrice());
        assertEquals(Category.OTHER, result.getCategory());
    }

    @Test
    void givenPartialUpdate_whenUpdateService_thenUpdatesAndReturnsService() {
        Service service = servicesRepository.save(new Service(null, "test1", "test1", 100.0, Category.DIET));

        Service updatedService = new Service(null, "testUpdate", null, null, null);
        Service result = servicesService.updateService(service.getId(), updatedService);

        assertEquals("testUpdate", result.getName());
        assertEquals("test1", result.getDescription()); // Unchanged
        assertEquals(100.0, result.getPrice()); // Unchanged
        assertEquals(Category.DIET, result.getCategory()); // Unchanged
    }

    @Test
    void givenInvalidId_whenUpdateService_thenThrowsServiceNotFoundException() {
        Service updatedDetails = new Service(null, "testUpdate", null, null, null);
        Exception exception = assertThrows(ServiceNotFoundException.class, () -> servicesService.updateService(invalidId, updatedDetails));

        assertEquals(serviceNotFoundMessage(invalidId), exception.getMessage());
    }

    @Test
    void givenValidId_whenDeleteService_thenDeletesService() {
        Service service = servicesRepository.save(new Service(null, "testDelete", "testDelete", 100.0, Category.OTHER));
        servicesService.deleteService(service.getId());

        assertFalse(servicesRepository.findById(service.getId()).isPresent());
    }

    @Test
    void givenInvalidId_whenDeleteService_thenThrowsServiceNotFoundException() {
        Exception exception = assertThrows(ServiceNotFoundException.class, () -> servicesService.deleteService(invalidId));

        assertEquals(serviceNotFoundMessage(invalidId), exception.getMessage());
    }
}