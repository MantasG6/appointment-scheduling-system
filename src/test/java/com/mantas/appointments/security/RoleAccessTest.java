package com.mantas.appointments.security;

import com.mantas.appointments.controller.AppointmentsController;
import com.mantas.appointments.controller.ServicesController;
import com.mantas.appointments.service.OfferedServicesService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static com.mantas.appointments.utils.TestSecurityUtils.jwtWithRole;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {ServicesController.class, AppointmentsController.class})
@Import(SecurityConfig.class)
public class RoleAccessTest {

    private final static String CLIENT_ROLE = "CLIENT";
    private final static String PROVIDER_ROLE = "PROVIDER";

    private final static String SERVICES_API = ServicesController.SERVICES_API;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OfferedServicesService servicesService;

    @Test
    void givenClient_whenAccessServices_thenForbidden() throws Exception {
        mockMvc.perform(get(SERVICES_API)
                        .with(jwtWithRole(CLIENT_ROLE)))
                .andExpect(status().isForbidden());
    }

    @Test
    void givenProvider_whenAccessServices_thenOk() throws Exception {
        mockMvc.perform(get(SERVICES_API)
                        .with(jwtWithRole(PROVIDER_ROLE)))
                .andExpect(status().isOk());
    }

    @Test
    void givenClient_whenAccessProvider_thenForbidden() throws Exception {
        mockMvc.perform(get("/api/v1/appointments/provider")
                        .with(jwtWithRole(CLIENT_ROLE)))
                .andExpect(status().isForbidden());
    }

    @Test
    void givenProvider_whenAccessProvider_thenOk() throws Exception {
        mockMvc.perform(get("/api/v1/appointments/provider")
                        .with(jwtWithRole(PROVIDER_ROLE)))
                .andExpect(status().isOk());
    }

    @Test
    void givenClient_whenAccessClient_thenOk() throws Exception {
        mockMvc.perform(get("/api/v1/appointments/client")
                        .with(jwtWithRole(CLIENT_ROLE)))
                .andExpect(status().isOk());
    }
}