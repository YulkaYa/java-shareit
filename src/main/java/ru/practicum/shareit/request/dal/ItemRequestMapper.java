package ru.practicum.shareit.request.dal;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ItemRequestMapper {
    ItemRequestMapper INSTANCE = Mappers.getMapper(ItemRequestMapper.class);

    ItemRequest toItemRequest(ItemRequest itemRequest);

    ItemRequestDto itemRequestToItemRequestDto(ItemRequest itemRequest);

    ItemRequest updateFromDto(ItemRequestDto itemRequestDto, @MappingTarget ItemRequest itemRequest);

    List<ItemRequestDto> listItemRequestToListItemRequestDto(List<ItemRequest> listItemRequest);
}
