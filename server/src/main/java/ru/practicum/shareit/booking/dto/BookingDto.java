package ru.practicum.shareit.booking.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.common.StorageData;
import ru.practicum.shareit.item.dto.ItemDtoBase;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@SuperBuilder(toBuilder = true)
@Data
@RequiredArgsConstructor
public class BookingDto extends StorageData {
    private ItemDtoBase item;
    private UserDto booker;
    private Status status;
    private LocalDateTime start;
    private LocalDateTime end;
}
