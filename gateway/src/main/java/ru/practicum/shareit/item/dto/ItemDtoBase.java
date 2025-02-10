package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.common.Create;
import ru.practicum.shareit.common.StorageData;

/**
 * TODO Sprint add-controllers.
 */

@SuperBuilder(toBuilder = true)
@Data
@RequiredArgsConstructor
public class ItemDtoBase extends StorageData {
    @NotBlank(groups = Create.class, message = "Название не может быть пустым")
    private String name;
    @NotBlank(groups = Create.class, message = "Описание не может быть пустым")
    @Pattern(regexp = ".*\\S+.*", message = "Описание не может состоять из пробелов или быть пустым")
    private String description;
    @NotBlank(groups = Create.class, message = "Доступность не может быть пустой")
    private String available;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private long requestId;
}
