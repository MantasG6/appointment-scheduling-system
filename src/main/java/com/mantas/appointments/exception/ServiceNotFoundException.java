package com.mantas.appointments.exception;

/**
 * Exception thrown when a requested service is not found.
 * This exception is used to indicate that a service with the specified ID does not exist in the system.
 */
public class ServiceNotFoundException extends RuntimeException {
    public ServiceNotFoundException(String message) {
        super(message);
    }
}