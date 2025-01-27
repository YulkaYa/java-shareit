package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.common.Create;
import ru.practicum.shareit.common.StorageData;
import ru.practicum.shareit.common.Update;
import ru.practicum.shareit.item.dto.CommentDto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@SuperBuilder(toBuilder = true)
@Data
@RequiredArgsConstructor

// todo пробую без extends StorageData  public class ItemRequestCreatedDto extends StorageData {
public class ItemRequestCreatedDto extends StorageData {
    @NotBlank(groups = Create.class, message = "Описание не может быть пустым")
    @Pattern(regexp = ".*\\S+.*", message = "Описание не может состоять из пробелов или быть пустым")
    private String description;
    private LocalDateTime created = LocalDateTime.now();
}
