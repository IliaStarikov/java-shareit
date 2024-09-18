package ru.practicum.shareit.booking;

import jakarta.validation.constraints.NotNull;
import lombok.Data;


import java.time.LocalDateTime;

@Data
public class Booking {
    private final int id;
    private final int item;
    private final int booker;
    @NotNull
    private LocalDateTime start;
    @NotNull
    private LocalDateTime end;
    @NotNull
    private Status status;

    public enum Status {
        WAITING,
        APPROVED,
        REJECTED,
        CANCELED
    }
}
