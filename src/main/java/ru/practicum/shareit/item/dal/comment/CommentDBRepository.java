package ru.practicum.shareit.item.dal.comment;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.user.User;

public interface CommentDBRepository extends JpaRepository<Comment, Long> {
}
