package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import java.util.List;

public interface ItemService {
    ItemDto addItem(ItemCreateDto requestDto, long userId);

    ItemDto findItem(long itemId);

    ItemDto updateItem(ItemUpdateDto requestDto, long userId, long itemId);

    List<ItemDto> getOwnerItems(long userId);

    List<ItemDto> getAvailableItemsByText(String text);
}