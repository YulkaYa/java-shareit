package ru.practicum.shareit.request.dal;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestBaseDto;
import ru.practicum.shareit.request.dto.ItemRequestCreatedDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoWithItems;
import ru.practicum.shareit.user.User;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ItemRequestMapper {

    @Mapping(source = "requestor", target = "requestor")
    @Mapping(target = "id", ignore = true)
    ItemRequest toItemRequest(ItemRequestCreatedDto itemRequestCreatedDto, User requestor);

    @Mapping(source = "requestor.id", target = "requestorId")
    ItemRequestBaseDto toItemRequestBaseDto(ItemRequest itemRequest);

    @Mapping(source = "requestor.id", target = "requestorId")
    @Mapping(source = "items", target = "items")
    ItemRequestDtoWithItems toItemRequestDtoWithItems(ItemRequest itemRequest);

    @Mapping(source = "requestor.id", target = "requestorId")
    @Mapping(source = "items", target = "items")
    List<ItemRequestDtoWithItems> toListItemRequestDtoWithItems(List<ItemRequest> listItemRequest);

}
