package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.exception.NotFoundEntityException;
import ru.practicum.shareit.exception.IncorrectArgumentException;
import ru.practicum.shareit.exception.NotEnoughOwnershipRightsException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.booking.BookingSearchStatus.ALL;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingService bookingService;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final ItemMapper itemMapper;

    @Override
    @Transactional
    public ItemDto addItem(ItemCreateDto request, long userId) {
        log.info("Создание нового Item, OwnerId: {}", userId);
        Item item = itemMapper.toItem(request);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundEntityException("User не найден"));
        item.setOwner(user);

        item = itemRepository.save(item);
        log.info("Item с ID: {} создан", item);
        return itemMapper.toItemDto(item);
    }

    @Override
    @Transactional
    public CommentDto addComment(CommentCreateDto request, long itemId, long userId) {
        log.info("Создание комментария к предмету с ID: {}. ID владельца: {}", itemId, userId);
        Item item = itemRepository.findByIdWithUser(itemId)
                .orElseThrow(() -> new NotFoundEntityException("Item не найден"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundEntityException("User не найден"));
        Booking booking = bookingService.findUserBookingForItem(userId, itemId)
                .orElseThrow(() -> new NotFoundEntityException("У User нет запрашиваемого бронирования"));
        if (booking.getEnd().isAfter(LocalDateTime.now())) {
            throw new NotEnoughOwnershipRightsException("Бронирование должно быть в прошлом для комментария");
        }

        Comment comment = commentMapper.toComment(request);
        comment.setItem(item);
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());
        comment = commentRepository.save(comment);
        log.info("Комментарий к предмету с ID: {} добавлен: {}", itemId, comment);
        return commentMapper.toCommentDto(comment);
    }

    @Override
    @Transactional
    public ItemDto updateItem(ItemUpdateDto request, long userId, long itemId) {
        log.info("Обновление Item c Id: {}, Owner Id: {}", itemId, userId);
        Item updateItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundEntityException("Item не найден"));

        checkItemOwner(userId, updateItem.getOwner().getId());
        itemMapper.updateItem(request, updateItem);
        updateItem.setId(itemId);

        itemRepository.save(updateItem);
        log.info("Item - {} обновлен", updateItem);
        return itemMapper.toItemDto(updateItem);
    }

    @Override
    public ItemOwnerDto findItem(long itemId, long ownerId) {
        log.info("Получение Item c Id: {}", itemId);
        Item item = itemRepository.findByIdWithUser(itemId)
                .orElseThrow(() -> new NotFoundEntityException("Item не найден"));

        List<BookingDto> hisBookings = bookingService.findOwnerBookings(ownerId, ALL);
        BookingDates bookingDates = getBookingDates(hisBookings);
        ItemOwnerDto itemDto = itemMapper.toItemOwnerDto(item);

        itemDto.setComments(commentRepository.findByItemId(itemId)
                .stream()
                .map(commentMapper::toCommentDto)
                .collect(Collectors.toSet()));

        itemDto.setLastBooking(bookingDates.lastBooking());
        itemDto.setNextBooking(bookingDates.nextBooking());

        log.info("Получен Item: {}", itemDto);
        return itemDto;
    }

    @Override
    public List<ItemOwnerDto> findOwnerItems(long userId) {
        log.info("Получение списка Items пользователя Id: {}", userId);
        List<BookingDto> hisBookings = bookingService.findOwnerBookings(userId, ALL);
        BookingDates bookingDates = getBookingDates(hisBookings);

        return itemRepository.findAllByOwnerId(userId)
                .stream()
                .map(itemMapper::toItemOwnerDto)
                .peek(itemDto -> {
                    itemDto.setLastBooking(bookingDates.lastBooking());
                    itemDto.setNextBooking(bookingDates.nextBooking());
                })
                .toList();
    }

    @Override
    public List<ItemDto> findItemsByText(String text) {
        if (text.isBlank()) {
            log.info("Запрос text пустой, возвращен пустой лист");
            return Collections.emptyList();
        }

        log.info("Поиск по запросу: {}", text);
        return itemRepository.searchAvailableItemsByNameOrDescription(text.toLowerCase())
                .stream()
                .map(itemMapper::toItemDto)
                .toList();
    }

    private void checkItemOwner(long itemIdOwner, long requestUserId) {
        log.info("Проверка на владение Item пользователя");
        if (!(itemIdOwner == requestUserId)) {
            throw new IncorrectArgumentException("Неверный Id владельца Item");
        }
        log.info("Владелец подтвержден");
    }

    private BookingDates getBookingDates(List<BookingDto> bookings) {
        LocalDateTime now = LocalDateTime.now();

        LocalDateTime lastBooking = bookings.stream()
                .map(BookingDto::getEnd)
                .filter(end -> end.isBefore(now))
                .max(LocalDateTime::compareTo)
                .orElse(null);

        LocalDateTime nextBooking = bookings.stream()
                .map(BookingDto::getStart)
                .filter(start -> start.isAfter(now))
                .min(LocalDateTime::compareTo)
                .orElse(null);
        return new BookingDates(lastBooking, nextBooking);
    }

    private record BookingDates(LocalDateTime lastBooking, LocalDateTime nextBooking) {
    }
}