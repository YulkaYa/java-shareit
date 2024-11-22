package ru.practicum.shareit.user.dal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.User;

import java.util.Optional;

@Primary
public interface UserDBRepository extends JpaRepository<User, Long>, UserBaseRepository {
    @Override
    User save(User data);

    @Override
    default User update(User data) {
        return save(data);
    }
}
