package ru.practicum.shareit.request.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValueMappingStrategy;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.RequestCreateDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.Request;
import ru.practicum.shareit.user.User;

@Mapper(componentModel = "spring", nullValueMapMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
        uses = ItemMapper.class)
public interface RequestMapper {

    Request toRequest(RequestCreateDto request);

    @Mapping(target = "items", source = "items", qualifiedByName = "toRequestDto")
    RequestDto toRequestDto(Request request);

    @Named("toRequestDto")
    @Mapping(target = "ownerId", expression = "java(getOwnerId(item.getOwner()))")
    ItemRequestDto toItemRequestDto(Item item);

    default long getOwnerId(User user) {
        return user.getId();
    }
}