package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.DuplicatedDataException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dal.UserBaseRepository;
import ru.practicum.shareit.user.dal.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional
public class UserServiceImpl implements UserService {
    private final UserBaseRepository userRepository;
    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Override
    public UserDto create(UserDto userDto) {
        User user = userMapper.toUser(userDto);
        return userMapper.userToUserDto(userRepository.save(user));
    }

    @Override
    public UserDto update(long userId, UserDto userDto) {
        if (userDto.getEmail() != null) {
            Optional<User> userWithSameEmail = userRepository.findByEmail(userDto.getEmail());
            if (userWithSameEmail.isPresent() && userWithSameEmail.get().getId() != userId) {
                throw new DuplicatedDataException("Данный имейл уже используется");
            }
        }
        User userToUpdate = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с таким id не найден"));
        userToUpdate = userMapper.updateFromDto(userDto, userToUpdate);
        return userMapper.userToUserDto(userRepository.update(userToUpdate));
    }


    @Override
    public void delete(long id) {
        User userToDelete = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с таким id не найден"));
        userRepository.delete(userToDelete);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto get(long id) {
        return userMapper.userToUserDto(userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id=" + id + " не найден")));
    }
}
