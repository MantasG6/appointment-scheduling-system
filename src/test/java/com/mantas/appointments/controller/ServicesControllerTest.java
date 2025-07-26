package com.mantas.appointments.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mantas.appointments.dto.OfferedServiceDTO;
import com.mantas.appointments.entity.Category;
import com.mantas.appointments.exception.GlobalExceptionHandler;
import com.mantas.appointments.security.TestSecurityConfig;
import com.mantas.appointments.service.OfferedServicesService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ServicesController.class)
@Import({GlobalExceptionHandler.class, TestSecurityConfig.class})
public class ServicesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OfferedServicesService offeredServicesService;

    @Test
    void givenServicesEndpoint_whenGetAllServices_thenReturnsOk() throws Exception {
        List<OfferedServiceDTO> services = List.of(
                new OfferedServiceDTO("testService1", "testService2", BigDecimal.valueOf(100), Category.DIET),
                new OfferedServiceDTO("testService2", "testService2", BigDecimal.valueOf(200), Category.HAIRCARE)
        );
        when(offeredServicesService.getAllServices()).thenReturn(services);

        mockMvc.perform(get("/api/v1/services"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void givenValidId_whenGetServiceById_thenReturnsOk() throws Exception {
        OfferedServiceDTO service = new OfferedServiceDTO("testService1", "testService1", BigDecimal.valueOf(100), Category.DIET);
        when(offeredServicesService.getServiceById(1L)).thenReturn(service);

        mockMvc.perform(get("/api/v1/services/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("testService1"))
                .andExpect(jsonPath("$.description").value("testService1"))
                .andExpect(jsonPath("$.price").value(100))
                .andExpect(jsonPath("$.category").value("DIET"));
    }

    @Test
    void givenValidRequest_whenCreateService_thenReturnsCreated() throws Exception {
        OfferedServiceDTO service = new OfferedServiceDTO("testService1", "testService1", BigDecimal.valueOf(100), Category.DIET);
        when(offeredServicesService.createService(service)).thenReturn(service);

        String jsonContent = new ObjectMapper().writeValueAsString(service);
        mockMvc.perform(post("/api/v1/services")
                        .contentType("application/json")
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("testService1"))
                .andExpect(jsonPath("$.description").value("testService1"))
                .andExpect(jsonPath("$.price").value(100))
                .andExpect(jsonPath("$.category").value("DIET"));
    }

    @Test
    void givenNoName_whenCreateService_thenReturnsBadRequest() throws Exception {
        OfferedServiceDTO service = new OfferedServiceDTO("", "testService", BigDecimal.valueOf(100), Category.OTHER);
        String jsonContent = new ObjectMapper().writeValueAsString(service);

        mockMvc.perform(post("/api/v1/services")
                        .contentType("application/json")
                        .content(jsonContent))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").value("Service name cannot be blank"));
    }

    @Test
    void givenNoDescription_whenCreateService_thenReturnsOk() throws Exception {
        OfferedServiceDTO service = new OfferedServiceDTO("testService", "", BigDecimal.valueOf(100), Category.OTHER);
        String jsonContent = new ObjectMapper().writeValueAsString(service);
        when(offeredServicesService.createService(service)).thenReturn(service);

        mockMvc.perform(post("/api/v1/services")
                        .contentType("application/json")
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("testService"))
                .andExpect(jsonPath("$.description").value(""))
                .andExpect(jsonPath("$.price").value(100))
                .andExpect(jsonPath("$.category").value("OTHER"));
    }

    @Test
    void givenPriceNull_whenCreateService_thenReturnsBadRequest() throws Exception {
        OfferedServiceDTO service = new OfferedServiceDTO("testService", "testService", null, Category.OTHER);
        String jsonContent = new ObjectMapper().writeValueAsString(service);

        mockMvc.perform(post("/api/v1/services")
                        .contentType("application/json")
                        .content(jsonContent))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.price").value("Service price cannot be null"));
    }

    @Test
    void givenPriceNegative_whenCreateService_thenReturnsBadRequest() throws Exception {
        OfferedServiceDTO service = new OfferedServiceDTO("testService", "testService", BigDecimal.valueOf(-1), Category.OTHER);
        String jsonContent = new ObjectMapper().writeValueAsString(service);

        mockMvc.perform(post("/api/v1/services")
                        .contentType("application/json")
                        .content(jsonContent))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.price").value("Service price must be greater than zero"));
    }

    @Test
    void givenNoCategory_whenCreateService_thenReturnsBadRequest() throws Exception {
        OfferedServiceDTO service = new OfferedServiceDTO("testService", "testService", BigDecimal.valueOf(100), null);
        String jsonContent = new ObjectMapper().writeValueAsString(service);

        mockMvc.perform(post("/api/v1/services")
                        .contentType("application/json")
                        .content(jsonContent))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.category").value("Service category cannot be null"));
    }
}
