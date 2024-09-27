package ru.practicum.shareit.exception;

public class AlreadyOccupiedEmailException extends RuntimeException {
    public AlreadyOccupiedEmailException(String message) {
        super(message);
    }
}
