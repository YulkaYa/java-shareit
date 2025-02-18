package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.dal.item.ItemDBRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dal.ItemRequestDBRepository;
import ru.practicum.shareit.request.dto.ItemRequestBaseDto;
import ru.practicum.shareit.request.dto.ItemRequestCreatedDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoWithItems;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dal.UserDBRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemRequestServiceImplTest {

    private final ItemRequestServiceImpl itemRequestService;
    private final UserDBRepository userDBRepository;
    private final ItemRequestDBRepository itemRequestDBRepository;
    private final ItemDBRepository itemDBRepository;

    private ItemRequestBaseDto itemRequestBaseDto1;
    private ItemRequest itemRequest1;
    private ItemRequest itemRequest2;
    private ItemRequest itemRequest3;
    private User user;
    private User user1;

    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yy hh:mm:ss");

    @BeforeEach
    void setUp() {
        user = userDBRepository.save(User.builder()
                .name("name")
                .email("email@mail.com")
                .build());
        user1 = userDBRepository.save(User.builder()
                .name("name1")
                .email("email1@mail.com")
                .build());
        itemRequest1 = itemRequestDBRepository.save(ItemRequest.builder()
                .requestor(user)
                .created(LocalDateTime.now().minusMonths(1))
                .description("description1")
                .build());
        itemRequest2 = itemRequestDBRepository.save(ItemRequest.builder()
                .requestor(user)
                .created(LocalDateTime.now())
                .description("description2")
                .build());
        itemRequest3 = itemRequestDBRepository.save(ItemRequest.builder()
                .requestor(user1)
                .created(LocalDateTime.now())
                .description("description3")
                .build());
    }

    @AfterEach
    void tearDown() {
        itemDBRepository.deleteAll();
        itemRequestDBRepository.deleteAll();
        userDBRepository.deleteAll();
    }

    @Test
    void createTest() {
        itemRequestDBRepository.deleteAll();
        ItemRequestCreatedDto itemRequestCreatedDto1 = ItemRequestCreatedDto.builder()
                .description("description11")
                .build();
        itemRequestBaseDto1 = itemRequestService.create(user.getId(), itemRequestCreatedDto1);
        ItemRequest itemRequest = itemRequestDBRepository.findById(itemRequestBaseDto1.getId()).get();

        assertEquals(itemRequestCreatedDto1.getCreated().format(dateTimeFormatter), itemRequest.getCreated().format(dateTimeFormatter));
        assertEquals(itemRequestCreatedDto1.getDescription(), itemRequest.getDescription());
        assertEquals(user.getId(), itemRequest.getRequestor().getId());
    }

    @Test
    void getItemsRequestsByUserIdTest() {
        List<ItemRequestDtoWithItems> itemRequests = itemRequestService.getItemsRequestsByUserId(user.getId());

        assertEquals(2, itemRequests.size());
        assertEquals(user.getId(), itemRequests.getFirst().getRequestorId());
        assertEquals(user.getId(), itemRequests.getLast().getRequestorId());
        assertEquals(itemRequest2.getDescription(), itemRequests.get(0).getDescription());
        assertEquals(itemRequest1.getDescription(), itemRequests.get(1).getDescription());
    }

    @Test
    void getRequestsFromOtherUsers() {
        Item item = Item.builder()
                .name("nameItem")
                .description("descriptionItem")
                .available(true)
                .owner(user)
                .request(itemRequest3)
                .build();
        item = itemDBRepository.save(item);
        List<ItemRequestDtoWithItems> itemRequests = itemRequestService.getRequestsFromOtherUsers(user.getId());

        assertEquals(1, itemRequests.size());
        assertEquals(user1.getId(), itemRequests.get(0).getRequestorId());
        assertEquals(itemRequest3.getDescription(), itemRequests.get(0).getDescription());
        assertEquals(item.getId(), itemRequests.get(0).getItems().get(0).getId());
    }

    @Test
    void getItemsRequestsByRequestId() {
        Item item = Item.builder()
                .name("nameItem")
                .description("descriptionItem")
                .available(true)
                .owner(user)
                .request(itemRequest3)
                .build();
        item = itemDBRepository.save(item);
        Item item1 = Item.builder()
                .name("nameItem1")
                .description("descriptionItem1")
                .available(true)
                .owner(user)
                .request(itemRequest3)
                .build();
        item1 = itemDBRepository.save(item1);
        ItemRequestDtoWithItems itemRequestDtoWithItems = itemRequestService.getItemsRequestsByRequestId(itemRequest3.getId());

        assertEquals(itemRequest3.getId(), itemRequestDtoWithItems.getId());
        assertEquals(2, itemRequestDtoWithItems.getItems().size());
        assertEquals(item.getName(), itemRequestDtoWithItems.getItems().get(0).getName());
        assertEquals(item1.getName(), itemRequestDtoWithItems.getItems().get(1).getName());
    }
}