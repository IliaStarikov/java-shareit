package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingCreateDto;

import java.util.List;
import java.util.Optional;

public interface BookingService {

    BookingDto addBooking(BookingCreateDto bookingRequest, long userId);

    BookingDto approveBooking(long userId, long bookingId, boolean isApproved);

    BookingDto findBooking(long userId, long bookingId);

    List<BookingDto> findBookingsUser(long userId, BookingSearchStatus status);

    List<BookingDto> findOwnerBookings(long userId, BookingSearchStatus status);

    Optional<Booking> findUserBookingForItem(long userId, long itemId);
}