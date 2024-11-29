package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dal.BookingDBRepository;
import ru.practicum.shareit.exception.ConditionsNotMetException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dal.comment.CommentDBRepository;
import ru.practicum.shareit.item.dal.comment.CommentMapper;
import ru.practicum.shareit.item.dal.item.ItemBaseRepository;
import ru.practicum.shareit.item.dal.item.ItemMapper;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentDtoCreatedText;
import ru.practicum.shareit.item.dto.ItemDtoWithoutDates;
import ru.practicum.shareit.item.dto.ItemDtoFull;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dal.UserBaseRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ItemServiceImpl implements ItemService {
    private final ItemBaseRepository itemRepository;
    private final UserBaseRepository userRepository;
    private final CommentDBRepository commentDBRepository;
    private final BookingDBRepository bookingDBRepository;
    private final ItemMapper itemMapper = Mappers.getMapper(ItemMapper.class);
    private final CommentMapper commentMapper = Mappers.getMapper(CommentMapper.class);

    @Override
    public ItemDtoWithoutDates create(long userId, ItemDtoWithoutDates itemDtoWithoutDates) {
        User owner = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь с id = " + userId + "не найден"));
        itemDtoWithoutDates.setOwnerId(userId);
        Item item = itemMapper.toItem(itemDtoWithoutDates, owner);
        return itemMapper.itemToItemDto(itemRepository.save(item));
    }
/* todo
    @Override
    public ItemDtoWithoutDates getItemById(long itemId) {
        ItemDtoWithoutDates itemDto = itemMapper.itemToItemDto((itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Товар с id=" + itemId + " не найден"))));
        return itemMapper.itemToItemDto((itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Товар с id=" + itemId + " не найден"))));
    }*/

    @Override
    public ItemDtoFull getItemById(long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Товар с id=" + itemId + " не найден"));
        List<Comment> comments = commentDBRepository.findByItemId(itemId);
        return itemMapper.itemToItemDtoWithAll(item, comments);
    }

    public List<ItemDtoWithoutDates> getItemsByUserId(long userId) {
        return itemMapper.listItemToListItemDto(itemRepository.findByOwnerId(userId));
    }

    @Override
    public ItemDtoWithoutDates update(long userId, long itemId, ItemDtoWithoutDates itemDtoWithoutDates) {
        Item itemToUpdate = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Товар с id=" + itemId + " не найден"));
        if (userId != itemToUpdate.getOwner().getId()) {
            throw new NotFoundException("Id владельца и id пользователя в запросе не совпадают");
        }
        itemToUpdate = itemMapper.updateFromDto(itemDtoWithoutDates, itemToUpdate);
        return itemMapper.itemToItemDto(itemRepository.save(itemToUpdate));
    }

    @Override
    public List<ItemDtoWithoutDates> searchItemsByText(String text) {
        return itemMapper.listItemToListItemDto(itemRepository.findByDescriptionOrName(text));
    }

    @Override
    public CommentDto createComment(long userId, long itemId, CommentDtoCreatedText commentDtoOnlyText) {
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id = " + userId + "не найден"));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Товар с id=" + itemId + " не найден"));
        List<Booking> bookingList = bookingDBRepository.findByItemIdAndBookerIdAndEndIsBefore(itemId,userId, LocalDateTime.now());
        if (bookingList.size() == 0) {
            throw new ConditionsNotMetException("Бронь для товара с id = " + itemId + " и пользователя с id = " + userId + " не найдена");
        }

        Comment comment = (commentMapper.commentDtoOnlyTextToComment(commentDtoOnlyText, item, author));
        comment = commentDBRepository.save(comment);
        return commentMapper.commentToCommentDto(comment);
    }
}