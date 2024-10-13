package ru.practicum.shareit.request;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.exception.NotFoundEntityException;
import ru.practicum.shareit.request.dto.RequestCreateDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureTestDatabase
public class RequestServiceImplIntegrationTest {

    @Autowired
    private RequestServiceImpl requestService;

    @MockBean
    private RequestRepository requestRepository;

    @MockBean
    private UserService userService;

    @Mock
    private RequestMapper requestMapper;

    private User user;
    private Request request;
    private RequestDto requestDto;

    @BeforeEach
    void setup() {
        user = new User();
        user.setId(1L);
        user.setName("Test User");

        RequestCreateDto newRequestDto = new RequestCreateDto();
        newRequestDto.setDescription("Test description");

        request = new Request();
        request.setId(1L);
        request.setDescription("Test description");
        request.setRequestor(user);
        request.setCreated(LocalDateTime.now());

        requestDto = new RequestDto();
        requestDto.setId(request.getId());
        requestDto.setDescription(request.getDescription());
    }

    @Test
    void findRequestsByUserId_ShouldReturnRequestListByUserId() {
        // Arrange: Настройка моков
        when(userService.findById(1L)).thenReturn(user);
        when(requestRepository.findByRequestor_Id(user.getId())).thenReturn(Collections.singletonList(request));
        when(requestMapper.toRequestDto(request)).thenReturn(requestDto);

        // Act
        List<RequestDto> requests = requestService.findRequestsByUserId(user.getId());

        // Assert: Проверка возвращаемого результата
        assertEquals(1, requests.size());
        assertEquals(requestDto.getId(), requests.getFirst().getId());
        assertEquals(requestDto.getDescription(), requests.getFirst().getDescription());
    }

    @Test
    void findAllRequests_ShouldReturnAllRequests() {
        // Arrange
        when(requestRepository.findAllOrderByDate()).thenReturn(Collections.singletonList(request));
        when(requestMapper.toRequestDto(request)).thenReturn(requestDto);

        // Act
        List<RequestDto> allRequests = requestService.findAllRequests();

        // Assert
        assertEquals(1, allRequests.size());
        assertEquals(requestDto.getId(), allRequests.getFirst().getId());
        assertEquals(requestDto.getDescription(), allRequests.getFirst().getDescription());
    }

    @Test
    void getRequestInfo_WhenExistingRequest_ReturnRequestDto() {
        // Arrange
        when(requestRepository.findById(1L)).thenReturn(Optional.of(request));
        when(requestMapper.toRequestDto(request)).thenReturn(requestDto);

        // Act
        RequestDto foundItemRequestDto = requestService.getRequestInfo(1L);

        // Assert
        assertNotNull(foundItemRequestDto);
        assertEquals(requestDto.getId(), foundItemRequestDto.getId());
        assertEquals(requestDto.getDescription(), foundItemRequestDto.getDescription());
    }

    @Test
    void getRequestInfo_WhenNonExistingRequest_ThrowNotFoundEntityException() {
        // Arrange
        when(requestRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundEntityException.class, () -> {
            requestService.getRequestInfo(1L);
        });
    }
}