package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingCreateDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class BookingCreateDtoTest {

    private BookingCreateDto bookingCreateDto;

    @BeforeEach
    void setup() {
        // Arrange
        bookingCreateDto = new BookingCreateDto();
    }

    @Test
    void testIdSetterAndGetter() {
        // Arrange
        bookingCreateDto.setId(1L);

        // Assert
        assertThat(bookingCreateDto.getId()).isEqualTo(1L);
    }

    @Test
    void testStartSetterAndGetter() {
        // Arrange
        LocalDateTime startTime = LocalDateTime.of(2024, 10, 10, 10, 0);
        bookingCreateDto.setStart(startTime);

        // Assert
        assertThat(bookingCreateDto.getStart()).isEqualTo(startTime);
    }

    @Test
    void testEndSetterAndGetter() {
        // Arrange
        LocalDateTime endTime = LocalDateTime.of(2024, 10, 12, 10, 0);
        bookingCreateDto.setEnd(endTime);

        // Assert
        assertThat(bookingCreateDto.getEnd()).isEqualTo(endTime);
    }

    @Test
    void testItemIdSetterAndGetter() {
        // Arrange
        bookingCreateDto.setItemId(2L);

        // Assert
        assertThat(bookingCreateDto.getItemId()).isEqualTo(2L);
    }

    @Test
    void testBookingRequestCreation() {
        // Arrange
        LocalDateTime startTime = LocalDateTime.of(2024, 10, 10, 10, 0);
        LocalDateTime endTime = LocalDateTime.of(2024, 10, 12, 10, 0);
        bookingCreateDto = new BookingCreateDto();
        bookingCreateDto.setId(1L);
        bookingCreateDto.setStart(startTime);
        bookingCreateDto.setEnd(endTime);
        bookingCreateDto.setItemId(2L);

        // Assert
        assertThat(bookingCreateDto.getId()).isEqualTo(1L);
        assertThat(bookingCreateDto.getStart()).isEqualTo(startTime);
        assertThat(bookingCreateDto.getEnd()).isEqualTo(endTime);
        assertThat(bookingCreateDto.getItemId()).isEqualTo(2L);
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
