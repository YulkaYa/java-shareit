package ru.practicum.shareit.request.dal;


import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.ItemRequest;

import java.util.List;

public interface ItemRequestDBRepository extends JpaRepository<ItemRequest, Long> {
    List<ItemRequest> findByRequestorId(long requestorId, Sort sort);
    List<ItemRequest> findAllByRequestorIdNot(long requestorId, PageRequest page);
    List<ItemRequest> findAllByRequestorIdNot(long requestorId, Sort sort);
}
