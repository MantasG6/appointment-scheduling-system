package com.mantas.appointments.controller;

import com.mantas.appointments.security.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static com.mantas.appointments.utils.TestUtils.jwtWithRole;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AppointmentsController.class)
@Import(SecurityConfig.class)
class AppointmentsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void givenClient_whenAccessProvider_thenForbidden() throws Exception {
        mockMvc.perform(get("/api/v1/appointments/provider")
                        .with(jwtWithRole("CLIENT")))
                .andExpect(status().isForbidden());
    }

    @Test
    void givenProvider_whenAccessProvider_thenOk() throws Exception {
        mockMvc.perform(get("/api/v1/appointments/provider")
                        .with(jwtWithRole("PROVIDER")))
                .andExpect(status().isOk());
    }

    @Test
    void givenClient_whenAccessClient_thenOk() throws Exception {
        mockMvc.perform(get("/api/v1/appointments/client")
                        .with(jwtWithRole("CLIENT")))
                .andExpect(status().isOk());
    }
}