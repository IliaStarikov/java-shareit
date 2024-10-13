package ru.practicum.shareit.gateway.item;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.gateway.client.BaseClient;
import ru.practicum.shareit.gateway.item.dto.CommentCreateDto;
import ru.practicum.shareit.gateway.item.dto.ItemCreateDto;
import ru.practicum.shareit.gateway.item.dto.ItemUpdateDto;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {

    private static final String API_PREFIX = "/items";

    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> addItem(ItemCreateDto requestDto, long userId) {
        return post("", userId, requestDto);
    }

    public ResponseEntity<Object> addComment(CommentCreateDto requestDto,
                                             long itemId, long userId) {
        return post("/" + itemId + "/comment", userId, requestDto);
    }

    public ResponseEntity<Object> updateItem(ItemUpdateDto requestDto,
                                             long itemId, long itemOwnerId) {
        return patch("/" + itemId, itemOwnerId, requestDto);
    }

    public ResponseEntity<Object> findItem(long itemId, long ownerItemId) {
        return get("/" + itemId, ownerItemId);
    }

    public ResponseEntity<Object> getOwnersItems(long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> findItemsByText(String text) {
        Map<String, Object> params = Map.of("text", text);

        return get("/search", null, params);
    }
}