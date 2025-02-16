package ru.practicum.shareit.booking.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.common.StorageData;
import java.time.LocalDateTime;

@SuperBuilder(toBuilder = true)
@Data
@RequiredArgsConstructor
public class BookingDtoCreated extends StorageData {
    private long itemId;
    private LocalDateTime start;
    private LocalDateTime end;
    private final Status status = Status.WAITING;
}
