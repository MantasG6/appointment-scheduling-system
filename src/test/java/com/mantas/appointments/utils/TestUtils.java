package com.mantas.appointments.utils;

import com.jayway.jsonpath.JsonPath;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.mantas.appointments.utils.OfferedServiceTestFactory.DEFAULT_CATEGORY;
import static com.mantas.appointments.utils.OfferedServiceTestFactory.DEFAULT_CREATED;
import static com.mantas.appointments.utils.OfferedServiceTestFactory.DEFAULT_DESCRIPTION;
import static com.mantas.appointments.utils.OfferedServiceTestFactory.DEFAULT_NAME;
import static com.mantas.appointments.utils.OfferedServiceTestFactory.DEFAULT_PRICE;
import static com.mantas.appointments.utils.OfferedServiceTestFactory.DEFAULT_UPDATED;
import static com.mantas.appointments.utils.OfferedServiceTestFactory.UPDATED_CATEGORY;
import static com.mantas.appointments.utils.OfferedServiceTestFactory.UPDATED_DESCRIPTION;
import static com.mantas.appointments.utils.OfferedServiceTestFactory.UPDATED_NAME;
import static com.mantas.appointments.utils.OfferedServiceTestFactory.UPDATED_PRICE;
import static com.mantas.appointments.utils.OfferedServiceTestFactory.UPDATED_UPDATE_DATE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * Utility class for test-related methods.
 */
public final class TestUtils {

    /**
     * Private constructor to prevent instantiation.
     */
    private TestUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Creates a {@link JwtRequestPostProcessor} with the specified role.
     *
     * @param role the role to be added to the JWT
     * @return a {@link JwtRequestPostProcessor} with the specified role
     */
    public static JwtRequestPostProcessor jwtWithRole(String role) {
        return jwt().authorities(List.of(new SimpleGrantedAuthority("ROLE_" + role)));
    }

    /**
     * Generates an error message for when an entity is not found by its ID.
     *
     * @param id the ID of the entity that was not found
     * @return a formatted error message
     */
    public static String entityNotFoundMessage(Long id) {
        return "Entity not found with id: " + id;
    }

    public static ResultActions assertJsonResultMatchesDefaultOfferedServiceResponse(ResultActions result) throws Exception {
        assertDatesMatch(result, DEFAULT_UPDATED);
        return result
                .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
                .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
                .andExpect(jsonPath("$.price").value(DEFAULT_PRICE))
                .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY.toString()));
    }

    public static ResultActions assertJsonResultMatchesNoDescOfferedServiceResponse(ResultActions result) throws Exception {
        assertDatesMatch(result, DEFAULT_UPDATED);
        return result
                .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
                .andExpect(jsonPath("$.description").value(""))
                .andExpect(jsonPath("$.price").value(DEFAULT_PRICE))
                .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY.toString()));
    }

    public static ResultActions assertJsonResultMatchesUpdatedOfferedServiceResponse(ResultActions result) throws Exception {
        assertDatesMatch(result, UPDATED_UPDATE_DATE);
        return result
                .andExpect(jsonPath("$.name").value(UPDATED_NAME))
                .andExpect(jsonPath("$.description").value(UPDATED_DESCRIPTION))
                .andExpect(jsonPath("$.price").value(UPDATED_PRICE))
                .andExpect(jsonPath("$.category").value(UPDATED_CATEGORY.toString()));
    }

    private static void assertDatesMatch(ResultActions result, LocalDateTime expectedUpdated) throws Exception {
        String content = result.andReturn().getResponse().getContentAsString();

        LocalDateTime createdFromJson = LocalDateTime.parse(JsonPath.read(content, "$.created"));
        LocalDateTime updatedFromJson = LocalDateTime.parse(JsonPath.read(content, "$.updated"));

        assertEquals(DEFAULT_CREATED.truncatedTo(ChronoUnit.MICROS), createdFromJson.truncatedTo(ChronoUnit.MICROS));
        assertEquals(expectedUpdated.truncatedTo(ChronoUnit.MICROS), updatedFromJson.truncatedTo(ChronoUnit.MICROS));
    }
}
