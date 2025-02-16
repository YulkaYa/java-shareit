package ru.practicum.shareit.request.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.common.StorageData;
import java.time.LocalDateTime;

@SuperBuilder(toBuilder = true)
@Data
@RequiredArgsConstructor
public class ItemRequestBaseDto extends StorageData { // todo разобраться с валидациями
    private String description;
    private LocalDateTime created;
    private long requestorId;
}
