package ru.practicum.shareit.gateway.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static ru.practicum.shareit.gateway.util.Header.X_SHARER_USER_ID;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
public class RequestController {

    private final RequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> addRequest(@RequestBody RequestCreateDto request,
                                             @RequestHeader(X_SHARER_USER_ID) long userId) {
        return requestClient.addRequest(request, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getRequestsByUserId(@RequestHeader(X_SHARER_USER_ID) long userId) {
        return requestClient.getRequestsByUserId(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequests() {
        return requestClient.getAllRequests();
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestInfo(@PathVariable long requestId) {
        return requestClient.getRequestInfo(requestId);
    }
}