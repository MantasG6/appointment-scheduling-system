package com.mantas.appointments.exception;

import lombok.Data;

import java.util.Map;

/**
 * Represents an API error response.
 * This class is used to encapsulate error details that will be returned in the API response.
 */
@Data
public class ApiError {
    private String error;
    private String message;
    private int status;
    private String path;
    private String timestamp;
    private Map<String, String> validationErrors;

    private ApiError(Builder builder) {
        this.error = builder.error;
        this.message = builder.message;
        this.status = builder.status;
        this.path = builder.path;
        this.timestamp = builder.timestamp;
        this.validationErrors = builder.validationErrors;
    }

    public static class Builder {
        private String error;
        private String message;
        private int status;
        private String path;
        private String timestamp;
        private Map<String, String> validationErrors;

        public Builder error(String error) {
            this.error = error;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder status(int status) {
            this.status = status;
            return this;
        }

        public Builder path(String path) {
            this.path = path;
            return this;
        }

        public Builder timestamp(String timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder validationErrors(Map<String, String> validationErrors) {
            this.validationErrors = validationErrors;
            return this;
        }

        public ApiError build() {
            return new ApiError(this);
        }
    }
}
