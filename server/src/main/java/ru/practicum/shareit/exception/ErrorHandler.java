package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {
    @ExceptionHandler({NotFoundEntityException.class, IncorrectArgumentException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundEntityException(final Exception e) {
        log.info("Status 404, Сущность не найдена {}", e.getMessage());
        return new ErrorResponse("404" + e.getMessage(), "Entity not found");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleNotUniqueFieldException(final AlreadyOccupiedEmailException e) {
        log.info("Status 409, Email должен быть уникальным: {}", e.getMessage());
        return new ErrorResponse("409" + e.getMessage(), "Email already exist");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleNotEnoughRightsException(final NotEnoughOwnershipRightsException e) {
        log.warn("Status 400, Недостаточно прав для запроса: {}.", e.getMessage());
        return new ErrorResponse("400" + e.getMessage(),"Wrong booking owner");
    }
}