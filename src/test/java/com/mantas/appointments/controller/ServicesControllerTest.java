package com.mantas.appointments.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mantas.appointments.dto.OfferedServiceDTO;
import com.mantas.appointments.entity.Category;
import com.mantas.appointments.exception.GlobalExceptionHandler;
import com.mantas.appointments.exception.ServiceNotFoundException;
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
    void givenInvalidId_WhenGetServiceById_thenReturnsNotFound() throws Exception {
        when(offeredServicesService.getServiceById(1L)).thenThrow(new ServiceNotFoundException("Service not found with id: 1"));

        mockMvc.perform(get("/api/v1/services/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Service not found with id: 1"));
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
}
