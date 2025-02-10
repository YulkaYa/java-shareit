package ru.practicum.shareit.request.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.common.StorageData;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@SuperBuilder(toBuilder = true)
@Data
@RequiredArgsConstructor

// todo пробую без extends StorageData  public class ItemRequestCreatedDto extends StorageData {
public class ItemRequestCreatedDto extends StorageData {
    private String description;
    private LocalDateTime created = LocalDateTime.now();
}
