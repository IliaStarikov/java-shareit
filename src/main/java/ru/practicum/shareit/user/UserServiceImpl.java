package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.AlreadyOccupiedEmailException;
import ru.practicum.shareit.exception.NotFoundEntityException;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto addUser(UserCreateDto request) {
        log.info("Создание нового пользователя {}", request);
        if (!emailNotBusy(request.getEmail())) {
            throw new AlreadyOccupiedEmailException("Email занят");
        }
        User user = userMapper.toUser(request);
        user = userRepository.addUser(user);
        log.info("Создан пользователь: {}", user);
        return userMapper.toUserDto(user);
    }

    @Override
    public UserDto findUser(long id) {
        log.info("Получение User c id: {}", id);
        return userRepository.findUser(id)
                .map(userMapper::toUserDto)
                .orElseThrow(() -> new NotFoundEntityException("Пользователь не найден"));
    }

    @Override
    public UserDto updateUser(long id, UserUpdateDto request) {
        log.info("Обновление пользователя: {}", request);
        User updateUser = userRepository.findUser(id)
                .map(user -> {
                    boolean updateUserEmail = request.getEmail() != null && !request.getEmail().equals(user.getEmail());
                    if (updateUserEmail && !emailNotBusy(request.getEmail())) {
                        throw new AlreadyOccupiedEmailException("Email занят, укажите другой");
                    }
                    return userMapper.updateUser(request);
                })
                .orElseThrow(() -> new NotFoundEntityException("Пользователь не найден"));
        updateUser = userRepository.updateUser(updateUser, id);
        log.info("Пользователь {} обновлен", updateUser);
        return userMapper.toUserDto(updateUser);
    }

    @Override
    public void deleteUser(long id) {
        log.info("Удаление пользователя с id: {}", id);
        userRepository.findUser(id)
                .orElseThrow(() -> new NotFoundEntityException("Пользователь не найден"));
        userRepository.deleteUser(id);
        log.info("Пользователь с id: {} удален", id);
    }

    @Override
    public List<UserDto> getUsers() {
        log.info("Получение всех пользователей");
        return userRepository.getUsers().stream()
                .map(userMapper::toUserDto)
                .toList();
    }

    @Override
    public boolean emailNotBusy(String email) {
        List<String> emails = userRepository.getEmails();
        return emails.isEmpty() || !emails.contains(email);
    }
}
