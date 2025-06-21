package com.mantas.appointments.controller;

import com.mantas.appointments.security.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static com.mantas.appointments.utils.TestUtils.jwtWithRole;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(ServicesController.class)
@Import(SecurityConfig.class)
public class ServicesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void givenClient_whenAccessServices_thenForbidden() throws Exception {
        mockMvc.perform(get("/api/v1/services")
                        .with(jwtWithRole("CLIENT")))
                .andExpect(status().isForbidden());
    }

    @Test
    void givenProvider_whenAccessServices_thenOk() throws Exception {
        mockMvc.perform(get("/api/v1/services")
                        .with(jwtWithRole("PROVIDER")))
                .andExpect(status().isOk());
    }

}
