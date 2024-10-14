package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.practicum.shareit.booking.BookingStatus.APPROVED;
import static ru.practicum.shareit.util.Header.X_SHARER_USER_ID;

@WebMvcTest(BookingController.class)
public class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookingService bookingService;

    private BookingDto bookingDto;
    private BookingCreateDto bookingCreateDto;

    @BeforeEach
    void setup() {
        // Arrange
        bookingDto = new BookingDto();
        bookingDto.setId(1L);
        bookingDto.setStatus(APPROVED);

        bookingCreateDto = new BookingCreateDto();
        bookingCreateDto.setItemId(1L);
        bookingCreateDto.setStart(LocalDateTime.now());
        bookingCreateDto.setEnd(LocalDateTime.now().plusDays(2));
    }

    @Test
    void addBooking_WhenValidInput_ReturnCreatedBooking() throws Exception {
        // Arrange
        when(bookingService.addBooking(any(BookingCreateDto.class), anyLong()))
                .thenReturn(bookingDto);

        // Act & Expect
        mockMvc.perform(post("/bookings")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingCreateDto))
                        .header(X_SHARER_USER_ID, 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(bookingDto.getId()))
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    @Test
    void approveBooking_WhenApprovalTrue_ReturnApprovedBooking() throws Exception {
        // Arrange
        bookingDto.setStatus(APPROVED);
        when(bookingService.approveBooking(anyLong(), anyLong(), eq(true)))
                .thenReturn(bookingDto);

        // Act & Expect
        mockMvc.perform(patch("/bookings/1")
                        .param("approved", "true")
                        .header(X_SHARER_USER_ID, 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(bookingDto.getId()))
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    @Test
    void findBooking_WhenValidRequest_ReturnBooking() throws Exception {
        // Arrange
        when(bookingService.findBooking(anyLong(), anyLong()))
                .thenReturn(bookingDto);

        // Act & Expect
        mockMvc.perform(get("/bookings/1")
                        .header(X_SHARER_USER_ID, 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(bookingDto.getId()))
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    @Test
    void findBookingsUser_ShouldReturnListOfBookings() throws Exception {
        // Arrange
        when(bookingService.findBookingsUser(anyLong(), any(BookingSearchStatus.class)))
                .thenReturn(List.of(bookingDto));

        // Act & Expect
        mockMvc.perform(get("/bookings")
                        .param("status", "ALL")
                        .header(X_SHARER_USER_ID, 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(bookingDto.getId()))
                .andExpect(jsonPath("$[0].status").value("APPROVED"));
    }

    @Test
    void findOwnerBookings_ShouldReturnListOfBookings() throws Exception {
        // Arrange
        when(bookingService.findOwnerBookings(anyLong(), any(BookingSearchStatus.class)))
                .thenReturn(List.of(bookingDto));

        // Act & Expect
        mockMvc.perform(get("/bookings/owner")
                        .param("status", "ALL")
                        .header(X_SHARER_USER_ID, 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(bookingDto.getId()))
                .andExpect(jsonPath("$[0].status").value("APPROVED"));
    }
}
