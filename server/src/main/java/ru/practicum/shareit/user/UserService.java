package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

interface UserService {
    UserDto create(UserDto data);

    UserDto update(long userId, UserDto data);

    void delete(long id);

    UserDto get(long id);
}