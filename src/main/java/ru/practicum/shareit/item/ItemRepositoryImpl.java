package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Repository
public class ItemRepositoryImpl implements ItemRepository {
    private long itemId;
    private final Map<Long, Item> items = new HashMap<>();

    @Override
    public Item createItem(Item item) {
        item.setId(itemId++);
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Optional<Item> findItemById(long itemId) {
        return Optional.ofNullable(items.get(itemId));
    }

    @Override
    public List<Item> getOwnerItems(long userId) {
        return items.values()
                .stream()
                .filter(item -> item.getOwnerId() == userId)
                .toList();
    }

    @Override
    public List<Item> searchItemByText(String text) {
        return items.values()
                .stream()
                .filter(item -> (item.getName().toLowerCase().contains(text) ||
                        item.getDescription().toLowerCase().contains(text)) &&
                        item.getAvailable().equals(true))
                .toList();
    }

    @Override
    public Item updateItem(Item updateItem) {
        items.put(updateItem.getId(), updateItem);
        return updateItem;
    }
}