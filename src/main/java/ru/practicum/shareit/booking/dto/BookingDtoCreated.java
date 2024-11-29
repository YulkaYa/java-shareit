package ru.practicum.shareit.booking.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.common.Create;
import ru.practicum.shareit.common.StorageData;

import java.time.Instant;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@SuperBuilder(toBuilder = true)
@Data
@RequiredArgsConstructor
public class BookingDtoCreated extends StorageData {
    @NotNull(groups = Create.class, message = "Товар должен быть указан")
    long itemId;
    @NotNull(groups = Create.class, message = "Дата старта аренды не может быть пустым")
    private LocalDateTime start;
    @NotNull(groups = Create.class, message = "Дата окончания аренды не может быть пустым")
    private LocalDateTime end;
    @NotNull(groups = Create.class, message = "Статус не может быть пустым")
    private Status status = Status.WAITING ;
}
