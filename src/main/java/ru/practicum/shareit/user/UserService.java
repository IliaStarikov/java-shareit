package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

public interface UserService {

    UserDto addUser(UserCreateDto request);

    UserDto findUser(long id);

    UserDto updateUser(long id, UserUpdateDto request);

    void deleteUser(long id);
}