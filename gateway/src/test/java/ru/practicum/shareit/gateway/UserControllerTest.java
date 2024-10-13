package ru.practicum.shareit.gateway;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.gateway.user.UserClient;
import ru.practicum.shareit.gateway.user.UserController;
import ru.practicum.shareit.gateway.user.dto.UserCreateDto;
import ru.practicum.shareit.gateway.user.dto.UserUpdateDto;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserClient userClient;

    @Autowired
    private ObjectMapper objectMapper;

    private UserCreateDto validUserCreateDto;
    private UserUpdateDto validUserUpdateDto;

    @BeforeEach
    void setup() {
        // Arrange
        validUserCreateDto = new UserCreateDto();
        validUserCreateDto.setName("User name");
        validUserCreateDto.setEmail("user@mail.com");

        validUserUpdateDto = new UserUpdateDto();
        validUserUpdateDto.setName("New name");
        validUserUpdateDto.setEmail("new@mail.com");
    }

    @Test
    void addUser_ValidRequest() throws Exception {
        // Arrange
        when(userClient.addUser(any(UserCreateDto.class)))
                .thenReturn(ResponseEntity.ok().body("User created"));

        // Act & Expect
        mockMvc.perform(post("/users")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validUserCreateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("User created"));
    }

    @Test
    void addUser_InvalidRequest() throws Exception {
        // Arrange
        validUserCreateDto.setName("");

        mockMvc.perform(post("/users")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validUserCreateDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addUser_InvalidEmail() throws Exception {
        // Arrange
        validUserCreateDto.setEmail("invalid-email");

        // Act & Expect
        mockMvc.perform(post("/users")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validUserCreateDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateUser_ValidRequest() throws Exception {
        // Arrange
        when(userClient.updateUser(any(UserUpdateDto.class), anyLong()))
                .thenReturn(ResponseEntity.ok().body("User updated"));

        // Act & Expect
        mockMvc.perform(patch("/users/{userId}", 1L)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validUserUpdateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("User updated"));
    }

    @Test
    void updateUser_InvalidEmail() throws Exception {
        // Arrange
        validUserUpdateDto.setEmail("invalid-email");

        // Act & Expect
        mockMvc.perform(patch("/users/{userId}", 1L)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validUserUpdateDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findUserById_ValidRequest() throws Exception {
        // Arrange
        when(userClient.findUserById(anyLong()))
                .thenReturn(ResponseEntity.ok().body("User found"));

        // Act & Expect
        mockMvc.perform(get("/users/{userId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("User found"));
    }
}
