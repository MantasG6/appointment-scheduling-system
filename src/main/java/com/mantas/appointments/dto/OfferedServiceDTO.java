package com.mantas.appointments.dto;

import com.mantas.appointments.entity.Category;

import java.math.BigDecimal;

/**
 * Data Transfer Object for Offered Service.
 * Contains details about the service such as name, description, price, and category.
 */
public record OfferedServiceDTO(String name, String description, BigDecimal price, Category category) {
}
