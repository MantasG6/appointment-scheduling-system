package com.mantas.appointments.utils;

import com.jayway.jsonpath.JsonPath;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static com.mantas.appointments.utils.OfferedServiceTestFactory.DEFAULT_CATEGORY;
import static com.mantas.appointments.utils.OfferedServiceTestFactory.DEFAULT_CREATED;
import static com.mantas.appointments.utils.OfferedServiceTestFactory.DEFAULT_DESCRIPTION;
import static com.mantas.appointments.utils.OfferedServiceTestFactory.DEFAULT_NAME;
import static com.mantas.appointments.utils.OfferedServiceTestFactory.DEFAULT_OWNER_ID;
import static com.mantas.appointments.utils.OfferedServiceTestFactory.DEFAULT_PRICE;
import static com.mantas.appointments.utils.OfferedServiceTestFactory.DEFAULT_UPDATED;
import static com.mantas.appointments.utils.OfferedServiceTestFactory.UPDATED_CATEGORY;
import static com.mantas.appointments.utils.OfferedServiceTestFactory.UPDATED_DESCRIPTION;
import static com.mantas.appointments.utils.OfferedServiceTestFactory.UPDATED_NAME;
import static com.mantas.appointments.utils.OfferedServiceTestFactory.UPDATED_PRICE;
import static com.mantas.appointments.utils.OfferedServiceTestFactory.UPDATED_UPDATE_DATE;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
                .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY.toString()))
                .andExpect(jsonPath("$.ownerId").value(DEFAULT_OWNER_ID));
    }

    public static ResultActions assertJsonResultMatchesNoDescOfferedServiceResponse(ResultActions result) throws Exception {
        assertDatesMatch(result, DEFAULT_UPDATED);
        return result
                .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
                .andExpect(jsonPath("$.description").value(""))
                .andExpect(jsonPath("$.price").value(DEFAULT_PRICE))
                .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY.toString()))
                .andExpect(jsonPath("$.ownerId").value(DEFAULT_OWNER_ID));
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
