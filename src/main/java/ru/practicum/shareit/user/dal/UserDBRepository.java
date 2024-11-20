package ru.practicum.shareit.user.dal;

import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.user.User;

import java.util.Optional;

@Primary
public interface UserDBRepository extends JpaRepository<User, Long>, UserBaseRepository {
    @Override
    User save(User data);

    @Override
    Optional<User> findByEmail(String email);
    @Override
    default User update(User data) {
        save(data);
        return findById(data.getId()).get();
    }
}
