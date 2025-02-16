package ru.practicum.shareit.booking.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.common.Create;
import ru.practicum.shareit.common.StorageData;
import ru.practicum.shareit.item.dto.ItemDtoBase;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

@SuperBuilder(toBuilder = true)
@Data
@RequiredArgsConstructor
public class BookingDto extends StorageData {
    @NotNull(message = "Товар должен быть указан")
    private ItemDtoBase item;
    @NotNull(message = "Арендатор должен быть указан")
    private UserDto booker;
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Статус должен быть указан")
    private Status status;
    @NotNull(groups = Create.class, message = "Дата старта аренды не может быть пустым")
    private LocalDateTime start;
    @NotNull(groups = Create.class, message = "Дата старта аренды не может быть пустым")
    private LocalDateTime end;

}
