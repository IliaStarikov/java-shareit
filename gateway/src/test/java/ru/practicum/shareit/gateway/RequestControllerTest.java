package ru.practicum.shareit.gateway;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.gateway.request.RequestClient;
import ru.practicum.shareit.gateway.request.RequestController;
import ru.practicum.shareit.gateway.request.RequestCreateDto;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.gateway.util.Header.X_SHARER_USER_ID;

@WebMvcTest(controllers = RequestController.class)
class RequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RequestClient requestClient;

    @Autowired
    private ObjectMapper objectMapper;

    private RequestCreateDto validRequestCreateDto;

    @BeforeEach
    void setup() {
        // Arrange
        validRequestCreateDto = new RequestCreateDto();
        validRequestCreateDto.setDescription("Request for an item");
    }

    @Test
    void createItemRequest_ValidRequest() throws Exception {
        // Arrange
        when(requestClient.addRequest(any(RequestCreateDto.class), anyLong()))
                .thenReturn(ResponseEntity.ok().body("Request created"));

        // Act & Expect
        mockMvc.perform(post("/requests")
                        .header(X_SHARER_USER_ID, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequestCreateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Request created"));
    }

    @Test
    void getAllRequests_ValidRequest() throws Exception {
        // Arrange
        when(requestClient.getAllRequests())
                .thenReturn(ResponseEntity.ok().body("All item requests"));

        // Act & Expect
        mockMvc.perform(get("/requests/all")
                        .header(X_SHARER_USER_ID, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("All item requests"));
    }
}