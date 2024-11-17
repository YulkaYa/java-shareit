package ru.practicum.shareit.item.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.common.StorageData;

/**
 * TODO Sprint add-controllers.
 */
@SuperBuilder(toBuilder = true)
@Data
@RequiredArgsConstructor
public class Item extends StorageData {
    private long ownerId;
    private String name;
    private String description;
    private boolean isAvailable;
}
