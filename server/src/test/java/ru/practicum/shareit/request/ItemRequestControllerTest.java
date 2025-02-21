package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.ItemDtoWithoutDates;
import ru.practicum.shareit.request.dto.ItemRequestBaseDto;
import ru.practicum.shareit.request.dto.ItemRequestCreatedDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoWithItems;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
class ItemRequestControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemRequestService itemRequestService;

    private final Long requestorId = 1L;
    private final Long requestId = 0L;
    private final Long itemId = 2L;
    private final Long userId = 3L;
    private final ItemDtoWithoutDates itemDtoWithoutDates = ItemDtoWithoutDates.builder()
            .id(itemId)
            .requestId(requestId)
            .name("itemDtoWithoutDates")
            .description("description itemDtoWithoutDates")
            .available("true")
            .ownerId(userId)
            .comments(new ArrayList<>())
            .build();

    private final ItemRequestCreatedDto itemRequestCreatedDto = ItemRequestCreatedDto.builder()
            .description("itemRequestCreatedDto 1")
            .build();

    private final ItemRequestBaseDto itemRequestBaseDto = ItemRequestBaseDto.builder()
            .id(requestId)
            .created(itemRequestCreatedDto.getCreated())
            .description(itemRequestCreatedDto.getDescription())
            .build();

    private final ItemRequestDtoWithItems itemRequestDtoWithItems = ItemRequestDtoWithItems.builder()
            .id(requestId)
            .created(itemRequestCreatedDto.getCreated())
            .description(itemRequestCreatedDto.getDescription())
            .requestorId(requestorId)
            .items(List.of(itemDtoWithoutDates))
            .build();

    @SneakyThrows
    @Test
    void createTest() {

        when(itemRequestService.create(requestorId, itemRequestCreatedDto)).thenReturn(itemRequestBaseDto);

        String result = mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", requestorId)
                .contentType("application/json")
                        .content(objectMapper.writeValueAsString(itemRequestCreatedDto)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(itemRequestService, times(1)).create(any(Long.class), any(ItemRequestCreatedDto.class));
        assertEquals(objectMapper.writeValueAsString(itemRequestBaseDto), result);
    }

    @SneakyThrows
    @Test
    void getRequestsByRequestIdTest() {

        when(itemRequestService.getItemsRequestsByRequestId(requestId)).thenReturn(itemRequestDtoWithItems);

        String result = mockMvc.perform(get("/requests/{requestId}", requestId)
                        .header("X-Sharer-User-Id", userId)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(itemRequestService, times(1)).getItemsRequestsByRequestId(any(Long.class));
        assertEquals(objectMapper.writeValueAsString(itemRequestDtoWithItems), result);
    }

    @SneakyThrows
    @Test
    void getRequestsByUserIdTest() {

        when(itemRequestService.getItemsRequestsByUserId(requestorId)).thenReturn(List.of(itemRequestDtoWithItems));

        String result = mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", requestorId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(itemRequestService, times(1)).getItemsRequestsByUserId(any(Long.class));
        assertEquals(objectMapper.writeValueAsString(List.of(itemRequestDtoWithItems)), result);
    }

    @SneakyThrows
    @Test
    void getRequestsFromOtherUsersTest() {

        when(itemRequestService.getRequestsFromOtherUsers(requestorId)).thenReturn(List.of(itemRequestDtoWithItems));

        String result = mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", requestorId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(itemRequestService, times(1)).getRequestsFromOtherUsers(any(Long.class));
        assertEquals(objectMapper.writeValueAsString(List.of(itemRequestDtoWithItems)), result);
    }
}