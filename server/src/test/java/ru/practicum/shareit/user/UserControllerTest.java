package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;
import ru.practicum.shareit.user.dto.UserDto;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private UserDto userDto;
    private UserCreateDto newUserCreateDto;
    private UserUpdateDto userUpdateDto;

    @BeforeEach
    void setup() {
        // Arrange: Создание объектов
        userDto = new UserDto();
        userDto.setId(1L);
        userDto.setEmail("user@mail.com");
        userDto.setName("Test user");

        newUserCreateDto = new UserCreateDto();
        newUserCreateDto.setEmail("newuser@mail.com");
        newUserCreateDto.setName("Test newUser");

        userUpdateDto = new UserUpdateDto();
        userUpdateDto.setEmail("updated@mail.com");
        userUpdateDto.setName(null);
    }

    @Test
    void findUserById_WhenExistingUser_ReturnUserDto() throws Exception {
        // Arrange
        doReturn(userDto).when(userService).findUser(1L);

        // Act & Expect
        mockMvc.perform(get("/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userDto)));
    }

    @Test
    void addUser_WhenValidRequest_ReturnUserDto() throws Exception {
        // Arrange
        doReturn(userDto).when(userService).addUser(any(UserCreateDto.class));

        // Act & Expect
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUserCreateDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userDto)));
    }

    @Test
    void updateUser_WhenExistingUser_ReturnUpdatedUserDto() throws Exception {
        // Arrange
        doReturn(userDto).when(userService).updateUser(any(Long.class), any(UserUpdateDto.class));

        // Act & Expect
        mockMvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userUpdateDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userDto)));
    }

    @Test
    void deleteUser_WhenExistingUser_ReturnNoContent() throws Exception {
        // Act & Expect
        mockMvc.perform(delete("/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Verify
        verify(userService).deleteUser(1L);
    }
}