package ru.practicum.shareit.gateway.item.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ItemUpdateDto {

    private long id;

    @Size(max = 255)
    private String name;

    @Size(max = 1000)
    private String description;

    private Boolean available;
}