package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dal.BookingDBRepository;
import ru.practicum.shareit.booking.dal.BookingMapper;
import ru.practicum.shareit.exception.ConditionsNotMetException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dal.comment.CommentDBRepository;
import ru.practicum.shareit.item.dal.comment.CommentMapper;
import ru.practicum.shareit.item.dal.item.ItemBaseRepository;
import ru.practicum.shareit.item.dal.item.ItemMapper;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentDtoCreatedText;
import ru.practicum.shareit.item.dto.ItemDtoFull;
import ru.practicum.shareit.item.dto.ItemDtoWithoutDates;
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
    private final BookingMapper bookingMapper = Mappers.getMapper(BookingMapper.class);

    @Override
    public ItemDtoWithoutDates create(long userId, ItemDtoWithoutDates itemDtoWithoutDates) {
        User owner = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с id = " + userId + "не найден"));
        itemDtoWithoutDates.setOwnerId(userId);
        Item item = itemMapper.toItem(itemDtoWithoutDates, owner);
        return itemMapper.itemToItemDto(itemRepository.save(item));
    }

    @Override
    public <T extends ItemDtoWithoutDates> T getItemById(long userId, long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Товар с id=" + itemId + " не найден"));
        ItemDtoFull itemDtoFull = itemMapper.itemToItemDtoFullPartly(item);

        addBookingsToItemDtoFull(userId, itemId, itemDtoFull);
        return (T) itemDtoFull;
    }

    private void addBookingsToItemDtoFull(long userId, long itemId, ItemDtoFull itemDtoFull) {
        Sort sortByStart = Sort.by(Sort.Direction.ASC, "start");
        Booking lastBooking;
        Booking nextBooking;
        LocalDateTime now = LocalDateTime.now();

        // Находим список бронирований, стартующих до текущей даты, и сортируем по возрастанию даты оконачания брони
        List<Booking> lastBookings = bookingDBRepository.findByItemIdAndStartIsBeforeAndEndIsAfter(itemId,
                now, now, sortByStart);
        LocalDateTime endOfLastBooking;

        // Если последних броней нет, то последнее бронирование оставляем пустым, а дата старта след. брони = текущая
        if (lastBookings.isEmpty()) {
            lastBooking = null;
            endOfLastBooking = now;
        } else {
            lastBooking = lastBookings.getLast();
            endOfLastBooking = lastBooking.getEnd(); // Дата след. брони = дата окончания последней брони
        }

        List<Booking> nextBookings = bookingDBRepository.findByItemIdAndStartIsAfter(userId,
                endOfLastBooking, sortByStart);
        if (nextBookings.isEmpty()) {
            nextBooking = null;
        } else {
            nextBooking = nextBookings.getFirst();
        }

        itemDtoFull.setLastBooking(bookingMapper.boookingToBookingDto(lastBooking));
        itemDtoFull.setNextBooking(bookingMapper.boookingToBookingDto(nextBooking));
        itemDtoFull.setComments(commentMapper.listCommentToListCommentDto(commentDBRepository
                .findByItemId(itemId)));
    }

    public List<ItemDtoFull> getItemsByUserId(long userId) {
        List<ItemDtoFull> listItemDtoFull = itemMapper.listItemToListItemDtoFull(itemRepository
                .findByOwnerId(userId));
        for (ItemDtoFull itemDtoFull : listItemDtoFull) {
            long itemId = itemDtoFull.getId();
            addBookingsToItemDtoFull(userId, itemId, itemDtoFull);
        }
        return listItemDtoFull;
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
        List<Booking> bookingList = bookingDBRepository.findByItemIdAndBookerIdAndEndIsBefore(itemId, userId, LocalDateTime.now());
        if (bookingList.isEmpty()) {
            throw new ConditionsNotMetException("Бронь для товара с id = " + itemId + " и пользователя с id = " + userId + " не найдена");
        }

        Comment comment = (commentMapper.commentDtoOnlyTextToComment(commentDtoOnlyText, item, author));
        comment = commentDBRepository.save(comment);
        return commentMapper.commentToCommentDto(comment);
    }
}