package ru.practicum.shareit.request.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import java.time.LocalDateTime;

@SuperBuilder(toBuilder = true)
@Data
@RequiredArgsConstructor

public class ItemRequestCreatedDto {
    private String description;
    private final LocalDateTime created = LocalDateTime.now();
}
