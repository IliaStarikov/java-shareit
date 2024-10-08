package ru.practicum.shareit.util;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.practicum.shareit.booking.dto.BookingCreateDto;

import java.time.LocalDateTime;

public class CheckDateValidator implements ConstraintValidator<StartBeforeEndDateValid, BookingCreateDto> {

    @Override
    public void initialize(StartBeforeEndDateValid constraintAnnotation) {
    }

    @Override
    public boolean isValid(BookingCreateDto booking, ConstraintValidatorContext constraintValidatorContext) {
        LocalDateTime start = booking.getStart();
        LocalDateTime end = booking.getEnd();
        if (start == null || end == null) {
            return false;
        }
        return start.isBefore(end);
    }
}