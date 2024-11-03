package ru.practicum.shareit.item.dal;


import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemBaseRepository {
    Item create(Item data);

    Item update(Item data);

    Optional<Item> get(long id);

    List<Item> getAll();

    List<Item> getItemsByUserId(long userId);

    List<Item> searchByDescriptionOrName(String text);
}