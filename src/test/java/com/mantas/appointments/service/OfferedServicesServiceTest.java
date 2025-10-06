package com.mantas.appointments.service;

import com.mantas.appointments.dto.OfferedServiceRequest;
import com.mantas.appointments.dto.OfferedServiceResponse;
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

import static com.mantas.appointments.utils.TestSecurityUtils.initializeDefaultTestUserAuthentication;
import static com.mantas.appointments.utils.TestUtils.entityNotFoundMessage;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
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
        OfferedService created = servicesRepository.save(OfferedServiceTestFactory.buildDefaultOfferedService());
        defaultService = servicesRepository.findById(created.getId()).orElse(null);
        initializeDefaultTestUserAuthentication();
    }

    @Test
    void givenMultipleServices_whenGetAllServices_thenReturnsAllServices() {
        // Create another service to ensure multiple entries
        servicesRepository.save(OfferedServiceTestFactory.buildDefaultOfferedService());

        List<OfferedServiceResponse> result = servicesService.getAllServices();

        assertEquals(2, result.size());
    }

    @Test
    void givenValidId_whenGetServiceById_thenReturnsCorrectService() {
        OfferedServiceResponse result = servicesService.getServiceById(defaultService.getId());

        assertThat(result)
                .isNotNull()
                .extracting(
                        OfferedServiceResponse::name,
                        OfferedServiceResponse::description,
                        OfferedServiceResponse::price,
                        OfferedServiceResponse::category,
                        OfferedServiceResponse::created,
                        OfferedServiceResponse::updated
                )
                .satisfies(tuple -> {
                    assertThat(tuple.get(0)).isEqualTo(defaultService.getName());
                    assertThat(tuple.get(1)).isEqualTo(defaultService.getDescription());
                    assertThat((BigDecimal) tuple.get(2)).isEqualByComparingTo(defaultService.getPrice());
                    assertThat(tuple.get(3)).isEqualTo(defaultService.getCategory());
                    assertThat(tuple.get(4)).isEqualTo(defaultService.getCreated());
                    assertThat(tuple.get(5)).isEqualTo(defaultService.getUpdated());
                });
    }

    @Test
    void givenInvalidId_whenGetServiceById_thenThrowsServiceNotFoundException() {
        Exception exception = assertThrows(EntityNotFoundException.class, () -> servicesService.getServiceById(INVALID_ID));

        assertEquals(entityNotFoundMessage(INVALID_ID), exception.getMessage());
    }

    @Test
    void givenValidCreateRequest_whenCreateService_thenSavesAndReturnsService() {
        OfferedServiceRequest offeredServiceRequest = OfferedServiceTestFactory.buildDefaultOfferedServiceRequest();
        OfferedServiceResponse result = servicesService.createService(offeredServiceRequest);

        assertThat(result)
                .isNotNull()
                .extracting(
                        OfferedServiceResponse::name,
                        OfferedServiceResponse::description,
                        OfferedServiceResponse::price,
                        OfferedServiceResponse::category
                )
                .containsExactly(
                        offeredServiceRequest.name(),
                        offeredServiceRequest.description(),
                        offeredServiceRequest.price(),
                        offeredServiceRequest.category()
                );

        // Validate created and updated fields
        assertThat(result.created()).isNotNull();
        assertThat(result.updated()).isNotNull();
        assertEquals(result.created(), result.updated()); // For a new entity, created and updated should be the same
    }

    @Test
    void givenFullUpdate_whenUpdateService_thenUpdatesAndReturnsService() {
        OfferedServiceRequest updateServiceRequest = OfferedServiceTestFactory.buildFullUpdateOfferedServiceRequest();
        OfferedServiceResponse result = servicesService.updateService(defaultService.getId(), updateServiceRequest);

        assertEquals(updateServiceRequest.name(), result.name());
        assertEquals(updateServiceRequest.description(), result.description());
        assertThat(updateServiceRequest.price()).isEqualByComparingTo(result.price());
        assertEquals(updateServiceRequest.category(), result.category());
        assertEquals(defaultService.getCreated(), result.created()); // Unchanged
        assertNotEquals(defaultService.getUpdated(), result.updated()); // Updated
    }

    @Test
    void givenPartialUpdate_whenUpdateService_thenUpdatesAndReturnsService() {
        OfferedServiceRequest updateServiceRequest = OfferedServiceTestFactory.buildPartialUpdateOfferedServiceRequest();
        OfferedServiceResponse result = servicesService.updateService(defaultService.getId(), updateServiceRequest);

        assertEquals(OfferedServiceTestFactory.UPDATED_NAME, result.name());
        assertEquals(OfferedServiceTestFactory.DEFAULT_DESCRIPTION, result.description()); // Unchanged
        assertThat(OfferedServiceTestFactory.DEFAULT_PRICE).isEqualByComparingTo(result.price()); // Unchanged
        assertEquals(OfferedServiceTestFactory.DEFAULT_CATEGORY, result.category()); // Unchanged
    }

    @Test
    void givenInvalidId_whenUpdateService_thenThrowsServiceNotFoundException() {
        OfferedServiceRequest request = OfferedServiceTestFactory.buildFullUpdateOfferedServiceRequest();
        Exception exception = assertThrows(EntityNotFoundException.class, () -> servicesService.updateService(INVALID_ID, request));

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