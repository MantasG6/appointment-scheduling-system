package com.mantas.appointments.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a service entity in the application.
 * Contains details about the service such as name, description, price, and category.
 */
@Data
@Entity
@Table(name = "services")
@AllArgsConstructor
@NoArgsConstructor
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private Double price;
    @Enumerated(EnumType.STRING)
    private Category category;
}