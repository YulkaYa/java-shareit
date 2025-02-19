package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exception.DuplicatedDataException;
import ru.practicum.shareit.user.dal.UserDBRepository;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@WebMvcTest(UserService.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserServiceMockTest {

    private final MockMvc mockMvc;
    private final UserServiceImpl userService;

    @MockBean
    private UserDBRepository userDBRepository;

    private final Long userId = 1L;
    private final Long userId1 = 2L;
    private final UserDto userDto = UserDto.builder()
            .id(userId)
            .name("userName")
            .email("email@email.ru")
            .build();

    private final User user = User.builder()
            .id(userId1)
            .name("userName1")
            .email("email@email.ru")
            .build();

    @SneakyThrows
    @Test
    void updateTest() {

        when(userDBRepository.findByEmail(any(String.class))).thenReturn(Optional.of(user));

        Throwable throwable = assertThrows(DuplicatedDataException.class, () -> userService.update(1L, userDto));
        assertEquals("Данный имейл уже используется", throwable.getMessage());

        verify(userDBRepository, times(0)).save(any(User.class));
    }
}