package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;

import static ru.practicum.shareit.booking.BookingStatus.APPROVED;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BookingMapperTest {

    private BookingMapper bookingMapper;

    @BeforeEach
    void setup() {
        bookingMapper = Mappers.getMapper(BookingMapper.class);
    }

    @Test
    void toBooking_ShouldMapBookingCreateDtoToBooking() {
        // Arrange
        BookingCreateDto request = new BookingCreateDto();
        request.setStart(LocalDateTime.now());
        request.setEnd(LocalDateTime.now().plusDays(2));

        // Act
        Booking booking = bookingMapper.toBooking(request);

        // Assert
        assertEquals(request.getStart(), booking.getStart());
        assertEquals(request.getEnd(), booking.getEnd());
    }

    @Test
    void toBookingDto_ShouldMapBookingToBookingDto() {
        // Arrange
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setStatus(APPROVED);
        booking.setStart(LocalDateTime.now());
        booking.setEnd(LocalDateTime.now().plusDays(2));

        // Act
        BookingDto bookingDto = bookingMapper.toBookingDto(booking);

        // Assert
        assertEquals(1L, bookingDto.getId());
        assertEquals(APPROVED, bookingDto.getStatus());
        assertEquals(booking.getStart(), bookingDto.getStart());
        assertEquals(booking.getEnd(), bookingDto.getEnd());
    }
}