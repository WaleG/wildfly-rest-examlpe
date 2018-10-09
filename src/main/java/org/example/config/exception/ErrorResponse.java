package org.example.config.exception;

import lombok.Data;

/**
 * @author Valentyn.Moliakov
 */
@Data
public class ErrorResponse {
    private String message;

    public ErrorResponse(String message) {
        this.message = message;
    }
}
