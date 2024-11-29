package ru.practicum.shareit.item.dal.item;

import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Primary
public interface ItemDBRepository extends JpaRepository<Item, Long>, ItemBaseRepository {
    @Override
    Item save(Item data);

    @Override
    default Item update(Item data) {
        return save(data);
    }

    @Query("select i from Item i " +
            "where (upper(i.name) like upper(concat('%', ?1, '%')) " +
            "or upper(i.description) like upper(concat('%', ?1, '%'))) and i.available = true")
    List<Item> findByDescriptionOrName(String searchText);
}
