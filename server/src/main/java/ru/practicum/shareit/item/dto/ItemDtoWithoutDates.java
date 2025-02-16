package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.common.StorageData;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@Data
@RequiredArgsConstructor
public class ItemDtoWithoutDates extends StorageData {
    private long ownerId;
    private String name;
    private String description;
    private String available;
    private List<CommentDto> comments;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private long requestId;
}
