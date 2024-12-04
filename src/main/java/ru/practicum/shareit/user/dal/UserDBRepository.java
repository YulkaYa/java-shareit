package ru.practicum.shareit.user.dal;

import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.user.User;

@Primary
public interface UserDBRepository extends JpaRepository<User, Long>, UserBaseRepository {
    @Override
    User save(User data);

    @Override
    default User update(User data) {
        return save(data);
    }
}
