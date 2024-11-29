package ru.practicum.shareit.item.dal.item;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.item.dto.ItemDtoWithoutDates;
import ru.practicum.shareit.item.dto.ItemDtoFull;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ItemMapper {
    ItemMapper INSTANCE = Mappers.getMapper(ItemMapper.class);

    @Mapping(source = "owner", target = "owner")
    @Mapping(source = "itemDtoWithoutDates.id", target = "id")
    @Mapping(source = "itemDtoWithoutDates.name", target = "name")
    Item toItem(ItemDtoWithoutDates itemDtoWithoutDates, User owner);
    @Mapping(source = "owner.id", target = "ownerId")
    ItemDtoWithoutDates itemToItemDto(Item item);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "owner", ignore = true)
    Item updateFromDto(ItemDtoWithoutDates itemDtoWithoutDates, @MappingTarget Item item);

//todo    @Mapping(source = "owner.id", target = "ownerId")
    List<ItemDtoWithoutDates> listItemToListItemDto(List<Item> listItems);

    @Mapping(source = "item.owner.id", target = "ownerId")
    ItemDtoFull itemToItemDtoWithAll(Item item, List<Comment> comments);
}
