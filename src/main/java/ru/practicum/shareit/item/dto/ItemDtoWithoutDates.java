package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.common.Create;
import ru.practicum.shareit.common.StorageData;
import ru.practicum.shareit.common.Update;

/**
 * TODO Sprint add-controllers.
 */

@SuperBuilder(toBuilder = true)
@Data
@RequiredArgsConstructor
public class ItemDtoWithoutDates extends StorageData {
    @NotNull(groups = Update.class, message = "Id при обновлении не должен быть пустым") //todo убрать update.class так как владелец должен быть всегда указан?
    private long ownerId;
    @NotBlank(groups = Create.class, message = "Название не может быть пустым")
    @Pattern(regexp = ".*\\S+.*", message = "Название не может состоять из пробелов или быть пустым")
    private String name;
    @NotBlank(groups = Create.class, message = "Описание не может быть пустым")
    @Pattern(regexp = ".*\\S+.*", message = "Описание не может состоять из пробелов или быть пустым")
    private String description;
    @NotBlank(groups = Create.class, message = "Доступность не может быть пустой")
    private String available;
}
