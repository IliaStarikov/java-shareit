package ru.practicum.shareit.gateway;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.gateway.booking.BookingClient;
import ru.practicum.shareit.gateway.booking.BookingController;
import ru.practicum.shareit.gateway.booking.dto.BookingCreateDto;
import ru.practicum.shareit.gateway.booking.dto.BookingSearchStatus;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.gateway.util.Header.X_SHARER_USER_ID;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingClient bookingClient;

    @Autowired
    private ObjectMapper objectMapper;

    private BookingCreateDto validBookingRequest;
    private BookingCreateDto invalidBookingRequest;

    @BeforeEach
    void setup() {
        // Arrange: Создание объектов
        validBookingRequest = new BookingCreateDto();
        validBookingRequest.setItemId(1L);
        validBookingRequest.setStart(LocalDateTime.now().plusDays(1));
        validBookingRequest.setEnd(LocalDateTime.now().plusDays(2));

        invalidBookingRequest = new BookingCreateDto();
        invalidBookingRequest.setItemId(1L);
        invalidBookingRequest.setStart(LocalDateTime.now().plusDays(2));
        invalidBookingRequest.setEnd(LocalDateTime.now().plusDays(1));
    }

    @Test
    void addBookingTest_ValidRequest() throws Exception {
        // Arrange
        when(bookingClient.addBooking(any(BookingCreateDto.class), anyLong()))
                .thenReturn(ResponseEntity.ok().body("Booking created"));

        // Act & Expect
        mockMvc.perform(post("/bookings")
                        .header(X_SHARER_USER_ID, 1L)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validBookingRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Booking created"));
    }

    @Test
    void addBookingTest_InvalidRequest() throws Exception {
        // Act & Expect
        mockMvc.perform(post("/bookings")
                        .header(X_SHARER_USER_ID, 1L)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidBookingRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addBookingTest_MissingStartDate() throws Exception {
        // Arrange
        validBookingRequest.setStart(null);

        // Act & Expect
        mockMvc.perform(post("/bookings")
                        .header(X_SHARER_USER_ID, 1L)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validBookingRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void approvedBookingTest() throws Exception {
        // Arrange
        when(bookingClient.approveBooking(anyLong(), anyLong(), any(Boolean.class)))
                .thenReturn(ResponseEntity.ok().body("Booking approved"));

        // Act & Expect
        mockMvc.perform(patch("/bookings/{bookingId}", 1L)
                        .header(X_SHARER_USER_ID, 1L)
                        .param("approved", "true"))
                .andExpect(status().isOk());
    }

    @Test
    void getBookingTest() throws Exception {
        // Arrange
        when(bookingClient.getBooking(anyLong(), anyLong()))
                .thenReturn(ResponseEntity.ok().body("Booking details"));

        // Act & Expect
        mockMvc.perform(get("/bookings/{bookingId}", 1L)
                        .header(X_SHARER_USER_ID, 1L))
                .andExpect(status().isOk());

    }

    @Test
    void getBookingCurrentUserTest() throws Exception {
        // Arrange
        when(bookingClient.getBookingsUser(anyLong(), any(BookingSearchStatus.class)))
                .thenReturn(ResponseEntity.ok().body("User bookings"));

        // Act & Expect
        mockMvc.perform(get("/bookings")
                        .header(X_SHARER_USER_ID, 1L)
                        .param("status", "ALL"))
                .andExpect(status().isOk());
    }

    @Test
    void findOwnerBookingsTest() throws Exception {
        // Arrange
        when(bookingClient.getOwnerBookings(anyLong(), any(BookingSearchStatus.class)))
                .thenReturn(ResponseEntity.ok().body("Owner bookings"));

        // Act & Expect
        mockMvc.perform(get("/bookings/owner")
                        .header(X_SHARER_USER_ID, 1L)
                        .param("status", "ALL"))
                .andExpect(status().isOk());
    }
}