package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dal.BookingDBRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoCreated;
import ru.practicum.shareit.exception.ConditionsNotMetException;
import ru.practicum.shareit.item.dal.item.ItemDBRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dal.UserDBRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingServiceImplTest {

    private final BookingServiceImpl bookingService;
    private final BookingDBRepository bookingDBRepository;
    private final ItemDBRepository itemDBRepository;
    private final UserDBRepository userDBRepository;

    private User user;
    private User user1;
    private User user2;
    Item item;
    Item item1;
    Item item2;
    Booking booking;
    Booking booking1;
    Booking booking2;
    BookingDtoCreated bookingDtoCreated;
    BookingDtoCreated bookingDtoCreated1;
    LocalDateTime start;
    LocalDateTime end;
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yy hh:mm:ss");


    @BeforeEach
    void setUp() {
        start = LocalDateTime.now();
        end = LocalDateTime.now().plusMonths(1);
        user = userDBRepository.save(User.builder()
                .name("name")
                .email("email@mail.com")
                .build());
        user1 = userDBRepository.save(User.builder()
                .name("name1")
                .email("email1@mail.com")
                .build());
        user2 = userDBRepository.save(User.builder()
                .name("name2")
                .email("email2@mail.com")
                .build());
        item = itemDBRepository.save(Item.builder()
                .name("nameItem")
                .description("descriptionItem")
                .available(true)
                .owner(user)
                .build());
        item1 = itemDBRepository.save(Item.builder()
                .name("nameItem1")
                .description("descriptionItem1")
                .available(false)
                .owner(user)
                .build());
        item2 = itemDBRepository.save(Item.builder()
                .name("nameItem1")
                .description("descriptionItem1")
                .available(true)
                .owner(user)
                .build());
        booking = Booking.builder()
                .status(Status.WAITING)
                .booker(user1)
                .item(item)
                .start(start)
                .end(end)
                .build();
        booking1 = Booking.builder()
                .status(Status.APPROVED)
                .booker(user1)
                .item(item2)
                .start(start)
                .end(end)
                .build();
        booking2 = Booking.builder()
                .status(Status.WAITING)
                .booker(user2)
                .item(item2)
                .start(start)
                .end(end)
                .build();
    }

    @AfterEach
    void tearDown() {
        bookingDBRepository.deleteAll();
        itemDBRepository.deleteAll();
        userDBRepository.deleteAll();
    }

    @Test
    void createTest_Positive() {
        BookingDtoCreated bookingDtoCreated = BookingDtoCreated.builder()
                .itemId(item.getId())
                .start(start)
                .end(end)
                .build();
        BookingDtoCreated bookingDtoCreated1 = BookingDtoCreated.builder()
                .itemId(item1.getId())
                .start(start)
                .end(end)
                .build();

        BookingDto bookingDto = bookingService.create(user1.getId(), bookingDtoCreated);

       List<Booking> bookings = bookingDBRepository.findAll();
       Booking bookingCreated = bookings.get(0);

       assertEquals(1, bookings.size());
       assertEquals(bookingDto.getId(), bookingCreated.getId());
       assertEquals(start.format(dateTimeFormatter), bookingCreated.getStart().format(dateTimeFormatter));
       assertEquals(end.format(dateTimeFormatter), bookingCreated.getEnd().format(dateTimeFormatter));
       assertEquals(user1, bookingCreated.getBooker());
       assertEquals(item, bookingCreated.getItem());
    }

    @Test
    void createTest_Negative() {
        BookingDtoCreated bookingDtoCreated1 = BookingDtoCreated.builder()
                .itemId(item1.getId())
                .start(start)
                .end(end)
                .build();
        bookingDBRepository.deleteAll();
        Throwable throwable = assertThrows(ConditionsNotMetException.class, () -> bookingService.create(user1.getId(), bookingDtoCreated1));
        assertEquals("Товар с id = " + item1.getId() + " недоступен для бронирования", throwable.getMessage());
    }

    @Test
    void approveTest_Positive() {

        booking = bookingDBRepository.save(booking);

        bookingService.approve(booking.getItem().getOwner().getId(),
                booking.getId(),
                true);

        Booking bookingApproved = bookingDBRepository.findById(booking.getId()).get();

        assertEquals(booking.getId(), bookingApproved.getId());
        assertEquals(booking.getBooker(), bookingApproved.getBooker());
        assertEquals(Status.APPROVED,bookingApproved.getStatus());
    }

    @Test
    void getTest_Positive() {
        bookingDBRepository.save(booking);

        BookingDto bookingDto = bookingService.get(user.getId(), booking.getId());
        assertEquals(booking.getId(), bookingDto.getId());
        assertEquals(booking.getBooker().getId(), bookingDto.getBooker().getId());
        assertEquals(booking.getItem().getId(), bookingDto.getItem().getId());

        bookingDto = bookingService.get(user1.getId(), booking.getId());
        assertEquals(booking.getId(), bookingDto.getId());
        assertEquals(booking.getBooker().getId(), bookingDto.getBooker().getId());
        assertEquals(booking.getItem().getId(), bookingDto.getItem().getId());
    }

    @Test
    void getTest_Negative() {
        bookingDBRepository.save(booking);

        Throwable throwable = assertThrows(ConditionsNotMetException.class, () -> bookingService.get(user2.getId(), booking.getId()));
        assertEquals("Id владельца/арендатора и id пользователя в запросе не совпадают", throwable.getMessage());
    }

    @Test
    void getAllByUserTest() {
        bookingDBRepository.save(booking);
        bookingDBRepository.save(booking1);
        bookingDBRepository.save(booking2);

        List<BookingDto> bookingDtos = bookingService.getAllByUser(user1.getId(), BookingState.WAITING);

        assertEquals(1, bookingDtos.size());
        BookingDto bookingDto = bookingDtos.get(0);

        assertEquals(booking.getId(), bookingDto.getId());
        assertEquals(booking.getBooker().getId(), bookingDto.getBooker().getId());
        assertEquals(booking.getItem().getId(), bookingDto.getItem().getId());

        bookingDtos = bookingService.getAllByUser(user1.getId(), BookingState.ALL);

        assertEquals(2, bookingDtos.size());
        bookingDto = bookingDtos.get(0);
        BookingDto bookingDto1 = bookingDtos.get(1);

        assertEquals(booking.getId(), bookingDto.getId());
        assertEquals(booking.getBooker().getId(), bookingDto.getBooker().getId());
        assertEquals(booking.getItem().getId(), bookingDto.getItem().getId());
        assertEquals(booking1.getId(), bookingDto1.getId());
    }

    @Test
    void getAllByOwnerTest() {
        bookingDBRepository.save(booking);
        bookingDBRepository.save(booking1);
        bookingDBRepository.save(booking2);

        List<BookingDto> bookingDtos = bookingService.getAllByOwner(user.getId(), BookingState.WAITING);

        assertEquals(2, bookingDtos.size());
        BookingDto bookingDto = bookingDtos.get(0);
        BookingDto bookingDto1 = bookingDtos.get(1);

        assertEquals(booking.getId(), bookingDto.getId());
        assertEquals(booking.getBooker().getId(), bookingDto.getBooker().getId());
        assertEquals(booking.getItem().getId(), bookingDto.getItem().getId());

        assertEquals(booking2.getId(), bookingDto1.getId());
    }
}