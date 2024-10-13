package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundEntityException;
import ru.practicum.shareit.request.dto.RequestCreateDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final UserService userService;
    private final RequestMapper requestMapper;

    @Override
    @Transactional
    public RequestDto addRequest(RequestCreateDto requestDto, long userId) {
        log.info("Создать новый запрос для Item с userId: {}", userId);
        Request request = requestMapper.toRequest(requestDto);

        User requester = userService.findById(userId);

        request.setRequestor(requester);
        request.setCreated(LocalDateTime.now());

        request = requestRepository.save(request);

        log.info("Запрос создан: {}", request);
        return requestMapper.toRequestDto(request);
    }

    @Override
    public List<RequestDto> findRequestsByUserId(long userId) {
        log.info("Получение всех запросов для Item, пользователя с id: {}", userId);
        return requestRepository.findByRequestor_Id(userId)
                .stream()
                .map(requestMapper::toRequestDto).toList();
    }

    @Override
    public List<RequestDto> findAllRequests() {
        log.info("Получение всех запросов для Item");
        return requestRepository.findAllOrderByDate()
                .stream()
                .map(requestMapper::toRequestDto).toList();
    }

    @Override
    public RequestDto getRequestInfo(long requestId) {
        log.info("Получение информации о запросе на Item, c requestId: {}", requestId);
        return requestRepository.findById(requestId)
                .map(requestMapper::toRequestDto)
                .orElseThrow(() -> new NotFoundEntityException("Запрос для Item не найден"));
    }
}
