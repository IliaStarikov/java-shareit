package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingCreateDto;

import java.util.List;

import static ru.practicum.shareit.util.Header.X_SHARER_USER_ID;

@RestController
@RequestMapping(path = "/bookings")
@Validated
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingDto addBooking(@RequestBody BookingCreateDto bookingRequest,
                                 @RequestHeader(X_SHARER_USER_ID) long userId) {
        return bookingService.addBooking(bookingRequest, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approveBooking(@PathVariable long bookingId,
                                     @RequestParam("approved") boolean isApproved,
                                     @RequestHeader(X_SHARER_USER_ID) long userId) {
        return bookingService.approveBooking(userId, bookingId, isApproved);
    }

    @GetMapping("{bookingId}")
    public BookingDto getBooking(@PathVariable long bookingId,
                                 @RequestHeader(X_SHARER_USER_ID) long userId) {
        return bookingService.findBooking(userId, bookingId);
    }

    @GetMapping
    public List<BookingDto> getBookingsUser(@RequestParam(value = ("status"),
            defaultValue = "ALL") BookingSearchStatus status,
                                            @RequestHeader(X_SHARER_USER_ID) long userId) {
        return bookingService.findBookingsUser(userId, status);
    }

    @GetMapping("/owner")
    public List<BookingDto> getOwnerBookings(@RequestParam(value = ("status"),
            defaultValue = "ALL") BookingSearchStatus status,
                                             @RequestHeader(X_SHARER_USER_ID) long userId) {
        return bookingService.findOwnerBookings(userId, status);
    }
}