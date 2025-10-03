package com.mantas.appointments.service.utils;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Utility class for service-related operations.
 */
public final class ServiceUtils {

    /**
     * Private constructor to prevent instantiation.
     * This class is intended to be a utility class and should not be instantiated.
     */
    private ServiceUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Fetches an entity from the repository by its ID.
     *
     * @param id         ID of the entity to fetch.
     * @param repository Repository to fetch the entity from.
     * @param <E>        Type of the entity.
     * @param <R>        Type of the repository extending JpaRepository.
     * @return The entity with the specified ID.
     * @throws EntityNotFoundException if no entity is found with the given ID.
     */
    public static <E, R extends JpaRepository<E, Long>> E getEntityFromRepoById(Long id, R repository) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Entity not found with id: " + id));
    }
}
