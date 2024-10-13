package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.*;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.util.Header.X_SHARER_USER_ID;

@WebMvcTest(ItemController.class)
public class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemService itemService;

    private ItemDto itemDto;
    private ItemOwnerDto itemOwnerDto;
    private ItemCreateDto itemCreateDto;
    private ItemUpdateDto itemUpdateDto;
    private CommentDto commentDto;

    @BeforeEach
    void setup() {
        // Arrange
        itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("item");
        itemDto.setDescription("description");
        itemDto.setAvailable(true);

        commentDto = new CommentDto();
        commentDto.setId(1L);
        commentDto.setText("good item");
        commentDto.setAuthorName("user");

        itemCreateDto = new ItemCreateDto();
        itemCreateDto.setName("New itemName");
        itemCreateDto.setDescription("description");
        itemCreateDto.setAvailable(true);
        itemCreateDto.setRequestId(1L);

        itemUpdateDto = new ItemUpdateDto();
        itemUpdateDto.setName("UpdateName");
        itemUpdateDto.setDescription("UpdateDescription");
        itemUpdateDto.setAvailable(true);

        itemOwnerDto = new ItemOwnerDto();
        itemOwnerDto.setId(1L);
        itemOwnerDto.setName("item2");
        itemOwnerDto.setDescription("description");
        itemOwnerDto.setAvailable(true);
    }

    @Test
    void addItem_ReturnCreatedItem() throws Exception {
        // Arrange
        when(itemService.addItem(any(ItemCreateDto.class), anyLong())).thenReturn(itemDto);

        // Act & Expect
        mockMvc.perform(post("/items")
                        .header(X_SHARER_USER_ID, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemCreateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemDto.getId()))
                .andExpect(jsonPath("$.name").value(itemDto.getName()))
                .andExpect(jsonPath("$.description").value(itemDto.getDescription()));
    }

    @Test
    void updateItem_ReturnUpdatedItem() throws Exception {
        // Arrange
        when(itemService.updateItem(any(ItemUpdateDto.class), anyLong(), anyLong())).thenReturn(itemDto);

        // Act & Expect
        mockMvc.perform(patch("/items/{itemId}", 1L)
                        .header(X_SHARER_USER_ID, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemUpdateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemDto.getId()))
                .andExpect(jsonPath("$.name").value(itemDto.getName()))
                .andExpect(jsonPath("$.description").value(itemDto.getDescription()));
    }

    @Test
    void findItem_ReturnItemWithOwnerDetails() throws Exception {
        // Arrange
        when(itemService.findItem(anyLong(), anyLong())).thenReturn(itemOwnerDto);

        // Act & Expect
        mockMvc.perform(get("/items/{itemId}", 1L)
                        .header(X_SHARER_USER_ID, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemOwnerDto.getId()))
                .andExpect(jsonPath("$.name").value(itemOwnerDto.getName()))
                .andExpect(jsonPath("$.description").value(itemOwnerDto.getDescription()));
    }

    @Test
    void addComment_ReturnCreatedComment() throws Exception {
        // Arrange
        when(itemService.addComment(any(CommentCreateDto.class), anyLong(), anyLong())).thenReturn(commentDto);

        CommentCreateDto newCommentDto = new CommentCreateDto();
        newCommentDto.setText("item good");

        // Act & Expect
        mockMvc.perform(post("/items/{itemId}/comment", 1L)
                        .header(X_SHARER_USER_ID, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCommentDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(commentDto.getId()))
                .andExpect(jsonPath("$.text").value(commentDto.getText()))
                .andExpect(jsonPath("$.authorName").value(commentDto.getAuthorName()));
    }

    @Test
    void getOwnersItems_ReturnListOfItems() throws Exception {
        // Arrange
        when(itemService.findOwnerItems(anyLong())).thenReturn(List.of(itemOwnerDto));

        // Act & Expect
        mockMvc.perform(get("/items")
                        .header(X_SHARER_USER_ID, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(itemOwnerDto.getId()))
                .andExpect(jsonPath("$[0].name").value(itemOwnerDto.getName()))
                .andExpect(jsonPath("$[0].description").value(itemOwnerDto.getDescription()));
    }

    @Test
    void searchItemsByText_ReturnItems() throws Exception {
        // Arrange
        when(itemService.findItemsByText(any(String.class))).thenReturn(List.of(itemDto));

        // Act & Expect
        mockMvc.perform(get("/items/search")
                        .param("text", "item"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(itemDto.getId()))
                .andExpect(jsonPath("$[0].name").value(itemDto.getName()))
                .andExpect(jsonPath("$[0].description").value(itemDto.getDescription()));
    }
}