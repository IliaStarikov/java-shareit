package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserDto addUser(@Valid @RequestBody UserCreateDto request) {
        return userService.addUser(request);
    }

    @GetMapping("/{id}")
    public UserDto findUser(@PathVariable long id) {
        return userService.findUser(id);
    }

    @PatchMapping("/{id}")
    public UserDto updateUser(@PathVariable long id, @Valid @RequestBody UserUpdateDto request) {
        return userService.updateUser(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable long id) {
        userService.deleteUser(id);
    }

    @GetMapping
    public List<UserDto> getUsers() {
        return userService.getUsers();
    }
}
