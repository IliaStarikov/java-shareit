package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.item.dto.ItemCreateDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemOwnerDto;
import ru.practicum.shareit.item.dto.ItemUpdateDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import static org.assertj.core.api.Assertions.assertThat;

class ItemMapperTest {

    private ItemMapper itemMapper;

    private ItemCreateDto newItemDto;
    private Item item;

    @BeforeEach
    void setup() {
        // Arrange: Инициализация и создание объектов
        itemMapper = Mappers.getMapper(ItemMapper.class);

        newItemDto = new ItemCreateDto();
        newItemDto.setName("Test item");
        newItemDto.setDescription("description");
        newItemDto.setAvailable(true);

        User owner = new User();
        owner.setId(2L);
        owner.setName("Test owner");

        item = new Item();
        item.setId(1L);
        item.setName("item");
        item.setDescription("description");
        item.setAvailable(true);
        item.setOwner(owner);
    }

    @Test
    void toItem_ShouldMapItemCreateDtoToItem() {
        // Act
        Item mappedItem = itemMapper.toItem(newItemDto);

        // Assert
        assertThat(mappedItem).isNotNull();
        assertThat(mappedItem.getName()).isEqualTo(newItemDto.getName());
        assertThat(mappedItem.getDescription()).isEqualTo(newItemDto.getDescription());
        assertThat(mappedItem.getAvailable()).isEqualTo(newItemDto.getAvailable());
    }

    @Test
    void toItemDto_ShouldMapItemToItemDto() {
        // Act
        ItemDto itemDto = itemMapper.toItemDto(item);

        // Assert
        assertThat(itemDto).isNotNull();
        assertThat(itemDto.getId()).isEqualTo(item.getId());
        assertThat(itemDto.getName()).isEqualTo(item.getName());
        assertThat(itemDto.getDescription()).isEqualTo(item.getDescription());
    }

    @Test
    void toItemOwnerDto_ShouldMapItemToItemOwnerDto() {
        // Act
        ItemOwnerDto itemOwnerDto = itemMapper.toItemOwnerDto(item);

        // Assert
        assertThat(itemOwnerDto).isNotNull();
        assertThat(itemOwnerDto.getId()).isEqualTo(item.getId());
        assertThat(itemOwnerDto.getName()).isEqualTo(item.getName());
        assertThat(itemOwnerDto.getDescription()).isEqualTo(item.getDescription());
    }

    @Test
    void updateItem_ShouldUpdateItemFields() {
        // Arrange
        ItemUpdateDto itemUpdateDto = new ItemUpdateDto();
        itemUpdateDto.setName("Updated name");
        itemUpdateDto.setAvailable(true);

        // Act
        itemMapper.updateItem(itemUpdateDto, item);

        // Assert
        assertThat(item.getName()).isEqualTo(itemUpdateDto.getName());
        assertThat(item.getAvailable()).isEqualTo(itemUpdateDto.getAvailable());
        assertThat(item.getDescription()).isEqualTo("description");
    }
}