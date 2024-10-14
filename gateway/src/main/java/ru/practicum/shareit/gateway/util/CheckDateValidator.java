package ru.practicum.shareit.gateway.util;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.practicum.shareit.gateway.booking.dto.BookingCreateDto;

import java.time.LocalDateTime;

public class CheckDateValidator implements ConstraintValidator<StartBeforeEndDateValid, BookingCreateDto> {

    @Override
    public void initialize(StartBeforeEndDateValid constraintAnnotation) {
    }

    @Override
    public boolean isValid(BookingCreateDto bookingRequest, ConstraintValidatorContext constraintValidatorContext) {
        LocalDateTime start = bookingRequest.getStart();
        LocalDateTime end = bookingRequest.getEnd();
        if (start == null || end == null) {
            return false;
        }
        return start.isBefore(end);
    }
}