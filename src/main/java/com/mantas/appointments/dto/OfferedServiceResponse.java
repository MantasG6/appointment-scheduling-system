package com.mantas.appointments.dto;

import com.mantas.appointments.entity.Category;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for Offered Service Response.
 * Contains details about the service such as
 * id, name, description, price, category, date of creation and date of last update.
 */
@Builder
public record OfferedServiceResponse(
        String name,
        String description,
        BigDecimal price,
        Category category,
        LocalDateTime created,
        LocalDateTime updated
) {
}
