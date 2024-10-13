package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserMapperTest {

    private UserMapper userMapper;

    @BeforeEach
    void setup() {
        // Arrange
        userMapper = Mappers.getMapper(UserMapper.class);
    }

    @Test
    void requestToUser_ShouldMapUserCreateDtoToUser() {
        // Arrange
        UserCreateDto userCreateDto = new UserCreateDto();
        userCreateDto.setEmail("test@mail.com");
        userCreateDto.setName("Test name");

        // Act
        User user = userMapper.toUser(userCreateDto);

        // Assert
        assertEquals("test@mail.com", user.getEmail());
        assertEquals("Test name", user.getName());
    }

    @Test
    void toUserDto_ShouldMapUserToUserDto() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setEmail("test@mail.com");
        user.setName("Test name");

        // Act
        UserDto userDto = userMapper.toUserDto(user);

        // Assert
        assertEquals(1L, userDto.getId());
        assertEquals("test@mail.com", userDto.getEmail());
        assertEquals("Test name", userDto.getName());
    }

    @Test
    void updateUserRequest_ShouldUpdateUserFromUserUpdateDto() {
        // Arrange
        UserUpdateDto userUpdateDto = new UserUpdateDto();
        userUpdateDto.setEmail("new@mail.com");

        User user = new User();
        user.setEmail("old@mail.com");
        user.setName("Test name");

        // Act
        userMapper.updateUser(userUpdateDto, user);

        // Assert
        assertEquals("new@mail.com", user.getEmail());
        assertEquals("Test name", user.getName());
    }

    @Test
    void updateUserRequest_ShouldIgnoreNullValues() {
        // Arrange
        UserUpdateDto userUpdateDto = new UserUpdateDto();
        userUpdateDto.setEmail(null);
        userUpdateDto.setName("New name");

        User user = new User();
        user.setEmail("old@mail.com");
        user.setName("Old name");

        // Act
        userMapper.updateUser(userUpdateDto, user);

        // Assert
        assertEquals("old@mail.com", user.getEmail());
        assertEquals("New name", user.getName());
    }
}