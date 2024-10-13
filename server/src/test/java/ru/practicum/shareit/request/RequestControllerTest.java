package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.RequestCreateDto;
import ru.practicum.shareit.request.dto.RequestDto;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.practicum.shareit.util.Header.X_SHARER_USER_ID;

@WebMvcTest(RequestController.class)
class RequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RequestService requestService;

    @Autowired
    private ObjectMapper objectMapper;

    private RequestCreateDto requestCreateDto;
    private RequestDto requestDto;

    @BeforeEach
    void setup() {
        // Arrange
        requestDto = new RequestDto();
        requestDto.setId(1L);
        requestDto.setDescription("Test description");

        requestCreateDto = new RequestCreateDto();
        requestCreateDto.setDescription("Test description");
    }

    @Test
    void addRequest_ReturnCreatedRequestDto() throws Exception {
        // Arrange
        when(requestService.addRequest(any(RequestCreateDto.class), anyLong())).thenReturn(requestDto);

        // Act & Assert
        mockMvc.perform(post("/requests")
                        .header(X_SHARER_USER_ID, 1L)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestCreateDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(requestDto)));
    }

    @Test
    void getOwnItemRequests_ReturnListOfRequestDto() throws Exception {
        // Arrange
        List<RequestDto> requests = List.of(requestDto);
        when(requestService.findRequestsByUserId(anyLong())).thenReturn(requests);

        // Act & Assert
        mockMvc.perform(get("/requests")
                        .header(X_SHARER_USER_ID, 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(requests)));
    }

    @Test
    void getAllRequests_ReturnListOfRequestDto() throws Exception {
        // Arrange
        List<RequestDto> requests = List.of(requestDto);
        when(requestService.findAllRequests()).thenReturn(requests);

        // Act & Assert
        mockMvc.perform(get("/requests/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(requests)));
    }

    @Test
    void getRequestInfo_ReturnRequestDto() throws Exception {
        // Arrange
        when(requestService.getRequestInfo(1L)).thenReturn(requestDto);

        // Act & Assert
        mockMvc.perform(get("/requests/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(requestDto)));
    }
}