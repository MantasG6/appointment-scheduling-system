package com.mantas.appointments.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

/**
 * Utility class for handling exceptions
 */
public class ExceptionUtils {

    /**
     * Extracts the field name from an {@link InvalidFormatException}.
     *
     * @param ife the {@link InvalidFormatException} to extract the field name from
     * @return the field name, or "unknown" if it cannot be determined
     */
    public static String extractFieldName(InvalidFormatException ife) {
        if (ife.getPath() != null && !ife.getPath().isEmpty()) {
            String field = ife.getPath().getLast().getFieldName();
            return field != null ? field : "unknown";
        }
        return "unknown";
    }
}
