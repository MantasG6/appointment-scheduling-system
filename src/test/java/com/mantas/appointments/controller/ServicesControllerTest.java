package com.mantas.appointments.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mantas.appointments.dto.OfferedServiceRequest;
import com.mantas.appointments.dto.OfferedServiceResponse;
import com.mantas.appointments.exception.ErrorMessage;
import com.mantas.appointments.exception.GlobalExceptionHandler;
import com.mantas.appointments.security.TestSecurityConfig;
import com.mantas.appointments.service.OfferedServices;
import com.mantas.appointments.utils.OfferedServiceTestFactory;
import jakarta.persistence.EntityNotFoundException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static com.mantas.appointments.utils.TestUtils.assertJsonResultMatchesDefaultOfferedServiceResponse;
import static com.mantas.appointments.utils.TestUtils.assertJsonResultMatchesNoDescOfferedServiceResponse;
import static com.mantas.appointments.utils.TestUtils.assertJsonResultMatchesUpdatedOfferedServiceResponse;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ServicesController.class, excludeAutoConfiguration = {DataJpaTest.class})
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
        List<OfferedServiceResponse> services = List.of(
                OfferedServiceTestFactory.buildDefaultOfferedServiceResponse(),
                OfferedServiceTestFactory.buildFullUpdateOfferedServiceResponse()
        );
        when(offeredServicesService.getAllServices()).thenReturn(services);

        mockMvc.perform(get(BASE_ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void givenValidId_whenGetServiceById_thenReturnsOk() throws Exception {
        OfferedServiceResponse service = OfferedServiceTestFactory.buildDefaultOfferedServiceResponse();
        when(offeredServicesService.getServiceById(VALID_ID)).thenReturn(service);

        ResultActions result = mockMvc.perform(get(ENDPOINT_WITH_ID))
                .andExpect(status().isOk());
        assertJsonResultMatchesDefaultOfferedServiceResponse(result);
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
        OfferedServiceRequest request = OfferedServiceTestFactory.buildDefaultOfferedServiceRequest();
        OfferedServiceResponse response = OfferedServiceTestFactory.buildDefaultOfferedServiceResponse();
        when(offeredServicesService.createService(request)).thenReturn(response);

        String jsonContent = new ObjectMapper().writeValueAsString(request);
        ResultActions result = mockMvc.perform(post(BASE_ENDPOINT)
                        .contentType(CONTENT_TYPE)
                        .content(jsonContent))
                .andExpect(status().isOk());
        assertJsonResultMatchesDefaultOfferedServiceResponse(result);
    }

    @Test
    void givenNoName_whenCreateService_thenReturnsBadRequest() throws Exception {
        OfferedServiceRequest request = OfferedServiceTestFactory.buildNoNameOfferedServiceRequest();
        String jsonContent = new ObjectMapper().writeValueAsString(request);

        mockMvc.perform(post(BASE_ENDPOINT)
                        .contentType(CONTENT_TYPE)
                        .content(jsonContent))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.name").value(ErrorMessage.NAME_BLANK));
    }

    @Test
    void givenNoDescription_whenCreateService_thenReturnsOk() throws Exception {
        OfferedServiceRequest request = OfferedServiceTestFactory.buildNoDescOfferedServiceRequest();
        OfferedServiceResponse response = OfferedServiceTestFactory.buildNoDescOfferedServiceResponse();
        when(offeredServicesService.createService(request)).thenReturn(response);

        String jsonContent = new ObjectMapper().writeValueAsString(request);
        ResultActions result = mockMvc.perform(post(BASE_ENDPOINT)
                        .contentType(CONTENT_TYPE)
                        .content(jsonContent))
                .andExpect(status().isOk());
        assertJsonResultMatchesNoDescOfferedServiceResponse(result);
    }

    @Test
    void givenPriceNull_whenCreateService_thenReturnsBadRequest() throws Exception {
        OfferedServiceRequest request = OfferedServiceTestFactory.buildPriceNullOfferedServiceRequest();
        String jsonContent = new ObjectMapper().writeValueAsString(request);

        mockMvc.perform(post(BASE_ENDPOINT)
                        .contentType(CONTENT_TYPE)
                        .content(jsonContent))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.price").value(ErrorMessage.PRICE_NULL));
    }

    @Test
    void givenPriceNegative_whenCreateService_thenReturnsBadRequest() throws Exception {
        OfferedServiceRequest request = OfferedServiceTestFactory.buildNegativePriceOfferedServiceRequest();
        String jsonContent = new ObjectMapper().writeValueAsString(request);

        mockMvc.perform(post(BASE_ENDPOINT)
                        .contentType(CONTENT_TYPE)
                        .content(jsonContent))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.price").value(ErrorMessage.PRICE_NEGATIVE));
    }

    @Test
    void givenNoCategory_whenCreateService_thenReturnsBadRequest() throws Exception {
        OfferedServiceRequest request = OfferedServiceTestFactory.buildCategoryNullOfferedServiceRequest();
        String jsonContent = new ObjectMapper().writeValueAsString(request);

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
        OfferedServiceRequest request = OfferedServiceTestFactory.buildFullUpdateOfferedServiceRequest();
        OfferedServiceResponse response = OfferedServiceTestFactory.buildFullUpdateOfferedServiceResponse();
        when(offeredServicesService.updateService(VALID_ID, request)).thenReturn(response);

        String jsonContent = new ObjectMapper().writeValueAsString(request);
        ResultActions result = mockMvc.perform(put(BASE_ENDPOINT + "/" + VALID_ID)
                        .contentType(CONTENT_TYPE)
                        .content(jsonContent))
                .andExpect(status().isOk());
        assertJsonResultMatchesUpdatedOfferedServiceResponse(result);
    }

    @Test
    void givenInvalidId_whenUpdateService_thenReturnsNotFound() throws Exception {
        OfferedServiceRequest request = OfferedServiceTestFactory.buildDefaultOfferedServiceRequest();
        when(offeredServicesService.updateService(VALID_ID, request)).thenThrow(new EntityNotFoundException(ErrorMessage.ENTITY_NOT_FOUND + VALID_ID));

        String jsonContent = new ObjectMapper().writeValueAsString(request);
        mockMvc.perform(put(BASE_ENDPOINT + "/" + VALID_ID)
                        .contentType(CONTENT_TYPE)
                        .content(jsonContent))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(ErrorMessage.ENTITY_NOT_FOUND + VALID_ID));
    }

    @Test
    void givenNoName_whenUpdateService_thenReturnsBadRequest() throws Exception {
        OfferedServiceRequest request = OfferedServiceTestFactory.buildNoNameOfferedServiceRequest();
        String jsonContent = new ObjectMapper().writeValueAsString(request);

        mockMvc.perform(put(ENDPOINT_WITH_ID)
                        .contentType(CONTENT_TYPE)
                        .content(jsonContent))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.name").value(ErrorMessage.NAME_BLANK));
    }

    @Test
    void givenNoDescription_whenUpdateService_thenReturnsOk() throws Exception {
        OfferedServiceRequest request = OfferedServiceTestFactory.buildNoDescOfferedServiceRequest();
        OfferedServiceResponse response = OfferedServiceTestFactory.buildNoDescOfferedServiceResponse();
        String jsonContent = new ObjectMapper().writeValueAsString(request);
        when(offeredServicesService.updateService(1L, request)).thenReturn(response);

        ResultActions result = mockMvc.perform(put(ENDPOINT_WITH_ID)
                        .contentType(CONTENT_TYPE)
                        .content(jsonContent))
                .andExpect(status().isOk());
        assertJsonResultMatchesNoDescOfferedServiceResponse(result);
    }

    @Test
    void givenPriceNull_whenUpdateService_thenReturnsBadRequest() throws Exception {
        OfferedServiceRequest request = OfferedServiceTestFactory.buildPriceNullOfferedServiceRequest();
        String jsonContent = new ObjectMapper().writeValueAsString(request);

        mockMvc.perform(put(ENDPOINT_WITH_ID)
                        .contentType(CONTENT_TYPE)
                        .content(jsonContent))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.price").value(ErrorMessage.PRICE_NULL));
    }

    @Test
    void givenPriceNegative_whenUpdateService_thenReturnsBadRequest() throws Exception {
        OfferedServiceRequest request = OfferedServiceTestFactory.buildNegativePriceOfferedServiceRequest();
        String jsonContent = new ObjectMapper().writeValueAsString(request);

        mockMvc.perform(put(ENDPOINT_WITH_ID)
                        .contentType(CONTENT_TYPE)
                        .content(jsonContent))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.price").value(ErrorMessage.PRICE_NEGATIVE));
    }

    @Test
    void givenNoCategory_whenUpdateService_thenReturnsBadRequest() throws Exception {
        OfferedServiceRequest request = OfferedServiceTestFactory.buildCategoryNullOfferedServiceRequest();
        String jsonContent = new ObjectMapper().writeValueAsString(request);

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
