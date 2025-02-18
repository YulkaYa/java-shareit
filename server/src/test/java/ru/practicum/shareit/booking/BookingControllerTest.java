package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoCreated;
import ru.practicum.shareit.item.dto.ItemDtoBase;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
class BookingControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    private final Long requestId = 1L;
    private final Long itemId = 2L;
    private final Long userId = 3L;
    private final Long bookingId = 4L;

    private BookingDtoCreated bookingDtoCreated = BookingDtoCreated.builder()
            .itemId(itemId)
            .start(LocalDateTime.now())
            .end(LocalDateTime.now().plusMonths(3))
            .id(bookingId)
            .build();

    private BookingDto bookingDto = BookingDto.builder()
            .id(bookingId)
            .start(bookingDtoCreated.getStart())
            .end(bookingDtoCreated.getEnd())
            .item(new ItemDtoBase().toBuilder().name("item").build())
            .booker(new UserDto().toBuilder().name("user").build())
            .status(Status.WAITING)
            .build();

    @SneakyThrows
    @Test
    void createTest() {

        when(bookingService.create(userId, bookingDtoCreated)).thenReturn(bookingDto);

        String result = mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", userId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(bookingDtoCreated)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(bookingService, times(1)).create(anyLong(), any(BookingDtoCreated.class));
        assertEquals(objectMapper.writeValueAsString(bookingDto), result);
    }

    @SneakyThrows
    @Test
    void getTest() {

        when(bookingService.get(userId, bookingId)).thenReturn(bookingDto);

        String result = mockMvc.perform(get("/bookings/{bookingId}", bookingId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(bookingService, times(1)).get(any(Long.class), any(Long.class));
        assertEquals(objectMapper.writeValueAsString(bookingDto), result);
    }

    @SneakyThrows
    @Test
    void approveBookingTest() {

        when(bookingService.approve(userId, bookingId, true)).thenReturn(bookingDto);

        String result = mockMvc.perform(patch("/bookings/{bookingId}", bookingId)
                        .header("X-Sharer-User-Id", userId)
                        .param("approved", "true"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(bookingService, times(1)).approve(any(Long.class), any(Long.class), eq(true));
        assertEquals(objectMapper.writeValueAsString(bookingDto), result);
    }

    @SneakyThrows
    @Test
    void getAllTest() {
        BookingState bookingState = BookingState.CURRENT;

        when(bookingService.getAllByUser(anyLong(), any())).thenReturn(List.of(bookingDto));

        String result = mockMvc.perform(get("/bookings")
                        .param("state", bookingState.toString())
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(bookingService, times(1)).getAllByUser(any(Long.class), any(BookingState.class));
        assertEquals(objectMapper.writeValueAsString(List.of(bookingDto)), result);
    }

    @SneakyThrows
    @Test
    void getAllByOwner_positive() {
        BookingState bookingState = BookingState.CURRENT;

        when(bookingService.getAllByOwner(anyLong(), any())).thenReturn(List.of(bookingDto));

        String result = mockMvc.perform(get("/bookings/owner")
                        .param("state", bookingState.toString())
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(bookingService, times(1)).getAllByOwner(any(Long.class), any(BookingState.class));
        assertEquals(objectMapper.writeValueAsString(List.of(bookingDto)), result);
    }

    @SneakyThrows
    @Test
    void getAllByOwner_negative() {
        BookingState bookingState = BookingState.CURRENT;

        when(bookingService.getAllByOwner(anyLong(), any())).thenReturn(new ArrayList<>());

        String result = mockMvc.perform(get("/bookings/owner")
                        .param("state", bookingState.toString())
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        verify(bookingService, times(1)).getAllByOwner(any(Long.class), any(BookingState.class));
        assertTrue(result.contains("Бронь для пользователя с id = " + userId + " не найдена"));
       }
}