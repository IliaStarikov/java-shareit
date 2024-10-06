package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import ru.practicum.shareit.util.StartBeforeEndDateValid;

import java.time.LocalDateTime;

@Data
@StartBeforeEndDateValid(message = "Дата начала должна быть до даты окончания")
public class BookingCreateDto {

    private Long id;

    @Positive
    @NotNull
    private Long itemId;

    @NotNull
    @FutureOrPresent(message = "Дата начала должна быть либо в настоящем времени, либо в будущем")
    private LocalDateTime start;

    @NotNull
    @Future(message = "Дата окончания должна быть в будущем времени")
    private LocalDateTime end;
}
