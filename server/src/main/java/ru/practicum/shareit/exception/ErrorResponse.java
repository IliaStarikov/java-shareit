package ru.practicum.shareit.exception;

import lombok.Data;

@Data
public class ErrorResponse {

    private String status;
    private String message;

    public ErrorResponse(String status, String errorMessage) {
        this.status = status;
        this.message = errorMessage;
    }
}