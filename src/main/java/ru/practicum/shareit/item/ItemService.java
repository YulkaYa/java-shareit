package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentDtoCreatedText;
import ru.practicum.shareit.item.dto.ItemDtoWithoutDates;
import ru.practicum.shareit.item.dto.ItemDtoFull;

import java.util.List;

public interface ItemService {
    ItemDtoWithoutDates create(long userId, ItemDtoWithoutDates itemDtoWithoutDates);

    ItemDtoFull getItemById(long itemId);

    List<ItemDtoWithoutDates> getItemsByUserId(long userId);

    ItemDtoWithoutDates update(long userId, long itemId, ItemDtoWithoutDates itemDtoWithoutDates);

    List<ItemDtoWithoutDates> searchItemsByText(String text);

    CommentDto createComment(long userId, long itemId, CommentDtoCreatedText commentDtoOnlyText);
}
