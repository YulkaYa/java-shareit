package ru.practicum.shareit.item.dal.item;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ItemMapper {
    ItemMapper INSTANCE = Mappers.getMapper(ItemMapper.class);
    @Mapping(source = "owner", target = "owner")
    @Mapping(source = "itemDto.id", target = "id")
    @Mapping(source = "itemDto.name", target = "name")
    Item toItem( ItemDto itemDto, User owner);
    @Mapping(source = "owner.id", target = "ownerId")
    ItemDto itemToItemDto(Item item);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "owner", ignore = true)
    Item updateFromDto(ItemDto itemDto, @MappingTarget Item item);
    @Mapping(source = "owner.id", target = "ownerId")
    List<ItemDto> listItemToListItemDto(List<Item> listItems);
}
