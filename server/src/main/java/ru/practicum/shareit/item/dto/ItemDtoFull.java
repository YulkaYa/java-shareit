package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.booking.dto.BookingDto;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */

@SuperBuilder(toBuilder = true)
@Data
@RequiredArgsConstructor
public class ItemDtoFull extends ItemDtoWithoutDates {
    private long ownerId;
    private String name;
    private String description;
    private String available;
    private List<CommentDto> comments;
    private BookingDto lastBooking;
    private BookingDto nextBooking;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private long requestId;

}
