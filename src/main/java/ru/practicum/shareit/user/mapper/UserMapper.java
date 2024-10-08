package ru.practicum.shareit.user.mapper;

import org.mapstruct.*;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

@Mapper(componentModel = "spring", nullValueMapMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface UserMapper {

    User toUser(UserCreateDto request);

    UserDto toUserDto(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUser(UserUpdateDto request, @MappingTarget User user);
}