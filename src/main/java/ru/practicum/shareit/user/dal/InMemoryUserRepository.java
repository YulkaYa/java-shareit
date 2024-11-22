package ru.practicum.shareit.user.dal;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.DuplicatedDataException;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Component
public class InMemoryUserRepository implements UserBaseRepository {
    private final List<User> users = new ArrayList<>();
    private long count = 0;

    public User save(User data) {
        if (findByEmail(data.getEmail()).isPresent()) {
            throw new DuplicatedDataException("Данный имейл уже используется");
        }
        data.setId(++count);
        users.add(data);
        return data;
    }

    @Override
    public User update(User data) {
        User user = findById(data.getId()).get();
        user = data;
        return findById(data.getId()).get();
    }

    @Override
    public void delete(User user) {
        users.remove(user);
    }

    @Override
    public Optional<User> findById(long id) {
        for (User user : users) {
            if (user.getId() == id) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }
}
