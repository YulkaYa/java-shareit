package ru.practicum.shareit.item.dal.comment;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentDtoCreatedText;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CommentMapper {
    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    @Mapping(source = "item", target = "item")
    @Mapping(source = "author", target = "author")
    @Mapping(source = "commentDtoOnlyText.id", target = "id")
    Comment commentDtoOnlyTextToComment(CommentDtoCreatedText commentDtoOnlyText, Item item, User author);

    @Mapping(source = "item.id", target = "itemId")
    @Mapping(source = "author.name", target = "authorName")
    CommentDto commentToCommentDto(Comment comment);

    @Mapping(source = "item.id", target = "itemId")
    @Mapping(source = "author.name", target = "authorName")
    List<CommentDto> listCommentToListCommentDto(List<Comment> listComments);
}
