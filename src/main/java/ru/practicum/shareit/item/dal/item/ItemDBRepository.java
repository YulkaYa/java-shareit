package ru.practicum.shareit.item.dal.item;

import org.apache.coyote.Request;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import java.util.List;

@Primary
public interface ItemDBRepository extends JpaRepository<Item, Long>, ItemBaseRepository {
    @Override
    Item save(Item data);

    @Override
    default Item update(Item data) {
        return save(data);
    }

    @Query("select distinct it from Item it where it.description like %?1 or it.name like %?1")
    List<Item> findByDescriptionOrName(String text);
}
