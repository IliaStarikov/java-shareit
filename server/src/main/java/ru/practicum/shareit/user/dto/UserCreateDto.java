package ru.practicum.shareit.user.dto;

import lombok.Data;

@Data
public class UserCreateDto {

    private long id;
    private String name;
    private String email;
}