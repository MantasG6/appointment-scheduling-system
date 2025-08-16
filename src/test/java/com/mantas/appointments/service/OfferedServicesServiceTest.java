package com.mantas.appointments.service;

import com.mantas.appointments.dto.OfferedServiceDTO;
import com.mantas.appointments.entity.OfferedService;
import com.mantas.appointments.integration.AbstractIntegrationTest;
import com.mantas.appointments.repository.OfferedServicesRepository;
import com.mantas.appointments.utils.OfferedServiceTestFactory;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;

import static com.mantas.appointments.utils.TestUtils.entityNotFoundMessage;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Testcontainers
public class OfferedServicesServiceTest extends AbstractIntegrationTest {

    private static final Long INVALID_ID = 999L;

    @Autowired
    private OfferedServices servicesService;

    @Autowired
    private OfferedServicesRepository servicesRepository;

    private OfferedService defaultService;

    @BeforeEach
    void setUp() {
        servicesRepository.deleteAll();
        defaultService = servicesRepository.save(OfferedServiceTestFactory.buildDefaultOfferedService());
    }

    @Test
    void givenMultipleServices_whenGetAllServices_thenReturnsAllServices() {
        // Create another service to ensure multiple entries
        servicesRepository.save(OfferedServiceTestFactory.buildDefaultOfferedService());

        List<OfferedServiceDTO> result = servicesService.getAllServices();

        assertEquals(2, result.size());
    }

    @Test
    void givenValidId_whenGetServiceById_thenReturnsCorrectService() {
        OfferedServiceDTO result = servicesService.getServiceById(defaultService.getId());

        assertThat(result)
                .isNotNull()
                .extracting(OfferedServiceDTO::name, OfferedServiceDTO::description, OfferedServiceDTO::price, OfferedServiceDTO::category)
                .satisfies(tuple -> {
                    assertThat(tuple.get(0)).isEqualTo(defaultService.getName());
                    assertThat(tuple.get(1)).isEqualTo(defaultService.getDescription());
                    assertThat((BigDecimal) tuple.get(2)).isEqualByComparingTo(defaultService.getPrice());
                    assertThat(tuple.get(3)).isEqualTo(defaultService.getCategory());
                });
    }

    @Test
    void givenInvalidId_whenGetServiceById_thenThrowsServiceNotFoundException() {
        Exception exception = assertThrows(EntityNotFoundException.class, () -> servicesService.getServiceById(INVALID_ID));

        assertEquals(entityNotFoundMessage(INVALID_ID), exception.getMessage());
    }

    @Test
    void givenValidCreateRequest_whenCreateService_thenSavesAndReturnsService() {
        OfferedServiceDTO offeredService = OfferedServiceTestFactory.buildDefaultOfferedServiceDTO();
        OfferedServiceDTO result = servicesService.createService(offeredService);

        assertThat(result)
                .isNotNull()
                .extracting(OfferedServiceDTO::name, OfferedServiceDTO::description, OfferedServiceDTO::price, OfferedServiceDTO::category)
                .containsExactly(offeredService.name(), offeredService.description(), offeredService.price(), offeredService.category());
    }

    @Test
    void givenFullUpdate_whenUpdateService_thenUpdatesAndReturnsService() {
        OfferedServiceDTO updatedService = OfferedServiceTestFactory.buildFullUpdateOfferedServiceDTO();
        OfferedServiceDTO result = servicesService.updateService(defaultService.getId(), updatedService);

        assertEquals(OfferedServiceTestFactory.UPDATED_NAME, result.name());
        assertEquals(OfferedServiceTestFactory.UPDATED_DESCRIPTION, result.description());
        assertThat(OfferedServiceTestFactory.UPDATED_PRICE).isEqualByComparingTo(result.price());
        assertEquals(OfferedServiceTestFactory.UPDATED_CATEGORY, result.category());
    }

    @Test
    void givenPartialUpdate_whenUpdateService_thenUpdatesAndReturnsService() {
        OfferedServiceDTO updatedService = OfferedServiceTestFactory.buildPartialUpdateOfferedServiceDTO();
        OfferedServiceDTO result = servicesService.updateService(defaultService.getId(), updatedService);

        assertEquals(OfferedServiceTestFactory.UPDATED_NAME, result.name());
        assertEquals(OfferedServiceTestFactory.DEFAULT_DESCRIPTION, result.description()); // Unchanged
        assertThat(OfferedServiceTestFactory.DEFAULT_PRICE).isEqualByComparingTo(result.price()); // Unchanged
        assertEquals(OfferedServiceTestFactory.DEFAULT_CATEGORY, result.category()); // Unchanged
    }

    @Test
    void givenInvalidId_whenUpdateService_thenThrowsServiceNotFoundException() {
        OfferedServiceDTO updatedDetails = OfferedServiceTestFactory.buildFullUpdateOfferedServiceDTO();
        Exception exception = assertThrows(EntityNotFoundException.class, () -> servicesService.updateService(INVALID_ID, updatedDetails));

        assertEquals(entityNotFoundMessage(INVALID_ID), exception.getMessage());
    }

    @Test
    void givenValidId_whenDeleteService_thenDeletesService() {
        servicesService.deleteService(defaultService.getId());

        assertFalse(servicesRepository.findById(defaultService.getId()).isPresent());
    }

    @Test
    void givenInvalidId_whenDeleteService_thenDoNothing() {
        servicesService.deleteService(INVALID_ID);

        assertFalse(servicesRepository.findById(INVALID_ID).isPresent());
    }
}