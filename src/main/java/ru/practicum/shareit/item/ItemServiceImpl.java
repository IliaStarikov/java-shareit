package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.IncorrectArgumentException;
import ru.practicum.shareit.exception.NotFoundEntityException;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemMapper itemMapper;

    @Override
    public ItemDto addItem(ItemCreateDto requestDto, long userId) {
        log.info("Создание нового Item, OwnerId: {}", userId);
        checkUserExist(userId);
        Item item = itemMapper.toItem(requestDto);
        item.setOwnerId(userId);
        item = itemRepository.createItem(item);
        log.info("Item с ID: {} создан", item);
        return itemMapper.toItemDto(item);
    }

    @Override
    public ItemDto findItem(long itemId) {
        log.info("Получение информации Item c Id: {}", itemId);
        return itemRepository.findItemById(itemId)
                .map(itemMapper::toItemDto)
                .orElseThrow(() -> new NotFoundEntityException("Item не найден"));
    }

    @Override
    public ItemDto updateItem(ItemUpdateDto requestDto, long userId, long itemId) {
        log.info("Обновление Item c Id: {}, Owner Id: {}", itemId, userId);
        checkUserExist(userId);
        Item updateItem = itemRepository.findItemById(itemId)
                .orElseThrow(() -> new NotFoundEntityException("Item не найден"));
        checkItemOwner(userId, updateItem.getOwnerId());
        updateItem = itemMapper.updateItem(requestDto);
        log.info("Item - {} обновлен", updateItem);
        return itemMapper.toItemDto(updateItem);
    }

    @Override
    public List<ItemDto> getOwnerItems(long userId) {
        log.info("Получение списка Items пользователя Id: {}", userId);
        return itemRepository.getOwnerItems(userId)
                .stream()
                .map(itemMapper::toItemDto)
                .toList();
    }

    @Override
    public List<ItemDto> getAvailableItemsByText(String text) {
        if (text.isBlank()) {
            log.info("Запрос text пустой, возвращен пустой лист");
            return Collections.emptyList();
        }
        log.info("Поиск по запросу: {}", text);
        return itemRepository.searchItemByText(text.toLowerCase())
                .stream()
                .map(itemMapper::toItemDto)
                .toList();
    }

    private void checkUserExist(long userId) {
        log.info("Проверка пользователя {} на существование", userId);
        userRepository.findUser(userId)
                .orElseThrow(() -> new NotFoundEntityException("Пользователь не найден"));
    }

    private void checkItemOwner(long userId, long itemUserId) {
        log.info("Проверка на владение Item пользователя с Id: {}", itemUserId);
        if (userId != itemUserId) {
            throw new IncorrectArgumentException("Неверный Id владельца Item");
        }
    }
}