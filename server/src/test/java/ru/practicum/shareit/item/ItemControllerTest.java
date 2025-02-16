package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentDtoCreatedText;
import ru.practicum.shareit.item.dto.ItemDtoFull;
import ru.practicum.shareit.item.dto.ItemDtoWithoutDates;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
class ItemControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    private final Long requestId = 1L;
    private final Long itemId = 2L;
    private final Long userId = 3L;

    private final ItemDtoWithoutDates itemDtoWithoutDates = ItemDtoWithoutDates.builder()
            .requestId(requestId)
            .name("itemDtoWithoutDates")
            .description("description itemDtoWithoutDates")
            .available("true")
            .ownerId(userId)
            .build();

    private final ItemDtoWithoutDates itemDtoWithoutDates2 = ItemDtoWithoutDates.builder()
            .requestId(requestId)
            .name("itemDtoWithoutDates2")
            .description("description itemDtoWithoutDates2")
            .available("true")
            .ownerId(userId)
            .build();

    private final ItemDtoWithoutDates itemDtoWithoutDates3 = itemDtoWithoutDates.toBuilder()
            .name("itemDtoWithoutDates2")
            .description("description itemDtoWithoutDates2")
            .build();

    private final ItemDtoFull itemDtoFull = ItemDtoFull.builder()
            .requestId(requestId)
            .name("itemDtoFull")
            .description("description itemDtoFull")
            .available("true")
            .ownerId(userId)
            .comments(new ArrayList<>())
            .lastBooking(new BookingDto())
            .nextBooking(new BookingDto())
            .build();

    @SneakyThrows
    @Test
    void createTest() {

        when(itemService.create(userId, itemDtoWithoutDates)).thenReturn(itemDtoWithoutDates2);

        String result = mockMvc.perform(post("/items")
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(itemDtoWithoutDates)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(itemService, times(1)).create(any(Long.class), any(ItemDtoWithoutDates.class));
        assertEquals(objectMapper.writeValueAsString(itemDtoWithoutDates2), result);
    }

    @SneakyThrows
    @Test
    void getItemByIdTest() {

        when(itemService.getItemById(userId, itemId)).thenReturn(itemDtoWithoutDates2);

        String result = mockMvc.perform(get("/items/{itemId}", itemId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(itemService, times(1)).getItemById(any(Long.class), any(Long.class));
        assertEquals(objectMapper.writeValueAsString(itemDtoWithoutDates2), result);
    }

    @SneakyThrows
    @Test
    void getItemsByUserIdTest() {

        when(itemService.getItemsByUserId(userId)).thenReturn(List.of(itemDtoFull));

        String result = mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(itemService, times(1)).getItemsByUserId(any(Long.class));
        assertEquals(objectMapper.writeValueAsString(List.of(itemDtoFull)), result);
    }

    @SneakyThrows
    @Test
    void update() {

        when(itemService.update(userId, itemId, itemDtoWithoutDates2)).thenReturn(itemDtoWithoutDates3);

        String result = mockMvc.perform(patch("/items/{itemId}", itemId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemDtoWithoutDates2))
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(itemService, times(1)).update(any(Long.class), any(Long.class),
                any(ItemDtoWithoutDates.class));
        assertEquals(objectMapper.writeValueAsString(itemDtoWithoutDates2), result);
    }

    @SneakyThrows
    @Test
    void searchItemsByText() {
        String text = "dsfd";
        when(itemService.searchItemsByText(text)).thenReturn(List.of(itemDtoWithoutDates, itemDtoWithoutDates2));

        String result = mockMvc.perform(get("/items/search")
                        .param("text", text)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(itemService, times(1)).searchItemsByText(any(String.class));
        assertEquals(objectMapper.writeValueAsString(List.of(itemDtoWithoutDates, itemDtoWithoutDates2)), result);
    }

    @SneakyThrows
    @Test
    void createCommentTest() {
        CommentDtoCreatedText commentDtoCreatedText = CommentDtoCreatedText.builder()
                .id(1L)
                .text("comment")
                .build();
        CommentDto commentDto = CommentDto.builder()
                .itemId(itemId)
                .created(commentDtoCreatedText.getCreated())
                .text(commentDtoCreatedText.getText())
                .authorName("author")
                .id(commentDtoCreatedText.getId())
                .build();

        when(itemService.createComment(userId, itemId, commentDtoCreatedText)).thenReturn(commentDto);

        String result = mockMvc.perform(post("/items/{itemId}/comment", itemId)
                        .contentType("application/json")
                        .header("X-Sharer-User-Id", userId)
                        .content(objectMapper.writeValueAsString(commentDtoCreatedText)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(itemService, times(1)).createComment(anyLong(), anyLong(), any(CommentDtoCreatedText.class));
        assertEquals(objectMapper.writeValueAsString(commentDto), result);
    }
}