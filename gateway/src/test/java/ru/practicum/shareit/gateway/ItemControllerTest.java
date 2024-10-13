package ru.practicum.shareit.gateway;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.gateway.item.ItemClient;
import ru.practicum.shareit.gateway.item.ItemController;
import ru.practicum.shareit.gateway.item.dto.ItemCreateDto;
import ru.practicum.shareit.gateway.item.dto.ItemUpdateDto;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.gateway.util.Header.X_SHARER_USER_ID;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemClient itemClient;

    @Autowired
    private ObjectMapper objectMapper;

    private ItemCreateDto validItemCreateDto;
    private ItemUpdateDto validItemUpdateDto;

    @BeforeEach
    void setup() {
        // Arrange: Создание объектов
        validItemCreateDto = new ItemCreateDto();
        validItemCreateDto.setName("Test name");
        validItemCreateDto.setDescription("description");
        validItemCreateDto.setAvailable(true);
        validItemCreateDto.setRequestId(1L);

        validItemUpdateDto = new ItemUpdateDto();
        validItemUpdateDto.setName("New name");
        validItemUpdateDto.setDescription("New description");
        validItemUpdateDto.setAvailable(false);
    }

    @Test
    void addItem_ValidRequest() throws Exception {
        // Arrange
        when(itemClient.addItem(any(ItemCreateDto.class), anyLong()))
                .thenReturn(ResponseEntity.ok().body("Item created"));

        // Act & Expect
        mockMvc.perform(post("/items")
                        .header(X_SHARER_USER_ID, 1L)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validItemCreateDto)))
                .andExpect(status().isOk());
    }

    @Test
    void addItem_InvalidRequest() throws Exception {
        // Arrange
        validItemCreateDto.setName("");

        // Act & Expect
        mockMvc.perform(post("/items")
                        .header(X_SHARER_USER_ID, 1L)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validItemCreateDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateItem_ValidRequest() throws Exception {
        // Arrange
        when(itemClient.updateItem(any(ItemUpdateDto.class), anyLong(), anyLong()))
                .thenReturn(ResponseEntity.ok().body("Item updated"));

        // Act & Expect
        mockMvc.perform(patch("/items/{itemId}", 1L)
                        .header(X_SHARER_USER_ID, 1L)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validItemUpdateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Item updated"));
    }

    @Test
    void updateItem_InvalidRequest() throws Exception {
        // Arrange
        validItemUpdateDto.setDescription("A".repeat(2001));

        // Act & Expect
        mockMvc.perform(patch("/items/{itemId}", 1L)
                        .header(X_SHARER_USER_ID, 1L)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validItemUpdateDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getItem_ValidRequest() throws Exception {
        // Arrange
        when(itemClient.findItem(anyLong(), anyLong()))
                .thenReturn(ResponseEntity.ok().body("Item details"));

        // Act & Expect
        mockMvc.perform(get("/items/{itemId}", 1L)
                        .header(X_SHARER_USER_ID, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Item details"));
    }
}