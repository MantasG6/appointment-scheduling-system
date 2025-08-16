package com.mantas.appointments.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mantas.appointments.dto.OfferedServiceDTO;
import com.mantas.appointments.exception.ErrorMessage;
import com.mantas.appointments.exception.GlobalExceptionHandler;
import com.mantas.appointments.security.TestSecurityConfig;
import com.mantas.appointments.service.OfferedServices;
import com.mantas.appointments.utils.OfferedServiceTestFactory;
import jakarta.persistence.EntityNotFoundException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ServicesController.class)
@Import({GlobalExceptionHandler.class, TestSecurityConfig.class})
public class ServicesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OfferedServices offeredServicesService;

    private final static Long VALID_ID = 1L;
    private final static String BASE_ENDPOINT = ServicesController.SERVICES_API;
    private final static String ENDPOINT_WITH_ID = BASE_ENDPOINT + "/" + VALID_ID;
    private final static String CONTENT_TYPE = "application/json";
    private final static String INVALID_CATEGORY_REQUEST = """
            {
                "name": "testService",
                "description": "testService",
                "price": 100,
                "category": "INVALID_CATEGORY"
            }
            """;

    @Test
    void givenServicesEndpoint_whenGetAllServices_thenReturnsOk() throws Exception {
        List<OfferedServiceDTO> services = List.of(
                OfferedServiceTestFactory.buildDefaultOfferedServiceDTO(),
                OfferedServiceTestFactory.buildFullUpdateOfferedServiceDTO()
        );
        when(offeredServicesService.getAllServices()).thenReturn(services);

        mockMvc.perform(get(BASE_ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void givenValidId_whenGetServiceById_thenReturnsOk() throws Exception {
        OfferedServiceDTO service = OfferedServiceTestFactory.buildDefaultOfferedServiceDTO();
        when(offeredServicesService.getServiceById(VALID_ID)).thenReturn(service);

        mockMvc.perform(get(ENDPOINT_WITH_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(OfferedServiceTestFactory.DEFAULT_NAME))
                .andExpect(jsonPath("$.description").value(OfferedServiceTestFactory.DEFAULT_DESCRIPTION))
                .andExpect(jsonPath("$.price").value(OfferedServiceTestFactory.DEFAULT_PRICE))
                .andExpect(jsonPath("$.category").value(OfferedServiceTestFactory.DEFAULT_CATEGORY.toString()));
    }

    @Test
    void givenInvalidId_WhenGetServiceById_thenReturnsNotFound() throws Exception {
        when(offeredServicesService.getServiceById(VALID_ID)).thenThrow(new EntityNotFoundException(ErrorMessage.ENTITY_NOT_FOUND + VALID_ID));

        mockMvc.perform(get(ENDPOINT_WITH_ID))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(ErrorMessage.ENTITY_NOT_FOUND + VALID_ID));
    }

    @Test
    void givenValidRequest_whenCreateService_thenReturnsCreated() throws Exception {
        OfferedServiceDTO service = OfferedServiceTestFactory.buildDefaultOfferedServiceDTO();
        when(offeredServicesService.createService(service)).thenReturn(service);

        String jsonContent = new ObjectMapper().writeValueAsString(service);
        mockMvc.perform(post(BASE_ENDPOINT)
                        .contentType(CONTENT_TYPE)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(OfferedServiceTestFactory.DEFAULT_NAME))
                .andExpect(jsonPath("$.description").value(OfferedServiceTestFactory.DEFAULT_DESCRIPTION))
                .andExpect(jsonPath("$.price").value(OfferedServiceTestFactory.DEFAULT_PRICE))
                .andExpect(jsonPath("$.category").value(OfferedServiceTestFactory.DEFAULT_CATEGORY.toString()));
    }

    @Test
    void givenNoName_whenCreateService_thenReturnsBadRequest() throws Exception {
        OfferedServiceDTO service = OfferedServiceTestFactory.buildNoNameOfferedServiceDTO();
        String jsonContent = new ObjectMapper().writeValueAsString(service);

        mockMvc.perform(post(BASE_ENDPOINT)
                        .contentType(CONTENT_TYPE)
                        .content(jsonContent))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.name").value(ErrorMessage.NAME_BLANK));
    }

    @Test
    void givenNoDescription_whenCreateService_thenReturnsOk() throws Exception {
        OfferedServiceDTO service = OfferedServiceTestFactory.buildNoDescOfferedServiceDTO();
        String jsonContent = new ObjectMapper().writeValueAsString(service);
        when(offeredServicesService.createService(service)).thenReturn(service);

        mockMvc.perform(post(BASE_ENDPOINT)
                        .contentType(CONTENT_TYPE)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(OfferedServiceTestFactory.DEFAULT_NAME))
                .andExpect(jsonPath("$.description").value(""))
                .andExpect(jsonPath("$.price").value(OfferedServiceTestFactory.DEFAULT_PRICE))
                .andExpect(jsonPath("$.category").value(OfferedServiceTestFactory.DEFAULT_CATEGORY.toString()));
    }

    @Test
    void givenPriceNull_whenCreateService_thenReturnsBadRequest() throws Exception {
        OfferedServiceDTO service = OfferedServiceTestFactory.buildPriceNullOfferedServiceDTO();
        String jsonContent = new ObjectMapper().writeValueAsString(service);

        mockMvc.perform(post(BASE_ENDPOINT)
                        .contentType(CONTENT_TYPE)
                        .content(jsonContent))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.price").value(ErrorMessage.PRICE_NULL));
    }

    @Test
    void givenPriceNegative_whenCreateService_thenReturnsBadRequest() throws Exception {
        OfferedServiceDTO service = OfferedServiceTestFactory.buildNegativePriceOfferedServiceDTO();
        String jsonContent = new ObjectMapper().writeValueAsString(service);

        mockMvc.perform(post(BASE_ENDPOINT)
                        .contentType(CONTENT_TYPE)
                        .content(jsonContent))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.price").value(ErrorMessage.PRICE_NEGATIVE));
    }

    @Test
    void givenNoCategory_whenCreateService_thenReturnsBadRequest() throws Exception {
        OfferedServiceDTO service = OfferedServiceTestFactory.buildCategoryNullOfferedServiceDTO();
        String jsonContent = new ObjectMapper().writeValueAsString(service);

        mockMvc.perform(post(BASE_ENDPOINT)
                        .contentType(CONTENT_TYPE)
                        .content(jsonContent))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.category").value(ErrorMessage.CATEGORY_NULL));
    }

    @Test
    void givenWrongCategory_whenCreateService_thenReturnsBadRequest() throws Exception {
        mockMvc.perform(post(BASE_ENDPOINT)
                        .contentType(CONTENT_TYPE)
                        .content(INVALID_CATEGORY_REQUEST))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(Matchers.containsString(ErrorMessage.INVALID_CATEGORY)));
    }

    @Test
    void givenValidRequest_whenUpdateService_thenReturnsOk() throws Exception {
        OfferedServiceDTO service = OfferedServiceTestFactory.buildFullUpdateOfferedServiceDTO();
        when(offeredServicesService.updateService(VALID_ID, service)).thenReturn(service);

        String jsonContent = new ObjectMapper().writeValueAsString(service);
        mockMvc.perform(put(BASE_ENDPOINT + "/" + VALID_ID)
                        .contentType(CONTENT_TYPE)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(OfferedServiceTestFactory.UPDATED_NAME))
                .andExpect(jsonPath("$.description").value(OfferedServiceTestFactory.UPDATED_DESCRIPTION))
                .andExpect(jsonPath("$.price").value(OfferedServiceTestFactory.UPDATED_PRICE))
                .andExpect(jsonPath("$.category").value(OfferedServiceTestFactory.UPDATED_CATEGORY.toString()));
    }

    @Test
    void givenInvalidId_whenUpdateService_thenReturnsNotFound() throws Exception {
        OfferedServiceDTO service = OfferedServiceTestFactory.buildFullUpdateOfferedServiceDTO();
        when(offeredServicesService.updateService(VALID_ID, service)).thenThrow(new EntityNotFoundException(ErrorMessage.ENTITY_NOT_FOUND + VALID_ID));

        String jsonContent = new ObjectMapper().writeValueAsString(service);
        mockMvc.perform(put(BASE_ENDPOINT + "/" + VALID_ID)
                        .contentType(CONTENT_TYPE)
                        .content(jsonContent))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(ErrorMessage.ENTITY_NOT_FOUND + VALID_ID));
    }

    @Test
    void givenNoName_whenUpdateService_thenReturnsBadRequest() throws Exception {
        OfferedServiceDTO service = OfferedServiceTestFactory.buildNoNameOfferedServiceDTO();
        String jsonContent = new ObjectMapper().writeValueAsString(service);

        mockMvc.perform(put(ENDPOINT_WITH_ID)
                        .contentType(CONTENT_TYPE)
                        .content(jsonContent))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.name").value(ErrorMessage.NAME_BLANK));
    }

    @Test
    void givenNoDescription_whenUpdateService_thenReturnsOk() throws Exception {
        OfferedServiceDTO service = OfferedServiceTestFactory.buildNoDescOfferedServiceDTO();
        String jsonContent = new ObjectMapper().writeValueAsString(service);
        when(offeredServicesService.updateService(1L, service)).thenReturn(service);

        mockMvc.perform(put(ENDPOINT_WITH_ID)
                        .contentType(CONTENT_TYPE)
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(OfferedServiceTestFactory.DEFAULT_NAME))
                .andExpect(jsonPath("$.description").value(""))
                .andExpect(jsonPath("$.price").value(OfferedServiceTestFactory.DEFAULT_PRICE))
                .andExpect(jsonPath("$.category").value(OfferedServiceTestFactory.DEFAULT_CATEGORY.toString()));
    }

    @Test
    void givenPriceNull_whenUpdateService_thenReturnsBadRequest() throws Exception {
        OfferedServiceDTO service = OfferedServiceTestFactory.buildPriceNullOfferedServiceDTO();
        String jsonContent = new ObjectMapper().writeValueAsString(service);

        mockMvc.perform(put(ENDPOINT_WITH_ID)
                        .contentType(CONTENT_TYPE)
                        .content(jsonContent))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.price").value(ErrorMessage.PRICE_NULL));
    }

    @Test
    void givenPriceNegative_whenUpdateService_thenReturnsBadRequest() throws Exception {
        OfferedServiceDTO service = OfferedServiceTestFactory.buildNegativePriceOfferedServiceDTO();
        String jsonContent = new ObjectMapper().writeValueAsString(service);

        mockMvc.perform(put(ENDPOINT_WITH_ID)
                        .contentType(CONTENT_TYPE)
                        .content(jsonContent))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.price").value(ErrorMessage.PRICE_NEGATIVE));
    }

    @Test
    void givenNoCategory_whenUpdateService_thenReturnsBadRequest() throws Exception {
        OfferedServiceDTO service = OfferedServiceTestFactory.buildCategoryNullOfferedServiceDTO();
        String jsonContent = new ObjectMapper().writeValueAsString(service);

        mockMvc.perform(put(ENDPOINT_WITH_ID)
                        .contentType(CONTENT_TYPE)
                        .content(jsonContent))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.category").value(ErrorMessage.CATEGORY_NULL));
    }

    @Test
    void givenWrongCategory_whenUpdateService_thenReturnsBadRequest() throws Exception {
        mockMvc.perform(put(ENDPOINT_WITH_ID)
                        .contentType(CONTENT_TYPE)
                        .content(INVALID_CATEGORY_REQUEST))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(Matchers.containsString(ErrorMessage.INVALID_CATEGORY)));
    }

    @Test
    void givenValidId_whenDeleteService_thenReturnsNoContent() throws Exception {
        doNothing().when(offeredServicesService).deleteService(VALID_ID);

        mockMvc.perform(delete(ENDPOINT_WITH_ID))
                .andExpect(status().isNoContent());
    }

    @Test
    void givenInvalidId_whenDeleteService_thenReturnNoContent() throws Exception {
        doNothing().when(offeredServicesService).deleteService(VALID_ID);

        mockMvc.perform(delete(ENDPOINT_WITH_ID))
                .andExpect(status().isNoContent());
    }
}
