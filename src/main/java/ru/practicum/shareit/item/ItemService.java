package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.*;

import java.util.List;

public interface ItemService {

    ItemDto addItem(ItemCreateDto request, long userId);

    ItemDto updateItem(ItemUpdateDto request, long userId, long itemId);

    ItemOwnerDto findItem(long userId, long ownerId);

    List<ItemOwnerDto> findOwnerItems(long userId);

    List<ItemDto> findItemsByText(String text);

    CommentDto addComment(CommentCreateDto request, long itemId, long userId);
}