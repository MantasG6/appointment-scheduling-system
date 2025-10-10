package com.mantas.appointments.service.utils;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;

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

    /**
     * Extracts the user ID from the given Authentication object.
     * Assumes that the principal is a Jwt token and retrieves the 'sub' claim.
     *
     * @param authentication The Authentication object containing the principal.
     * @return The user ID extracted from the Jwt token.
     * @throws IllegalArgumentException if the principal is not of type Jwt.
     */
    public static String extractUserIdFromAuthentication(Authentication authentication) {
        // In Keycloak OAuth2 the principal is a Jwt token
        if (authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt.getSubject(); // sub claim represents the user ID
        }
        throw new IllegalArgumentException("Authentication principal is not of type Jwt");
    }
}
