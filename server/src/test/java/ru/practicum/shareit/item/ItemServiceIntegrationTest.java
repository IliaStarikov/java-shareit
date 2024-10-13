package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundEntityException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ItemServiceIntegrationTest {

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemMapper itemMapper;

    private User user;
    private ItemCreateDto itemCreateDto;

    @BeforeEach
    void setup() {
        // Arrange
        user = new User();
        user.setEmail("test@mail.com");
        user.setName("user");
        userRepository.save(user);

        itemCreateDto = new ItemCreateDto();
        itemCreateDto.setName("test");
        itemCreateDto.setDescription("description");
        itemCreateDto.setAvailable(true);
    }

    @Test
    void addItem_ShouldCreateNewItem() {
        // Arrange
        Item item = itemMapper.toItem(itemCreateDto);
        item.setOwner(user);
        item.setAvailable(true);
        item.setName(itemCreateDto.getName());
        item.setDescription(itemCreateDto.getDescription());

        // Act
        Item createdItem = itemRepository.save(item);

        // Assert
        assertThat(createdItem).isNotNull();
        assertThat(createdItem.getName()).isEqualTo(itemCreateDto.getName());
        assertThat(createdItem.getDescription()).isEqualTo(itemCreateDto.getDescription());
        assertThat(createdItem.getOwner().getId()).isEqualTo(user.getId());

        Optional<Item> foundItem = itemRepository.findById(createdItem.getId());
        assertThat(foundItem).isPresent();
    }

    @Test
    void updateItem_ShouldUpdateExistingItem() {
        // Arrange
        ItemDto createdItem = itemService.addItem(itemCreateDto, user.getId());
        ItemUpdateDto itemUpdateDto = new ItemUpdateDto();
        itemUpdateDto.setName("New Item");
        itemUpdateDto.setDescription("New Description");

        // Act
        ItemDto updatedItem = itemService.updateItem(itemUpdateDto, user.getId(), createdItem.getId());

        // Assert
        assertThat(updatedItem).isNotNull();
        assertThat(updatedItem.getName()).isEqualTo(itemUpdateDto.getName());
        assertThat(updatedItem.getDescription()).isEqualTo(itemUpdateDto.getDescription());
    }

    @Test
    void findItem_ShouldReturnItemDetails() {
        // Arrange
        ItemDto createdItem = itemService.addItem(itemCreateDto, user.getId());

        // Act
        ItemOwnerDto foundItem = itemService.findItem(createdItem.getId(), user.getId());

        // Assert
        assertThat(foundItem).isNotNull();
        assertThat(foundItem.getId()).isEqualTo(createdItem.getId());
        assertThat(foundItem.getName()).isEqualTo(createdItem.getName());
    }

    @Test
    void searchItemsByText_ShouldReturnMatchingItems() {
        // Arrange
        itemService.addItem(itemCreateDto, user.getId());

        // Act
        List<ItemDto> foundItems = itemService.findItemsByText("test");

        // Assert
        assertThat(foundItems).isNotEmpty();
        assertThat(foundItems.getFirst().getName()).contains("test");
    }

    @Test
    void addComment_WhenNoBookingExists_ThrowNotFoundEntityException() {
        // Arrange
        ItemDto createdItem = itemService.addItem(itemCreateDto, user.getId());
        CommentCreateDto newCommentDto = new CommentCreateDto();
        newCommentDto.setText("comment");

        // Act & Assert
        assertThrows(NotFoundEntityException.class, () -> {
            itemService.addComment(newCommentDto, createdItem.getId(), user.getId());
        });
    }
}