package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.ItemServiceImpl;
import ru.practicum.shareit.item.dal.item.ItemDBRepository;
import ru.practicum.shareit.request.dal.ItemRequestDBRepository;
import ru.practicum.shareit.request.dto.ItemRequestBaseDto;
import ru.practicum.shareit.request.dto.ItemRequestCreatedDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoWithItems;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserServiceImpl;
import ru.practicum.shareit.user.dal.UserDBRepository;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemRequestServiceImplTest {

    private final ItemRequestServiceImpl itemRequestService;
    private final UserServiceImpl userService;
    private final UserDBRepository userDBRepository;
    private final ItemRequestDBRepository itemRequestDBRepository;
    private final ItemDBRepository itemDBRepository;

    ItemServiceImpl itemService;
    private UserDto userDto = UserDto.builder()
            .name("name")
            .email("email@mail.com")
            .build();
    private UserDto userDto1 = UserDto.builder()
            .name("name1")
            .email("email1@mail.com")
            .build();

    private ItemRequestCreatedDto itemRequestCreatedDto1;
    private ItemRequestCreatedDto itemRequestCreatedDto2;
    private ItemRequestBaseDto itemRequestBaseDto1;
    private ItemRequestBaseDto itemRequestBaseDto2;
    private ItemRequest itemRequest1;
    private ItemRequest itemRequest2;
    private ItemRequest itemRequest3;
    private User user;
    private User user1;
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yy hh:mm:ss");

    @BeforeEach
    void setUp() {
        userDto = userService.create(userDto);
        user = userDBRepository.findById(userDto.getId()).get();
        userDto1 = userService.create(userDto1);
        user1 = userDBRepository.findById(userDto1.getId()).get();
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
        tearDown();
        userDto = userService.create(userDto);

        ItemRequestCreatedDto itemRequestCreatedDto1 = ItemRequestCreatedDto.builder()
                .description("description1")
                .build();
        itemRequestBaseDto1 = itemRequestService.create(userDto.getId(), itemRequestCreatedDto1);
        ItemRequest itemRequest = itemRequestDBRepository.findById(itemRequestBaseDto1.getId()).get();

        assertEquals(itemRequestCreatedDto1.getCreated().format(dateTimeFormatter), itemRequest.getCreated().format(dateTimeFormatter));
        assertEquals(itemRequestCreatedDto1.getDescription(), itemRequest.getDescription());
        assertEquals(userDto.getId(), itemRequest.getRequestor().getId());
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
        List<ItemRequestDtoWithItems> itemRequests = itemRequestService.getRequestsFromOtherUsers(user.getId());

        assertEquals(1, itemRequests.size());
        assertEquals(user1.getId(), itemRequests.get(0).getRequestorId());
        assertEquals(itemRequest3.getDescription(), itemRequests.get(0).getDescription());
    }

/*    @Test
    void getItemsRequestsByRequestId() {
        Item item = Item.builder()
                .name("nameItem")
                .description("descriptionItem")
                .available(true)
                .owner(user)
                .request(itemRequest1)
                .build();
        itemDBRepository.save(item);
        Item item1 = Item.builder()
                .name("nameItem1")
                .description("descriptionItem1")
                .available(true)
                .owner(user1)
                .request(itemRequest1)
                .build();
        itemDBRepository.save(item1);
        ItemRequestDtoWithItems itemRequestDtoWithItems = itemRequestService.getItemsRequestsByRequestId(itemRequest1.getId());

        assertEquals(itemRequest1.getId(), itemRequestDtoWithItems.getId());
        assertEquals(item.getName(), itemRequestDtoWithItems.getItems().get(0).getName());
        assertEquals(item1.getName(), itemRequestDtoWithItems.getItems().get(1).getName());


*//*        itemRequestBaseDto2 = itemRequestService.create(userDto1.getId(), itemRequestCreatedDto2);
        ItemRequest itemRequest = itemRequestDBRepository.findById(itemRequestBaseDto2.getId()).get();
        ItemDtoWithoutDates item = ItemDtoWithoutDates.builder()
                .name("nameItem")
                .description("descriptionItem")
                .available("true")
                .requestId(itemRequestBaseDto2.getId())
                .ownerId(userDto.getId())
                .build();

        item = itemService.create(userDto.getId(), item);
        itemService.getItemById(userDto.getId(), item.getId());
        ItemRequestDtoWithItems itemRequestDtoWithItems = itemRequestService.getItemsRequestsByRequestId(itemRequest.getId());

        assertEquals(2L, itemRequestDtoWithItems.getId());
        assertEquals(1L, itemRequestDtoWithItems.getItems().get(0).getId());
        assertEquals(2L, itemRequestDtoWithItems.getItems().get(0).getRequestId());
        assertEquals(item.getName(), itemRequestDtoWithItems.getItems().get(0).getName());*//*
    }*/
}