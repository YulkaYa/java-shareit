package ru.practicum.shareit.item.dal.item;


import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemBaseRepository {

    Item save(Item data);

    Item update(Item data);

    Optional<Item> findById(long id);

    List<Item> findAll();

    List<Item> findByOwnerId(long ownerId);

    List<Item> findByDescriptionOrName(String text);
}