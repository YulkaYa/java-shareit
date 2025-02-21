package ru.practicum.shareit.user.dal;

import org.mapstruct.*;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    User toUser(UserDto userDto);

    UserDto userToUserDto(User user);

    @Mapping(target = "id", ignore = true)
    User updateFromDto(UserDto userDto, @MappingTarget User user);
}
