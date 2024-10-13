package ru.practicum.shareit.exception;

public class NotEnoughOwnershipRightsException extends RuntimeException {
    public NotEnoughOwnershipRightsException(String message) {
        super(message);
    }
}