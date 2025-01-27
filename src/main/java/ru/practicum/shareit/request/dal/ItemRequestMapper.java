package ru.practicum.shareit.request.dal;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
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

/*
    @Mapping(source = "requestor.id", target = "requestorId")
    <T extends ItemRequestBaseDto> T itemRequestToItemRequestDto(ItemRequest itemRequest);
todo */

    @Mapping(source = "requestor.id", target = "requestorId")
    ItemRequestBaseDto toItemRequestBaseDto(ItemRequest itemRequest);



/*    <T extends ItemRequestBaseDto> List<ItemRequestDtoWithItems> listItemRequestToListItemRequestDto(List<ItemRequest> listItemRequest);
   todo */

    @Mapping(source = "requestor.id", target = "requestorId")
    List<ItemRequestDtoWithItems> toListItemRequestDtoWithItems(List<ItemRequest> listItemRequest);

}
