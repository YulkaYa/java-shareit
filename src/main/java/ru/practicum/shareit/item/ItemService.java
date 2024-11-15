package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto create(Long userId, ItemDto itemDto);

    ItemDto getItemById(long itemId);

    List<ItemDto> getItemsByUserId(long userId);

    ItemDto update(Long userId, Long itemId, ItemDto itemDto);

    List<ItemDto> searchItemsByText(String text);
}
