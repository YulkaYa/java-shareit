package ru.practicum.shareit.item.dal;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Primary
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Component
public class InMemoryItemRepository implements ItemBaseRepository {
    private final List<Item> items = new ArrayList<>();
    private long count = 0;

    @Override
    public Item create(Item data) {
        data.setId(++count);
        items.add(data);
        return data;
    }

    @Override
    public Item update(Item data) {
        Item item = get(data.getId()).get();
        item = data;
        return get(data.getId()).get();
    }

    @Override
    public Optional<Item> get(long id) {
        for (Item item : items) {
            if (item.getId() == id) {
                return Optional.of(item);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Item> getAll() {
        return new ArrayList<>(items);
    }

    @Override
    public List<Item> getItemsByUserId(long userId) {
        return getAll()
                .stream()
                .filter(item -> item.getOwnerId() == userId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> searchByDescriptionOrName(String text) {
        List<Item> itemListByText = new ArrayList<>();
        if (!text.isBlank()) {
            text = text.toUpperCase();
            for (Item item : items) {
                if (item.isAvailable()) {
                    if (item.getDescription().toUpperCase().contains(text) || item.getName().toUpperCase().contains(text)) {
                        itemListByText.add(item);
                    }
                }
            }
        }
        return itemListByText;
    }
}
