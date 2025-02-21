package ru.practicum.shareit.item.dal.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dal.UserBaseRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemBaseRepositoryTest {

    private final ItemBaseRepository itemBaseRepository;
    private final UserBaseRepository userBaseRepository;

    private User user;
    private Item item;

    @Test
    void update() {
        user = userBaseRepository.save(User.builder().name("name").email("email@mail.ru").build());
        item = Item.builder()
                .name("name")
                .owner(user)
                .description("description")
                .available(true)
                .build();
        item = itemBaseRepository.save(item);
        itemBaseRepository.update(item.toBuilder().name("newItem").build());

        List<Item> items = itemBaseRepository.findAll();
        Item updatedItem = items.get(0);

        assertEquals(1, items.size());
        assertEquals(item.getId(), updatedItem.getId());
        assertEquals(item.getDescription(), updatedItem.getDescription());
        assertEquals("newItem", updatedItem.getName());
        assertEquals(item.getOwner(), updatedItem.getOwner());
        assertEquals(item.isAvailable(), updatedItem.isAvailable());
    }
}