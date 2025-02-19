package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dal.BookingDBRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dal.item.ItemBaseRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dal.UserBaseRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.when;

@WebMvcTest(BookingService.class)
@RequiredArgsConstructor
class BookingServiceMockTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private BookingServiceImpl bookingService;

    @MockBean
    private BookingDBRepository bookingDBRepository;
    @MockBean
    private ItemBaseRepository itemRepository1;
    @MockBean
    private UserBaseRepository userRepository;

    LocalDateTime start = LocalDateTime.now();
    LocalDateTime end = LocalDateTime.now().plusMonths(1);
    private Sort sort = Sort.by(Sort.Direction.DESC, "start");
    private User user = User.builder()
            .id(3L)
            .name("name")
            .email("email@mail.com")
            .build();
    private Item item = Item.builder()
            .id(4L)
            .name("nameItem")
            .description("descriptionItem")
            .available(true)
            .owner(user)
            .build();
    private Booking booking = Booking.builder()
            .id(2L)
            .status(Status.WAITING)
            .booker(user)
            .item(item)
            .start(start)
            .end(end)
            .build();


    @SneakyThrows
    @Test
    void getAllByUserTest_StateCurrent() {

        when(bookingDBRepository.findByBookerIdAndStartIsBeforeAndEndIsAfterAndStatusNotRejectedAndStatusNotCanceledIgnoreCase(anyLong(), any(LocalDateTime.class),
                any(LocalDateTime.class), any(Sort.class))).thenReturn(List.of(booking));

        List<BookingDto> bookingDtos = bookingService.getAllByUser(1L, BookingState.CURRENT);
        BookingDto bookingDto = bookingDtos.get(0);
        assertEquals(booking.getId(), bookingDto.getId());
        assertEquals(booking.getBooker().getId(), bookingDto.getBooker().getId());
        assertEquals(booking.getItem().getId(), bookingDto.getItem().getId());
    }

    @SneakyThrows
    @Test
    void getAllByUserTest_StatePast() {
        Booking bookingPast = booking.toBuilder()
                .end(LocalDateTime.now().minusDays(1))
                .status(Status.REJECTED)
                .build();
        when(bookingDBRepository.findByBookerIdAndEndIsBefore(anyLong(), any(LocalDateTime.class), any(Sort.class))).thenReturn(List.of(bookingPast));

        List<BookingDto> bookingDtos = bookingService.getAllByUser(1L, BookingState.PAST);
        BookingDto bookingDto = bookingDtos.get(0);
        assertEquals(bookingPast.getId(), bookingDto.getId());
        assertEquals(bookingPast.getBooker().getId(), bookingDto.getBooker().getId());
        assertEquals(bookingPast.getItem().getId(), bookingDto.getItem().getId());
    }

    @SneakyThrows
    @Test
    void getAllByUserTest_StateFuture() {

        when(bookingDBRepository.findByBookerIdAndStartIsAfterAndStatusNotRejectedAndStatusNotCanceledIgnoreCase(anyLong(), any(LocalDateTime.class), any(Sort.class))).thenReturn(List.of(booking));

        List<BookingDto> bookingDtos = bookingService.getAllByUser(1L, BookingState.FUTURE);
        BookingDto bookingDto = bookingDtos.get(0);
        assertEquals(booking.getId(), bookingDto.getId());
        assertEquals(booking.getBooker().getId(), bookingDto.getBooker().getId());
        assertEquals(booking.getItem().getId(), bookingDto.getItem().getId());
    }

    @SneakyThrows
    @Test
    void getAllByUserTest_StateAll() {

        when(bookingDBRepository.findByBookerId(anyLong(), any(Sort.class))).thenReturn(List.of(booking));

        List<BookingDto> bookingDtos = bookingService.getAllByUser(1L, BookingState.ALL);
        BookingDto bookingDto = bookingDtos.get(0);
        assertEquals(booking.getId(), bookingDto.getId());
        assertEquals(booking.getBooker().getId(), bookingDto.getBooker().getId());
        assertEquals(booking.getItem().getId(), bookingDto.getItem().getId());
    }

    @SneakyThrows
    @Test
    void getAllByUserTest_StateWaiting() {

        when(bookingDBRepository.findByBookerIdAndStatus(anyLong(), eq(Status.WAITING), any(Sort.class))).thenReturn(List.of(booking));

        List<BookingDto> bookingDtos = bookingService.getAllByUser(1L, BookingState.WAITING);
        BookingDto bookingDto = bookingDtos.get(0);
        assertEquals(booking.getId(), bookingDto.getId());
        assertEquals(booking.getBooker().getId(), bookingDto.getBooker().getId());
        assertEquals(booking.getItem().getId(), bookingDto.getItem().getId());
    }

    @SneakyThrows
    @Test
    void getAllByUserTest_StateRejected() {

        Booking bookingPast = booking.toBuilder()
                .end(LocalDateTime.now().minusDays(1))
                .status(Status.REJECTED)
                .build();

        when(bookingDBRepository.findByBookerIdAndStatus(anyLong(), eq(Status.REJECTED), any(Sort.class))).thenReturn(List.of(bookingPast));

        List<BookingDto> bookingDtos = bookingService.getAllByUser(1L, BookingState.REJECTED);
        BookingDto bookingDto = bookingDtos.get(0);
        assertEquals(bookingPast.getId(), bookingDto.getId());
        assertEquals(bookingPast.getBooker().getId(), bookingDto.getBooker().getId());
        assertEquals(bookingPast.getItem().getId(), bookingDto.getItem().getId());
    }

    @SneakyThrows
    @Test
    void getAllByOwnerTest_StateCurrent() {

        when(bookingDBRepository.findByItemOwnerIdAndStartIsBeforeAndEndIsAfterAndStatusNotRejectedAndStatusNotCanceledIgnoreCase(anyLong(), any(LocalDateTime.class),
                any(LocalDateTime.class), any(Sort.class))).thenReturn(List.of(booking));

        List<BookingDto> bookingDtos = bookingService.getAllByOwner(1L, BookingState.CURRENT);
        BookingDto bookingDto = bookingDtos.get(0);
        assertEquals(booking.getId(), bookingDto.getId());
        assertEquals(booking.getBooker().getId(), bookingDto.getBooker().getId());
        assertEquals(booking.getItem().getId(), bookingDto.getItem().getId());
    }

    @SneakyThrows
    @Test
    void getAllByOwnerTest_StatePast() {
        Booking bookingPast = booking.toBuilder()
                .end(LocalDateTime.now().minusDays(1))
                .status(Status.REJECTED)
                .build();
        when(bookingDBRepository.findByItemOwnerIdAndEndIsBefore(anyLong(), any(LocalDateTime.class), any(Sort.class))).thenReturn(List.of(bookingPast));

        List<BookingDto> bookingDtos = bookingService.getAllByOwner(1L, BookingState.PAST);
        BookingDto bookingDto = bookingDtos.get(0);
        assertEquals(bookingPast.getId(), bookingDto.getId());
        assertEquals(bookingPast.getBooker().getId(), bookingDto.getBooker().getId());
        assertEquals(bookingPast.getItem().getId(), bookingDto.getItem().getId());
    }

    @SneakyThrows
    @Test
    void getAllByOwnerTest_StateFuture() {

        when(bookingDBRepository.findByItemOwnerIdAndStartIsAfterAndStatusNotRejectedAndStatusNotCanceledIgnoreCase(anyLong(), any(LocalDateTime.class), any(Sort.class))).thenReturn(List.of(booking));

        List<BookingDto> bookingDtos = bookingService.getAllByOwner(1L, BookingState.FUTURE);
        BookingDto bookingDto = bookingDtos.get(0);
        assertEquals(booking.getId(), bookingDto.getId());
        assertEquals(booking.getBooker().getId(), bookingDto.getBooker().getId());
        assertEquals(booking.getItem().getId(), bookingDto.getItem().getId());
    }

    @SneakyThrows
    @Test
    void getAllByOwnerTest_StateAll() {

        when(bookingDBRepository.findByItemOwnerId(anyLong(), any(Sort.class))).thenReturn(List.of(booking));

        List<BookingDto> bookingDtos = bookingService.getAllByOwner(1L, BookingState.ALL);
        BookingDto bookingDto = bookingDtos.get(0);
        assertEquals(booking.getId(), bookingDto.getId());
        assertEquals(booking.getBooker().getId(), bookingDto.getBooker().getId());
        assertEquals(booking.getItem().getId(), bookingDto.getItem().getId());
    }

    @SneakyThrows
    @Test
    void getAllByOwnerTest_StateWaiting() {

        when(bookingDBRepository.findByItemOwnerIdAndStatus(anyLong(), eq(Status.WAITING), any(Sort.class))).thenReturn(List.of(booking));

        List<BookingDto> bookingDtos = bookingService.getAllByOwner(1L, BookingState.WAITING);
        BookingDto bookingDto = bookingDtos.get(0);
        assertEquals(booking.getId(), bookingDto.getId());
        assertEquals(booking.getBooker().getId(), bookingDto.getBooker().getId());
        assertEquals(booking.getItem().getId(), bookingDto.getItem().getId());
    }

    @SneakyThrows
    @Test
    void getAllByOwnerTest_StateRejected() {

        Booking bookingPast = booking.toBuilder()
                .end(LocalDateTime.now().minusDays(1))
                .status(Status.REJECTED)
                .build();

        when(bookingDBRepository.findByItemOwnerIdAndStatus(anyLong(), eq(Status.REJECTED), any(Sort.class))).thenReturn(List.of(bookingPast));

        List<BookingDto> bookingDtos = bookingService.getAllByOwner(1L, BookingState.REJECTED);
        BookingDto bookingDto = bookingDtos.get(0);
        assertEquals(bookingPast.getId(), bookingDto.getId());
        assertEquals(bookingPast.getBooker().getId(), bookingDto.getBooker().getId());
        assertEquals(bookingPast.getItem().getId(), bookingDto.getItem().getId());
    }
}