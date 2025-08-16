package com.mantas.appointments.exception;

/**
 * Exception thrown when a requested entity is not found.
 * This exception is used to indicate that an entity with the specified ID does not exist in the system.
 */
@Deprecated
public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}