package ru.practicum.shareit.booking;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingCreateDto;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@Validated
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingDto addBooking(@Valid @RequestBody BookingCreateDto bookingRequest,
                                 @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.addBooking(bookingRequest, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approveBooking(@PathVariable @Positive long bookingId,
                                     @RequestParam("approved") boolean isApproved,
                                     @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.approveBooking(userId, bookingId, isApproved);
    }

    @GetMapping("{bookingId}")
    public BookingDto getBooking(@PathVariable @Positive long bookingId,
                                 @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.findBooking(userId, bookingId);
    }

    @GetMapping
    public List<BookingDto> getBookingsUser(@RequestParam(value = ("status"),
            defaultValue = "ALL") BookingSearchStatus status,
                                            @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.findBookingsUser(userId, status);
    }

    @GetMapping("/owner")
    public List<BookingDto> getOwnerBookings(@RequestParam(value = ("status"),
            defaultValue = "ALL") BookingSearchStatus status,
                                             @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.findOwnerBookings(userId, status);
    }
}