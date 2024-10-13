package ru.practicum.shareit.exception;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestControllerTest {

    @GetMapping("/not-found")
    public void throwNotFound() {
        throw new NotFoundEntityException("Test entity not found");
    }

    @PostMapping("/email-exists")
    public void throwAlreadyOccupiedEmailExceptions() {
        throw new AlreadyOccupiedEmailException("Test email already exists");
    }

    @PostMapping("/not-enough-rights")
    public void throwNotEnoughRights() {
        throw new NotEnoughOwnershipRightsException("User does not have enough rights");
    }
}