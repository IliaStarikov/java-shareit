package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.RequestCreateDto;
import ru.practicum.shareit.request.mapper.RequestMapper;
import ru.practicum.shareit.user.User;

import static org.assertj.core.api.Assertions.assertThat;

class RequestMapperTest {

    private RequestMapper requestMapper;

    @BeforeEach
    void setup() {
        // Arrange
        requestMapper = Mappers.getMapper(RequestMapper.class);
    }

    @Test
    void toRequest_ShouldMapRequestItemDtoToItemRequest() {
        // Arrange
        RequestCreateDto requestDto = new RequestCreateDto();
        requestDto.setDescription("Request description");

        // Act
        Request request = requestMapper.toRequest(requestDto);

        // Assert
        assertThat(request).isNotNull();
        assertThat(request.getDescription()).isEqualTo("Request description");
    }

    @Test
    void toItemRequestDto_ShouldMapItemToItemRequestDto() {
        // Arrange
        User user = new User();
        user.setId(3L);

        Item item = new Item();
        item.setId(2L);
        item.setName("Item Name");
        item.setOwner(user);

        // Act
        ItemRequestDto itemRequestDto = requestMapper.toItemRequestDto(item);

        // Assert
        assertThat(itemRequestDto).isNotNull();
        assertThat(itemRequestDto.getOwnerId()).isEqualTo(3L);
        assertThat(itemRequestDto.getId()).isEqualTo(2L);
        assertThat(itemRequestDto.getName()).isEqualTo("Item Name");
    }
}