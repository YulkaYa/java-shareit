package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.common.Create;
import ru.practicum.shareit.common.Update;

import java.util.List;

/**
 * TODO Sprint add-controllers.
 */

@SuperBuilder(toBuilder = true)
@Data
@RequiredArgsConstructor
public class ItemDtoFull extends ItemDtoWithoutDates {
    @NotNull(groups = Update.class, message = "Id при обновлении не должен быть пустым")
    private long ownerId;
    @NotBlank(groups = Create.class, message = "Название не может быть пустым")
    @Pattern(regexp = ".*\\S+.*", message = "Название не может состоять из пробелов или быть пустым")
    private String name;
    @NotBlank(groups = Create.class, message = "Описание не может быть пустым")
    @Pattern(regexp = ".*\\S+.*", message = "Описание не может состоять из пробелов или быть пустым")
    private String description;
    @NotBlank(groups = Create.class, message = "Доступность не может быть пустой")
    private String available;
    private List<CommentDto> comments;
    private BookingDto lastBooking;
    private BookingDto nextBooking;

}
