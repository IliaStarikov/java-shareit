package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.exception.ItemNotAvailableException;
import ru.practicum.shareit.exception.NotEnoughOwnershipRightsException;
import ru.practicum.shareit.exception.NotFoundEntityException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private BookingMapper bookingMapper;

    private User user;
    private Item item;
    private Booking booking;
    private BookingCreateDto bookingRequest;

    @BeforeEach
    void setup() {
        // Arrange: Создание объектов
        user = new User();
        user.setId(1L);
        user.setName("user1");
        user.setEmail("user1@mail.com");

        item = new Item();
        item.setId(1L);
        item.setName("itemName");
        item.setDescription("description");
        item.setAvailable(true);
        item.setOwner(user);

        bookingRequest = new BookingCreateDto();
        bookingRequest.setId(1L);
        bookingRequest.setStart(LocalDateTime.now());
        bookingRequest.setEnd(LocalDateTime.now().plusDays(2));
        bookingRequest.setItemId(item.getId());

        booking = new Booking();
        booking.setId(1L);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStart(LocalDateTime.now());
        booking.setEnd(LocalDateTime.now().plusDays(2));
        booking.setStatus(BookingStatus.WAITING);
    }

    @Test
    void addBooking_WhenRequestIsValid_ReturnsBookingDto() {
        // Arrange: Настройка моков
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepository.findByIdWithUser(anyLong())).thenReturn(Optional.of(item));
        when(bookingMapper.toBooking(any(BookingCreateDto.class))).thenReturn(booking);
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        when(bookingMapper.toBookingDto(any(Booking.class))).thenReturn(new BookingDto());

        // Act
        BookingDto result = bookingService.addBooking(bookingRequest, user.getId());

        // Verify: Проверка вызова методов
        verify(userRepository).findById(user.getId());
        verify(itemRepository).findByIdWithUser(bookingRequest.getItemId());
        verify(bookingRepository).save(booking);

        // Assert: Проверка возвращаемого результата
        assertThat(result).isNotNull();
    }

    @Test
    void addBooking_WhenItemIsNotFound_ThrowsNotFoundEntityException() {
        // Arrange
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepository.findByIdWithUser(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundEntityException.class, () -> {
            bookingService.addBooking(bookingRequest, user.getId());
        });
    }

    @Test
    void addBooking_WhenUserIsNotFound_ThrowsNotFoundEntityException() {
        // Arrange
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundEntityException.class, () -> {
            bookingService.addBooking(bookingRequest, user.getId());
        });
    }

    @Test
    void addBooking_WhenItemIsUnavailable_ThrowsItemNotAvailableException() {
        // Arrange
        item.setAvailable(false);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepository.findByIdWithUser(anyLong())).thenReturn(Optional.of(item));

        // Act & Assert
        assertThrows(ItemNotAvailableException.class, () -> {
            bookingService.addBooking(bookingRequest, user.getId());
        });
    }

    @Test
    void approveBooking_WhenUserIsNotOwner_ThrowsNotEnoughOwnershipRightsException() {
        // Arrange: Настройка моков, создание объектов
        when(bookingRepository.findByIdWithUserAndItem(anyLong())).thenReturn(Optional.of(booking));
        User user1 = new User();
        user1.setId(2L);
        user1.setName("user2");
        user1.setEmail("user2@mail.com");
        booking.getItem().setOwner(user1);

        // Act & Assert
        assertThrows(NotEnoughOwnershipRightsException.class, () -> {
            bookingService.approveBooking(user.getId(), booking.getId(), true);
        });
    }

    @Test
    void findBooking_WhenBookingDoesNotExist_ThrowsNotFoundEntityException() {
        // Arrange
        when(bookingRepository.findByIdWithUserAndItem(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundEntityException.class, () -> {
            bookingService.findBooking(user.getId(), booking.getId());
        });
    }

    @Test
    void findBooking_WhenUserIsNeitherBookerNorOwner_ThrowsNotEnoughOwnershipRightsException() {
        // Arrange
        when(bookingRepository.findByIdWithUserAndItem(anyLong())).thenReturn(Optional.of(booking));
        booking.getBooker().setId(2L);

        // Act & Assert
        assertThrows(NotEnoughOwnershipRightsException.class, () -> {
            bookingService.findBooking(5, booking.getId());
        });
    }

    @Test
    void findBooking_WhenRequestIsValid_ReturnsBookingDto() {
        // Arrange
        when(bookingRepository.findByIdWithUserAndItem(anyLong())).thenReturn(Optional.of(booking));
        when(bookingMapper.toBookingDto(any(Booking.class))).thenReturn(new BookingDto());

        // Act
        BookingDto result = bookingService.findBooking(user.getId(), booking.getId());

        // Assert
        assertThat(result).isNotNull();
    }

    @Test
    void findBookingsUser_WhenUserIsNotFound_ThrowsNotFoundEntityException() {
        // Arrange
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundEntityException.class, () -> {
            bookingService.findBookingsUser(user.getId(), BookingSearchStatus.ALL);
        });
    }

    @Test
    void findOwnerBookings_WhenUserIsNotFound_ThrowsNotFoundEntityException() {
        // Arrange
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundEntityException.class, () -> {
            bookingService.findOwnerBookings(user.getId(), BookingSearchStatus.ALL);
        });
    }
}