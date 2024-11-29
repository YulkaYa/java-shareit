package ru.practicum.shareit.item.dto;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.common.Create;
import ru.practicum.shareit.common.StorageData;
import ru.practicum.shareit.common.Update;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.Instant;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-controllers.
 */

@SuperBuilder(toBuilder = true)
@Data
@RequiredArgsConstructor
public class CommentDto extends StorageData {
    @NotBlank(groups = Create.class, message = "Товар должен быть указан")
    private long itemId;
    @NotBlank(groups = Create.class, message = "Владелец комментария должен быть указан")
    private String authorName;
    @NotBlank(groups = Create.class, message = "Текст комментария не может быть пустым")
    private String text;
    @NotBlank(groups = Create.class, message = "Время создания комментария не может быть пустым")
    private LocalDateTime created;
}
