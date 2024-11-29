package ru.practicum.shareit.item.dal.comment;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface CommentDBRepository extends JpaRepository<Comment, Long> {
      List<Comment> findByItemId(long itemId);
}
