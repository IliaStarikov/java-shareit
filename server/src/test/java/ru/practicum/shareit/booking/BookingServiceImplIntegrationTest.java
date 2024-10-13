package ru.practicum.shareit.booking;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class BookingServiceImplIntegrationTest {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private User user;
    private Item item;

    @BeforeEach
    void setup() {
        // Arrange: Создание объектов
        user = new User();
        user.setName("Test userName");
        user.setEmail("testuser@mail.com");
        userRepository.save(user);

        item = new Item();
        item.setName("Test itemName");
        item.setDescription("Test description");
        item.setAvailable(true);
        item.setOwner(user);
        itemRepository.save(item);
    }

    @Test
    void addBooking_ShouldCreateNewBooking() {
        // Arrange
        BookingCreateDto bookingRequest = new BookingCreateDto();
        bookingRequest.setItemId(item.getId());
        bookingRequest.setStart(LocalDateTime.now().plusDays(1));
        bookingRequest.setEnd(LocalDateTime.now().plusDays(2));

        // Act
        BookingDto createdBooking = bookingService.addBooking(bookingRequest, user.getId());

        // Assert
        assertThat(createdBooking).isNotNull();
        assertThat(createdBooking.getId()).isNotNull();
        assertThat(createdBooking.getItem().getId()).isEqualTo(item.getId());
        assertThat(createdBooking.getStatus()).isEqualTo(BookingStatus.WAITING);
    }

    @Test
    void approveBooking_ShouldUpdateBookingStatus() {
        // Arrange
        BookingCreateDto bookingRequest = new BookingCreateDto();
        bookingRequest.setItemId(item.getId());
        bookingRequest.setStart(LocalDateTime.now().plusDays(1));
        bookingRequest.setEnd(LocalDateTime.now().plusDays(2));

        // Act
        BookingDto createdBooking = bookingService.addBooking(bookingRequest, user.getId());
        BookingDto approvedBooking = bookingService.approveBooking(user.getId(), createdBooking.getId(), true);

        // Assert
        assertThat(approvedBooking).isNotNull();
        assertThat(approvedBooking.getStatus()).isEqualTo(BookingStatus.APPROVED);
    }

    @Test
    void getBooking_ShouldReturnBooking() {
        // Arrange
        BookingCreateDto bookingRequest = new BookingCreateDto();
        bookingRequest.setItemId(item.getId());
        bookingRequest.setStart(LocalDateTime.now().plusDays(1));
        bookingRequest.setEnd(LocalDateTime.now().plusDays(2));

        // Act
        BookingDto createdBooking = bookingService.addBooking(bookingRequest, user.getId());
        BookingDto retrievedBooking = bookingService.findBooking(user.getId(), createdBooking.getId());

        // Assert
        assertThat(retrievedBooking).isNotNull();
        assertThat(retrievedBooking.getId()).isEqualTo(createdBooking.getId());
    }

    @Test
    void getBookingsOfUser_ShouldReturnBookings() {
        // Arrange
        BookingCreateDto bookingRequest = new BookingCreateDto();
        bookingRequest.setItemId(item.getId());
        bookingRequest.setStart(LocalDateTime.now().plusDays(1));
        bookingRequest.setEnd(LocalDateTime.now().plusDays(2));

        // Act
        bookingService.addBooking(bookingRequest, user.getId());
        List<BookingDto> bookings = bookingService.findBookingsUser(user.getId(), BookingSearchStatus.ALL);

        // Assert
        assertThat(bookings).isNotEmpty();
        assertThat(bookings.size()).isGreaterThan(0);
    }
}
