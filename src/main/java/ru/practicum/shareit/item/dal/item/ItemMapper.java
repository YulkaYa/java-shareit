package ru.practicum.shareit.item.dal.item;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDtoWithoutDates;
import ru.practicum.shareit.item.dto.ItemDtoFull;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
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

    List<ItemDtoWithoutDates> listItemToListItemDto(List<Item> listItems);

    List<ItemDtoFull> listItemToListItemDtoFull(List<Item> listItems);

    @Mapping(source = "item.owner.id", target = "ownerId")
    @Mapping(source = "item.id", target = "id")
    ItemDtoFull itemToItemDtoFull(Item item, List<CommentDto> comments, Booking lastBooking, Booking nextBooking);

    @Mapping(source = "item.owner.id", target = "ownerId")
    ItemDtoWithoutDates itemToItemItemDtoWithoutDates (Item item, List<CommentDto> comments);
}
