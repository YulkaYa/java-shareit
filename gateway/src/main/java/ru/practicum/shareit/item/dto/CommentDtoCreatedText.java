package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.common.Create;
import ru.practicum.shareit.common.StorageData;

import java.time.LocalDateTime;

@SuperBuilder(toBuilder = true)
@Data
@RequiredArgsConstructor
public class CommentDtoCreatedText extends StorageData {
    private LocalDateTime created = LocalDateTime.now();
    @NotBlank(groups = Create.class, message = "Текст комментария не может быть пустым")
    private String text;
}
