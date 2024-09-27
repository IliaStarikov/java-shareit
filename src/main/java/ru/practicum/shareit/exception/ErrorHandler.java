package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {
    @ExceptionHandler({NotFoundEntityException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundEntityException(final Exception e) {
        log.info("Сущность не найдена 404 {}", e.getMessage());
        return new ErrorResponse("Сущность не найдена 404 " + e.getMessage());
    }

    @ExceptionHandler({AlreadyOccupiedEmailException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleNotUniqueFieldException(final AlreadyOccupiedEmailException e) {
        log.info("Email должен быть уникальным: {}", e.getMessage());
        return new ErrorResponse("Email должен быть уникальным: " + e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleNoValidRequestBodyException(final MethodArgumentNotValidException e) {
        log.warn("Тело запроса содержит невалидные данные: {}.", e.getMessage());
        return new ErrorResponse(
                "Проверьте валидность введенных данных, error 400");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowableException(final Throwable e) {
        log.info("Ошибка обработки запроса {}", e.getMessage());
        return new ErrorResponse("Ошибка обработки запроса, error 500 " + e.getMessage());
    }
}