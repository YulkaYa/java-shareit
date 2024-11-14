package ru.practicum.shareit.user.dal;

import ru.practicum.shareit.user.User;

import java.util.Optional;

public interface UserBaseRepository {
    User create(User data);

    User update(User data);

    void delete(long id);

    Optional<User> get(long id);

    Optional<User> findByEmail(String email);
}
