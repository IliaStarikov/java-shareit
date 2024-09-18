package ru.practicum.shareit.item.mapper;

import org.mapstruct.MappingConstants;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.model.Item;
import org.mapstruct.Mapper;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ItemMapper {
    Item toItem(ItemCreateDto requestDto);

    ItemDto toItemDto(Item item);

    Item updateItem(ItemUpdateDto requestDto);
}
