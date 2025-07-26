package com.mantas.appointments.dto;

import com.mantas.appointments.entity.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

/**
 * Data Transfer Object for Offered Service.
 * Contains details about the service such as name, description, price, and category.
 */
public record OfferedServiceDTO(
        @NotBlank(message = "Service name cannot be blank")
        String name,

        String description,

        @NotNull(message = "Service price cannot be null")
        @Positive(message = "Service price must be greater than zero")
        BigDecimal price,

        @NotNull(message = "Service category cannot be null")
        Category category
) {
}
