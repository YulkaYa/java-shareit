package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dal.BookingDBRepository;
import ru.practicum.shareit.item.dal.comment.CommentDBRepository;
import ru.practicum.shareit.item.dal.item.ItemDBRepository;
import ru.practicum.shareit.item.dto.CommentDtoCreatedText;
import ru.practicum.shareit.item.dto.ItemDtoFull;
import ru.practicum.shareit.item.dto.ItemDtoWithoutDates;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dal.UserDBRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemServiceImplTest {

    private final ItemServiceImpl itemService;
    private final ItemDBRepository itemDBRepository;
    private final UserDBRepository userDBRepository;
    private final BookingDBRepository bookingDBRepository;
    private final CommentDBRepository commentDBRepository;

    private final Long requestId = 1L;
    private Long itemId;
    private User user;
    private User user2;
    private Long userId;
    private Long userId2;
    private ItemDtoWithoutDates itemDtoWithoutDates;
    private ItemDtoWithoutDates savedItemDtoWithoutDates;
    private ItemDtoWithoutDates item2;
    private ItemDtoWithoutDates item3;

    @BeforeEach
    void setUp() {
        user = userDBRepository.save(User.builder().name("name").email("email@mail.ru").build());
        userId = user.getId();
        user2 = userDBRepository.save(User.builder().name("name2").email("email2@mail.ru").build());
        userId2 = user2.getId();
        itemDtoWithoutDates = ItemDtoWithoutDates.builder()
                .name("sdsd name1 sdsd")
                .description("description 1")
                .available("false")
                .ownerId(userId)
                .build();
        savedItemDtoWithoutDates = itemService.create(userId, itemDtoWithoutDates);
        itemId = savedItemDtoWithoutDates.getId();
        item2 = ItemDtoWithoutDates.builder()
                .name("name2")
                .description("description2")
                .available("true")
                .build();
        item2 = itemService.create(userId, item2);
        item3 = ItemDtoWithoutDates.builder()
                .name("name3")
                .description("description3")
                .available("true")
                .build();
        item3 = itemService.create(userId2, item3);
    }

    @AfterEach
    void tearDown() {
        commentDBRepository.deleteAll();
        bookingDBRepository.deleteAll();
        itemDBRepository.deleteAll();
        userDBRepository.deleteAll();
    }

    @Test
    void createAndGetItemByIdTest() {
        ItemDtoFull userDtoAfterSave = itemService.getItemById(userId, savedItemDtoWithoutDates.getId());

        assertNotEquals(0L, savedItemDtoWithoutDates.getId());
        assertEquals(savedItemDtoWithoutDates.getId(), userDtoAfterSave.getId());
        assertEquals(itemDtoWithoutDates.getName(), userDtoAfterSave.getName());
        assertEquals(itemDtoWithoutDates.getDescription(), userDtoAfterSave.getDescription());
        assertEquals(itemDtoWithoutDates.getAvailable(), userDtoAfterSave.getAvailable());
        assertEquals(itemDtoWithoutDates.getOwnerId(), userDtoAfterSave.getOwnerId());
    }

    @Test
    void getItemsByUserId() {
        List<ItemDtoFull> items = itemService.getItemsByUserId(userId);

        assertEquals(2, items.size());
        assertEquals(itemId, items.get(0).getId());
        assertEquals(item2.getId(), items.get(1).getId());
    }

    @Test
    void updateTest() {
        ItemDtoWithoutDates item4 = savedItemDtoWithoutDates.toBuilder().id(6L).description("изменено").build();
        String descriprionOfItem2 = item2.getDescription();

        itemService.update(userId, savedItemDtoWithoutDates.getId(), item4);

        List<ItemDtoFull> items = itemService.getItemsByUserId(userId);

        assertEquals("изменено", items.get(0).getDescription());
        assertEquals(itemDtoWithoutDates.getName(), items.get(0).getName());
        assertEquals(itemId, items.get(0).getId());
        assertEquals(descriprionOfItem2, items.get(1).getDescription());
    }

    @Test
    void searchItemsByText() {
        String description = item3.getDescription();
        String name = item3.getName();
        String subStringName = "name";
        String subStringDescription = "description";

        List<ItemDtoWithoutDates> items = itemService.searchItemsByText(description);

        assertEquals(1, items.size());
        assertEquals(description, items.get(0).getDescription());

        items = itemService.searchItemsByText(name);

        assertEquals(1, items.size());
        assertEquals(name, items.get(0).getName());

        items = itemService.searchItemsByText(subStringName);

        assertEquals(2, items.size());
        assert (items.get(0).getName().contains(subStringName));
        assert (items.get(1).getName().contains(subStringName));
        assertEquals(item2.getId(), items.get(0).getId());
        assertEquals(item3.getId(), items.get(1).getId());

        items = itemService.searchItemsByText(subStringDescription);

        assertEquals(2, items.size());
        assert (items.get(0).getDescription().contains(subStringDescription));
        assert (items.get(1).getDescription().contains(subStringDescription));
        assertEquals(item2.getId(), items.get(0).getId());
        assertEquals(item3.getId(), items.get(1).getId());
    }

    @SneakyThrows
    @Test
    void createComment() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yy hh:mm:ss");
        Booking booking = bookingDBRepository.save(Booking.builder()
                .booker(user)
                .start(LocalDateTime.now().minusMonths(1))
                .end(LocalDateTime.now().minusHours(5))
                .item(itemDBRepository.findById(item3.getId()).get())
                .status(Status.APPROVED)
                .build());
        CommentDtoCreatedText commentDtoCreatedText = CommentDtoCreatedText.builder().text("text of comment").build();

        itemService.createComment(userId, item3.getId(), commentDtoCreatedText);
        ItemDtoWithoutDates item = itemService.getItemById(userId, item3.getId());

        assertEquals(item3.getId(), item.getId());
        assertEquals(commentDtoCreatedText.getText(), item.getComments().get(0).getText());
        assertEquals(commentDtoCreatedText.getCreated().format(dateTimeFormatter), item.getComments().get(0).getCreated().format(dateTimeFormatter));
        assertEquals(item3.getId(), item.getComments().get(0).getItemId());
        assertEquals(user.getName(), item.getComments().get(0).getAuthorName());
    }
}