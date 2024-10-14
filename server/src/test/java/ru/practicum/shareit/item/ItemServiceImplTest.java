package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.exception.IncorrectArgumentException;
import ru.practicum.shareit.exception.NotFoundEntityException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    @InjectMocks
    private ItemServiceImpl itemService;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookingService bookingService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private CommentMapper commentMapper;

    @Mock
    private ItemMapper itemMapper;

    private User user;
    private Item item;
    private ItemDto itemDto;
    private ItemCreateDto itemCreateDto;
    private CommentCreateDto commentCreateDto;
    ItemUpdateDto itemUpdateDto;

    @BeforeEach
    void setup() {
        // Arrange
        user = new User();
        user.setId(1L);
        user.setName("user");

        item = new Item();
        item.setId(1L);
        item.setOwner(user);
        item.setName("item");

        itemCreateDto = new ItemCreateDto();
        itemCreateDto.setName("item1");
        itemCreateDto.setDescription("description");
        itemCreateDto.setAvailable(true);

        itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());

        itemUpdateDto = new ItemUpdateDto();
        commentCreateDto = new CommentCreateDto();
    }

    @Test
    void addItem_WhenUserNotFound_ThrowNotFoundEntityException() {
        // Arrange
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(NotFoundEntityException.class, () -> {
            itemService.addItem(itemCreateDto, user.getId());
        });

        // Assert
        assertEquals("User не найден", exception.getMessage());
    }

    @Test
    void addComment_WhenValidRequest_ReturnCommentDto() {
        // Arrange
        commentCreateDto.setText("comment");

        Booking booking = new Booking();
        booking.setEnd(LocalDateTime.now().minusDays(1));

        when(itemRepository.findByIdWithUser(item.getId())).thenReturn(Optional.of(item));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(bookingService.findUserBookingForItem(user.getId(), item.getId())).thenReturn(Optional.of(booking));
        when(commentMapper.toComment(commentCreateDto)).thenReturn(new Comment());

        // Act
        itemService.addComment(commentCreateDto, item.getId(), user.getId());

        // Verify
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void addComment_WhenItemNotFound_ThrowNotFoundEntityException() {
        // Arrange
        when(itemRepository.findByIdWithUser(item.getId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundEntityException.class, () -> {
            itemService.addComment(commentCreateDto, item.getId(), user.getId());
        });
    }

    @Test
    void addComment_WhenUserNotFound_ThrowNotFoundEntityException() {
        // Arrange
        when(itemRepository.findByIdWithUser(item.getId())).thenReturn(Optional.of(item));
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundEntityException.class, () -> {
            itemService.addComment(commentCreateDto, item.getId(), user.getId());
        });
    }

    @Test
    void updateItem_WhenItemNotFound_ThrowNotFoundEntityException() {
        // Arrange
        when(itemRepository.findById(item.getId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundEntityException.class, () -> {
            itemService.updateItem(itemUpdateDto, user.getId(), item.getId());
        });
    }

    @Test
    void updateItem_WhenNotOwner_ThrowIncorrectArgumentException() {
        // Arrange
        when(itemRepository.findById(item.getId())).thenReturn(Optional.of(item));

        // Act & Assert
        assertThrows(IncorrectArgumentException.class, () -> {
            itemService.updateItem(itemUpdateDto, 2L, item.getId());
        });
    }

    @Test
    void getItem_WhenItemExists_ReturnItemOwnerDto() {
        // Arrange
        when(itemRepository.findByIdWithUser(item.getId())).thenReturn(Optional.of(item));
        when(itemMapper.toItemOwnerDto(item)).thenReturn(new ItemOwnerDto());

        // Act
        ItemOwnerDto itemOwnerDto = itemService.findItem(item.getId(), user.getId());

        // Assert
        assertNotNull(itemOwnerDto);
    }

    @Test
    void findItem_WhenItemNotFound_ThrowNotFoundEntityException() {
        // Arrange
        when(itemRepository.findByIdWithUser(item.getId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundEntityException.class, () -> {
            itemService.findItem(item.getId(), user.getId());
        });
    }

    @Test
    void findItemsByText_WhenNonBlankText_ReturnItemDtoList() {
        // Arrange
        when(itemRepository.searchAvailableItemsByNameOrDescription("text"))
                .thenReturn(Collections.singletonList(item));
        when(itemMapper.toItemDto(item)).thenReturn(itemDto);

        // Act
        List<ItemDto> result = itemService.findItemsByText("text");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void findItemsByText_WhenBlankText_ReturnEmptyList() {
        // Arrange
        List<ItemDto> result = itemService.findItemsByText(" ");

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}