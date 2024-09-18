package ru.practicum.shareit.exception;

public class EmailIsBusy extends RuntimeException {
    public EmailIsBusy(String message) {
        super(message);
    }
}
