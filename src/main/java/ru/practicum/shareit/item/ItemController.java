package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ItemDto addItem(@Valid @RequestBody ItemCreateDto requestDto,
                              @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.addItem(requestDto, userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable long itemId) {
        return itemService.findItem(itemId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@Valid @RequestBody ItemUpdateDto requestDto,
                              @PathVariable long itemId,
                              @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.updateItem(requestDto, userId, itemId);
    }

    @GetMapping
    public List<ItemDto> getOwnersItems(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.getOwnerItems(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> getAvailableItemsByText(@RequestParam(name = "text") String text) {
        return itemService.getAvailableItemsByText(text);
    }
}