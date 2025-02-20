package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dal.BookingDBRepository;
import ru.practicum.shareit.exception.ConditionsNotMetException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dal.comment.CommentDBRepository;
import ru.practicum.shareit.item.dal.item.ItemDBRepository;
import ru.practicum.shareit.item.dal.item.ItemMapper;
import ru.practicum.shareit.item.dto.CommentDtoCreatedText;
import ru.practicum.shareit.item.dto.ItemDtoWithoutDates;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.dal.ItemRequestDBRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dal.UserDBRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@WebMvcTest(ItemService.class)
class ItemServiceMockTest {
    @Autowired
    private ItemService itemService;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserDBRepository userDBRepository;
    @MockBean
    private ItemRequestDBRepository itemRequestDBRepository;
    @MockBean
    private ItemDBRepository itemBaseRepository;
    @MockBean
    private CommentDBRepository commentDBRepository;
    @MockBean
    private BookingDBRepository bookingDBRepository;
    @MockBean
    private ItemMapper itemMapper;

    @Test
    void createTest_UserNotFoundException() {
        when(userDBRepository.findById(any(Long.class))).thenReturn(null);
        Throwable throwable = assertThrows(NotFoundException.class, () -> itemService.create(1L, new ItemDtoWithoutDates()));
        assertEquals("Пользователь с id = 1 не найден", throwable.getMessage());
    }

    @Test
    void createTest_ItemRequestNotFound_Negative() {
        ItemDtoWithoutDates itemDtoWithoutDates = ItemDtoWithoutDates.builder().requestId(1L).build();
        User user = User.builder().id(1L).build();
        ItemRequest itemRequest = ItemRequest.builder().id(1L).build();

        when(userDBRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRequestDBRepository.findById(anyLong())).thenReturn(Optional.empty());

        Throwable throwable = assertThrows(NotFoundException.class, () -> itemService.create(1L, itemDtoWithoutDates));
        assertEquals(" Запрос с id = 1 не найден", throwable.getMessage());
    }

    @Test
    void updateTest_ItemNotFound_Negative() {
        ItemDtoWithoutDates itemDtoWithoutDates = ItemDtoWithoutDates.builder().requestId(1L).build();
        User user = User.builder().id(1L).build();
        ItemRequest itemRequest = ItemRequest.builder().id(1L).build();
        Item item = Item.builder().id(2L).owner(user).request(itemRequest).build();

        when(itemBaseRepository.findById(anyLong())).thenReturn(Optional.of(item));

        Throwable throwable = assertThrows(NotFoundException.class, () -> itemService.update(3L,2L, itemDtoWithoutDates));
        assertEquals("Id владельца и id пользователя в запросе не совпадают", throwable.getMessage());
    }

    @Test
    void createCommentTest_BookingNotFound_Negative() {
        ItemDtoWithoutDates itemDtoWithoutDates = ItemDtoWithoutDates.builder().requestId(1L).build();
        User user = User.builder().id(1L).build();
        ItemRequest itemRequest = ItemRequest.builder().id(1L).build();
        Item item = Item.builder().id(2L).owner(user).request(itemRequest).build();

        when(userDBRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemBaseRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(bookingDBRepository.findByItemIdAndBookerIdAndEndIsBefore(anyLong(),anyLong(), any(LocalDateTime.class)))
                .thenReturn(new ArrayList<>());

        Throwable throwable = assertThrows(ConditionsNotMetException.class, () -> itemService.createComment(user.getId(),item.getId(), CommentDtoCreatedText.builder().build()));
        assertEquals("Бронь для товара с id = " + item.getId() + " и пользователя с id = " + user.getId() + " не найдена", throwable.getMessage());
    }

    @Test
    void createTest_ItemRequestFound_Positive() {
        ItemDtoWithoutDates itemDtoWithoutDates = ItemDtoWithoutDates.builder().requestId(1L).build();
        User user = User.builder().id(1L).build();
        ItemRequest itemRequest = ItemRequest.builder().id(1L).build();
        Item item = Item.builder().id(2L).owner(user).request(itemRequest).build();

        when(userDBRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRequestDBRepository.findById(anyLong())).thenReturn(Optional.of(itemRequest));
        when(itemMapper.toItemWithRequest(any(ItemDtoWithoutDates.class), any(User.class),
                any(ItemRequest.class))).thenReturn(item);

        itemService.create(user.getId(), itemDtoWithoutDates);

        verify(itemBaseRepository, times(1)).save(any(Item.class));
    }
}