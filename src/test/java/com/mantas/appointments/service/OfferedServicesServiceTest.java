package com.mantas.appointments.service;

import com.mantas.appointments.dto.OfferedServiceDTO;
import com.mantas.appointments.entity.Category;
import com.mantas.appointments.entity.OfferedService;
import com.mantas.appointments.exception.ServiceNotFoundException;
import com.mantas.appointments.integration.AbstractIntegrationTest;
import com.mantas.appointments.repository.OfferedServicesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;

import static com.mantas.appointments.utils.TestUtils.serviceNotFoundMessage;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Testcontainers
public class OfferedServicesServiceTest extends AbstractIntegrationTest {

    @Autowired
    private IOfferedServicesService servicesService;

    @Autowired
    private OfferedServicesRepository servicesRepository;

    private final Long invalidId = 999L;

    @BeforeEach
    void cleanDB() {
        servicesRepository.deleteAll();
    }

    @Test
    void given3ExistingServices_whenGetAllServices_thenReturns3Services() {
        servicesRepository.save(new OfferedService(null, "test1", "test1", BigDecimal.valueOf(100), Category.DIET));
        servicesRepository.save(new OfferedService(null, "test2", "test2", BigDecimal.valueOf(10), Category.HAIRCARE));
        servicesRepository.save(new OfferedService(null, "test3", "test3", BigDecimal.valueOf(1000), Category.FITNESS));

        List<OfferedServiceDTO> result = servicesService.getAllServices();

        assertEquals(3, result.size());
    }

    @Test
    void givenValidId_whenGetServiceById_thenReturnsCorrectService() {
        OfferedService offeredService = servicesRepository.save(new OfferedService(null, "test1", "test1", BigDecimal.valueOf(100), Category.DIET));
        OfferedServiceDTO result = servicesService.getServiceById(offeredService.getId());

        assertEquals("test1", result.name());
    }

    @Test
    void givenInvalidId_whenGetServiceById_thenThrowsServiceNotFoundException() {
        Exception exception = assertThrows(ServiceNotFoundException.class, () -> servicesService.getServiceById(invalidId));

        assertEquals(serviceNotFoundMessage(invalidId), exception.getMessage());
    }

    @Test
    void givenValidCreateRequest_whenCreateService_thenSavesAndReturnsService() {
        OfferedServiceDTO offeredService = new OfferedServiceDTO("testCreate", "testCreate", BigDecimal.valueOf(1), Category.OTHER);
        OfferedServiceDTO result = servicesService.createService(offeredService);

        assertEquals("testCreate", result.name());
    }

    @Test
    void givenFullUpdate_whenUpdateService_thenUpdatesAndReturnsService() {
        OfferedService offeredService = servicesRepository.save(new OfferedService(null, "test1", "test1", BigDecimal.valueOf(100), Category.DIET));

        OfferedServiceDTO updatedService = new OfferedServiceDTO("testUpdate", "testUpdate", BigDecimal.valueOf(1), Category.OTHER);
        OfferedServiceDTO result = servicesService.updateService(offeredService.getId(), updatedService);

        assertEquals("testUpdate", result.name());
        assertEquals("testUpdate", result.description());
        assertEquals(0, result.price().compareTo(BigDecimal.valueOf(1)));
        assertEquals(Category.OTHER, result.category());
    }

    @Test
    void givenPartialUpdate_whenUpdateService_thenUpdatesAndReturnsService() {
        OfferedService offeredService = servicesRepository.save(new OfferedService(null, "test1", "test1", BigDecimal.valueOf(100), Category.DIET));

        OfferedServiceDTO updatedService = new OfferedServiceDTO("testUpdate", null, null, null);
        OfferedServiceDTO result = servicesService.updateService(offeredService.getId(), updatedService);

        assertEquals("testUpdate", result.name());
        assertEquals("test1", result.description()); // Unchanged
        assertEquals(0, result.price().compareTo(BigDecimal.valueOf(100))); // Unchanged
        assertEquals(Category.DIET, result.category()); // Unchanged
    }

    @Test
    void givenInvalidId_whenUpdateService_thenThrowsServiceNotFoundException() {
        OfferedServiceDTO updatedDetails = new OfferedServiceDTO("testUpdate", null, null, null);
        Exception exception = assertThrows(ServiceNotFoundException.class, () -> servicesService.updateService(invalidId, updatedDetails));

        assertEquals(serviceNotFoundMessage(invalidId), exception.getMessage());
    }

    @Test
    void givenValidId_whenDeleteService_thenDeletesService() {
        OfferedService offeredService = servicesRepository.save(new OfferedService(null, "testDelete", "testDelete", BigDecimal.valueOf(100), Category.OTHER));
        servicesService.deleteService(offeredService.getId());

        assertFalse(servicesRepository.findById(offeredService.getId()).isPresent());
    }

    @Test
    void givenInvalidId_whenDeleteService_thenThrowsServiceNotFoundException() {
        Exception exception = assertThrows(ServiceNotFoundException.class, () -> servicesService.deleteService(invalidId));

        assertEquals(serviceNotFoundMessage(invalidId), exception.getMessage());
    }
}