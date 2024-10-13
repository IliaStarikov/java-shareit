package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.RequestCreateDto;
import ru.practicum.shareit.request.dto.RequestDto;

import java.util.List;

public interface RequestService {

    RequestDto addRequest(RequestCreateDto requestDto, long userId);

    List<RequestDto> findRequestsByUserId(long userId);

    List<RequestDto> findAllRequests();

    RequestDto getRequestInfo(long requestId);
}