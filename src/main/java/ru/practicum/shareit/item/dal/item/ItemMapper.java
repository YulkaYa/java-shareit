package ru.practicum.shareit.item.dal.item;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ItemMapper {
    ItemMapper INSTANCE = Mappers.getMapper(ItemMapper.class);

    Item toItem(ItemDto itemDto);

    ItemDto itemToItemDto(Item item);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "owner", ignore = true)
    Item updateFromDto(ItemDto itemDto, @MappingTarget Item item);

    List<ItemDto> listItemToListItemDto(List<Item> listItems);
}
