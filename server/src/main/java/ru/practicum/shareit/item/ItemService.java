package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentDtoCreatedText;
import ru.practicum.shareit.item.dto.ItemDtoFull;
import ru.practicum.shareit.item.dto.ItemDtoWithoutDates;

import java.util.List;

public interface ItemService {
    ItemDtoWithoutDates create(long userId, ItemDtoWithoutDates itemDtoWithoutDates);

    <T extends ItemDtoWithoutDates> T getItemById(long userId, long itemId);

    List<ItemDtoFull> getItemsByUserId(long userId);

    ItemDtoWithoutDates update(long userId, long itemId, ItemDtoWithoutDates itemDtoWithoutDates);

    List<ItemDtoWithoutDates> searchItemsByText(String text);

    CommentDto createComment(long userId, long itemId, CommentDtoCreatedText commentDtoOnlyText);
}
