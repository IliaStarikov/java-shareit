package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.practicum.shareit.exception.NotFoundEntityException;
import ru.practicum.shareit.request.dto.RequestCreateDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class RequestServiceImplTest {

    @Mock
    private RequestRepository requestRepository;

    @Mock
    private UserService userService;

    @Mock
    private RequestMapper requestMapper;

    @InjectMocks
    private RequestServiceImpl requestService;

    private RequestCreateDto requestCreateDto;
    private RequestDto requestDto;
    private Request request;
    private User user;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        requestCreateDto = new RequestCreateDto();
        requestCreateDto.setDescription("Test description");

        requestDto = new RequestDto();
        requestDto.setDescription("Test description");
        requestDto.setId(1L);

        request = new Request();
        request.setId(1L);
        request.setDescription("Test description");

        user = new User();
        user.setId(1L);
        user.setName("Test User");
    }

    @Test
    void addRequest_WhenValidRequest_ReturnRequestDto() {
        // Arrange: Настройка моков
        when(requestMapper.toRequest(requestCreateDto)).thenReturn(request);
        when(userService.findById(1L)).thenReturn(user);
        when(requestRepository.save(request)).thenReturn(request);
        when(requestMapper.toRequestDto(request)).thenReturn(requestDto);

        // Act
        RequestDto createdRequestDto = requestService.addRequest(requestCreateDto, 1L);

        // Assert
        assertNotNull(createdRequestDto);
        assertEquals(requestDto.getId(), createdRequestDto.getId());
        assertEquals(requestDto.getDescription(), createdRequestDto.getDescription());
        assertEquals(requestDto.getId(), createdRequestDto.getId());

        // Verify
        verify(requestRepository).save(request);
    }

    @Test
    void find_ShouldReturnRequestListByUserId() {
        // Arrange
        when(userService.findById(1L)).thenReturn(user);
        when(requestRepository.findByRequestor_Id(1L)).thenReturn(Collections.singletonList(request));
        when(requestMapper.toRequestDto(request)).thenReturn(requestDto);

        // Act
        List<RequestDto> requests = requestService.findRequestsByUserId(1L);

        // Assert
        assertEquals(1, requests.size());
        assertEquals(requestDto.getId(), requests.getFirst().getId());
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
    }

    @Test
    void getRequestInfo_WhenNonExistingItemRequest_ThrowNotFoundEntityException() {
        // Arrange
        when(requestRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundEntityException.class, () -> {
            requestService.getRequestInfo(1L);
        });
    }
}