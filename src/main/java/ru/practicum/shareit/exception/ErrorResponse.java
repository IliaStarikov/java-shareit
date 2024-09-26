package ru.practicum.shareit.exception;

import lombok.Data;

@Data
public class ErrorResponse {

    private String message;

    public ErrorResponse(String errorMessage) {
        this.message = errorMessage;
    }
}