package ru.practicum.shareit.item.dto;

import lombok.Data;

@Data
public class ItemUpdateDto {

    private long id;
    private String name;
    private String description;
    private Boolean available;
}