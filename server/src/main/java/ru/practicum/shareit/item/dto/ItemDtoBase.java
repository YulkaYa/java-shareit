package ru.practicum.shareit.item.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.common.StorageData;

@SuperBuilder(toBuilder = true)
@Data
@RequiredArgsConstructor
public class ItemDtoBase extends StorageData {
    private String name;
    private String description;
    private String available;
    private long requestId;
}
