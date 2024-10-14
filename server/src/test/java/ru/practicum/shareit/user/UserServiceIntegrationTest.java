package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserCreateDto;
import ru.practicum.shareit.user.dto.UserUpdateDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserServiceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;

    @BeforeEach
    void setup() {
        // Arrange
        user = new User();
        user.setEmail("user@mail.com");
        user.setName("User");
        userRepository.save(user);
    }

    @Test
    void addUser_WhenRequestIsValid_ReturnUserDto() throws Exception {
        // Arrange
        UserCreateDto userCreateDto = new UserCreateDto();
        userCreateDto.setName("User name");
        userCreateDto.setEmail("other@mail.com");

        // Act & Expect
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreateDto)))
                .andExpect(status().isOk());

        // Assert
        List<User> users = userRepository.findAll();
        assertThat(users).hasSize(2);
    }

    @Test
    void updateUser_WhenValidRequest_ReturnUpdatedUserDto() throws Exception {
        // Arrange
        UserUpdateDto userUpdateDto = new UserUpdateDto();
        userUpdateDto.setEmail("updated@mail.com");
        userUpdateDto.setName(null);

        // Act & Expect
        mockMvc.perform(patch("/users/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userUpdateDto)))
                .andExpect(status().isOk());

        User updatedUser = userRepository.findById(user.getId()).orElseThrow();

        // Assert
        assertThat(updatedUser.getEmail()).isEqualTo("updated@mail.com");
    }

    @Test
    void findUserById_WhenExistingUser_ReturnUserDto() throws Exception {
        // Act & Expect
        mockMvc.perform(get("/users/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void deleteUser_WhenExistingUser_ReturnNoContent() throws Exception {
        // Act & Expect
        mockMvc.perform(delete("/users/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Assert
        assertThat(userRepository.findById(user.getId())).isEmpty();
    }

    @Test
    void addUser_WhenAlreadyOccupiedEmail_ReturnConflict() throws Exception {
        // Arrange
        UserCreateDto userCreateDto = new UserCreateDto();
        userCreateDto.setEmail("user@mail.com");
        userCreateDto.setName("Duplicate User");

        // Act & Expect
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userCreateDto)))
                .andExpect(status().isConflict());
    }
}