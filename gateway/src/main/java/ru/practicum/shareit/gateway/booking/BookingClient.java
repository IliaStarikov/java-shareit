package ru.practicum.shareit.gateway.booking;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.shareit.gateway.booking.dto.BookingCreateDto;
import ru.practicum.shareit.gateway.booking.dto.BookingSearchStatus;
import ru.practicum.shareit.gateway.client.BaseClient;

import java.util.Map;

@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> addBooking(@Valid BookingCreateDto bookingRequest, long userId) {
        return post("", userId, bookingRequest);
    }

    public ResponseEntity<Object> approveBooking(long userId, @Positive long bookingId, Boolean isApproved) {
        Map<String, Object> params = Map.of("approved", isApproved);

        String url = UriComponentsBuilder.fromPath("/" + bookingId)
                .queryParam("approved", isApproved)
                .toUriString();

        return patch(url, userId, params);
    }

    public ResponseEntity<Object> getBooking(long userId, Long bookingId) {
        return get("/" + bookingId, userId);
    }

    public ResponseEntity<Object> getBookingsUser(long userId, BookingSearchStatus status) {
        Map<String, Object> params = Map.of("status", status.name());
        return get("", userId, params);
    }

    public ResponseEntity<Object> getOwnerBookings(long userId, BookingSearchStatus status) {
        Map<String, Object> params = Map.of("status", status.name());
        return get("", userId, params);
    }
}