package ru.practicum.shareit.item.dal.item;

import org.apache.coyote.Request;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;

@Primary
public interface ItemDBRepository extends JpaRepository<Item, Long> {
}
