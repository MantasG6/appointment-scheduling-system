package com.mantas.appointments.dto;

import com.mantas.appointments.entity.Category;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for Offered Service Response.
 * Contains details about the service.
 */
@Builder
public record OfferedServiceResponse(
        String name,
        String description,
        BigDecimal price,
        String ownerId,
        Category category,
        LocalDateTime created,
        LocalDateTime updated
) {
}
