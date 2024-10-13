package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingCreateDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class BookingCreateDtoTest {

    private BookingCreateDto BookingCreateDto;

    @BeforeEach
    void setup() {
        // Arrange
        BookingCreateDto = new BookingCreateDto();
    }

    @Test
    void testIdSetterAndGetter() {
        // Arrange
        BookingCreateDto.setId(1L);

        // Assert
        assertThat(BookingCreateDto.getId()).isEqualTo(1L);
    }

    @Test
    void testStartSetterAndGetter() {
        // Arrange
        LocalDateTime startTime = LocalDateTime.of(2024, 10, 10, 10, 0);
        BookingCreateDto.setStart(startTime);

        // Assert
        assertThat(BookingCreateDto.getStart()).isEqualTo(startTime);
    }

    @Test
    void testEndSetterAndGetter() {
        // Arrange
        LocalDateTime endTime = LocalDateTime.of(2024, 10, 12, 10, 0);
        BookingCreateDto.setEnd(endTime);

        // Assert
        assertThat(BookingCreateDto.getEnd()).isEqualTo(endTime);
    }

    @Test
    void testItemIdSetterAndGetter() {
        // Arrange
        BookingCreateDto.setItemId(2L);

        // Assert
        assertThat(BookingCreateDto.getItemId()).isEqualTo(2L);
    }

    @Test
    void testBookingRequestCreation() {
        // Arrange
        LocalDateTime startTime = LocalDateTime.of(2024, 10, 10, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2024, 10, 12, 10, 0);
        BookingCreateDto = new BookingCreateDto();
        BookingCreateDto.setId(1L);
        BookingCreateDto.setStart(startTime);
        BookingCreateDto.setEnd(endTime);
        BookingCreateDto.setItemId(2L);

        // Assert
        assertThat(BookingCreateDto.getId()).isEqualTo(1L);
        assertThat(BookingCreateDto.getStart()).isEqualTo(startTime);
        assertThat(BookingCreateDto.getEnd()).isEqualTo(endTime);
        assertThat(BookingCreateDto.getItemId()).isEqualTo(2L);
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        BookingCreateDto bookingRequest1 = new BookingCreateDto();
        bookingRequest1.setId(1L);
        bookingRequest1.setStart(LocalDateTime.of(2024, 10, 10, 10, 0));
        bookingRequest1.setEnd(LocalDateTime.of(2024, 10, 12, 10, 0));
        bookingRequest1.setItemId(2L);

        BookingCreateDto bookingRequest2 = new BookingCreateDto();
        bookingRequest2.setId(1L);
        bookingRequest2.setStart(LocalDateTime.of(2024, 10, 10, 10, 0));
        bookingRequest2.setEnd(LocalDateTime.of(2024, 10, 12, 10, 0));
        bookingRequest2.setItemId(2L);

        // Assert
        assertThat(bookingRequest1).isEqualTo(bookingRequest2);
        assertThat(bookingRequest1.hashCode()).isEqualTo(bookingRequest2.hashCode());
    }
}
