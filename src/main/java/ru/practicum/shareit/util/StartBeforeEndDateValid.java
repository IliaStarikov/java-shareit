package ru.practicum.shareit.util;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(ElementType.TYPE)
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {CheckDateValidator.class})
public @interface StartBeforeEndDateValid {
    String message() default "Дата начала должна быть до даты окончания и не равна null";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}