package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.AlreadyOccupiedEmailException;
import ru.practicum.shareit.exception.NotFoundEntityException;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserDto addUser(UserCreateDto request) {
        log.info("Создание нового пользователя {}", request);
        if (findByEmail(request.getEmail()).isPresent()) {
            throw new AlreadyOccupiedEmailException("Email занят");
        }
        User user = userMapper.toUser(request);

        user = userRepository.save(user);
        log.info("Создан пользователь: {}", user);
        return userMapper.toUserDto(user);
    }

    @Override
    public UserDto findUser(long userId) {
        log.info("Получение User c id: {}", userId);
        return userRepository.findById(userId)
                .map(userMapper::toUserDto)
                .orElseThrow(() -> new NotFoundEntityException("Пользователь не найден"));
    }

    @Override
    @Transactional
    public UserDto updateUser(long userId, UserUpdateDto request) {
        log.info("Обновление пользователя: {}", request);
        User updateUser = userRepository.findById(userId)
                .map(user -> {
                    boolean isEmailUpdateRequired = request.getEmail() != null &&
                            !request.getEmail().equals(user.getEmail());
                    if (isEmailUpdateRequired && findByEmail(request.getEmail()).isPresent()) {
                        throw new AlreadyOccupiedEmailException("Email занят, укажите другой");
                    }
                    userMapper.updateUser(request, user);
                    return user;
                })
                .orElseThrow(() -> new NotFoundEntityException("Пользователь не найден"));
        updateUser.setId(userId);

        updateUser = userRepository.save(updateUser);
        log.info("Пользователь {} обновлен", updateUser);
        return userMapper.toUserDto(updateUser);
    }

    @Override
    @Transactional
    public void deleteUser(long userId) {
        log.info("Удаление пользователя с id: {}", userId);
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundEntityException("Пользователь не найден"));

        userRepository.deleteById(userId);
        log.info("Пользователь с id: {} удален", userId);
    }

    private Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}