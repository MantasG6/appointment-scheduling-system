package com.mantas.appointments.dto;

import com.mantas.appointments.entity.Category;
import com.mantas.appointments.exception.ErrorMessage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.math.BigDecimal;

/**
 * Data Transfer Object for Offered Service.
 * Contains details about the service such as name, description, price, and category.
 */
@Builder
@Deprecated
public record OfferedServiceDTO(
        @NotBlank(message = ErrorMessage.NAME_BLANK)
        String name,

        String description,

        @NotNull(message = ErrorMessage.PRICE_NULL)
        @Positive(message = ErrorMessage.PRICE_NEGATIVE)
        BigDecimal price,

        @NotNull(message = ErrorMessage.CATEGORY_NULL)
        Category category
) {
}
