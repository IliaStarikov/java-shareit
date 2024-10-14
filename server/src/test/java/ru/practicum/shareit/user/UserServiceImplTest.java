package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.AlreadyOccupiedEmailException;
import ru.practicum.shareit.exception.NotFoundEntityException;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    private User user;
    private UserDto userDto;
    private UserCreateDto userCreateDto;
    private UserUpdateDto userUpdateDto;

    @BeforeEach
    void setup() {
        // Arrange: Создание объектов
        user = new User();
        user.setId(1L);
        user.setEmail("user@mail.com");
        user.setName("Test name");

        userDto = new UserDto();
        userDto.setId(1L);
        userDto.setEmail("user@mail.com");
        userDto.setName("Test name");

        userCreateDto = new UserCreateDto();
        userCreateDto.setEmail("user@mail.com");
        userCreateDto.setName("Test name");

        userUpdateDto = new UserUpdateDto();
        userUpdateDto.setEmail("new@mail.com");
        userUpdateDto.setName("New name");
    }

    @Test
    void addUser_WhenUserIsCreatedSuccessfully_ReturnUserDto() {
        // Arrange
        when(userRepository.findByEmail(userCreateDto.getEmail())).thenReturn(Optional.empty());
        when(userMapper.toUser(userCreateDto)).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toUserDto(user)).thenReturn(userDto);

        // Act
        UserDto result = userService.addUser(userCreateDto);

        // Assert
        assertNotNull(result);
        assertEquals(userDto, result);

        // Verify
        verify(userRepository).save(user);
        verify(userMapper).toUser(userCreateDto);
    }

    @Test
    void addUser_WhenValidRequest_ReturnUserDto() {
        // Arrange
        when(userMapper.toUser(any())).thenReturn(user);
        when(userMapper.toUserDto(any())).thenReturn(userDto);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenReturn(user);

        // Act
        UserDto createdUser = userService.addUser(userCreateDto);

        // Assert
        assertNotNull(createdUser);
        assertEquals(user.getId(), createdUser.getId());
        assertEquals(user.getEmail(), createdUser.getEmail());

        // Verify
        verify(userRepository).save(any(User.class));
    }

    @Test
    void addUser_WhenEmailAlreadyExists_ThrowAlreadyOccupiedEmailException() {
        // Arrange
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        // Act & Assert
        assertThrows(AlreadyOccupiedEmailException.class, () -> {
            userService.addUser(userCreateDto);
        });

        // Verify
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void deleteUser_WhenExistingUser_DeleteUser() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Act
        userService.deleteUser(1L);

        // Verify
        verify(userRepository).deleteById(1L);
    }

    @Test
    void deleteUser_WhenNonExistingUser_ThrowNotFoundEntityException() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundEntityException.class, () -> {
            userService.deleteUser(1L);
        });
    }

    @Test
    void updateUser_WhenExistingUser_ReturnUpdatedUserDto() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toUserDto(any())).thenReturn(userDto);

        // Act
        UserDto updatedUser = userService.updateUser(1L, userUpdateDto);

        // Assert
        assertNotNull(updatedUser);
        assertEquals(user.getId(), updatedUser.getId());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void updateUser_WhenNonExistingUser_ThrowNotFoundEntityException() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundEntityException.class, () -> {
            userService.updateUser(1L, userUpdateDto);
        });
    }

    @Test
    void updateUser_WhenEmailAlreadyExists_ThrowAlreadyOccupiedEmailException() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findByEmail("another@mail.com")).thenReturn(Optional.of(user));

        userUpdateDto.setEmail("another@mail.com");

        // Act & Assert
        assertThrows(AlreadyOccupiedEmailException.class, () -> {
            userService.updateUser(1L, userUpdateDto);
        });
    }

    @Test
    void findUser_WhenExistingUser_ReturnUserDto() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toUserDto(any())).thenReturn(userDto);

        // Act
        UserDto foundUser = userService.findUser(1L);

        // Assert
        assertNotNull(foundUser);
        assertEquals(user.getId(), foundUser.getId());
    }

    @Test
    void findUser_WhenNonExistingUser_ThrowNotFoundEntityException() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundEntityException.class, () -> {
            userService.findUser(1L);
        });
    }
}