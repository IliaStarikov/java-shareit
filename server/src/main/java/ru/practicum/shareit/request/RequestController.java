package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestCreateDto;
import ru.practicum.shareit.request.dto.RequestDto;

import java.util.List;

import static ru.practicum.shareit.util.Header.X_SHARER_USER_ID;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class RequestController {

    private final RequestService requestService;

    @PostMapping
    public RequestDto addRequest(@RequestBody RequestCreateDto request,
                                 @RequestHeader(X_SHARER_USER_ID) long userId) {
        return requestService.addRequest(request, userId);
    }

    @GetMapping
    public List<RequestDto> getRequestsByUserId(@RequestHeader(X_SHARER_USER_ID) long userId) {
        return requestService.findRequestsByUserId(userId);
    }

    @GetMapping("/all")
    public List<RequestDto> getAllRequests() {
        return requestService.findAllRequests();
    }

    @GetMapping("/{requestId}")
    public RequestDto getRequestInfo(@PathVariable long requestId) {
        return requestService.getRequestInfo(requestId);
    }
}