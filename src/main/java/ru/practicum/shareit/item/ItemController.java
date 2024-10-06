package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.*;

import java.util.List;

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

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@Valid @RequestBody CommentCreateDto request,
                                 @PathVariable long itemId,
                                 @RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.addComment(request, itemId, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@Valid @RequestBody ItemUpdateDto requestDto,
                              @PathVariable long itemId,
                              @RequestHeader("X-Sharer-User-Id") long ownerId) {
        return itemService.updateItem(requestDto, ownerId, itemId);
    }

    @GetMapping("/{itemId}")
    public ItemOwnerDto getItem(@PathVariable long itemId,
                                 @RequestHeader("X-Sharer-User-Id") long ownerId) {
        return itemService.findItem(itemId, ownerId);
    }

    @GetMapping
    public List<ItemOwnerDto> getOwnersItems(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.findOwnerItems(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> getItemsByText(@RequestParam(name = "text") String text) {
        return itemService.findItemsByText(text);
    }
}