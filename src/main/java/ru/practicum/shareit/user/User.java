package ru.practicum.shareit.user;

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
public class User extends StorageData {
    private String name;
    private String email;
}
