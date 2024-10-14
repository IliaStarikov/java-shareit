package ru.practicum.shareit.booking;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.exception.ItemNotAvailableException;
import ru.practicum.shareit.exception.NotEnoughOwnershipRightsException;
import ru.practicum.shareit.exception.NotFoundEntityException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static ru.practicum.shareit.booking.BookingStatus.APPROVED;
import static ru.practicum.shareit.booking.BookingStatus.REJECTED;
import static ru.practicum.shareit.booking.BookingStatus.WAITING;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    @Override
    @Transactional
    public BookingDto addBooking(BookingCreateDto bookingRequest, long userId) {
        log.info("Пользователь с id: {}, создает бронь: {}", userId, bookingRequest);

        // Маппим запрос в Booking
        Booking booking = bookingMapper.toBooking(bookingRequest);

        // Находим пользователя и предмет
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundEntityException("User не найден"));
        Item item = itemRepository.findByIdWithUser(bookingRequest.getItemId())
                .orElseThrow(() -> new NotFoundEntityException("Item не найден"));

        // Проверяем доступность предмета
        if (!item.getAvailable()) {
            log.info("Item не доступен для бронирования");
            throw new ItemNotAvailableException("Item не доступен");
        }

        // Устанавливаем недостающие значения в booking
        booking.setBooker(user);
        booking.setItem(item);
        booking.setStatus(WAITING);

        // Сохраняем booking в базе данных
        booking = bookingRepository.save(booking);
        log.info("Создана бронь: {}", booking);
        return bookingMapper.toBookingDto(booking);
    }

    @Override
    @Transactional
    public BookingDto approveBooking(long userId, long bookingId, boolean isApproved) {
        log.info("Подтверждение брони id: {}, пользователем id: {}", bookingId, userId);
        Booking booking = bookingRepository.findByIdWithUserAndItem(bookingId)
                .orElseThrow(() -> new NotFoundEntityException("Бронь не найдена"));

        if (booking.getItem().getOwner().getId() != userId) {
            log.info("Недостаточно прав для {}", booking);
            throw new NotEnoughOwnershipRightsException("Недостаточно прав для подтверждения брони");
        }
        booking.setStatus(isApproved ? APPROVED : REJECTED);
        return bookingMapper.toBookingDto(booking);
    }

    @Override
    public BookingDto findBooking(long userId, long bookingId) {
        log.info("Запрос брони id: {}, владельцем вещи с id: {}", bookingId, userId);
        Booking booking = bookingRepository.findByIdWithUserAndItem(bookingId)
                .orElseThrow(() -> new NotFoundEntityException("Бронь не найдена"));

        if (booking.getBooker().getId() != userId && booking.getItem().getOwner().getId() != userId) {
            log.info("User с id: {} не имеет прав для просмотра", userId);
            throw new NotEnoughOwnershipRightsException("Недостаточно прав для получения booking");
        }

        log.info("Получен booking: {}", booking);
        return bookingMapper.toBookingDto(booking);
    }

    @Override
    public List<BookingDto> findBookingsUser(long userId, BookingSearchStatus status) {
        log.info("User c id: {}, запрашивает bookings со статусом: {}", userId, status);
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundEntityException("User не найден"));

        BooleanExpression byUserId = QBooking.booking.booker.id.eq(userId);
        BooleanExpression byState = getBookingStatusPredicate(status);

        Iterable<Booking> items = bookingRepository.findAll(
                byUserId.and(byState),
                Sort.by(Sort.Direction.DESC, "start"));

        log.info("Получены bookings пользователя с id: {}", userId);
        return StreamSupport.stream(items.spliterator(), false)
                .map(bookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> findOwnerBookings(long userId, BookingSearchStatus status) {
        log.info("User id: {}, пытается получить свои bookings. Статус: {}", userId, status);
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundEntityException("User не найден"));

        BooleanExpression byUserId = QBooking.booking.item.owner.id.eq(userId);
        BooleanExpression byState = getBookingStatusPredicate(status);

        Iterable<Booking> items = bookingRepository.findAll(
                byUserId.and(byState),
                Sort.by(Sort.Direction.DESC, "start"));

        log.info("Получен User: {} владелец bookings, сортировка по дате", userId);
        return StreamSupport.stream(items.spliterator(), false)
                .map(bookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    public Optional<Booking> findUserBookingForItem(long userId, long itemId) {
        return bookingRepository.findByBooker_IdAndItem_Id(userId, itemId);
    }

    private BooleanExpression getBookingStatusPredicate(BookingSearchStatus status) {
        return switch (status) {
            case ALL -> null;
            case PAST -> QBooking.booking.end.before(LocalDateTime.now());
            case CURRENT -> QBooking.booking.end.after(LocalDateTime.now());
            case FUTURE -> QBooking.booking.start.after(LocalDateTime.now());
            case REJECTED -> QBooking.booking.status.eq(REJECTED);
            case WAITING -> QBooking.booking.status.eq(WAITING);
        };
    }
}