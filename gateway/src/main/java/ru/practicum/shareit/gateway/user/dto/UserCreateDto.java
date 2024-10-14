package ru.practicum.shareit.gateway.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserCreateDto {

    private long id;

    @NotBlank
    private String name;

    @NotBlank
    @Email
    @Size(max = 255)
    private String email;
}