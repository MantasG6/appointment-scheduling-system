package com.mantas.appointments.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Represents a service entity in the application.
 * Contains details about the service such as name, description, price, and category.
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class OfferedService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Service name cannot be blank")
    private String name;

    @NotBlank(message = "Service description cannot be blank")
    private String description;

    @NotNull(message = "Service price cannot be null")
    @Positive(message = "Service price must be greater than zero")
    private BigDecimal price;

    @NotNull(message = "Service category cannot be null")
    @Enumerated(EnumType.STRING)
    private Category category;
}