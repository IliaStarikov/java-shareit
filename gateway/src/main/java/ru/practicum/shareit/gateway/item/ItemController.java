package ru.practicum.shareit.gateway.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.gateway.item.dto.CommentCreateDto;
import ru.practicum.shareit.gateway.item.dto.ItemCreateDto;
import ru.practicum.shareit.gateway.item.dto.ItemUpdateDto;

import java.util.ArrayList;

import static ru.practicum.shareit.gateway.util.Header.X_SHARER_USER_ID;

@Controller
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
public class ItemController {

    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> addItem(@Valid @RequestBody ItemCreateDto requestDto,
                                          @RequestHeader(X_SHARER_USER_ID) long userId) {
        return itemClient.addItem(requestDto, userId);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@Valid @RequestBody CommentCreateDto request,
                                             @PathVariable long itemId,
                                             @RequestHeader(X_SHARER_USER_ID) long userId) {
        return itemClient.addComment(request, itemId, userId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@Valid @RequestBody ItemUpdateDto requestDto,
                                             @PathVariable long itemId,
                                             @RequestHeader(X_SHARER_USER_ID) long itemOwnerId) {
        return itemClient.updateItem(requestDto, itemOwnerId, itemId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@PathVariable long itemId,
                                          @RequestHeader(X_SHARER_USER_ID) long ownerItemId) {
        return itemClient.findItem(itemId, ownerItemId);
    }

    @GetMapping
    public ResponseEntity<Object> getOwnersItems(@RequestHeader(X_SHARER_USER_ID) long userId) {
        return itemClient.getOwnersItems(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> getItemsByText(@RequestParam(name = "text") String text) {
        if (text.isBlank()) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        }
        return itemClient.findItemsByText(text);
    }
}