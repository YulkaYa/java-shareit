package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.common.Create;
import ru.practicum.shareit.common.StorageData;
import ru.practicum.shareit.item.dto.ItemDtoWithoutDates;
import java.time.LocalDateTime;
import java.util.List;

@SuperBuilder(toBuilder = true)
@Data
@RequiredArgsConstructor
public class ItemRequestDtoWithItems extends StorageData { // todo разобраться с валидациями
    @NotBlank(groups = Create.class, message = "Описание не может быть пустым")
    @Pattern(regexp = ".*\\S+.*", message = "Описание не может состоять из пробелов или быть пустым")
    private String description;
    private LocalDateTime created;
    @NotBlank(groups = Create.class, message = "Id пользователя не может быть пустым")
    private long requestorId;
    private List<ItemDtoWithoutDates> items;
}
