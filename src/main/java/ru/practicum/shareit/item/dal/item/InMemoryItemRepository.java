package ru.practicum.shareit.item.dal.item;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Component
public class InMemoryItemRepository implements ItemBaseRepository {
    private final List<Item> items = new ArrayList<>();
    private long count = 0;

    @Override
    public Item save(Item data) {
        data.setId(++count);
        items.add(data);
        return data;
    }

    @Override
    public Item update(Item data) {
        Item item = findById(data.getId()).get();
        item = data;
        return findById(data.getId()).get();
    }

    @Override
    public Optional<Item> findById(long id) {
        for (Item item : items) {
            if (item.getId() == id) {
                return Optional.of(item);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Item> findAll() {
        return new ArrayList<>(items);
    }

    @Override
    public List<Item> findByOwnerId(long ownerId) {
        return findAll()
                .stream()
                .filter(item -> item.getOwner().getId() == ownerId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> findByDescriptionOrName(String text) {
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
