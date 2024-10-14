package ru.practicum.shareit.gateway.booking;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.shareit.gateway.booking.dto.BookingCreateDto;
import ru.practicum.shareit.gateway.booking.dto.BookingSearchStatus;

import static ru.practicum.shareit.gateway.util.Header.X_SHARER_USER_ID;

@Controller
@RequestMapping(path = "/bookings")
@Validated
@RequiredArgsConstructor
public class BookingController {

    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> addBooking(@Valid @RequestBody BookingCreateDto bookingRequest,
                                             @RequestHeader(X_SHARER_USER_ID) long userId) {
        return bookingClient.addBooking(bookingRequest, userId);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approveBooking(@PathVariable @Positive long bookingId,
                                                 @RequestParam("approved") Boolean isApproved,
                                                 @RequestHeader(X_SHARER_USER_ID) long userId) {
        return bookingClient.approveBooking(userId, bookingId, isApproved);
    }

    @GetMapping("{bookingId}")
    public ResponseEntity<Object> getBooking(@PathVariable @Positive long bookingId,
                                             @RequestHeader(X_SHARER_USER_ID) long userId) {
        return bookingClient.getBooking(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getBookingsUser(@RequestParam(value = ("status"),
            defaultValue = "ALL") BookingSearchStatus status,
                                                  @RequestHeader(X_SHARER_USER_ID) long userId) {
        return bookingClient.getBookingsUser(userId, status);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getOwnerBookings(@RequestParam(value = ("status"),
            defaultValue = "ALL") BookingSearchStatus status,
                                                   @RequestHeader(X_SHARER_USER_ID) long userId) {
        return bookingClient.getOwnerBookings(userId, status);
    }
}