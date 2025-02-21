package ru.practicum.shareit.user.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.common.StorageData;

@SuperBuilder(toBuilder = true)
@Data
@RequiredArgsConstructor
public class UserDto extends StorageData {
    private String name;
    private String email;
}
