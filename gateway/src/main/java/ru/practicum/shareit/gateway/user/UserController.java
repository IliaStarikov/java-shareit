package ru.practicum.shareit.gateway.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.gateway.user.dto.UserCreateDto;
import ru.practicum.shareit.gateway.user.dto.UserUpdateDto;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserClient userClient;

    @GetMapping("/{userId}")
    public ResponseEntity<Object> findUserById(@PathVariable long userId) {
        return userClient.findUserById(userId);
    }

    @PostMapping
    public ResponseEntity<Object> addUser(@Valid @RequestBody UserCreateDto request) {
        return userClient.addUser(request);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@Valid @RequestBody UserUpdateDto request,
                                             @PathVariable long userId) {
        return userClient.updateUser(request, userId);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable long userId) {
        return userClient.deleteUser(userId);
    }
}