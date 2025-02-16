package ru.practicum.shareit.request.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.common.StorageData;
import ru.practicum.shareit.item.dto.ItemDtoWithoutDates;
import java.time.LocalDateTime;
import java.util.List;

@SuperBuilder(toBuilder = true)
@Data
@RequiredArgsConstructor
public class ItemRequestDtoWithItems extends StorageData { // todo разобраться с валидациями
    private String description;
    private LocalDateTime created;
    private long requestorId;
    private List<ItemDtoWithoutDates> items;
}
