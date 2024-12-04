package ru.practicum.shareit.request.dal;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Comment;

public interface ItemRequestDBRepository extends JpaRepository<Comment, Long> {
}
