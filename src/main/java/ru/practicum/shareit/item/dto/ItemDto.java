package ru.practicum.shareit.item.dto;

import lombok.Data;

@Data
public class ItemDto {

    private int id;
    private String name;
    private String description;
    private Boolean available;
}